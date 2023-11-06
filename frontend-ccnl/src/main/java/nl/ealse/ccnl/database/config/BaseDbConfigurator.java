package nl.ealse.ccnl.database.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;

@Slf4j
public abstract class BaseDbConfigurator {

  private static final String INFO_STYLE =
      "-fx-text-fill: green;  -fx-font-size: 1em; -fx-font-weight: bold;";
  private static final String ERROR_STYLE = INFO_STYLE + "-fx-text-fill: red";

  @FXML
  private TextField dbFolder;

  @FXML
  private TextField dbName;

  @FXML
  @Getter
  private Label message;

  @FXML
  private Button saveButton;

  @Getter
  private final Stage stage;

  protected BaseDbConfigurator(Stage stage) {
    this.stage = stage;
  }

  /**
   * Configure the initial database location.
   */
  public void openDialog() {
    stage.setTitle("Database locatie configureren");
    stage.getIcons().add(getStageIcon());

    Parent page = loadFxml("db/dbconfig");
    Scene scene = new Scene(page);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  void configureExistingDatabase() {
    Stage chooserStage = new Stage();
    chooserStage.initModality(Modality.APPLICATION_MODAL);
    chooserStage.initOwner(stage);
    chooserStage.getIcons().add(getStageIcon());
    FileChooser fs = new FileChooser();
    fs.getExtensionFilters().add(FileExtension.DB.getFilter());
    File file = fs.showOpenDialog(chooserStage);
    Parent page;
    if (file != null) {
      page = loadFxml("db/dbconfigExisting");

      File dir = file.getParentFile();
      dbFolder.setText(dir.getAbsolutePath());
      String fileName = file.getName();
      int ix = fileName.indexOf(".mv.db");
      dbName.setText(fileName.substring(0, ix));
    } else {
      page = loadFxml("db/dbconfigNew");
      message.setStyle(ERROR_STYLE);
      message.setText("Geen (geldige) database geselecteerd");
    }
    stage.setTitle("Locatie bestaande database configureren");
    Scene scene = new Scene(page);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  void configureNewDatabase() {
    Parent page = loadFxml("db/dbconfigNew");
    dbFolder.setText("S:\\ledenadministratie-ccnl\\db\\ccnl");
    dbName.setText("ccnl");
    Scene scene = new Scene(page);
    stage.setScene(scene);
    stage.setTitle("Locatie nieuwe database configureren");
    stage.show();
  }

  @FXML
  void initialize() {
    if (dbName != null) {
      dbName.textProperty().addListener((observable, oldValue, newValue) -> {
        saveButton.setDisable(false);
        message.setText("");
      });
    }
    if (dbFolder != null) {
      dbFolder.textProperty().addListener((observable, oldValue, newValue) -> {
        saveButton.setDisable(false);
        message.setText("");
      });
    }
  }

  @FXML
  void saveNew() {
    File dir = new File(dbFolder.getText());
    dir.mkdirs();
    save();
  }

  @FXML
  void save() {
    if (validInput()) {
      String msg = configureDatabaseLocation();
      saveButton.setDisable(true);
      Alert info = new Alert(AlertType.INFORMATION);
      info.setHeaderText(msg);
      info.setTitle("Database locatie");
      Stage cs = (Stage) info.getDialogPane().getScene().getWindow();
      cs.getIcons().add(getStageIcon());
      cs.setAlwaysOnTop(true);
      info.showAndWait();
      nextAction();
    } else {
      saveButton.setDisable(true);
    }
  }

  protected abstract Parent loadFxml(String fxmlName);

  protected abstract Image getStageIcon();

  protected abstract void nextAction();

  private boolean validInput() {
    StringBuilder error = new StringBuilder("");
    boolean result = true;
    if (!validFolder()) {
      error.append("Geef een database locatie op ");
      result = false;
    }
    if (!validDbName()) {
      error.append("Geef een databasenaam op");
      result = false;
    }
    message.setStyle(ERROR_STYLE);
    message.setText(error.toString());
    return result;
  }

  private boolean validDbName() {
    String name = dbName.getText();
    return name == null || name.isBlank();
  }

  private boolean validFolder() {
    String folder = dbFolder.getText();
    if (folder != null) {
      folder = folder.replace('\\', '/');
      File dir = new File(folder);
      dir.mkdirs();
      return dir.exists();
    }
    return false;
  }

  private String configureDatabaseLocation() {
    String dbLocation = new StringBuilder().append(dbFolder.getText()).append("/")
        .append(dbName.getText()).toString();
    return DbProperties.writeFile(dbLocation);
  }

  private static final class DbProperties {
    private static final File DB_PROPERTIES = new File("db.properties");
    private static final String PREFIX = "db.locatie = ";
    private static final String COMMENT = """
        # locatie (pad) en naam van database
        ####################################
        # Bijvoorbeeld: db.locatie = pad/dbName"
        #
        # Het pad mag een volledig pad zijn of een relatief pad."
        # Bij een relatief pad: relatief t.o.v. de Map waarin deze applicatie is geplaatst"
        # Let op: de database mag NIET in de Map van de applicatie worden geplaatst!
        """;

    private static String writeFile(String dbLocation) {
      try {
        FileWriter writer = new FileWriter(DB_PROPERTIES);
        writer.write(COMMENT, 0, COMMENT.length());
        String prop = PREFIX + dbLocation;
        writer.write(prop, 0, prop.length());
        writer.close();
        return "Configuratie is opgeslagen";
      } catch (IOException e) {
        log.error("Error writing file", e);
        return "Kan configuratie niet opslaan";
      }
    }

  }

}
