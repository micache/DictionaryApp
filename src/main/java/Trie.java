import java.util.ArrayList;
import java.util.HashMap;

/**
 * A trie data structure for storing words and their meanings.
 */
public class Trie {

    private class Node {
        Node[] children;
        Word word = null;

        Node() {
            children = new Node[30];
        }
    }

    private final Node root;
    private final HashMap<Character, Integer> charset;
    private int charsetSize;
    private ArrayList<Word> wordsList;

    /**
     * Constructs a new Trie.
     */
    Trie() {
        root = new Node();
        charsetSize = 0;
        charset = new HashMap<>();
        for (char c = 'a'; c <= 'z'; c++) {
            charset.put(c, charsetSize++);
        }
        charset.put('-', charsetSize++);
        charset.put(' ', charsetSize++);
        charset.put('.', charsetSize++);
        charset.put('\'', charsetSize++);
    }

    /**
     * Looks up a word in the trie.
     *
     * @param word the word to be searched
     * @return the Word object if found, null otherwise
     */
    public Word lookupWord(String word) {
        String lowerCaseWord = word.toLowerCase();
        Node foundNode = findString(lowerCaseWord);
        return (foundNode != null) ? foundNode.word : null;
    }

    /**
     * Finds a node corresponding to a string in the trie.
     *
     * @param str the string to find
     * @return the node corresponding to the string, or null if not found
     */
    private Node findString(String str) {
        Node currentNode = root;
        for (int i = 0; i < str.length(); i++) {
            Integer index = charset.get(str.charAt(i));
            if (index == null || currentNode.children[index] == null) {
                return null;
            }
            currentNode = currentNode.children[index];
        }
        return currentNode;
    }

    /**
     * Adds a word to the trie.
     *
     * @param word    the word to be added
     * @param meaning the meaning of the word
     */
    public void addWord(String word, String meaning) {
        String lowerCaseWord = word.toLowerCase();
        Node currentNode = root;

        for (int i = 0; i < lowerCaseWord.length(); i++) {
            Integer index = charset.get(lowerCaseWord.charAt(i));
            if (index == null) {
                System.out.println("Invalid word!");
                return;
            }
            if (currentNode.children[index] == null) {
                currentNode.children[index] = new Node();
            }
            currentNode = currentNode.children[index];
        }

        if (currentNode.word == null) {
            currentNode.word = new Word(lowerCaseWord, meaning);
        } else {
            currentNode.word.setWordExplain(currentNode.word.getWordExplain() + "\n" + meaning);
        }
    }

    /**
     * Deletes a word from the trie.
     *
     * @param word the word to be deleted
     * @return true if the word was successfully deleted
     */
    public boolean deleteWord(String word) {
        String lowerCaseWord = word.toLowerCase();
        Node currentNode = root;

        for (int i = 0; i < lowerCaseWord.length(); i++) {
            Integer index = charset.get(lowerCaseWord.charAt(i));
            if (index == null || currentNode.children[index] == null) {
                System.out.println("No word found to be deleted!");
                return false;
            }
            currentNode = currentNode.children[index];
        }

        if (currentNode.word != null) {
            currentNode.word = null;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Edits a word in the trie.
     *
     * @param word    the word to be edited
     * @param meaning the new meaning of the word
     * @return true if the word was successfully edited
     */
    public boolean editWord(String word, String meaning) {
        String lowerCaseWord = word.toLowerCase();
        Node currentNode = root;

        for (int i = 0; i < lowerCaseWord.length(); i++) {
            Integer index = charset.get(lowerCaseWord.charAt(i));
            if (index == null || currentNode.children[index] == null) {
                return false;
            }
            currentNode = currentNode.children[index];
        }

        if (currentNode.word == null) {
            currentNode.word = new Word(lowerCaseWord, meaning);
        } else {
            currentNode.word.setWordExplain(meaning);
        }

        return true;
    }

    /**
     * Searches for words with a given prefix in the trie.
     *
     * @param prefix the prefix to search for
     * @return a list of words with the given prefix
     */
    public ArrayList<Word> getProposedString(String prefix) {
        String lowerCasePrefix = prefix.toLowerCase();
        wordsList = new ArrayList<>();
        Node currentNode = root;
        for (int i = 0; i < lowerCasePrefix.length(); i++) {
            Integer index = charset.get(lowerCasePrefix.charAt(i));
            if (index == null || currentNode.children[index] == null) {
                return null;
            }
            currentNode = currentNode.children[index];
        }
        findAllWords(currentNode);
        return wordsList;
    }

    /**
     * Retrieves all words from the trie.
     *
     * @return a list of all words in the trie
     */
    public ArrayList<Word> queryAllWords() {
        wordsList = new ArrayList<>();
        findAllWords(root);
        return wordsList;
    }

    /**
     * Finds all words starting from the given node.
     *
     * @param node the starting node
     */
    private void findAllWords(Node node) {
        if (node.word != null) {
            wordsList.add(node.word);
        }
        for (int i = 0; i < charsetSize; i++) {
            if (node.children[i] != null) {
                findAllWords(node.children[i]);
            }
        }
    }

    /**
     * Deletes a branch of nodes in the trie.
     *
     * @param node       the root of the branch
     * @param freeMemory true to free memory after deleting, false otherwise
     */
    private void deleteBranch(Node node, boolean freeMemory) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < 30; i++) {
            deleteBranch(node.children[i], freeMemory);
            if (freeMemory) {
                node.children[i] = null;
            }
        }
        node.word = null;
    }
}
