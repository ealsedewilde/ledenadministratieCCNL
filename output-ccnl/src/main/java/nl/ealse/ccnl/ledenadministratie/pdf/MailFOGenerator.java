package nl.ealse.ccnl.ledenadministratie.pdf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.output.GeneratorException;
import nl.ealse.ccnl.ledenadministratie.pdf.content.FOContent;
import nl.ealse.ccnl.ledenadministratie.pdf.content.StringSnippet;

@UtilityClass
@Slf4j
public class MailFOGenerator {

  private static final String BLOCK =
      "<fo:block linefeed-treatment=\"preserve\">\n<![CDATA[%s]]>\n</fo:block>\n";

  private static final String TO_TOKEN = "{{mail.to}}";
  private static final String CC_TOKEN = "{{mail.cc}}";
  private static final String SUBJECT_TOKEN = "{{mail.subject}}";
  private static final String CONTENT_TOKEN = "{{mail.content}}";

  private static final String TEMPLATE = "/mailTemplate.fo";

  public FOContent generateFO(String to, String cc, String subject, String text) {
    if (cc == null) {
      cc = "";
    }
    FOContent content = initializeFOContent(to, cc, subject);
    buildText(content, text);
    return content;
  }

  public void buildText(FOContent content, String text) {
    StringJoiner sj = new StringJoiner("\n");
    String[] lines = text.split("\\r?\\n");
    for (String line : lines) {
      line = line.trim();
      if (line.length() == 0) {
        String block = String.format(BLOCK, sj.toString());
        content.getContentSnippets().add(new StringSnippet(block));
        sj = new StringJoiner("\n");
      } else {
        sj.add(line);
      }
    }
    String block = String.format(BLOCK, sj.toString());
    content.getContentSnippets().add(new StringSnippet(block));
  }

  private FOContent initializeFOContent(String to, String cc, String subject) {
    FOContent content = new FOContent();
    StringJoiner sj = new StringJoiner("\n");
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        MailFOGenerator.class.getResourceAsStream(TEMPLATE), StandardCharsets.UTF_8))) {
      String line = reader.readLine();
      while (line != null && line.indexOf(CONTENT_TOKEN) == -1) {
        line = inspectLine(line, to, cc, subject);
        sj.add(line);
        line = reader.readLine();
      }
      content.setPreContent(sj.toString());
      sj = new StringJoiner("\n");
      line = reader.readLine();
      while (line != null) {
        sj.add(line);
        line = reader.readLine();
      }
      content.setPostContent(sj.toString());
    } catch (IOException e) {
      log.error("Error generating XSL-FO", e);
      throw new GeneratorException("Error generating XSL-FO", e);
    }
    return content;
  }

  private String inspectLine(String line, String to, String cc, String subject) {
    if (line.indexOf(TO_TOKEN) != -1) {
      return replaceInLine(line, TO_TOKEN, to);
    } else if (line.indexOf(CC_TOKEN) != -1) {
      return replaceInLine(line, CC_TOKEN, cc);
    } else if (line.indexOf(SUBJECT_TOKEN) != -1) {
      return replaceInLine(line, SUBJECT_TOKEN, subject);
    }
    return line;
  }

  private String replaceInLine(String line, String token, String replacement) {
    StringBuilder sb = new StringBuilder();
    int ix = line.indexOf(token);
    sb.append(line.substring(0, ix));
    sb.append(replacement);
    ix = ix + token.length();
    sb.append(line.substring(ix));
    return sb.toString();
  }
}
