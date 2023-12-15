package nl.ealse.javafx.util;

import java.io.File;
import java.util.Arrays;
import java.util.function.Supplier;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import nl.ealse.ccnl.MainStage;

/**
 * Wrapper around a {@link FileChooser}.
 *
 * @author ealse
 *
 */
public class WrappedFileChooser {

  private final Stage fileChooserStage;
  private final FileChooser fileChooser;
  private Supplier<String> directorySupplier;

  /**
   * Construct an instance.
   *
   * @param extensions - file extensions to handle
   */
  public WrappedFileChooser(FileExtension... extensions) {
    this.fileChooserStage = new Stage();
    this.fileChooser = initFileChooser(extensions);
  }

  /**
   * Initialize the wrapped {@link FileChooser} in a javafx thread.
   *
   * @param extensions - file extensions to handle
   * @return the wrapped {@link FileChooser}
   */
  private FileChooser initFileChooser(FileExtension... extensions) {
    fileChooserStage.initModality(Modality.APPLICATION_MODAL);
    fileChooserStage.initOwner(MainStage.getStage());
    fileChooserStage.getIcons().add(MainStage.getIcon());
    fileChooserStage.setAlwaysOnTop(true);
    FileChooser fs = new FileChooser();
    Arrays.stream(extensions)
        .forEach(extension -> fs.getExtensionFilters().add(extension.getFilter()));
    return fs;
  }

  public void setInitialDirectory(Supplier<String> dirSupplier) {
    this.directorySupplier = dirSupplier;
  }

  private void configureInitialDirectory() {
    String name = directorySupplier.get();
    if (name != null) {
      File directory = new File(name);
      fileChooser.setInitialDirectory(directory);
      if (!directory.exists()) {
        directory.mkdirs();
      }
    }
  }

  public void setInitialFileName(String fileName) {
    fileChooser.setInitialFileName(fileName);
  }

  public File showOpenDialog() {
    configureInitialDirectory();
    return fileChooser.showOpenDialog(fileChooserStage);
  }

  public File showSaveDialog() {
    configureInitialDirectory();
    return fileChooser.showSaveDialog(fileChooserStage);
  }

  public enum FileExtension {
    PDF(new FileChooser.ExtensionFilter("PDF-document", "*.pdf")),

    DOCX(new FileChooser.ExtensionFilter("MS Word-document", "*.docx")),

    XML(new FileChooser.ExtensionFilter("XML-document", "*.xml")),

    XLSX(new FileChooser.ExtensionFilter("Microsoft Excel-werkblad", "*.xlsx")),

    PNG(new FileChooser.ExtensionFilter("Afbeeldingen", "*.jpeg", "*.jpg", "*.png")),

    ZIP(new FileChooser.ExtensionFilter("Gecomprimeerd (zip) bestand", "*.zip")),

    DB(new FileChooser.ExtensionFilter("Database bestand", "*.mv.db"));

    @Getter
    private final FileChooser.ExtensionFilter filter;

    FileExtension(FileChooser.ExtensionFilter filter) {
      this.filter = filter;
    }
  }

}
