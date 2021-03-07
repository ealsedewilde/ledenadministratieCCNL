package nl.ealse.javafx;

import java.awt.print.PrinterJob;
import java.util.List;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Generic Spring JavaFX integration class.
 * 
 * @author ealse
 *
 */
public abstract class SpringJavaFXBase extends Application {

  /**
   * The class annotated with {@code @SpringBootApplication}.
   */
  private final Class<?> springbootClass;

  private ConfigurableApplicationContext context;

  protected SpringJavaFXBase(Class<? extends Object> springbootClass) {
    if (springbootClass.getAnnotation(SpringBootApplication.class) == null) {
      throw new IllegalArgumentException(
          "Please supply a '@SpringBootApplication' annotated class");
    }
    this.springbootClass = springbootClass;

  }

  /**
   * Both SpringBoot and JavaFX are ready to run. This method gets called after the init(). It will
   * publish a StageReadyEvent to inform other components that the application is ready to run.
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    this.context.publishEvent(new StageReadyEvent(primaryStage));
  }

  /**
   * Initialize the Spring {@code ApplicationContext}. It is initialized with headless(false) so the
   * AWT {@link PrinterJob#printDialog()} can be used for printing.
   */
  @Override
  public void init() {
    ApplicationContextInitializer<GenericApplicationContext> initializer = applicationContext -> {
      applicationContext.registerBean(Application.class, () -> SpringJavaFXBase.this);
      applicationContext.registerBean(Parameters.class, this::getParameters);
      applicationContext.registerBean(HostServices.class, this::getHostServices);
    };
    List<String> rawParameters = getParameters().getRaw();
    this.context = new SpringApplicationBuilder().headless(false).sources(springbootClass)
        .initializers(initializer).run(rawParameters.toArray(new String[rawParameters.size()]));
  }

  @Override
  public void stop() throws Exception {
    context.close();
    Platform.exit();
  }

  /**
   * The event indicating that the application is ready to handle the JavaFX stage.
   * 
   * @author ealse
   */
  @SuppressWarnings("serial")
  public static class StageReadyEvent extends ApplicationEvent {

    public StageReadyEvent(Stage source) {
      super(source);
    }

    public Stage getStage() {
      return (Stage) getSource();
    }

  }

}
