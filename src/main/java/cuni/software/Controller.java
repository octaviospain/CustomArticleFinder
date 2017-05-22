package cuni.software;

import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.*;
import javafx.stage.*;
import javafx.stage.FileChooser.*;
import javafx.util.converter.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * @author Octavio Calleya
 */
public class Controller {

    @FXML
    private TextField searchField;
    @FXML
    private ListView<Hyperlink> listView;
    @FXML
    private Button searchButton;
    @FXML
    private Button loadButton;
    @FXML
    private TextArea textArea;
    @FXML
    private Spinner<Double> similaritySpinner;

    private HostServices hostServices;
    private BooleanProperty searchingProperty = new SimpleBooleanProperty(false);
    private SearchEngine searchEngine = new SearchEngine();
    private List<RssFeed> loadedRssFeeds;

    @FXML
    public void initialize() {
        searchButton.disableProperty().bind(searchingProperty.not().and(searchField.textProperty().isEmpty()));
        loadButton.disableProperty().bind(searchingProperty);
        loadButton.setOnAction(e -> loadRssFromFile());
        searchButton.setOnAction(e -> performSearch());
        DoubleSpinnerValueFactory doubleSpinnerValueFactory = new DoubleSpinnerValueFactory(1.550, 1.575);
        doubleSpinnerValueFactory.setAmountToStepBy(0.00025);
        doubleSpinnerValueFactory.setConverter(new DoubleStringConverter());
        similaritySpinner.setValueFactory(doubleSpinnerValueFactory);
    }

    private void performSearch() {
        new Thread(() -> {
            Double similarityValue = similaritySpinner.getValue();
            List<Article> relatedArticles = searchEngine.findRelatedArticles(searchField.getText(), similarityValue);
            Platform.runLater(() -> log("Found " + relatedArticles.size() + " articles"));
            addArticlesToList(relatedArticles);
        }).start();
    }

    private void addArticlesToList(List<Article> articles) {
        Platform.runLater(() -> {
            ObservableList<Hyperlink> links = FXCollections.observableArrayList();
            articles.forEach(article -> {
                Hyperlink link = new Hyperlink(article.getUri());
                link.setOnAction(e -> hostServices.showDocument(article.getUri()));
                links.add(link);
            });
            listView.setItems(links);
        });
    }

    private void loadRssFromFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select file...");
        chooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
        File feedsFile = chooser.showOpenDialog(searchField.getScene().getWindow());
        if (feedsFile != null) {
            new Thread(() -> loadTask(feedsFile)).start();
            log("Importing feeds...");
            searchingProperty.setValue(true);
        }
    }

    private void loadTask(File feedsFile) {
        try {
            loadedRssFeeds = RssFeedParse.fromFile(feedsFile);
            printParsedFeeds();
            addToSearchEngine();
        }
        catch (IOException e) {
            log("Error loading rss feeds from file: " + e.getMessage());
        }
        finally {
            Platform.runLater(() -> searchingProperty.setValue(false));
        }
    }

    private void addToSearchEngine() {
        List<Article> allArticles = loadedRssFeeds.stream()
                                                  .flatMap(feed -> feed.getArticles().stream())
                                                  .collect(Collectors.toList());
        searchEngine.addArticles(allArticles);
    }

    private void printParsedFeeds() {
        loadedRssFeeds.forEach(feed -> {
            log("\t\t" + feed.getUrl().toString() + "\n\tLoaded articles:");
            feed.getArticles().forEach(article -> log(article.toString()));
        });
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void log(String message) {
        Platform.runLater(() -> textArea.appendText(message + "\n"));
    }
}