package nl.ealse.ccnl.ledenadministratie.excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nl.ealse.ccnl.ledenadministratie.excel.base.AdresColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.base.CCNLBestand;
import nl.ealse.ccnl.ledenadministratie.excel.base.ColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.base.SheetDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.club.ClubColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.intern.InternColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.lid.LidColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excel.partner.PartnerColumnDefinition;
import nl.ealse.ccnl.ledenadministratie.excelexport.CommercialPartnerExport;
import nl.ealse.ccnl.ledenadministratie.excelexport.ExternalClubExport;
import nl.ealse.ccnl.ledenadministratie.excelexport.InternalRelationExport;
import nl.ealse.ccnl.ledenadministratie.excelexport.MemberExport;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelation;
import nl.ealse.ccnl.ledenadministratie.model.ExternalRelationClub;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.ledenadministratie.model.Member;

public class Ledenbestand extends CCNLBestand {

  public Ledenbestand(File bestand, CCNLColumnProperties properties) throws IOException {
    super(bestand, properties);
    addSheet(SheetDefinition.LEDEN);
  }

  public void addMemberHeading() {
    addRow();
    List<ColumnDefinition> list = new ArrayList<>();
    list.addAll(Arrays.asList(LidColumnDefinition.values()));
    list.addAll(Arrays.asList(AdresColumnDefinition.values()));
    list.forEach(c -> addCell(c.heading(), getProperties().getKolomnummer(c)));
  }

  public void addMember(Member member) {
    MemberExport.addMember(this, member);
  }

  public void addInvalidAddressMember(Member member) {
    MemberExport.addInvalidAddressMember(this, member);
  }

  public void addClubHeading() {
    addRow();
    List<ColumnDefinition> list = new ArrayList<>();
    list.addAll(Arrays.asList(ClubColumnDefinition.values()));
    list.addAll(Arrays.asList(AdresColumnDefinition.values()));
    list.forEach(c -> addCell(c.heading(), getProperties().getKolomnummer(c)));
  }

  public void addClub(ExternalRelationClub club) {
    ExternalClubExport.addExternalRelation(this, club);
  }

  public void addExternalRelationHeading() {
    addRow();
    List<ColumnDefinition> list = new ArrayList<>();
    list.addAll(Arrays.asList(PartnerColumnDefinition.values()));
    list.addAll(Arrays.asList(AdresColumnDefinition.values()));
    list.forEach(c -> addCell(c.heading(), getProperties().getKolomnummer(c)));
  }

  public void addExternalRelation(ExternalRelation relation) {
    CommercialPartnerExport.addExternalRelation(this, relation);
  }

  public void addInternalRelationHeading() {
    addRow();
    List<ColumnDefinition> list = new ArrayList<>();
    list.addAll(Arrays.asList(InternColumnDefinition.values()));
    list.addAll(Arrays.asList(AdresColumnDefinition.values()));
    list.forEach(c -> addCell(c.heading(), getProperties().getKolomnummer(c)));
  }

  public void addInternalRelation(InternalRelation relation) {
    InternalRelationExport.addInternalRelation(this, relation);
  }

}
