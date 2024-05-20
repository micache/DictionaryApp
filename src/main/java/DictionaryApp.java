import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class DictionaryApp extends Application {
    private Map<String, String> dictionary = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        // Top
        Label header = new Label("Dictionary");
        header.getStyleClass().add("header-label");

        // Center
        GridPane centerGrid = new GridPane();
        centerGrid.setHgap(10);
        centerGrid.setVgap(10);

        TextField wordInput = new TextField();
        wordInput.setPromptText("Enter a word");

        TextField definitionInput = new TextField();
        definitionInput.setPromptText("Enter the definition");

        Button addButton = new Button("Add Word");
        Button searchButton = new Button("Search Word");

        TextArea definitionArea = new TextArea();
        definitionArea.setEditable(false);

        centerGrid.add(new Label("Word:"), 0, 0);
        centerGrid.add(wordInput, 1, 0);
        centerGrid.add(new Label("Definition:"), 0, 1);
        centerGrid.add(definitionInput, 1, 1);
        centerGrid.add(addButton, 2, 0);
        centerGrid.add(searchButton, 2, 1);
        centerGrid.add(definitionArea, 0, 2, 3, 1);

        // Actions
        addButton.setOnAction(e -> {
            dictionary.put(wordInput.getText(), definitionInput.getText());
            wordInput.clear();
            definitionInput.clear();
            definitionArea.setText("Word added successfully!");
        });

        searchButton.setOnAction(e -> {
            String definition = dictionary.get(wordInput.getText());
            if (definition != null) {
                definitionArea.setText(definition);
            } else {
                definitionArea.setText("Word not found!");
            }
        });

        // Layout
        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(centerGrid);

        // Styling
        Scene scene = new Scene(root, 500, 400);
        scene.getStylesheets().add("style.css");  // Link to your CSS file

        // Stage
        primaryStage.setTitle("Enhanced Dictionary App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
