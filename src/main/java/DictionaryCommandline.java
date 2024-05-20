import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * A command-line interface for managing a dictionary.
 */
public class DictionaryCommandline {

    private final DictionaryManagement manager = DictionaryManagement.getInstance();

    /**
     * Displays all words in the dictionary.
     */
    private void displayAllWords() {
        ArrayList<Word> words = manager.getAllWords();
        int maxLength = words.stream().mapToInt(word -> word.getWordTarget().length()).max().orElse(0);

        System.out.printf("%-8s| %-" + maxLength + "s | %s\n", "No", "English", "Vietnamese");
        for (int i = 0; i < words.size(); i++) {
            String[] meanings = words.get(i).getWordExplain().split("\n");
            System.out.printf("%-8d| %-" + maxLength + "s | %s\n", i + 1, words.get(i).getWordTarget(), meanings[0]);
            for (int j = 1; j < meanings.length; j++) {
                System.out.printf("%-8s| %-" + maxLength + "s | %s\n", "", "", meanings[j]);
            }
        }
        System.out.println("Press any key to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    /**
     * Looks up a word in the dictionary and displays its meaning.
     */
    private void lookupWord() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter word to look up: ");
        String word = scanner.nextLine();

        Word result = manager.lookupWord(word);
        System.out.println("Search result:");
        if (result == null) {
            System.out.println("No results found.");
        } else {
            System.out.println(result.getWordExplain());
        }
        System.out.println("Press any key to continue...");
        scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    /**
     * Updates the meaning of a word in the dictionary.
     */
    private void updateWord() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter word to update: ");
        String word = scanner.nextLine();
        System.out.print("Enter new meaning: ");
        String meaning = scanner.nextLine();

        if (manager.editWord(word, meaning)) {
            System.out.println("Word updated successfully.");
        } else {
            System.out.println("Failed to update word.");
        }
        System.out.println("Press any key to continue...");
        scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    /**
     * Removes a word from the dictionary.
     */
    private void deleteWord() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter word to delete: ");
        String word = scanner.nextLine();

        if (manager.removeWord(word)) {
            System.out.println("Word deleted successfully.");
        } else {
            System.out.println("Failed to delete word.");
        }
        System.out.println("Press any key to continue...");
        scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    /**
     * Adds words to the dictionary from user input.
     */
    private void addWordsFromInput() {
        System.out.print("How many words do you want to add? ");
        Scanner scanner = new Scanner(System.in);
        int count = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        for (int i = 0; i < count; i++) {
            System.out.print("Enter word: ");
            String word = scanner.nextLine();
            System.out.print("Enter meaning: ");
            String meaning = scanner.nextLine();
            manager.addWord(word, meaning);
        }
    }

    /**
     * Searches for words with a specified prefix in the dictionary.
     */
    private void searchWords() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter prefix to search: ");
        String prefix = scanner.nextLine();

        ArrayList<Word> results = manager.searchWords(prefix);
        System.out.println("Search results:");
        if (results.isEmpty()) {
            System.out.println("No results found.");
        } else {
            results.forEach(word -> System.out.println("- " + word.getWordTarget()));
        }
        System.out.println("Press any key to continue...");
        scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    /**
     * Placeholder for a game functionality.
     */
    private void playGame() {
        // Placeholder for game functionality
    }

    /**
     * Shows the main menu and returns the user's choice.
     *
     * @return the user's choice
     */
    private int showMenu() {
        System.out.println("Welcome to My Application!\n" +
                "[0] Exit\n" +
                "[1] Add\n" +
                "[2] Remove\n" +
                "[3] Update\n" +
                "[4] Display\n" +
                "[5] Lookup\n" +
                "[6] Search\n" +
                "[7] Game\n" +
                "[8] Import from file\n" +
                "[9] Export to file");
        Scanner input = new Scanner(System.in);
        System.out.print("Your action: ");
        return input.nextInt();
    }

    /**
     * Loads the dictionary from a file.
     */
    private static void loadDictionaryFromFile() {
        DictionaryManagement manager = DictionaryManagement.getInstance();
        manager.importFromFile();
    }

    public static void main(String[] args) {
        loadDictionaryFromFile();
        DictionaryCommandline app = new DictionaryCommandline();

        while (true) {
            int choice = app.showMenu();
            switch (choice) {
                case 0:
                    exit(0);
                    break;
                case 1:
                    app.addWordsFromInput();
                    break;
                case 2:
                    app.deleteWord();
                    break;
                case 3:
                    app.updateWord();
                    break;
                case 4:
                    app.displayAllWords();
                    break;
                case 5:
                    app.lookupWord();
                    break;
                case 6:
                    app.searchWords();
                    break;
                case 7:
                    app.playGame();
                    break;
                case 8:
                    app.manager.importFromFile();
                    break;
                case 9:
                    app.manager.exportToFile("output.txt");
                    break;
                default:
                    System.out.println("Invalid action.");
            }
        }
    }
}
