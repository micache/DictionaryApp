import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizzGame {
    private List<Question> questions;

    public QuizzGame() {
        this.questions = new ArrayList<>();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void loadQuestionsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("/");
                if (parts.length == 6) {
                    String questionText = parts[0];
                    String[] options = {parts[1], parts[2], parts[3], parts[4]};
                    char correctAnswer = parts[5].charAt(0);
                    questions.add(new Question(questionText, options, correctAnswer));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startQuiz() {
    }

    public static class Question {
        private String questionText;
        private String[] options;
        private char correctAnswer;

        public Question(String questionText, String[] options, char correctAnswer) {
            this.questionText = questionText;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }

        public String getQuestionText() {
            return questionText;
        }

        public String[] getOptions() {
            return options;
        }

        public char getCorrectAnswer() {
            return correctAnswer;
        }
    }
}
