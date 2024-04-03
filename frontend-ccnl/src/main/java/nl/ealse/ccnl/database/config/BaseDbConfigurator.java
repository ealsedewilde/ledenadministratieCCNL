package nl.ealse.ccnl.database.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringJoiner;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.ledenadministratie.config.ApplicationProperties;
import nl.ealse.ccnl.ledenadministratie.config.DatabaseLocation;
import nl.ealse.javafx.util.WrappedFileChooser.FileExtension;

@Slf4j
@Getter
public abstract class BaseDbConfigurator {

  protected static final String INFO_STYLE =
      "-fx-text-fill: green;  -fx-font-size: 1em; -fx-font-weight: bold;";
  private static final String ERROR_STYLE = INFO_STYLE + "-fx-text-fill: red";

  @FXML
  private TextField dbFolder;

  @FXML
  private TextField dbName;

  @FXML
  private Label message;

  @FXML
  private Button saveButton;

  private final Stage configStage = new Stage();

  private Alert info;

  /**
   * Configure the initial database location.
   */
  public void openDialog() {
    configStage.setTitle("Database locatie configureren");
    configStage.getIcons().add(getStageIcon());

    Parent page = loadFxml("db/dbconfig");
    Scene scene = new Scene(page);
    configStage.setScene(scene);
    configStage.show();
  }

  @FXML
  void configureExistingDatabase() {
    FileChooser fs = fileChooser();
    File file = fs.showOpenDialog(configStage);
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
    configStage.setTitle("Locatie bestaande database configureren");
    Scene scene = new Scene(page);
    configStage.setScene(scene);
    configStage.show();
  }

  protected FileChooser fileChooser() {
    FileChooser fs = new FileChooser();
    fs.getExtensionFilters().add(FileExtension.DB.getFilter());
    return fs;
  }

  @FXML
  void configureNewDatabase() {
    Parent page = loadFxml("db/dbconfigNew");
    dbFolder.setText("S:\\ledenadministratie-ccnl\\db");
    dbName.setText("ccnl");
    Scene scene = new Scene(page);
    configStage.setScene(scene);
    configStage.setTitle("Locatie nieuwe database configureren");
    configStage.show();
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
    String dbLocation = handleSave();
    if (dbLocation != null) {
      initializeDatabase(DatabaseLocation.initialize(dbLocation));
    }
  }

  @FXML
  void enter(KeyEvent evt) {
    if (evt.getCode() == KeyCode.ENTER) {
      handleSave();
    }
  }

  @FXML
  void save() {
    handleSave();
  }

  private String handleSave() {
    if (validInput()) {
      String dbLocation = configureDatabaseLocation();
      String msg = DbProperties.writeFile(dbLocation);
      saveButton.setDisable(true);
      info = new Alert(AlertType.INFORMATION);
      info.setHeaderText(msg);
      info.setTitle("Database locatie");
      Stage cs = (Stage) info.getDialogPane().getScene().getWindow();
      cs.getIcons().add(getStageIcon());
      cs.setAlwaysOnTop(true);
      info.showAndWait();
      nextAction();
      return dbLocation;
    } else {
      saveButton.setDisable(true);
      return null;
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
    return name != null && !name.isBlank();
  }

  private boolean validFolder() {
    String folder = dbFolder.getText();
    if (folder != null) {
      folder = folder.replace('\\', '/');
      File dir = new File(folder);
      return dir.exists();
    }
    return false;
  }

  private String configureDatabaseLocation() {
    String folder = dbFolder.getText();
    if (folder.endsWith("\\")) {
      folder = folder.substring(0, folder.length() - 1);
    }
    return new StringBuilder().append(folder).append(File.separator).append(dbName.getText())
        .toString();
  }

  private void initializeDatabase(String dbLocation) {
    try (Connection connection = getConnection(dbLocation)) {
      checkAndLoad(connection);
    } catch (SQLException e1) {
      log.error("Failed to close connection", e1);
    }

  }

  public void checkAndLoad(Connection connection) {
    try (PreparedStatement preparedStatement =
        connection.prepareStatement("SELECT count(*) FROM SETTING")) {
      preparedStatement.getResultSet();
    } catch (SQLException e) {
      initialLoad(connection);
    }
  }

  private Connection getConnection(String dbLocation) {
    try {
      Class.forName(ApplicationProperties.getProperty("database.driver", "org.h2.Driver"));
      return DriverManager.getConnection(dbLocation,
          ApplicationProperties.getProperty("database.user", "sa"),
          ApplicationProperties.getProperty("database.password", ""));
    } catch (ClassNotFoundException | SQLException e) {
      log.error("Failed to initialize connection", e);
      throw new DatabaseException("Failed to initialize connection", e);
    }
  }
  
  private void initialLoad(Connection connection)  {
    try {
      loadInitSql(connection);
    } catch (SQLException e) {
      log.error("Failed to initialize database", e);
      throw new DatabaseException("Failed to initialize database", e);
    }
  }
  
  private void loadInitSql(Connection connection) throws SQLException {
    Statement st = null;
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/init.sql")))) {
      String line = reader.readLine();
      StringJoiner sj = new StringJoiner(" ");
      connection.setAutoCommit(false);
      st = connection.createStatement();
      while (line != null) {
        line = line.trim();
        sj.add(line);
        if (line.endsWith(";")) {
          st.execute(sj.toString());
          sj = new StringJoiner(" ");
        }
        line = reader.readLine();
      }
      connection.commit();
    } catch (IOException e) {
      connection.rollback();
      log.error("Unable to start: Error reading init.sql", e);
      throw new DatabaseException("Error reading init.sql", e);
    } finally {
      if (st != null) {
        st.close();
      }
    }
    
  }

}
