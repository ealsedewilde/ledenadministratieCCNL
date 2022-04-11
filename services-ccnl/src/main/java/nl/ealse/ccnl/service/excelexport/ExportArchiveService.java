package nl.ealse.ccnl.service.excelexport;

import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.excel.Archiefbestand;
import nl.ealse.ccnl.ledenadministratie.excel.CCNLColumnProperties;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;
import nl.ealse.ccnl.ledenadministratie.model.dao.ArchiveRepository;
import org.springframework.stereotype.Service;

/**
 * Export all data to Excel.
 * 
 * @author ealse
 *
 */
@Service
@Slf4j
public class ExportArchiveService {

  private final ArchiveRepository archiveRepository;
  private final CCNLColumnProperties properties;

  public ExportArchiveService(ArchiveRepository archiveRepository,
      CCNLColumnProperties properties) {
    log.info("Service created");
    this.archiveRepository = archiveRepository;
    this.properties = properties;
  }

  /**
   * Export all data to an Excel file on the local filesystem.
   * 
   * @param selectedFile - location for the target Excel file
   * @throws IOException in case generating the file fails 
   */
  public void export(File selectedFile) throws IOException {
    try (Archiefbestand targetFile = new Archiefbestand(selectedFile, properties)) {
      List<ArchivedMember> archiveMembers = archiveRepository.findAllByYearAndMemberNumber();
      int year = 0;
      for (ArchivedMember member : archiveMembers) {
        int mYear = member.getId().getArchiveYear();
        if (year != mYear) {
          year = mYear;
          targetFile.addSheet("leden" + year);
          targetFile.addHeading();
        }
        targetFile.addMember(member);
      }
    }
  }

}
