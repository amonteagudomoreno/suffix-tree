package com.suffix_tree.tree;


import com.suffix_tree.node.SuffixTreeNode;

/**
 *  Implementation for basic suffix trees.
 *
 *  @authors Silvia UsÃ³n: 681721 at unizar dot es
 *           Álvaro Monteagudo: 681060 at unizar dot es
 *
 *  @version 1.0
 *
 */
class SuffixTree {

    // Root of the tree
    private final SuffixTreeNode root;

    /**
     * Suffix tree constructor
     * @param word from which the tree is built.
     */
    SuffixTree(String word) {
        root = new SuffixTreeNode(-1);

        for (int i = 1; i < word.length() - 1; ++i) {
            SuffixTreeNode current = root;
            int len = 0;

            // Check how many characters are already in the tree
            while (true) {

                SuffixTreeNode lastValidNode = current;
                SuffixTreeNode children;

                children = current.getChildren(word.charAt(i + len), word);

                if (children != null) {
                    lastValidNode.updateLeftDiverse(i, word);
                    current = children;
                    len++;
                } else {
                    break;
                }
            }

            // Update if proceeds
            if (!current.isLeftDiverse) {
                current.updateLeftDiverse(i, word);
            }

            // Add new characters that are not in the tree yet.
            for (int j = i + len; j < word.length() - 1; j++) {
                current = current.addChildren(j, i, word.charAt(j));
                current.listOfWords.add(0);
            }
        }
    }

    /**
     * Get root of the tree
     * @return root of this tree
     */
    public SuffixTreeNode getRoot() {
        return root;
    }

    /**
     * Add a new word to the suffix tree
     * @param word to be added
     * @param numberOfWord to add
     */
    public void addWord(String word, int numberOfWord) {
        word += "$";

        for (int i = 0; i < word.length() - 1; i++) {
            addSuffixes(root, i, word, numberOfWord);
        }
    }

    /**
     * Add all suffixes for a given word
     * @param current current node we are in the tree
     * @param i index in word
     * @param word which suffixes has te be added
     * @param numberOfWord to add
     */
    private void addSuffixes(SuffixTreeNode current, int i, String word, int numberOfWord) {
        for (SuffixTreeNode child : current.children) {
            if (child.character == word.charAt(i)) {
                child.listOfWords.add(numberOfWord);
                i++;
                addSuffixes(child, i, word, numberOfWord);
                return;
            }
        }

        for (int j = i; j < word.length() - 1; j++) {
            current = current.addChildren(j + 1, i, word.charAt(j));
            current.listOfWords.add(numberOfWord);
        }

    }
}

