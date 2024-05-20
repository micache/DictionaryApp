import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Manages dictionary operations such as adding, editing, removing words, and translating text.
 */
public class DictionaryManagement {

    private static final String DICTIONARY_FILE_PATH =
            DictionaryManagement.class.getResource("/dictionaries.txt").getPath();
    private static DictionaryManagement instance = null;
    private final Dictionary dictionary = Dictionary.getInstance();

    private DictionaryManagement() {
    }

    /**
     * Gets the singleton instance of DictionaryManager.
     *
     * @return the singleton instance
     */
    public static DictionaryManagement getInstance() {
        if (instance == null) {
            instance = new DictionaryManagement();
        }
        return instance;
    }

    /**
     * Retrieves all words from the dictionary.
     *
     * @return a list of all words
     */
    public ArrayList<Word> getAllWords() {
        ArrayList<Word> words = dictionary.queryAllWords();
        return words != null ? new ArrayList<>(words) : new ArrayList<>();
    }

    /**
     * Imports words from a file into the dictionary.
     * The file should be located at src/main/resources/dictionaries.txt.
     */
    public void importFromFile() {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/dictionaries.txt";
        try (Scanner scanner = new Scanner(new FileReader(filePath))) {
            dictionary.importWords(scanner);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exports all words in the dictionary to a specified file.
     *
     * @param outputPath the path of the output file
     */
    public void exportToFile(String outputPath) {
        try (PrintStream out = new PrintStream(new FileOutputStream(outputPath))) {
            PrintStream originalOut = System.out; // Store original System.out
            System.setOut(out); // Redirect System.out to the file
            dictionary.exportWords(); // Export dictionary words
            System.setOut(originalOut); // Restore original System.out
            System.out.println("Dictionary has been exported to file!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Looks up a word in the dictionary.
     *
     * @param word the word to look up
     * @return the word object if found, null otherwise
     */
    public Word lookupWord(String word) {
        return dictionary.lookupWord(word);
    }

    /**
     * Searches for words with the given prefix in the dictionary.
     *
     * @param prefix the prefix to search for
     * @return a list of words with the given prefix
     */
    public ArrayList<Word> searchWords(String prefix) {
        ArrayList<Word> words = dictionary.getProposedString(prefix);
        return words != null ? new ArrayList<>(words) : new ArrayList<>();
    }

    /**
     * Adds a new word to the dictionary.
     *
     * @param word    the word to add
     * @param meaning the meaning of the word
     */
    public void addWord(String word, String meaning) {
        dictionary.addWord(word, meaning);
    }

    /**
     * Removes a word from the dictionary.
     *
     * @param word the word to remove
     * @return true if the word was removed successfully, false otherwise
     */
    public boolean removeWord(String word) {
        return dictionary.deleteWord(word);
    }

    /**
     * Translates text from one language to another.
     *
     * @param text       the text to translate
     * @param sourceLang the source language code
     * @param targetLang the target language code
     * @return the translated text
     * @throws IOException if an I/O error occurs
     */
    public String translateText(String text, String sourceLang, String targetLang) throws IOException {
        return GoogleTranslate.translate(text, sourceLang, targetLang);
    }

    /**
     * Speaks the given text in the specified language.
     *
     * @param text the text to speak
     * @param lang the language code
     * @throws IOException                   if an I/O error occurs
     * @throws UnsupportedAudioFileException if the audio file format is not supported
     * @throws LineUnavailableException      if a line cannot be opened because it is unavailable
     */
    public void speakText(String text, String lang)
            throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        GoogleTranslate.speak(text, lang);
    }

    /**
     * Edits an existing word in the dictionary.
     *
     * @param word       the word to edit
     * @param newMeaning the new meaning of the word
     * @return true if the word was edited successfully, false otherwise
     */
    public boolean editWord(String word, String newMeaning) {
        return dictionary.editWord(word, newMeaning);
    }
}
