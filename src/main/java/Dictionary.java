import java.util.ArrayList;
import java.util.Scanner;

/**
 * Singleton class representing a dictionary.
 */
public class Dictionary {

    private static Dictionary instance = null;
    private final Trie trie;

    private Dictionary() {
        trie = new Trie();
    }

    /**
     * Gets the singleton instance of the Dictionary.
     *
     * @return the singleton instance
     */
    public static Dictionary getInstance() {
        if (instance == null) {
            instance = new Dictionary();
        }
        return instance;
    }

    /**
     * Looks up a word in the dictionary.
     *
     * @param word the word to look up
     * @return the Word object if found, null otherwise
     */
    public Word lookupWord(String word) {
        return trie.lookupWord(word);
    }

    /**
     * Adds a word to the dictionary.
     *
     * @param word    the word to be added
     * @param meaning the meaning of the word
     */
    public void addWord(String word, String meaning) {
        trie.addWord(word, meaning);
    }

    /**
     * Deletes a word from the dictionary.
     *
     * @param word the word to be deleted
     * @return true if the word was successfully deleted
     */
    public boolean deleteWord(String word) {
        return trie.deleteWord(word);
    }

    /**
     * Edits a word in the dictionary.
     *
     * @param word    the word to be edited
     * @param meaning the new meaning of the word
     * @return true if the word was successfully edited
     */
    public boolean editWord(String word, String meaning) {
        return trie.editWord(word, meaning);
    }

    /**
     * Retrieves all words in the dictionary.
     *
     * @return a list of all words
     */
    public ArrayList<Word> queryAllWords() {
        return trie.queryAllWords();
    }

    /**
     * Searches for words with the given prefix in the dictionary.
     *
     * @param prefix the prefix to search for
     * @return a list of words with the given prefix
     */
    public ArrayList<Word> getProposedString(String prefix) {
        return trie.getProposedString(prefix);
    }

    /**
     * Imports words from a Scanner input.
     * Expected format: "{English word}\t{Vietnamese meaning}"
     *
     * @param scanner the Scanner to read words from
     */
    public void importWords(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\t", 2);
            addWord(parts[0], parts[1].replaceAll("\\\\", "\n"));
        }
    }

    /**
     * Exports all words to the standard output.
     * Format: "{English word}\t{Vietnamese meaning}"
     */
    public void exportWords() {
        ArrayList<Word> words = trie.queryAllWords();
        for (Word word : words) {
            System.out.printf("%s\t%s\n", word.getWordTarget(), word.getWordExplain().replaceAll("\n", "\\\\"));
        }
    }
}
