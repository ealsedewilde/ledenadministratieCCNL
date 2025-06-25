package nl.ealse.ccnl.service.excelexport;

import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.dao.ArchiveRepository;
import nl.ealse.ccnl.ledenadministratie.excel.Archiefbestand;
import nl.ealse.ccnl.ledenadministratie.model.ArchivedMember;

/**
 * Export all data to Excel.
 *
 * @author ealse
 *
 */
@Slf4j
@AllArgsConstructor
public class ExportArchiveService {
  {log.info("Service created");}

  private final ArchiveRepository archiveRepository;

  /**
   * Export all data to an Excel file on the local filesystem.
   *
   * @param selectedFile - location for the target Excel file
   * @throws IOException in case generating the file fails 
   */
  public void export(File selectedFile) throws IOException {
    try (Archiefbestand targetFile = new Archiefbestand(selectedFile)) {
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
