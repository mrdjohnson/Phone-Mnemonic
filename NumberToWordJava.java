import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by djohnson on 5/4/2017.
 * Given a number given a number the app would return word the phone mnemonic(or dialed) of it (example 323 = DAD)
 * Given a word the app returns other words that could be returned (DAD gets converted to 323, then results of 323 is returned)
 */
public class NumberToWordJava {

    public static void main(String... args) throws FileNotFoundException {
        new NumberToWordJava();
    }

    public NumberToWordJava() throws FileNotFoundException {
        Trie trie = new Trie();
        downloadAllToTrie(trie);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Insert query: ");
            String next = scanner.next();

            if (next.contains("-")) {
                System.out.println("Thanks for trie-ing");
                return;
            } else if (next.matches("[0-9]+")) {
                System.out.println(trie.getNumberBasedWords(next));
            } else if (next.matches("[a-z]+")) {
                System.out.println(trie.getWordBasedWords(next));
            } else {
                System.out.println("Bad format, trie again.");
            }
        }
    }

    // modify before running locally
    void downloadAllToTrie(Trie trie) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("[file path here ] \\all-words.txt"));

        while (scanner.hasNext()) {
            String word = scanner.next().toLowerCase();
            if (word.matches("[a-z]+")) {
                trie.add(word);
            }
        }
    }

    class Trie {
        Node root = new Node();

        void add(String word) {
            Node parent = root;
            for (int letter : word.toCharArray()) {
                letter -= 'a';
                Node child = parent.nodes[letter];
                if (child == null) {
                    parent.nodes[letter] = child = new Node();
                }
                parent = child;
            }
            parent.endOfWord = true;
        }

        // Numbers on a phone -> The letter options, and back again
        int lettersToNumbers[] = {2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 9, 9, 9, 9};
        char numbersToLetters[][] = {{'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'}, {'j', 'k', 'l'}, {'m', 'n', 'o'}, {'p', 'q', 'r', 's'}, {'t', 'u', 'v'}, {'w', 'x', 'y', 'z'}};

        // in = 323 out = dad, some other 3 letter ones
        public String getNumberBasedWords(String number) {
            List<String> words = new ArrayList<>();
            getWords(number, number.length() - 1, "", words);
            return String.join(", ", words);
        }

        // finds the letters corresponding to each number and creates more strings with the new letters at the end of each
        //this could be optimized to fast fail when a word does not exist based on the given numbers
        // ex: 2222222222345 would fail early and no strings would be added to the list
        private void getWords(String indexes, int index, String currentString, List<String> words) {
            if (index == -1) {
                if (contains(currentString)) words.add(currentString);
                return;
            }

            for (char c : numbersToLetters[indexes.charAt(index) - '2']) {
                getWords(indexes, index - 1, c + currentString, words);
            }
        }

        // in dad, convert to 323, then return the conversions of those numbers to strings
        public String getWordBasedWords(String word) {
            String indexes = "";
            for (char letter : word.toCharArray()) {
                indexes += lettersToNumbers[letter - 'a'];
            }
            return getNumberBasedWords(indexes);
        }

        // not sure what this does
        public boolean contains(String word) {
            return contains(word, 0, root);
        }

        // only returns true if the ending node IS the end of the word
        private boolean contains(String word, int index, Node currentNode) {
            if (currentNode == null) {
                return false;
            } else if (index == word.length()) {
                return currentNode.endOfWord;
            }
            return contains(word, index + 1, currentNode.nodes[word.charAt(index) - 'a']);
        }

        class Node {
            Node[] nodes = new Node[26];
            boolean endOfWord = false;
        }
    }
}