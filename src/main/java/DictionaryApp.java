import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DictionaryApp extends Application {

    private DictionaryManagement dictionaryManager = DictionaryManagement.getInstance();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dictionary App");

        TabPane tabPane = new TabPane();

        Tab searchTab = new Tab("Search");
        searchTab.setContent(createSearchTab());

        Tab translateTab = new Tab("Translate");
        translateTab.setContent(createTranslateTab());

        Tab pronounceTab = new Tab("Pronounce");
        pronounceTab.setContent(createPronounceTab());

        Tab manageTab = new Tab("Manage");
        manageTab.setContent(createManageTab());

        tabPane.getTabs().addAll(searchTab, translateTab, pronounceTab, manageTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createSearchTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        TextField searchField = new TextField();
        searchField.setPromptText("Enter a word to search");

        Button searchButton = new Button("Search");
        Label meaningLabel = new Label();

        searchButton.setOnAction(e -> {
            String word = searchField.getText();
            Word result = dictionaryManager.lookupWord(word);
            if (result != null) {
                meaningLabel.setText(result.getWordExplain());
            } else {
                meaningLabel.setText("Word not found!");
            }
        });

        vbox.getChildren().addAll(searchField, searchButton, meaningLabel);
        return vbox;
    }

    private VBox createTranslateTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        TextField translateField = new TextField();
        translateField.setPromptText("Enter a word to translate");

        ComboBox<String> directionBox = new ComboBox<>();
        directionBox.getItems().addAll("English to Vietnamese", "Vietnamese to English");
        directionBox.setValue("English to Vietnamese");

        Button translateButton = new Button("Translate");
        Label translationLabel = new Label();

        translateButton.setOnAction(e -> {
            String word = translateField.getText();
            String sourceLang = directionBox.getValue().equals("English to Vietnamese") ? "en" : "vi";
            String targetLang = directionBox.getValue().equals("English to Vietnamese") ? "vi" : "en";
            try {
                String translation = GoogleTranslate.translate(word, sourceLang, targetLang);
                translationLabel.setText(translation);
            } catch (Exception ex) {
                translationLabel.setText("Translation error!");
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(translateField, directionBox, translateButton, translationLabel);
        return vbox;
    }

    private VBox createPronounceTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        TextField pronounceField = new TextField();
        pronounceField.setPromptText("Enter a word to pronounce");

        Button pronounceButton = new Button("Pronounce");
        Label statusLabel = new Label();

        pronounceButton.setOnAction(e -> {
            String word = pronounceField.getText();
            try {
                GoogleTranslate.speak(word, "en");
                statusLabel.setText("Pronouncing: " + word);
            } catch (Exception ex) {
                statusLabel.setText("Pronunciation error!");
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(pronounceField, pronounceButton, statusLabel);
        return vbox;
    }

    private VBox createManageTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        // Add word section
        Label addLabel = new Label("Add Word");
        TextField addWordField = new TextField();
        addWordField.setPromptText("Enter word");
        TextField addMeaningField = new TextField();
        addMeaningField.setPromptText("Enter meaning");
        Button addButton = new Button("Add");
        Label addStatusLabel = new Label();

        addButton.setOnAction(e -> {
            String word = addWordField.getText();
            String meaning = addMeaningField.getText();
            if (!word.isEmpty() && !meaning.isEmpty()) {
                dictionaryManager.addWord(word, meaning);
                addStatusLabel.setText("Word added successfully!");
            } else {
                addStatusLabel.setText("Word or meaning cannot be empty!");
            }
        });

        // Remove word section
        Label removeLabel = new Label("Remove Word");
        TextField removeWordField = new TextField();
        removeWordField.setPromptText("Enter word to remove");
        Button removeButton = new Button("Remove");
        Label removeStatusLabel = new Label();

        removeButton.setOnAction(e -> {
            String word = removeWordField.getText();
            if (dictionaryManager.removeWord(word)) {
                removeStatusLabel.setText("Word removed successfully!");
            } else {
                removeStatusLabel.setText("Word not found!");
            }
        });

        vbox.getChildren().addAll(
                addLabel, addWordField, addMeaningField, addButton, addStatusLabel,
                removeLabel, removeWordField, removeButton, removeStatusLabel
        );
        return vbox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
