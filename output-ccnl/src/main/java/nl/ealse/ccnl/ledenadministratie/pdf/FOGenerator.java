package nl.ealse.ccnl.ledenadministratie.pdf;

import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.FINALIZE;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.LINK;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.LIST_ENTRY;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.MAIL;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.NAME;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.NUMBER;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.AMOUNT;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.ORDERED_LIST;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.PAGE;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.UNORDERED_LIST;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import lombok.Data;
import lombok.experimental.UtilityClass;
import nl.ealse.ccnl.ledenadministratie.output.GeneratorException;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.ledenadministratie.output.LetterData.Token;
import nl.ealse.ccnl.ledenadministratie.pdf.content.AddressSnippet;
import nl.ealse.ccnl.ledenadministratie.pdf.content.AmountSnippet;
import nl.ealse.ccnl.ledenadministratie.pdf.content.ContentSnippet;
import nl.ealse.ccnl.ledenadministratie.pdf.content.FOContent;
import nl.ealse.ccnl.ledenadministratie.pdf.content.NameSnippet;
import nl.ealse.ccnl.ledenadministratie.pdf.content.NumberSnippet;
import nl.ealse.ccnl.ledenadministratie.pdf.content.StringSnippet;

/**
 *
 * @author ealse
 */
@UtilityClass
public class FOGenerator {

  private static final String CONTENT_START = "<fo:page-sequence";
  private static final String CONTENT_END = "</fo:page-sequence>";

  private static final String NEW_LINE = """
      <fo:block line-height="0pt" linefeed-treatment="preserve"
          white-space-treatment="preserve" white-space-collapse="false"/>
      """;

  private static final String EMPTY_LINE = "<fo:block white-space-treatment=\"preserve\"/>";

  private static final String NEW_PARAGRAPH = "<fo:block space-after=\"4mm\">";
  private static final String PAGE_PARAGRAPH =
      "<fo:block break-before=\"page\" space-after=\"4mm\">";

  private static final String BLOCK_LINE_BREAK =
      "<fo:block space-after=\"4mm\" linefeed-treatment=\"preserve\" white-space-treatment=\"preserve\">";
  private static final String BLOCK_END = "</fo:block>";

  private static final String BASIC_LINK = """
      <fo:basic-link external-destination="url(%s)">
          <fo:inline color="#0563C1" text-decoration="underline">
              %s
          </fo:inline>
      </fo:basic-link>
      """;

  private static final String LIST_START = """
      <fo:list-block provisional-distance-between-starts="0.25in"
          start-indent="0.25in" text-indent="0in">
      """;

  private static final String LIST_END = "</fo:list-block>";

  private static final String LIST_ITEM = """
      <fo:list-item>
          <fo:list-item-label>
              <fo:block>%s</fo:block>
          </fo:list-item-label>
          <fo:list-item-body start-indent="body-start()">
              <fo:block>%s</fo:block>
          </fo:list-item-body>
      </fo:list-item>""";

  private static final String TEMPLATE = "/letterTemplate.fo";

  private static final String NAW_TOKEN = "{{naw}}";
  private static final String CONTENT_TOKEN = "{{inhoud}}";

  public FOContent generateFO(LetterData data) {
    FOContent content = new FOContent();
    LineContext lineContext = new LineContext();
    StringJoiner sj = lineContext.newStringJoiner();
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(FOGenerator.class.getResourceAsStream(TEMPLATE),
            StandardCharsets.UTF_8))) {
      String templateLine = generatePreContent(content, sj, reader);
      
      templateLine = generateContent(data, content, lineContext, reader, templateLine);
      
      generatePostContent(content, lineContext, reader, templateLine);
      return content;
    } catch (IOException e) {
      throw new GeneratorException("Error generating XSL-FO", e);
    }
  }

  private String generatePreContent(FOContent content, StringJoiner sj, BufferedReader reader)
      throws IOException {
    String templateLine = reader.readLine();
    while (templateLine != null && templateLine.indexOf(CONTENT_START) == -1) {
      sj.add(templateLine);
      templateLine = reader.readLine();
    }
    content.setPreContent(sj.toString());
    return templateLine;
  }

  private String generateContent(LetterData data, FOContent content, LineContext context,
      BufferedReader reader, String templateLine) throws IOException {
    StringJoiner sj = context.newStringJoiner();
    while (templateLine != null && templateLine.indexOf(CONTENT_END) == -1) {
      if (templateLine.indexOf(NAW_TOKEN) != -1) {
        content.getContentSnippets().add(new StringSnippet(sj.toString()));
        content.getContentSnippets().add(new AddressSnippet());
        sj = context.newStringJoiner();
      } else if (templateLine.indexOf(CONTENT_TOKEN) != -1) {
        insertContent(content, context, data);
        sj = context.getStringJoiner();
      } else {
        sj.add(templateLine);
      }
      templateLine = reader.readLine();
    }
    if (templateLine != null) {
      sj.add(templateLine);
      content.getContentSnippets().add(new StringSnippet(sj.toString()));
      templateLine = reader.readLine();
    }
    return templateLine;
  }

  private void generatePostContent(FOContent content, LineContext lineContext,
      BufferedReader reader, String templateLine) throws IOException {
    if (templateLine != null) {
      StringJoiner sj = lineContext.newStringJoiner();
      while (templateLine != null) {
        sj.add(templateLine);
        templateLine = reader.readLine();
      }
      content.setPostContent(sj.toString());
    }
  }

  private void insertContent(FOContent content, LineContext lineContext, LetterData data) {
    StringJoiner sj = lineContext.getStringJoiner();
    sj.add(NEW_PARAGRAPH);

    String[] dataLines = data.content().split("\\r?\\n");
    for (String dataLine : dataLines) {
      sj = lineContext.getStringJoiner();
      if (dataLine.isEmpty()) {
        handleEmptyDataLine(lineContext, sj);
      } else {
        ContentContext context = new ContentContext(content, lineContext, dataLine);
        handleNonEmptyDataLine(context, sj);
      }  
    }
    sj.add(BLOCK_END);
  }

  private void handleEmptyDataLine(LineContext context, StringJoiner sj) {
    if (!context.isEmptyLine()) {
      sj.add(BLOCK_END);
      context.setEmptyLine(true);
    } else {
      sj.add(EMPTY_LINE);
    }
    context.setAfterLink(false);
  }

  private void handleNonEmptyDataLine(ContentContext contentContext, StringJoiner sj) {
    boolean placeholder = handlePlaceholders(new PlaceholderContext(contentContext));
    LineContext lineContext = contentContext.getLineContext();
    String dataLine = contentContext.getDataLine();
    if (dataLine.indexOf(Token.START) > -1) {
      configureContent(lineContext, dataLine);
      return;
    } else if (lineContext.isEmptyLine()) {
      lineContext.setEmptyLine(false);
      sj.add(NEW_PARAGRAPH);
    } else if (lineContext.isAfterLink()) {
      sj.add(NEW_LINE);
      lineContext.setAfterLink(false);
    }
    if (!placeholder) {
      sj.add(dataLine);
    }
  }

  private void configureContent(LineContext context, String dataLine) {
    StringJoiner sj = context.getStringJoiner();
    if (dataLine.indexOf(MAIL.symbol()) > -1) {
      makeLink(context, dataLine, "mailto:");
    } else if (dataLine.indexOf(LINK.symbol()) > -1) {
      makeLink(context, dataLine, "");
    } else if (dataLine.indexOf(ORDERED_LIST.symbol()) > -1) {
      context.setListCount(1);
      makeList(context);
    } else if (dataLine.indexOf(UNORDERED_LIST.symbol()) > -1) {
      context.setListCount(-1);
      makeList(context);
    } else if (dataLine.indexOf(LIST_ENTRY.symbol()) > -1) {
      makeListEntry(context, dataLine);
    } else if (dataLine.indexOf(PAGE.symbol()) > -1) {
      makePageBreak(context);
    } else if (dataLine.indexOf(FINALIZE.symbol()) > -1) {
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

  private void makeListEntry(LineContext context, String dataLine) {
    if (context.getListCount() == -1) {
      context.getStringJoiner().add(String.format(LIST_ITEM, "*", dataLine));
    } else {
      String count = Integer.toString(context.getListCount()) + ".";
      context.getStringJoiner().add(String.format(LIST_ITEM, count, dataLine.substring(6)));
      context.incrementListCount();
    }
  }

  private void makeLink(LineContext context, String dataLine, String prefix) {
    StringJoiner sj = context.getStringJoiner();
    int start = dataLine.indexOf(Token.START);
    String before = dataLine.substring(0, start);
    if (!before.isEmpty()) {
      sj.add(before);
    } else if (!context.isEmptyLine()) {
      sj.add(NEW_LINE);
    }
    int end = dataLine.indexOf(Token.END);
    String link = dataLine.substring(start + 7, end);
    sj.add(String.format(BASIC_LINK, prefix + link, link));
    String after = dataLine.substring(end + 2);
    if (!after.isEmpty()) {
      sj.add(after);
    }
    context.setAfterLink(true);
  }

  private boolean handlePlaceholders(PlaceholderContext placeholderContext) {
    handlePlaceholder(NAME, placeholderContext);
    handlePlaceholder(NUMBER, placeholderContext);
    handlePlaceholder(AMOUNT, placeholderContext);
    return placeholderContext.isPlaceholderPresent();
  }

  private void handlePlaceholder(LetterData.Token placeholder, PlaceholderContext placeholderContext) {
    FOContent content = placeholderContext.getFOContent();
    LineContext context = placeholderContext.getLineContext();
    String dataLine = placeholderContext.getDataLine();
    StringJoiner sj = context.getStringJoiner();
    
    int placeholderIx = dataLine.indexOf(placeholder.symbol());
    if (placeholderIx != -1) {
      if (context.isEmptyLine()) {
        context.setEmptyLine(false);
        sj.add(NEW_PARAGRAPH);
      }
      if (placeholderIx > 0) {
        sj.add(dataLine.subSequence(0, placeholderIx));
        content.getContentSnippets().add(new StringSnippet(sj.toString()));
      }
      content.getContentSnippets().add(PlaceholderContext.newContentSnippet(placeholder));
      placeholderIx = placeholderIx + placeholder.symbol().length();
      sj = context.newStringJoiner();
      sj.add(dataLine.substring(placeholderIx));
      placeholderContext.setPlaceholderPresent(true);
    }
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
  
  @Data
  private static class ContentContext {
    private final FOContent content;
    
    private final LineContext lineContext;
    
    private final String dataLine;
  }
  
  @Data
  private static class PlaceholderContext {
    private final ContentContext contentContext;
    
    public FOContent getFOContent() {
      return contentContext.getContent();
    }
    
    public LineContext getLineContext() {
      return contentContext.getLineContext();
    }
    
    public String getDataLine() {
      return contentContext.getDataLine();
    }
    
    private boolean placeholderPresent;
    
    public static ContentSnippet newContentSnippet(LetterData.Token placeholder) {
      switch(placeholder) {
        case NAME:
          return new NameSnippet();
        case NUMBER:
          return new NumberSnippet();
        case AMOUNT:
          return new AmountSnippet();
        default:
          throw new GeneratorException("logic error");
      }
    }

    
  }

}
