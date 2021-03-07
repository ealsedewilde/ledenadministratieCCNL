package nl.ealse.ccnl.ledenadministratie.pdf.content;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import nl.ealse.ccnl.ledenadministratie.model.Member;

public class FOContent {

  @Getter
  @Setter
  private String preContent;

  @Getter
  private final List<ContentSnippet> contentSnippets = new ArrayList<>();

  @Getter
  @Setter
  private String postContent;

  public String getContent(Member member) {
    StringBuilder sb = new StringBuilder();
    for (ContentSnippet snippet : contentSnippets) {
      sb.append(snippet.getSnippet(member));
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(preContent);
    for (ContentSnippet snippet : contentSnippets) {
      sb.append(snippet.toString());
    }
    sb.append(postContent);
    return sb.toString();
  }


}
