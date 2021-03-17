package nl.ealse.ccnl.service.excelexport;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.excel.Adresbestand;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.model.InternalRelation;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.ccnl.ledenadministratie.model.MembershipStatus;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationClubRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationOtherRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.ExternalRelationPartnerRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.InternalRelationRepository;
import nl.ealse.ccnl.ledenadministratie.model.dao.MemberRepository;
import org.springframework.stereotype.Service;

/**
 * Export all data to Excel.
 * 
 * @author ealse
 *
 */
@Service
@Slf4j
public class ExportAddressService {

  private static final EnumSet<MembershipStatus> statuses =
      EnumSet.of(MembershipStatus.ACTIVE, MembershipStatus.LAST_YEAR_MEMBERSHIP);

  private final ExternalRelationPartnerRepository commercialPartnerRepository;
  private final ExternalRelationClubRepository externalRelationClubRepository;
  private final ExternalRelationOtherRepository externalRelationOtherRepository;
  private final InternalRelationRepository internalRelationRepository;
  private final MemberRepository memberRepository;

  private final CCNLColumnProperties properties;

  public ExportAddressService(MemberRepository memberRepository,
      ExternalRelationPartnerRepository commercialPartnerRepository,
      InternalRelationRepository internalRelationRepository,
      ExternalRelationClubRepository externalRelationClubRepository,
      ExternalRelationOtherRepository externalRelationOtherRepository,
      CCNLColumnProperties properties) {
    log.info("Service created");
    this.commercialPartnerRepository = commercialPartnerRepository;
    this.externalRelationClubRepository = externalRelationClubRepository;
    this.externalRelationOtherRepository = externalRelationOtherRepository;
    this.internalRelationRepository = internalRelationRepository;
    this.memberRepository = memberRepository;
    this.properties = properties;
  }

  /**
   * Export all addresses to an Excel file on the local filesystem.
   * 
   * @param addressFile - location for the target Excel file
   * @return generated wrapper around Excel file
   * @throws IOException - in case generating the file fails 
   */
  public Adresbestand generateMagazineAddressFile(File addressFile) throws IOException {
    try (Adresbestand targetFile = new Adresbestand(addressFile, properties)) {

      List<Member> activeMembers = memberRepository.findMembersByStatuses(statuses);
      activeMembers.forEach(member -> {
        if (!member.isNoMagazine() && !member.getAddress().isAddressInvalid()) {
          targetFile.addMember(member);
        }
      });

      externalRelationClubRepository.findAll().forEach(targetFile::addClub);

      externalRelationOtherRepository.findAll().forEach(targetFile::addExternalRelation);
      commercialPartnerRepository.findAll().forEach(targetFile::addExternalRelation);

      internalRelationRepository.findAll().forEach(internalRelation -> {
        if (!internalRelation.isNoMagazine()) {
          targetFile.addInternalRelation(internalRelation);
        }
      });
      extraMagazines(targetFile);
      return targetFile;
    }
  }

  public Adresbestand generateCardAddressFile(File addressFile) throws IOException {
    try (Adresbestand targetFile = new Adresbestand(addressFile, properties)) {

      List<Member> activeMembers = memberRepository.findMembersByStatuses(statuses);
      activeMembers.forEach(targetFile::addMember);
      return targetFile;
    }
  }

  private void extraMagazines(Adresbestand targetFile) {
    int nummerLedenadministratie =
        Integer.parseInt(properties.getProperty("nummer_ledenadministratie"));
    Optional<InternalRelation> ledenadministratie =
        internalRelationRepository.findById(nummerLedenadministratie);
    if (ledenadministratie.isPresent()) {
      int aantalBladen = Integer.parseInt(properties.getProperty("aantal_bladen"));
      for (int ix = 0; ix < aantalBladen; ix++) {
        targetFile.addInternalRelation(ledenadministratie.get());
      }
    }

  }

}
