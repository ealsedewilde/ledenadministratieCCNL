package nl.ealse.ccnl.word;

import nl.ealse.ccnl.ledenadministratie.model.Address;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.output.LetterData;
import nl.ealse.ccnl.ledenadministratie.word.LetterGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LetterGeneratorTest {

  @Test
  void testDocumentGeneration() {
    Member m = getMember();
    LetterData data = new LetterData(getContent());
    data.getMembers().add(m);
    LetterGenerator generator = new LetterGenerator();
    byte[] doc = generator.generateDocument(data);
    int l = doc.length;
    Assertions.assertEquals(48174, l);
  }

  private Member getMember() {
    Member m = new Member();
    m.setInitials("Irene");
    m.setLastName("Wolf");
    m.setLastNamePrefix("van der");
    m.setMemberNumber(1473);
    Address a = new Address();
    a.setAddress("Helena Hoeve");
    a.setAddressNumber("26");
    a.setPostalCode("2804 HX");
    a.setCity("Gouda");
    m.setAddress(a);
    return m;
  }

  private String getContent() {
    return "Beste <<naam>>,\n" + "\n" + "Welkom bij Citroën Club Nederland.\n" + "\n"
        + "Je ontvangt hierbij het jubileumboekje en de laatste twee nummers  van `t2PKtje.\n"
        + "\n" + "De meest actuele clubinformatie vind je op facebook:\n"
        + "{{link:https://nl-nl.facebook.com/citroenclubnederland/}}.\n" + "\n"
        + "Citroën Club Nederland verzorgt in een normaal jaar een drietal meetings:\n" + "{{ol}}\n"
        + "{{li}}Waggelmeeting (rond hemelvaart)\n" + "{{li}}Lindetreffen (in juni)\n"
        + "{{li}}Kindermeeting (begin september)\n" + "{{ol}}\n"
        + "Vanwege Corona gaan de meetings dit jaar helaas niet door.\n" + "\n"
        + "Naast bovenstaande meerdaagse meetings proberen we jaarlijks een tweetal ééndaags activiteiten te organiseren.\n"
        + "De najaarspuzzelrit gaat wel door.\n "
        + "Ik beveel de rit van harte aan want de organisator is zeer bedreven in het maken van mooie maar uitdagende puzzelritten.\n"
        + "\n" + "Verder verschijnt 6 maal per ons clubblad ‘t 2PKtje.\n" + "\n" + "{{pagina}}\n"
        + "Je lidnummer is <<nummer>>. Het lidmaatschapsjaar loopt van april tot april.\n" + "\n"
        + "De jaarlijkse contributie bedraagt € 27,50 bij automatische incasso en € 30,00 bij zelf overmaken.\n"
        + "Alleen met een SEPA machtiging mag Citroën Club Nederland een automatische incasso uitvoeren.\n"
        + "Ik stuur een SEPA incasso machtigingsformulier mee.\n" + "\n"
        + "Als je kiest voor automatische incasso dan graag dit formulier ingevuld opsturen naar\n"
        + "{{mail:ledenadministratie@ccnl.nl}} of het adres onderaan deze brief.\n" + "\n"
        + "Voor komend lidmaatschapsjaar is de automatische incasso al uitgevoerd.\n"
        + "Voor het lopende jaar vraag ik je € 15,00 over te maken naar rekening\n"
        + "NL97INGB0004160835 van Citroën Club Nederland onder vermelding van lidnummer <<nummer>>.\n"
        + "\n" + "Veel plezier van jouw lidmaatschap.\n" + "\n" + "\n" + "{{afsluiting}}\n"
        + "Met vriendelijke groet,\n" + "Ealse de Wilde\n"
        + "Ledenadministratie Citroën Club Nederland\n" + "Helena Hoeve 26\n" + "2804 HX  Gouda";
  }
}
