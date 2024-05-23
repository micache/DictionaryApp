import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class QuizGameUI extends QuizzGame {
    private Label questionLabel;
    private RadioButton[] optionButtons;
    private ToggleGroup optionsGroup;
    private Button nextButton;
    private Label scoreLabel;
    private Label feedbackLabel;
    private VBox vbox;

    private int currentQuestionIndex;
    private int score;
    private boolean showingFeedback;

    public QuizGameUI(VBox vbox) {
        super();
        this.vbox = vbox;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.showingFeedback = false;

        // Initialize UI components
        this.questionLabel = new Label();
        this.questionLabel.getStyleClass().add("question-label");

        this.optionButtons = new RadioButton[4];
        this.optionsGroup = new ToggleGroup();

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new RadioButton();
            optionButtons[i].setToggleGroup(optionsGroup);
            optionButtons[i].getStyleClass().add("option-button");
        }

        this.nextButton = new Button("Submit");
        this.nextButton.getStyleClass().add("next-button");
        this.scoreLabel = new Label();
        this.scoreLabel.getStyleClass().add("score-label");
        this.feedbackLabel = new Label();
        this.feedbackLabel.getStyleClass().add("feedback-label");

        // Set up the layout
        vbox.getChildren().addAll(questionLabel);
        for (RadioButton optionButton : optionButtons) {
            vbox.getChildren().add(optionButton);
        }
        vbox.getChildren().addAll(feedbackLabel, nextButton, scoreLabel);

        // Load questions and display the first one
        loadQuestionsFromFile(QuizGameUI.class.getResource("/game_data/quiz_questions.txt").getPath());
        displayQuestion(currentQuestionIndex);

        // Set the next button action
        nextButton.setOnAction(e -> {
            if (showingFeedback) {
                // Move to the next question
                currentQuestionIndex++;
                if (currentQuestionIndex < getQuestions().size()) {
                    displayQuestion(currentQuestionIndex);
                    feedbackLabel.setText(""); // Clear feedback for the next question
                    nextButton.setText("Submit");
                    showingFeedback = false;
                } else {
                    showScore();
                    nextButton.setDisable(true);
                }
            } else {
                // Show feedback
                checkAnswer();
                showingFeedback = true;
                nextButton.setText(currentQuestionIndex < getQuestions().size() - 1 ? "Next Question" : "Finish");
            }
        });
    }

    private void displayQuestion(int index) {
        if (index < 0 || index >= getQuestions().size()) {
            return;
        }

        Question question = getQuestions().get(index);
        questionLabel.setText(question.getQuestionText());

        char optionLetter = 'A';
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(optionLetter + ". " + question.getOptions()[i]);
            optionLetter++;
        }

        // Clear selection
        optionsGroup.selectToggle(null);
    }

    private void checkAnswer() {
        if (currentQuestionIndex < 0 || currentQuestionIndex >= getQuestions().size()) {
            return;
        }

        Question question = getQuestions().get(currentQuestionIndex);
        RadioButton selectedRadioButton = (RadioButton) optionsGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            char selectedOption = selectedRadioButton.getText().charAt(0);
            if (selectedOption == question.getCorrectAnswer()) {
                score++;
                feedbackLabel.setText("Correct!");
                feedbackLabel.setStyle("-fx-text-fill: #27ae60;"); // Green text for correct answer
            } else {
                feedbackLabel.setText("Incorrect. The correct answer is: " + question.getCorrectAnswer());
                feedbackLabel.setStyle("-fx-text-fill: #e74c3c;"); // Red text for incorrect answer
            }
        } else {
            feedbackLabel.setText("No option selected.");
            feedbackLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    private void showScore() {
        Platform.runLater(() -> scoreLabel.setText("Quiz over! Your score: " + score + "/" + getQuestions().size()));
    }
}
