import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class QuizzGame {
    private ArrayList<Question> questions;
    private int score;

    public QuizzGame() {
        questions = new ArrayList<>();
        score = 0;
    }

    // Inner class to represent a Question
    private class Question {
        private String questionText;
        private String[] options;
        private char correctAnswerLetter;

        public Question(String questionText, String[] options, char correctAnswerLetter) {
            this.questionText = questionText;
            this.options = options;
            this.correctAnswerLetter = correctAnswerLetter;
        }

        public String getQuestionText() {
            return questionText;
        }

        public String[] getOptions() {
            return options;
        }

        public char getCorrectAnswerLetter() {
            return correctAnswerLetter;
        }
    }

    // Method to load questions from a file
    public void loadQuestionsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("/");
                if (parts.length == 6) {
                    String questionText = parts[0];
                    String[] options = {parts[1], parts[2], parts[3], parts[4]};
                    char correctAnswerLetter = parts[5].charAt(0);
                    addQuestion(questionText, options, correctAnswerLetter);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    // Method to add a new question
    public void addQuestion(String questionText, String[] options, char correctAnswerLetter) {
        questions.add(new Question(questionText, options, correctAnswerLetter));
    }

    // Method to display a question
    public void displayQuestion(int index) {
        if (index < 0 || index >= questions.size()) {
            System.out.println("Invalid question index.");
        } else {
            Question question = questions.get(index);
            System.out.println(question.getQuestionText());
            char optionLetter = 'A';
            for (String option : question.getOptions()) {
                System.out.println("[" + optionLetter + "] " + option);
                optionLetter++;
            }
        }
    }

    // Method to check the answer
    public void checkAnswer(int index, char userAnswer) {
        if (index < 0 || index >= questions.size()) {
            System.out.println("Invalid question index.");
        } else {
            Question question = questions.get(index);
            if (userAnswer == question.getCorrectAnswerLetter()) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Incorrect. The correct answer is: " + "[" + question.getCorrectAnswerLetter() + "] " + question.getOptions()[question.getCorrectAnswerLetter() - 'A']);
            }
        }
    }

    // Method to display the score
    public void displayScore() {
        System.out.println("Your score: " + score);
    }

    // Method to start the quiz
    public void startQuiz() {
        loadQuestionsFromFile(QuizzGame.class.getResource("/game_data/quiz_questions.txt").getPath());
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < questions.size(); i++) {
            displayQuestion(i);
            System.out.print("Your choice [A/B/C/D]: ");
            char userAnswer = scanner.next().toUpperCase().charAt(0);
            checkAnswer(i, userAnswer);
        }
        displayScore();
    }

    public static void main(String[] args) {
        QuizzGame game = new QuizzGame();
        game.startQuiz();
    }
}