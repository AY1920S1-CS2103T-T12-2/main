package seedu.deliverymans.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

/**
 * Javadoc comment
 */
class Trie {
    private final HashMap<Character, Trie> children;
    private final LinkedList<String> contentList = new LinkedList<>();

    private boolean containsCommandWord;

    Trie() {
        children = new HashMap<>();
    }

    /**
     * TO fill
     */
    void insertCommand(String key, String... prefixes) {
        insertCommand(key, key, prefixes);
    }

    /**
     * TO fill
     */
    void insertCommand(String key) {
        insertCommand(key, key, new String[0]);
    }

    /**
     * Inserts a {@code String} into the Trie.
     */
    private void insertCommand(String key, String command, String... prefixes) {
        if (key.length() == 1) {
            char c = key.charAt(0);
            if (!children.containsKey(c)) {
                children.put(c, new Trie());
            }
            Trie leaf = children.get(c);
            leaf.addContent(command);
            leaf.insertPrefixes(prefixes);
            leaf.containsCommandWord = true;
        } else if (key.length() > 1) {
            char first = key.charAt(0);
            if (!children.containsKey(first)) {
                children.put(first, new Trie());
            }
            String keySubstring = key.substring(1);
            children.get(first).insertCommand(keySubstring, command, prefixes);
            insertCommand(keySubstring, command, prefixes);
        }
    }

    /**
     * TO fill
     */
    private void insertPrefixes(String... prefixes) {
        for (String prefix : prefixes) {
            insertPrefix(prefix);
        }
    }

    /**
     * TO fill
     */
    private void insertPrefix(String prefix) {
        Trie curr = this;
        char[] cArray = prefix.toCharArray();
        for (int i = 0; i < cArray.length; ++i) {
            char c = cArray[i];
            if (i == cArray.length - 1) {
                if (!curr.children.containsKey(c)) {
                    curr.children.put(c, new Trie());
                }
                curr.children.get(c).addContent(prefix);
            } else {
                if (!curr.children.containsKey(c)) {
                    curr.children.put(c, new Trie());
                }
                curr = curr.children.get(c);
            }
        }
    }

    /**
     * TO fill
     */
    private void addContent(String content) {
        this.contentList.addLast(content);
    }

    /**
     * Tofill.
     */
    private Trie search(String toFind) {
        Trie curr = this;
        for (char c : toFind.toLowerCase().toCharArray()) {
            if (!curr.children.containsKey(c)) {
                return null;
            }
            curr = curr.children.get(c);
        }
        return curr;
    }

    /**
     * Tofill.
     */
    LinkedList<String> autoCompletePrefix(String commandWord, String prefixes) {
        Trie commandResult = search(commandWord);
        if (commandResult == null) { // commandWord does not exist
            return new LinkedList<>();
        }

        Trie prefixResult = commandResult.search(prefixes);
        if (prefixResult == null) {
            return new LinkedList<>();
        }
        return prefixResult.getAllPrefixes();
    }

    /**
     * Tofill.
     */
    private LinkedList<String> getAllPrefixes() {
        LinkedList<String> contentList = new LinkedList<>();
        if (children.isEmpty()) {
            contentList.addAll(this.contentList);
        }
        for (Map.Entry<Character, Trie> entry : children.entrySet()) {
            Trie child = entry.getValue();
            LinkedList<String> childPrefixes = child.getAllPrefixes();
            contentList.addAll(childPrefixes);
        }
        contentList.sort(String::compareToIgnoreCase);
        return contentList;
    }

    /**
     * Tofill.
     */
    LinkedList<String> autoCompleteCommandWord(String toFind) {
        Trie result = search(toFind);
        if (result == null) { // commandWord does not exist
            return new LinkedList<>();
        }
        return result.getAllCommandWords();
    }

    /**
     * Tofill
     */
    private LinkedList<String> getAllCommandWords() {
        HashSet<String> uniqueList = new HashSet<>();
        if (containsCommandWord) {
            uniqueList.addAll(this.contentList);
        }
        for (Map.Entry<Character, Trie> entry : children.entrySet()) {
            Trie child = entry.getValue();
            LinkedList<String> childCommandWords = child.getAllCommandWords();
            uniqueList.addAll(childCommandWords);
        }
        LinkedList<String> contentList = new LinkedList<>(uniqueList);
        contentList.sort(String::compareToIgnoreCase);
        return contentList;
    }
}