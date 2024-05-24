import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * WordleGameUI class extends EnglishWordleGame to provide a JavaFX user interface
 * for the Wordle game. It handles user input, displays feedback, and updates
 * the game state.
 */
public class WordleGameUI extends WordleGame {
    private TextField guessField;
    private Button submitButton;
    private Button playAgainButton;
    private Label attemptsLabel;
    private Label feedbackLabel;
    private VBox vbox;
    private VBox guessesBox;

    /**
     * Constructor for WordleGameUI.
     * Initializes the UI components and sets up the layout.
     *
     * @param vbox the VBox layout container
     */
    public WordleGameUI(VBox vbox) {
        super();
        this.vbox = vbox;

        // Initialize UI components
        Label instructionsLabel = new Label("Guess the word:");
        instructionsLabel.setFont(new Font("Arial", 20));
        this.guessField = new TextField();
        this.guessField.setPromptText("Enter your guess");
        this.guessField.setFont(new Font("Arial", 18));
        this.submitButton = new Button("Submit");
        this.submitButton.setFont(new Font("Arial", 18));
        this.attemptsLabel = new Label();
        this.attemptsLabel.setFont(new Font("Arial", 18));
        this.guessesBox = new VBox(10);
        this.guessesBox.setAlignment(Pos.CENTER);
        this.feedbackLabel = new Label();
        this.feedbackLabel.setFont(new Font("Arial", 18));
        this.playAgainButton = new Button("Play Again");
        this.playAgainButton.setFont(new Font("Arial", 18));
        this.playAgainButton.setVisible(false);

        // Set up the layout
        vbox.getChildren().addAll(instructionsLabel, guessField, submitButton, guessesBox, feedbackLabel, attemptsLabel, playAgainButton);

        // Load words and start the game
        loadWordsFromFile();
        updateAttemptsLabel();

        // Set the submit button action
        submitButton.setOnAction(e -> {
            if (hasAttemptsLeft()) {
                String guess = guessField.getText();
                if (guess.length() != getTargetWord().length()) {
                    feedbackLabel.setText("Please enter a word with " + getTargetWord().length() + " letters.");
                } else {
                    showFeedback(checkGuess(guess));
                    updateAttemptsLabel();
                    if (guess.equals(getTargetWord()) || !hasAttemptsLeft()) {
                        endGame();
                    }
                }
            }
        });

        // Set the play again button action
        playAgainButton.setOnAction(e -> resetGame());
    }

    /**
     * Updates the attempts label to display the current number of attempts.
     */
    private void updateAttemptsLabel() {
        Platform.runLater(() -> attemptsLabel.setText("Attempts: " + getAttempts() + "/" + getMaxAttempts()));
    }

    /**
     * Shows feedback for the given guess.
     *
     * @param feedback the feedback for the guess
     */
    private void showFeedback(String feedback) {
        HBox guessBox = new HBox(5);
        guessBox.setAlignment(Pos.CENTER);
        guessBox.setSpacing(5);
        String[] feedbackParts = feedback.split(" ");

        for (String part : feedbackParts) {
            Label charLabel = new Label(part);
            charLabel.setFont(new Font("Arial", 24));
            charLabel.setMinSize(40, 40);
            charLabel.setAlignment(Pos.CENTER);
            charLabel.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
            charLabel.setPadding(new Insets(5));

            switch (part) {
                case "_":
                    charLabel.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
                    break;
                case "*":
                    charLabel.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    break;
                default:
                    charLabel.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                    break;
            }

            guessBox.getChildren().add(charLabel);
        }

        guessesBox.getChildren().add(guessBox);
        guessField.clear();
    }

    /**
     * Ends the game by showing the correct word and enabling the play again button.
     */
    private void endGame() {
        feedbackLabel.setText("The correct word was: " + getTargetWord());
        submitButton.setDisable(true);
        playAgainButton.setVisible(true);
    }

    /**
     * Resets the game to start a new round.
     */
    private void resetGame() {
        clearGame();
        loadWordsFromFile();
        updateAttemptsLabel();
        feedbackLabel.setText("");
        submitButton.setDisable(false);
        playAgainButton.setVisible(false);
    }

    /**
     * Clears the game state for a new round.
     */
    private void clearGame() {
        guessesBox.getChildren().clear();
        guessField.clear();
        setAttempts(0);
    }
}
