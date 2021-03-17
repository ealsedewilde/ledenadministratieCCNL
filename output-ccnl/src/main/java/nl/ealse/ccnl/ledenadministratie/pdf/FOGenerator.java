package nl.ealse.ccnl.ledenadministratie.pdf;

import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.FINALIZE;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.LINK;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.LIST_ENTRY;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.MAIL;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.NAME;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.NUMBER;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.ORDERED_LIST;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.PAGE;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.UNORDERED_LIST;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import lombok.Data;
import nl.ealse.ccnl.ledenadministratie.output.GeneratorException;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.ledenadministratie.output.LetterData.Token;
import nl.ealse.ccnl.ledenadministratie.pdf.content.AddressSnippet;
import nl.ealse.ccnl.ledenadministratie.pdf.content.FOContent;
import nl.ealse.ccnl.ledenadministratie.pdf.content.NameSnippet;
import nl.ealse.ccnl.ledenadministratie.pdf.content.NumberSnippet;
import nl.ealse.ccnl.ledenadministratie.pdf.content.StringSnippet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 
 * @author ealse
 */
public class FOGenerator {

  private static final String CONTENT_START = "<fo:page-sequence";
  private static final String CONTENT_END = "</fo:page-sequence>";

  private static final String NEW_LINE =
      "<fo:block line-height=\"0pt\" linefeed-treatment=\"preserve\"\n white-space-treatment=\"preserve\" white-space-collapse=\"false\"/>";
  private static final String EMPTY_LINE = "<fo:block white-space-treatment=\"preserve\"/>";

  private static final String NEW_PARAGRAPH = "<fo:block space-after=\"4mm\">";
  private static final String PAGE_PARAGRAPH =
      "<fo:block break-before=\"page\" space-after=\"4mm\">";

  private static final String BLOCK_LINE_BREAK =
      "<fo:block space-after=\"4mm\" linefeed-treatment=\"preserve\" white-space-treatment=\"preserve\">";
  private static final String BLOCK_END = "</fo:block>";

  private static final String BASIC_LINK = "<fo:basic-link external-destination=\"url(%s)\">\n"
      + "          <fo:inline color=\"#0563C1\" text-decoration=\"underline\">\n"
      + "            %s</fo:inline>\n" + "        </fo:basic-link>";

  private static final String LIST_START =
      "<fo:list-block provisional-distance-between-starts=\"0.25in\"\n"
          + "        start-indent=\"0.25in\" text-indent=\"0in\">";

  private static final String LIST_END = "</fo:list-block>";

  private static final String LIST_ITEM = "<fo:list-item>\n" + "          <fo:list-item-label>\n"
      + "            <fo:block>%s</fo:block>\n" + "          </fo:list-item-label>\n"
      + "          <fo:list-item-body start-indent=\"body-start()\">\n"
      + "            <fo:block>%s</fo:block>\n" + "          </fo:list-item-body>\n"
      + "        </fo:list-item>";

  private final Resource template = new ClassPathResource("letterTemplate.fo");

  private static final String NAW_TOKEN = "{{naw}}";
  private static final String CONTENT_TOKEN = "{{inhoud}}";

  public FOContent generateFO(LetterData data) {
    FOContent content = new FOContent();
    LineContext context = new LineContext();
    StringJoiner sj = context.newStringJoiner();
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(template.getInputStream(), StandardCharsets.UTF_8))) {
      String line = reader.readLine();
      while (line != null && line.indexOf(CONTENT_START) == -1) {
        sj.add(line);
        line = reader.readLine();
      }
      content.setPreContent(sj.toString());
      sj = context.newStringJoiner();
      while (line != null && line.indexOf(CONTENT_END) == -1) {
        if (line.indexOf(NAW_TOKEN) != -1) {
          content.getContentSnippets().add(new StringSnippet(sj.toString()));
          content.getContentSnippets().add(new AddressSnippet());
          sj = context.newStringJoiner();
        } else if (line.indexOf(CONTENT_TOKEN) != -1) {
          insertContent(content, context, data);
          sj = context.getStringJoiner();
        } else {
          sj.add(line);
        }
        line = reader.readLine();
      }
      if (line != null) {
        sj.add(line);
        content.getContentSnippets().add(new StringSnippet(sj.toString()));

        line = reader.readLine();
        sj = context.newStringJoiner();
        while (line != null) {
          sj.add(line);
          line = reader.readLine();
        }
        content.setPostContent(sj.toString());
      }
      return content;
    } catch (IOException e) {
      throw new GeneratorException("Error generating XSL-FO", e);
    }
  }

  private void insertContent(FOContent content, LineContext context, LetterData data) {
    StringJoiner sj = context.getStringJoiner();
    sj.add(NEW_PARAGRAPH);

    String[] lines = data.getContent().split("\\r?\\n");
    for (String line : lines) {
      boolean placeholder = handlePlaceholders(content, context, line);
      sj = context.getStringJoiner();
      if (line.length() == 0) {
        if (!context.isEmptyLine()) {
          sj.add(BLOCK_END);
          context.setEmptyLine(true);
        } else {
          sj.add(EMPTY_LINE);
        }
        context.setAfterLink(false);
      } else if (line.indexOf(Token.START) > -1) {
        configureContext(context, line);
        continue;
      } else if (context.isEmptyLine()) {
        context.setEmptyLine(false);
        sj.add(NEW_PARAGRAPH);
      } else if (context.isAfterLink()) {
        sj.add(NEW_LINE);
        context.setAfterLink(false);
      }
      if (!placeholder) {
        sj.add(line);
      }
    }
    sj.add(BLOCK_END);
  }

  private void configureContext(LineContext context, String line) {
    StringJoiner sj = context.getStringJoiner();
    if (line.indexOf(MAIL.symbol()) > -1) {
      makeLink(context, line, "mailto:");
    } else if (line.indexOf(LINK.symbol()) > -1) {
      makeLink(context, line, "");
    } else if (line.indexOf(ORDERED_LIST.symbol()) > -1) {
      context.setListCount(1);
      makeList(context);
    } else if (line.indexOf(UNORDERED_LIST.symbol()) > -1) {
      context.setListCount(-1);
      makeList(context);
    } else if (line.indexOf(LIST_ENTRY.symbol()) > -1) {
      makeListEntry(context, line);
    } else if (line.indexOf(PAGE.symbol()) > -1) {
      makePageBreak(context);
    } else if (line.indexOf(FINALIZE.symbol()) > -1) {
      // Entering signature of the letter
      context.setEmptyLine(false);
      sj.add(BLOCK_LINE_BREAK);
    }
  }

  private void makePageBreak(LineContext context) {
    if (context.isEmptyLine()) {
      context.setEmptyLine(false);
    } else {
      context.getStringJoiner().add(BLOCK_END);
    }
    context.getStringJoiner().add(PAGE_PARAGRAPH);
  }

  private void makeList(LineContext context) {
    if (context.isList()) {
      context.getStringJoiner().add(LIST_END);
    } else {
      context.setListCount(1);
      context.setList(true);
      context.getStringJoiner().add(LIST_START);
    }
  }

  private void makeListEntry(LineContext context, String line) {
    if (context.getListCount() == -1) {
      context.getStringJoiner().add(String.format(LIST_ITEM, "*", line));
    } else {
      String count = Integer.toString(context.getListCount()) + ".";
      context.getStringJoiner().add(String.format(LIST_ITEM, count, line.substring(6)));
      context.incrementListCount();
    }
  }

  private void makeLink(LineContext context, String line, String prefix) {
    StringJoiner sj = context.getStringJoiner();
    int start = line.indexOf(Token.START);
    String before = line.substring(0, start);
    if (before.length() > 0) {
      sj.add(before);
    } else if (!context.isEmptyLine()) {
      sj.add(NEW_LINE);
    }
    int end = line.indexOf(Token.END);
    String link = line.substring(start + 7, end);
    sj.add(String.format(BASIC_LINK, prefix + link, link));
    String after = line.substring(end + 2);
    if (after.length() > 0) {
      sj.add(after);
    }
    context.setAfterLink(true);
  }

  private boolean handlePlaceholders(FOContent content, LineContext context, String line) {
    int nameIx = line.indexOf(NAME.symbol());
    boolean placeholderPresent = false;
    StringJoiner sj = context.getStringJoiner();
    if (nameIx != -1) {
      if (nameIx > 0) {
        sj.add(line.subSequence(0, nameIx));
        content.getContentSnippets().add(new StringSnippet(sj.toString()));
        content.getContentSnippets().add(new NameSnippet());
        nameIx = nameIx + NAME.symbol().length();
        sj = context.newStringJoiner();
        sj.add(line.substring(nameIx));
      }
      placeholderPresent = true;
    }
    int numberIx = line.indexOf(NUMBER.symbol());
    if (numberIx != -1) {
      if (numberIx > 0) {
        sj.add(line.subSequence(0, numberIx));
        content.getContentSnippets().add(new StringSnippet(sj.toString()));
        content.getContentSnippets().add(new NumberSnippet());
        numberIx = numberIx + NUMBER.symbol().length();
        sj = context.newStringJoiner();
        sj.add(line.substring(numberIx));
      }
      placeholderPresent = true;
    }
    return placeholderPresent;
  }


  @Data
  private static class LineContext {
    private boolean newParagraph = true;

    private boolean list;

    private boolean afterLink;

    private boolean emptyLine;

    private int listCount;

    private StringJoiner stringJoiner;

    public void incrementListCount() {
      listCount++;
    }

    private StringJoiner newStringJoiner() {
      stringJoiner = new StringJoiner("\n");
      return stringJoiner;
    }

  }

}
