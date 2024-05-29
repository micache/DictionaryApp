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
        Tab quizGameTab = new Tab("Quiz Game", createTabContent(createQuizGameTab(), "quizgame.css"));
        Tab wordleGameTab = new Tab("Wordle Game", createTabContent(createWordleGameTab(), "wordlegame.css"));

        tabPane.getTabs().addAll(searchTab, translateTab, pronounceTab, manageTab, quizGameTab, wordleGameTab);

        sidebar.getChildren().addAll(
                createSidebarItem("search.png", "Search", tabPane, searchTab),
                createSidebarItem("translate.png", "Translate", tabPane, translateTab),
                createSidebarItem("pronounce.png", "Pronounce", tabPane, pronounceTab),
                createSidebarItem("manage.png", "Manage", tabPane, manageTab),
                createSidebarItem("quizgame.png", "Quiz Game", tabPane, quizGameTab),
                createSidebarItem("wordlegame.png", "Wordle Game", tabPane, wordleGameTab)
        );

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 1200, 800);
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
        label.setStyle("-fx-text-fill: white; -fx-font-size: 16px;"); // Increased font size

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
        titleLabel.setStyle("-fx-font-size: 16px;"); // Increased font size

        TextField searchField = new TextField();
        searchField.setPromptText("Enter a word to search");
        searchField.setStyle("-fx-font-size: 16px;"); // Increased font size

        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("action-button");
        searchButton.setStyle("-fx-font-size: 16px;"); // Increased font size

        Label meaningLabel = new Label();
        meaningLabel.setWrapText(true);
        meaningLabel.setStyle("-fx-font-size: 16px;"); // Increased font size

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(meaningLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);

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
        titleLabel.setStyle("-fx-font-size: 16px;"); // Increased font size

        TextField translateField = new TextField();
        translateField.setPromptText("Enter a word to translate");
        translateField.setStyle("-fx-font-size: 16px;"); // Increased font size
        translateField.setPrefHeight(100);

        ComboBox<String> directionBox = new ComboBox<>();
        directionBox.getItems().addAll("English to Vietnamese", "Vietnamese to English");
        directionBox.setValue("English to Vietnamese");
        directionBox.setStyle("-fx-font-size: 16px;"); // Increased font size

        Button translateButton = new Button("Translate");
        translateButton.getStyleClass().add("action-button");
        translateButton.setStyle("-fx-font-size: 16px;"); // Increased font size

        TextField translationField = new TextField();
        translationField.setEditable(false);
        translationField.setStyle("-fx-font-size: 16px;"); // Increased font size
        translationField.setPrefHeight(100);

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
        titleLabel.setStyle("-fx-font-size: 16px;"); // Increased font size

        TextField pronounceField = new TextField();
        pronounceField.setPromptText("Enter a word to pronounce");
        pronounceField.setStyle("-fx-font-size: 16px;"); // Increased font size

        Button pronounceButton = new Button("Pronounce");
        pronounceButton.getStyleClass().add("action-button");
        pronounceButton.setStyle("-fx-font-size: 16px;"); // Increased font size

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 16px;"); // Increased font size

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
        addLabel.getStyleClass().add("title-label");
        addLabel.setStyle("-fx-font-size: 16px;"); // Increased font size

        TextField addWordField = new TextField();
        addWordField.setPromptText("Enter word");
        addWordField.setStyle("-fx-font-size: 16px;"); // Increased font size

        TextField addMeaningField = new TextField();
        addMeaningField.setPromptText("Enter meaning");
        addMeaningField.setStyle("-fx-font-size: 16px;"); // Increased font size

        Button addButton = new Button("Add");
        addButton.getStyleClass().add("action-button");
        addButton.setStyle("-fx-font-size: 16px;"); // Increased font size

        Label addStatusLabel = new Label();
        addStatusLabel.setStyle("-fx-font-size: 16px;"); // Increased font size

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
        removeLabel.getStyleClass().add("title-label");
        removeLabel.setStyle("-fx-font-size: 16px;"); // Increased font size

        TextField removeWordField = new TextField();
        removeWordField.setPromptText("Enter word to remove");
        removeWordField.setStyle("-fx-font-size: 16px;"); // Increased font size

        Button removeButton = new Button("Remove");
        removeButton.getStyleClass().add("action-button");
        removeButton.setStyle("-fx-font-size: 16px;"); // Increased font size

        Label removeStatusLabel = new Label();
        removeStatusLabel.setStyle("-fx-font-size: 16px;"); // Increased font size

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
        editLabel.getStyleClass().add("title-label");
        editLabel.setStyle("-fx-font-size: 16px;"); // Increased font size

        TextField editWordField = new TextField();
        editWordField.setPromptText("Enter word to edit");
        editWordField.setStyle("-fx-font-size: 16px;"); // Increased font size

        TextField editMeaningField = new TextField();
        editMeaningField.setPromptText("Enter new meaning");
        editMeaningField.setStyle("-fx-font-size: 16px;"); // Increased font size

        Button editButton = new Button("Edit");
        editButton.getStyleClass().add("action-button");
        editButton.setStyle("-fx-font-size: 16px;"); // Increased font size

        Label editStatusLabel = new Label();
        editStatusLabel.setStyle("-fx-font-size: 16px;"); // Increased font size

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
     * Create Quiz Game UI
     *
     * @return vbox
     */
    private VBox createQuizGameTab() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        // Create QuizGameUI instance
        QuizGameUI quizGameUI = new QuizGameUI(vbox);

        return vbox;
    }

    /**
     * Create Wordle Game UI
     *
     * @return vbox
     */
    private VBox createWordleGameTab() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        // Create WordleGameUI instance
        WordleGameUI wordleGameUI = new WordleGameUI(vbox);

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
