package nl.ealse.ccnl.pdf;

import nl.ealse.ccnl.ledenadministratie.pdf.MailFOGenerator;
import nl.ealse.ccnl.ledenadministratie.pdf.content.FOContent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MailFOGeneratorTest {

  private static final String text =
      "Beste T. Tester,    \n" + "    \n" + "Jammer dat je Citroën Club Nederland verlaat.    \n"
          + "Ik heb de opzegging genoteerd met ingang van het komende lidmaatschapsjaar.    \n"
          + "Een lidmaatschapsjaar loopt van april tot april.    \n"
          + "Je zult tot die tijd de 2PKtjes blijven ontvangen.    \n" + "    \n"
          + "Het beste en wellicht tot ziens.    \n" + "    \n" + "Met vriendelijke groet,    \n"
          + "Ealse de Wilde    \n" + "Ledenadministratie Citroën Club Nederland    \n"
          + "Helena Hoeve 26    \n" + "2804 HX  Gouda";
  private static final String to = "tester@mail.nl";

  private static final String subject = "Opzegging lidmaatschap Citroën Club Nederland";

  @Test
  void testMailFOGenerator() {
    FOContent content = MailFOGenerator.generateFO(to, null, subject, text);
    String cs = content.toString();
    boolean ok = cs.indexOf(subject) > -1;
    Assertions.assertTrue(ok);
  }

}
