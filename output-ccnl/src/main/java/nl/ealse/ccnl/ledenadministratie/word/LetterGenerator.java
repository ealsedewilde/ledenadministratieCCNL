package nl.ealse.ccnl.ledenadministratie.word;

import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.FINALIZE;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.LINK;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.LIST_ENTRY;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.MAIL;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.NAME;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.NUMBER;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.ORDERED_LIST;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.PAGE;
import static nl.ealse.ccnl.ledenadministratie.output.LetterData.Token.UNORDERED_LIST;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.EnumMap;
import java.util.Map;
import java.util.StringJoiner;
import lombok.Data;
import lombok.Getter;
import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.output.GeneratorException;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.ledenadministratie.output.LetterData.Token;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.ClassPathResource;

/**
 * docx-document Generator. Instances of this class are not reusable.
 * 
 * @author ealse
 */
public class LetterGenerator {
  /**
   * The letter template to use.
   */
  private static final String TEMPLATE = "letterTemplate.docx";

  private static final String HYPERLINK_STYLE = "Hyperlink";
  private static final String LIST_STYLE = "Lijstalinea";

  private static final BigInteger OL = BigInteger.valueOf(4L);
  private static final BigInteger UL = BigInteger.valueOf(3L);
  private static final BigInteger LVL0 = BigInteger.ZERO;

  private final Map<Paragraph, XWPFParagraph> paragraphMap = new EnumMap<>(Paragraph.class);

  /**
   * The result document.
   */
  private XWPFDocument doc;

  public LetterGenerator() {
    initialize();
  }

  /**
   * Generate a docx-document from a template document.
   * 
   * @param data - content for the document
   * @return the docx-document
   */
  public byte[] generateDocument(LetterData data) {
    Member member = data.getMembers().get(0);
    setAddress(member);

    LineContext lineContext = new LineContext();
    createParagraph(lineContext);

    String[] lines = data.getContent().split("\\r?\\n");
    for (String line : lines) {
      line = handlePlaceholders(member, line);
      if (lineContext.isList()) {
        addListItem(lineContext, line);
      } else if (line.indexOf(Token.START) > -1) {
        configureContext(lineContext, line);
      } else if (line.length() == 0) {
        createParagraph(lineContext);
      } else {
        addLine(lineContext, line);
      }
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      doc.write(out);
      out.close();
      doc.close();
    } catch (IOException e) {
      throw new GeneratorException("Error writing doc", e);
    }

    return out.toByteArray();
  }

  /**
   * Generate the address part of the letter.
   * 
   * @param data
   */
  private void setAddress(Member member) {
    paragraphMap.get(Paragraph.NAME).createRun().setText(member.getFullName());
    Address address = member.getAddress();
    paragraphMap.get(Paragraph.ADDRESS).createRun().setText(address.getStreetAndNumber());
    StringJoiner sj = new StringJoiner("  ");
    sj.add(address.getPostalCode());
    sj.add(address.getCity());
    paragraphMap.get(Paragraph.POSTALCODE_CITY).createRun().setText(sj.toString());
    if (address.getCountry() != null) {
      paragraphMap.get(Paragraph.COUNTRY).createRun().setText(address.getCountry());
    }
  }

  /**
   * Replace the placeholder with content.
   * 
   * @param data - with relevant content
   * @param line - with placeholders
   * @return line with content
   */
  private String handlePlaceholders(Member member, String line) {
    if (line.indexOf(NUMBER.symbol()) > -1) {
      line = line.replace(NUMBER.symbol(), member.getMemberNumber().toString());
    }
    if (line.indexOf(NAME.symbol()) > -1) {
      if (member.hasFirstName()) {
        line = line.replace(NAME.symbol(), member.getInitials());
      } else {
        line = line.replace(NAME.symbol(), member.getFullName());
      }
    }
    return line;
  }

  /**
   * Configure the context based on tokens in the line
   * 
   * @param lineContext
   * @param line
   */
  private void configureContext(LineContext lineContext, String line) {
    if (line.indexOf(MAIL.symbol()) > -1) {
      lineContext.setLinkPrefix("mailto:");
      addLink(lineContext, line);
    } else if (line.indexOf(LINK.symbol()) > -1) {
      lineContext.setLinkPrefix("");
      addLink(lineContext, line);
    } else if (line.indexOf(ORDERED_LIST.symbol()) > -1) {
      lineContext.getP().setSpacingAfter(0);
      lineContext.setNumId(OL);
      lineContext.setList(true);
    } else if (line.indexOf(UNORDERED_LIST.symbol()) > -1) {
      lineContext.getP().setSpacingAfter(0);
      lineContext.setNumId(UL);
      lineContext.setList(true);
    } else if (line.indexOf(PAGE.symbol()) > -1) {
      lineContext.getRun().addBreak(BreakType.PAGE);
    } else if (line.indexOf(FINALIZE.symbol()) > -1) {
      // Entering signature of the letter
      lineContext.setFinalize(true);
    }

  }

  /**
   * Create a paragraph and run with whitespace at the end.
   * 
   * @param lineContext
   */
  private void createParagraph(LineContext lineContext) {
    XWPFParagraph p = doc.createParagraph();
    p.setSpacingAfter(200);
    lineContext.setP(p);
    lineContext.setRun(p.createRun());
  }

  /**
   * Add one line to the letter.
   * 
   * @param lineContext
   * @param line
   */
  private void addLine(LineContext lineContext, String line) {
    if (!line.endsWith(" ")) {
      // Make sure there is space between sentences.
      line = line + " ";
    }
    XWPFRun r = lineContext.getRun();
    r.setText(line);
    if (lineContext.isFinalize()) {
      // In the signature every line has a break.
      r.addBreak();
    }
  }

  /**
   * Add an item to a list.
   * 
   * @param lineContext
   * @param itemLine
   */
  private void addListItem(LineContext lineContext, String itemLine) {
    if (itemLine.indexOf(LIST_ENTRY.symbol()) > -1) {
      createListRun(lineContext);
      lineContext.getListRun().setText(itemLine.substring(6));
    } else if (itemLine.indexOf(ORDERED_LIST.symbol()) > -1
        || itemLine.indexOf(UNORDERED_LIST.symbol()) > -1) {
      // the end of a list
      lineContext.setList(false);
      createParagraph(lineContext);
    } else {
      // next line in a list item
      lineContext.getListRun().addBreak();
      lineContext.getListRun().setText(itemLine);
    }
  }

  /**
   * Create paragraph an run for a list.
   * 
   * @param lineContext
   */
  private void createListRun(LineContext lineContext) {
    XWPFParagraph lp = doc.createParagraph();
    lp.setStyle(LIST_STYLE);
    lp.setNumID(lineContext.getNumId());
    lp.setNumILvl(LVL0);
    lineContext.setListRun(lp.createRun());
  }

  /**
   * Add hyperlink or mailto link.
   * 
   * @param lineContext
   * @param itemLine
   */
  private void addLink(LineContext lineContext, String itemLine) {
    int start = itemLine.indexOf(Token.START);
    String before = itemLine.substring(0, start);
    if (before.length() > 0) {
      lineContext.getRun().setText(before);
    }
    int end = itemLine.indexOf(Token.END);
    String link = itemLine.substring(start + 7, end);
    // Rough attempt to keep the hyperlijk on one line
    if (link.length() > 40 && lineContext.getRun().text().length() > 0) {
      lineContext.getRun().addBreak();
    }
    XWPFHyperlinkRun hpr =
        lineContext.getP().createHyperlinkRun(lineContext.getLinkPrefix() + link);
    hpr.setText(link);
    hpr.setStyle(HYPERLINK_STYLE);

    XWPFRun run = lineContext.getP().createRun();
    String after = itemLine.substring(end + 2);
    if (after.length() > 0) {
      run.setText(after);
    }
    lineContext.setRun(run);
  }

  /**
   * Read the template for the letter.
   */
  private void initialize() {
    ClassPathResource documentResource = new ClassPathResource(TEMPLATE);
    try (InputStream is = documentResource.getInputStream()) {
      doc = new XWPFDocument(is);
      for (XWPFParagraph element : doc.getParagraphs()) {
        String token = element.getText();
        if (token.startsWith(Token.START)) {
          Paragraph p = Paragraph.fromToken(element.getText());
          int runs = element.getRuns().size();
          for (int pos = 0; pos < runs; pos++) {
            element.removeRun(0);
          }

          paragraphMap.put(p, element);
        }
      }
    } catch (IOException e) {
      throw new GeneratorException("Error reading template", e);
    }
  }

  /**
   * Helper for building lines in the document.
   * 
   * @author ealse
   *
   */
  @Data
  private static class LineContext {
    private XWPFParagraph p;
    private XWPFRun run;

    private String linkPrefix;

    private boolean list;
    private BigInteger numId;
    private XWPFRun listRun;

    private boolean finalize;


  }

  /**
   * Names for fixed paragraphs in the template.
   * 
   * @author ealse
   *
   */
  public enum Paragraph {
    NAME("{{naam}}"), ADDRESS("{{adres}}"), POSTALCODE_CITY("{{postcode}}{{woonplaats}}"), COUNTRY(
        "{{land}}"), SALUTATION("{{aanhef}}"), CONTENT("{{inhoud}}");

    @Getter
    private final String token;

    Paragraph(String token) {
      this.token = token;
    }

    public static Paragraph fromToken(String token) {
      for (Paragraph p : Paragraph.values()) {
        if (p.getToken().equals(token)) {
          return p;
        }
      }
      throw new IllegalArgumentException("unknown token: " + token);
    }
  }

}
