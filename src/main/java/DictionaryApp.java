import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Main application class for the Dictionary App.
 */
public class DictionaryApp extends Application {

    private DictionaryManagement dictionaryManager = DictionaryManagement.getInstance();

    /**
     * The entry point of the JavaFX application.
     *
     * @param primaryStage the primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Super App");

        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(10));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("-fx-background-color: #2c3e50; -fx-pref-width: 120px;");

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab searchTab = new Tab("Search", createTabContent(createSearchTab(), "search.css"));
        Tab translateTab = new Tab("Translate", createTabContent(createTranslateTab(), "translate.css"));
        Tab pronounceTab = new Tab("Pronounce", createTabContent(createPronounceTab(), "pronounce.css"));
        Tab manageTab = new Tab("Manage", createTabContent(createManageTab(), "manage.css"));

        tabPane.getTabs().addAll(searchTab, translateTab, pronounceTab, manageTab);

        sidebar.getChildren().addAll(
                createSidebarItem("search.png", "Search", tabPane, searchTab),
                createSidebarItem("translate.png", "Translate", tabPane, translateTab),
                createSidebarItem("pronounce.png", "Pronounce", tabPane, pronounceTab),
                createSidebarItem("manage.png", "Manage", tabPane, manageTab)
        );

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates a sidebar item with an icon and a label.
     *
     * @param iconPath the path to the icon image.
     * @param text     the label text.
     * @param tabPane  the TabPane to switch tabs.
     * @param tab      the Tab to be selected.
     * @return a VBox containing the sidebar item.
     */
    private VBox createSidebarItem(String iconPath, String text, TabPane tabPane, Tab tab) {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-cursor: hand;");

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/icons/" + iconPath)));
        imageView.setFitHeight(24);
        imageView.setFitWidth(24);

        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        vbox.getChildren().addAll(imageView, label);
        vbox.setOnMouseClicked(event -> tabPane.getSelectionModel().select(tab));

        return vbox;
    }

    /**
     * Creates the content for a tab and applies the specified CSS file.
     *
     * @param vbox    the VBox to hold the tab content.
     * @param cssFile the CSS file to style the content.
     * @return the styled VBox containing the tab content.
     */
    private VBox createTabContent(VBox vbox, String cssFile) {
        vbox.setPadding(new Insets(15));
        vbox.setSpacing(10);
        vbox.getStylesheets().add(getClass().getResource("/frontend/" + cssFile).toExternalForm());
        return vbox;
    }

    /**
     * Creates the Search tab content.
     *
     * @return a VBox containing the Search tab content.
     */
    private VBox createSearchTab() {
        VBox vbox = new VBox();

        Label titleLabel = new Label("Type the word you want to search...");
        titleLabel.getStyleClass().add("title-label");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter a word to search");

        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("action-button");

        Label meaningLabel = new Label();
        meaningLabel.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(meaningLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);

        searchButton.setOnAction(e -> {
            String word = searchField.getText();
            Word result = dictionaryManager.lookupWord(word);
            if (result != null) {
                meaningLabel.setText(result.getWordExplain());
            } else {
                meaningLabel.setText("Word not found!");
            }
        });

        vbox.getChildren().addAll(titleLabel, searchField, searchButton, scrollPane);
        return vbox;
    }

    /**
     * Creates the Translate tab content.
     *
     * @return a VBox containing the Translate tab content.
     */
    private VBox createTranslateTab() {
        VBox vbox = new VBox();

        Label titleLabel = new Label("Type the word you want to translate...");
        titleLabel.getStyleClass().add("title-label");

        TextField translateField = new TextField();
        translateField.setPromptText("Enter a word to translate");

        ComboBox<String> directionBox = new ComboBox<>();
        directionBox.getItems().addAll("English to Vietnamese", "Vietnamese to English");
        directionBox.setValue("English to Vietnamese");

        Button translateButton = new Button("Translate");
        translateButton.getStyleClass().add("action-button");

        TextField translationField = new TextField();
        translationField.setEditable(false);

        translateButton.setOnAction(e -> {
            String word = translateField.getText();
            String sourceLang = directionBox.getValue().equals("English to Vietnamese") ? "en" : "vi";
            String targetLang = directionBox.getValue().equals("English to Vietnamese") ? "vi" : "en";
            try {
                String translation = GoogleTranslate.translate(word, sourceLang, targetLang);
                translationField.setText(translation);
            } catch (Exception ex) {
                translationField.setText("Translation error!");
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(titleLabel, translateField, directionBox, translateButton, translationField);
        return vbox;
    }

    /**
     * Creates the Pronounce tab content.
     *
     * @return a VBox containing the Pronounce tab content.
     */
    private VBox createPronounceTab() {
        VBox vbox = new VBox();

        Label titleLabel = new Label("Type the word you want to pronounce...");
        titleLabel.getStyleClass().add("title-label");

        TextField pronounceField = new TextField();
        pronounceField.setPromptText("Enter a word to pronounce");

        Button pronounceButton = new Button("Pronounce");
        pronounceButton.getStyleClass().add("action-button");

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

        vbox.getChildren().addAll(titleLabel, pronounceField, pronounceButton, statusLabel);
        return vbox;
    }

    /**
     * Creates the Manage tab content.
     *
     * @return a VBox containing the Manage tab content.
     */
    private VBox createManageTab() {
        VBox vbox = new VBox();

        // Add word section
        Label addLabel = new Label("Add Word");
        addLabel.getStyleClass().add("section-label");

        TextField addWordField = new TextField();
        addWordField.setPromptText("Enter word");

        TextField addMeaningField = new TextField();
        addMeaningField.setPromptText("Enter meaning");

        Button addButton = new Button("Add");
        addButton.getStyleClass().add("action-button");

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
        removeLabel.getStyleClass().add("section-label");

        TextField removeWordField = new TextField();
        removeWordField.setPromptText("Enter word to remove");

        Button removeButton = new Button("Remove");
        removeButton.getStyleClass().add("action-button");

        Label removeStatusLabel = new Label();

        removeButton.setOnAction(e -> {
            String word = removeWordField.getText();
            if (dictionaryManager.removeWord(word)) {
                removeStatusLabel.setText("Word removed successfully!");
            } else {
                removeStatusLabel.setText("Word not found!");
            }
        });

        // Edit word section
        Label editLabel = new Label("Edit Word");
        editLabel.getStyleClass().add("section-label");

        TextField editWordField = new TextField();
        editWordField.setPromptText("Enter word to edit");

        TextField editMeaningField = new TextField();
        editMeaningField.setPromptText("Enter new meaning");

        Button editButton = new Button("Edit");
        editButton.getStyleClass().add("action-button");

        Label editStatusLabel = new Label();

        editButton.setOnAction(e -> {
            String word = editWordField.getText();
            String meaning = editMeaningField.getText();
            if (!word.isEmpty() && !meaning.isEmpty()) {
                if (dictionaryManager.editWord(word, meaning)) {
                    editStatusLabel.setText("Word edited successfully!");
                } else {
                    editStatusLabel.setText("Word not found!");
                }
            } else {
                editStatusLabel.setText("Word or meaning cannot be empty!");
            }
        });

        vbox.getChildren().addAll(
                addLabel, addWordField, addMeaningField, addButton, addStatusLabel,
                removeLabel, removeWordField, removeButton, removeStatusLabel,
                editLabel, editWordField, editMeaningField, editButton, editStatusLabel
        );
        return vbox;
    }

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        DictionaryCommandline.loadDictionaryFromFile();
        launch(args);
    }
}
