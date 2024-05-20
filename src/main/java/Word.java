/**
 * Represents a word with its target form and explanation.
 */
public class Word {

    private final String wordTarget;
    private String wordExplain;

    /**
     * Constructs a Word with the specified target word and explanation.
     *
     * @param wordTarget the target word
     * @param wordExplain the explanation of the word
     */
    public Word(String wordTarget, String wordExplain) {
        this.wordTarget = wordTarget;
        this.wordExplain = wordExplain;
    }

    /**
     * Returns a string representation of the Word.
     *
     * @return a string in the format "word_target\nword_explain"
     */
    @Override
    public String toString() {
        return wordTarget + '\n' + wordExplain.replaceAll("\\\\", "\n");
    }

    /**
     * Gets the target word.
     *
     * @return the target word
     */
    public String getWordTarget() {
        return wordTarget;
    }

    /**
     * Gets the explanation of the word.
     *
     * @return the explanation of the word
     */
    public String getWordExplain() {
        return wordExplain;
    }

    /**
     * Sets the explanation for the word.
     *
     * @param wordExplain the new explanation for the word
     */
    public void setWordExplain(String wordExplain) {
        this.wordExplain = wordExplain;
    }
}
