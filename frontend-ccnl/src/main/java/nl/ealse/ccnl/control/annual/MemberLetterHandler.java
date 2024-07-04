package nl.ealse.ccnl.control.annual;

import java.util.List;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.DocumentType;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.pdf.content.FOContent;
import nl.ealse.ccnl.service.DocumentService;

public class MemberLetterHandler {

  private final DocumentService documentService;

  public MemberLetterHandler(DocumentService documentService) {
    this.documentService = documentService;
  }

  public void addLetterToMembers(FOContent foContent, List<Member> members) {
    for (Member member : members) {
      StringBuilder sb = new StringBuilder();
      sb.append(foContent.getPreContent());
      sb.append(foContent.getContent(member));
      sb.append(foContent.getPostContent());
      byte[] pdf = documentService.generatePDF(sb.toString());
      addLetterToMember(member, pdf);
    }
  }


  public void addLetterToMember(Member member, byte[] pdf) {
    List<Document> letters = documentService.findDocuments(member, DocumentType.PAYMENT_REMINDER);
    Document document;
    if (letters.isEmpty()) {
      document = new Document();
      document.setDocumentType(DocumentType.PAYMENT_REMINDER);
      document.setDocumentName("Betaalherinnering " + member.getMemberNumber() + ".pdf");
      document.setDescription(DocumentType.PAYMENT_REMINDER.getDescription());
      document.setOwner(member);
    } else {
      document = letters.get(0);
    }
    document.setPdf(pdf);
    documentService.saveDocument(document);
  }

}
