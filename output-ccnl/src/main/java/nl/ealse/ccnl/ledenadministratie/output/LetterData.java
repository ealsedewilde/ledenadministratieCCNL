package nl.ealse.ccnl.ledenadministratie.output;

import java.util.ArrayList;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Member;

/**
 * Content for generating a docx- of PDF-document.
 *
 * @author ealse
 *
 */
public record LetterData(List<Member> members, String content) {
  
  public LetterData(String content) {
    this(new ArrayList<>(), content);
  }

  /**
   * All possible tokens in a content template.
   *
   * @author ealse
   *
   */
  public enum Token {
    /**
     * Token indicating start and end of an numbered list.
     */
    ORDERED_LIST("{{ol}}"),
    /**
     * Token indicating start and end of an bulleted list.
     */
    UNORDERED_LIST("{{ul}}"),
    /**
     * List entry
     */
    LIST_ENTRY("{{li}}"),

    /**
     * Start of a 'mailto' link.
     */
    MAIL("{{mail:"),
    /**
     * Start of a hyperlink.
     */
    LINK("{{link:"),

    /**
     * Page break indicator.
     */
    PAGE("{{pagina}}"),

    /**
     * Placeholder for addresse's name.
     */
    NAME("<<naam>>"),
    /**
     * Placeholder for addresse's member number.
     */
    NUMBER("<<nummer>>"),

    /**
     * Placeholder for addresse's member number.
     */
    AMOUNT("<<bedrag>>"),

    FINALIZE("{{afsluiting}}");

    /**
     * Start String of a token.
     */
    public static final String START = "{{";
    /**
     * End String of a token.
     */
    public static final String END = "}}";

    private final String symbol;

    Token(String symbol) {
      this.symbol = symbol;
    }

    public String symbol() {
      return symbol;
    }
  }

}
