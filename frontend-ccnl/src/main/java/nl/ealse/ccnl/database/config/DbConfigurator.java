package nl.ealse.ccnl.database.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.javafx.ImagesMap;

@Slf4j
public class DbConfigurator extends BaseDbConfigurator {

  private static final String FXML_TEMPLATE = "/fxml/%s.fxml";
  private final Consumer<Stage> nextActionConsumer;

  public DbConfigurator(Stage primaryStage, Consumer<Stage> consumer) {
    super(primaryStage);
    this.nextActionConsumer = consumer;
  }

  @Override
  protected Parent loadFxml(String fxmlName) {
    try (InputStream is = getClass().getResourceAsStream(String.format(FXML_TEMPLATE, fxmlName))) {
      FXMLLoader loader = new FXMLLoader();
      loader.setController(this);
      return loader.load(is);
    } catch (IOException e) {
      log.error("Failed to load FXML", e);
      return null;
    }
  }

  @Override
  protected Image getStageIcon() {
    return ImagesMap.get("Citroen.png");
  }

  @Override
  protected void nextAction() {
    getConfigStage().close();
    nextActionConsumer.accept(getPrimaryStage());
    
  }
}
