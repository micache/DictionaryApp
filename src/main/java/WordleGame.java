import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;

/**
 * EnglishWordleGame class manages the logic for the Wordle game.
 * It includes methods to load words from a file, check guesses,
 * and keep track of attempts.
 */
public class WordleGame {
    private List<String> wordList;
    private String targetWord;
    private int attempts;
    private final int maxAttempts = 6;

    /**
     * Constructor for EnglishWordleGame.
     * Initializes the word list and attempts.
     */
    public WordleGame() {
        this.wordList = new ArrayList<>();
        this.attempts = 0;
    }

    /**
     * Loads words from a specified file.
     * Each line in the file should contain a single word.
     *
     */
    public void loadWordsFromFile() {
        DictionaryManagement.getInstance().importFromFile();
        ArrayList<Word> words = DictionaryManagement.getInstance().getAllWords();
        for (Word item : words) {
            wordList.add(item.getWordTarget());
        }

        Collections.sort(wordList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return Integer.compare(s1.length(), s2.length());
            }
        });
        int p = wordList.size() - 1;
        while (p > 0 && wordList.get(p).length() > 6)
            p--;
        // Randomly select a target word
        Random rand = new Random();
        targetWord = wordList.get(rand.nextInt(p + 1));
    }

    /**
     * Gets the target word.
     *
     * @return the target word
     */
    public String getTargetWord() {
        return targetWord;
    }

    /**
     * Gets the number of attempts made.
     *
     * @return the number of attempts
     */
    public int getAttempts() {
        return attempts;
    }

    /**
     * Sets the number of attempts made.
     *
     * @param attempts the number of attempts
     */
    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    /**
     * Gets the maximum number of attempts allowed.
     *
     * @return the maximum number of attempts
     */
    public int getMaxAttempts() {
        return maxAttempts;
    }

    /**
     * Checks if there are attempts left.
     *
     * @return true if there are attempts left, false otherwise
     */
    public boolean hasAttemptsLeft() {
        return attempts < maxAttempts;
    }

    /**
     * Checks a guess against the target word.
     * Increases the number of attempts and returns feedback on the guess.
     *
     * @param guess the guessed word
     * @return feedback on the guess
     */
    public String checkGuess(String guess) {
        attempts++;
        guess = guess.toLowerCase();

        if (guess.equals(targetWord)) {
            return "Correct! The word is " + targetWord;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == targetWord.charAt(i)) {
                result.append(guess.charAt(i)).append(" ");
            } else if (targetWord.contains(String.valueOf(guess.charAt(i)))) {
                result.append("_ ");
            } else {
                result.append("* ");
            }
        }

        return result.toString().trim();
    }
}
