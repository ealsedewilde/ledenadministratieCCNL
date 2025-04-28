package nl.ealse.ccnl.control;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.ealse.ccnl.MainStage;
import nl.ealse.ccnl.control.button.CancelButton;
import nl.ealse.ccnl.control.button.DeleteButton;
import nl.ealse.ccnl.control.button.PrintButton;
import nl.ealse.ccnl.control.button.SaveButton;
import nl.ealse.ccnl.ledenadministratie.model.Document;
import nl.ealse.ccnl.ledenadministratie.model.Member;
import nl.ealse.javafx.util.ImagePrintDocument;
import nl.ealse.javafx.util.PdfPrintDocument;
import nl.ealse.javafx.util.PrintDocument;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * Simple PDF-viewer with paging capability.
 *
 * @author ealse
 *
 */
@Slf4j
public class DocumentViewer extends BorderPane {

  private static final String HEADER_TEXT = "Pagina %d van %d";

  private Stage documentViewerStage;

  private Scene scene;
  private Region parent;

  /**
   * Title of the popup window of the PDF-viewer.
   */
  @Getter
  @Setter
  private Object windowTitle;

  private Label header;

  private Button prevButton;

  private Button nextButton;

  private PDFRenderer renderer;

  private int pageNum;

  private int pages;

  @Getter
  private PrintDocument document;

  private DocumentViewer() {
    this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
        CornerRadii.EMPTY, BorderWidths.DEFAULT)));
  }

  /**
   * Show a PDF for the document owner.
   *
   * @param document - document to show (PDF +owner)
   */
  public void showDocument(Document document) {
    if (isPdf(document.getDocumentName())) {
      showPdf(document.getPdf(), document.getOwner());
    } else {
      showImage(document.getPdf(), document.getOwner());
    }
  }

  /**
   * Display aPDF-document.
   *
   * @param selectedFile PDF-file
   * @param member the owner of the PDF-document
   */
  public void showDocument(File selectedFile, Member member) {
    if (isPdf(selectedFile.getName())) {
      showPdf(toByteArray(selectedFile), member);
    } else {
      showImage(toByteArray(selectedFile), member);
    }
  }

  /**
   * Show the PDF page by page.
   *
   * @param pdf binary PDF-content
   * @param member PDF-owner
   */
  public void showPdf(byte[] pdf, Member member) {
    this.document = new PdfPrintDocument(pdf);
    try {
      PDDocument pdfDocument = Loader.loadPDF(pdf);
      renderer = new PDFRenderer(pdfDocument);
      BufferedImage bufferedImage = renderer.renderImageWithDPI(0, 72);
      pages = pdfDocument.getPages().getCount();
      if (pages == 1) {
        initializeSinglePage(bufferedImage.getWidth(), bufferedImage.getHeight());
      } else {
        initializeMultiPage(bufferedImage.getWidth(), bufferedImage.getHeight());
      }
      documentViewerStage.setTitle(getStageTitle(member));
      show();

      pageNum = 0;
      showPage();
    } catch (IOException e) {
      String msg = "Error rendering PDF";
      log.error(msg, e);
      throw new PDFViewerException(msg, e);
    }
  }

  private void showImage(byte[] imageBytes, Member member) {
    try {
      Image image =
          SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(imageBytes)), null);
      double ratio = 841 / image.getHeight();
      double height = image.getHeight() * ratio;
      double width = image.getWidth() * ratio;
      this.document = new ImagePrintDocument(image);
      initializeSinglePage(width, height);
      documentViewerStage.setTitle(getStageTitle(member));
      show();
      ImageView imageView = new ImageView(image);
      imageView.setFitHeight(height);
      imageView.setFitWidth(width);
      this.setCenter(imageView);
    } catch (IOException e) {
      String msg = "Error rendering image";
      log.error(msg, e);
      throw new PDFViewerException(msg, e);
    }
  }

  private void show() {
    documentViewerStage.show();
    double y = documentViewerStage.getY();
    if (y < 0) {
      documentViewerStage.setY(0d);
      double scaleY = 1d + (y * 0.0022d);
      documentViewerStage.setHeight(documentViewerStage.getHeight() * scaleY);
    }
  }

  /**
   * Close this PDF viewer.
   */
  public void close() {
    documentViewerStage.close();
  }

  private boolean isPdf(String documentName) {
    return documentName.toLowerCase().endsWith(".pdf");
  }

  /**
   * Initialize for a single page PDF.
   */
  private void initializeSinglePage(double width, double height) {
    initializePage(width + 10d, height + 60d);
    this.setTop(null);
    this.setRight(null);
    this.setLeft(null);
  }

  /**
   * Initialize for a multi page PDF.
   */
  private void initializeMultiPage(double width, double height) {
    initializePage(width + 135d, height + 35d);

    this.setTop(header);
    this.setRight(nextButton);
    this.setLeft(prevButton);
    prevButton.setDisable(true);
    nextButton.setDisable(false);
    nextButton.requestFocus();
  }

  private void initializePage(double width, double height) {
    parent.setPrefWidth(width);
    parent.setPrefHeight(height);
    
    final Stage stage = new Stage();
    documentViewerStage = stage;
    stage.initOwner(MainStage.getStage());
    stage.getIcons().add(MainStage.getIcon());
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setScene(scene);
    stage.centerOnScreen();
    stage.sizeToScene();
    stage.setResizable(false);
    stage.heightProperty().addListener((obs, oldVal, newVal) -> {
      if (!Double.isNaN((double) oldVal)) {
        parent.setPrefHeight(newVal.doubleValue() - 160d);
      }
    });
  }

  private void previousPage() {
    pageNum--;
    newPage();
  }

  private void nextPage() {
    pageNum++;
    newPage();
  }

  private void newPage() {
    nextButton.setDisable(pageNum == pages - 1);
    prevButton.setDisable(pageNum == 0);
    showPage();
  }

  private void showPage() {
    try {

      BufferedImage bufferedImage = renderer.renderImageWithDPI(pageNum, 72);
      Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
      ImageView imageView = new ImageView(fxImage);
      this.setCenter(imageView);
      if (pages > 1) {
        header.setText(String.format(HEADER_TEXT, pageNum + 1, pages));
      }
    } catch (IOException e) {
      log.error("Error getting page", e);
      Thread.currentThread().interrupt();
      throw new PDFViewerException("Error getting page", e);
    }

  }

  private String getStageTitle(Member member) {
    return String.format(windowTitle.toString(), member.getMemberNumber(), member.getFullName());
  }

  private byte[] toByteArray(File selectedFile) {
    try (FileInputStream fis = new FileInputStream(selectedFile)) {
      return fis.readAllBytes();
    } catch (IOException e) {
      log.error("Error reading file", e);
      throw new PDFViewerException(e);
    }
  }

  private static class PagingButton extends Button {

    public PagingButton(String text) {
      initialize(text);
    }

    private void initialize(String text) {
      setText(text);
      setStyle("-fx-font-size: 18px; -fx-font-weight: bold;" + "-fx-background-radius: 5em; "
          + "-fx-min-width: 30px; " + "-fx-min-height: 30px;"
          + "-fx-max-width: 30px; -fx-alignment: top-center;"
          + "-fx-max-height: 30px; -fx-padding: 0px;");
      BorderPane.setAlignment(this, Pos.CENTER);
      BorderPane.setMargin(this, new Insets(15));
    }

  }

  /**
   * Always obtain a DocumentViewer via the builder pattern.
   *
   * @return builder for a PDFViwer
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder for DocumentViewer.
   */
  public static class Builder {

    private DocumentViewer instance = new DocumentViewer();
    private HBox buttons = new HBox();

    public Builder() {
      initialize();
    }

    public Builder withCancelButton(EventHandler<ActionEvent> handler) {
      CancelButton cb = new CancelButton();
      cb.setText("Sluiten");
      addButton(cb, handler);
      return this;
    }

    public Builder withDeleteButton(EventHandler<ActionEvent> handler) {
      addButton(new DeleteButton(), handler);
      return this;
    }

    private void initialize() {
      VBox root = new VBox();
      root.setPadding(new Insets(10.d));
      instance.scene = new Scene(root);
      
      ScrollPane pane = new ScrollPane();
      instance.parent = pane;
      pane.setContent(instance);
      pane.setHbarPolicy(ScrollBarPolicy.NEVER);
      pane.setStyle("-fx-background-color:transparent;");
      pane.setFitToWidth(true);
      root.getChildren().add(pane);
      
      buttons = new HBox();
      buttons.setPadding(new Insets(10d, 0d, 0d, 0d));
      buttons.setSpacing(20d);
      root.getChildren().add(buttons);
    
      instance.nextButton = new PagingButton("\u00BB");
      instance.nextButton.setOnAction(e -> instance.nextPage());
      instance.prevButton = new PagingButton("\u00AB");
      instance.prevButton.setOnAction(e -> instance.previousPage());
      instance.header = new Label();
      instance.header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
      
      BorderPane.setAlignment(instance.header, Pos.TOP_CENTER);
    }

    public Builder withPrintButton(EventHandler<ActionEvent> handler) {
      addButton(new PrintButton(), handler);
      return this;
    }

    public Builder withSaveButton(EventHandler<ActionEvent> handler) {
      SaveButton sb = new SaveButton();
      sb.setText("Toevoegen");
      addButton(sb, handler);
      return this;
    }

    private void addButton(Button button, EventHandler<javafx.event.ActionEvent> handler) {
      button.setOnAction(handler);
      buttons.getChildren().add(button);
    }


    public DocumentViewer build() {
      return instance;
    }

  }

}
