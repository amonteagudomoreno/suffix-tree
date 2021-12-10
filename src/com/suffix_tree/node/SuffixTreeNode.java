package com.suffix_tree.node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SuffixTreeNode {

    // Default value for node
    private final char DEFAULT_SYMBOL = 0;

    // Position of the node
    public int position;

    // List containing all the children of this node
    public ArrayList<SuffixTreeNode> children = new ArrayList<>();

    // Flag if node is left diverse
    public boolean isLeftDiverse = false;

    // Helpful parameter to determine if a node is left diverse
    public int indexStartPath = DEFAULT_SYMBOL;

    public char character = ' ';

    public Set<Integer> listOfWords = new HashSet<>();


    /**
     * Suffix tree node constructor
     * @param position value of the node
     */
    public SuffixTreeNode(int position) {

        this.position = position;
    }

    /**
     * @param position value for this node
     * @param indexStartPath start path of the new node
     * @return new added node as children of this
     */
    public SuffixTreeNode addChildren(int position, int indexStartPath, char character) {
        children.add(new SuffixTreeNode(position));
        SuffixTreeNode lastChildren = children.get(children.size() - 1);
        lastChildren.indexStartPath = indexStartPath;
        lastChildren.character =  character;
        return lastChildren;
    }

    /**
     * @param character to look for
     * @param word to look in
     * @return null if does not exist a children node with character as value,
     *          otherwise return the found children.
     */
    public SuffixTreeNode getChildren(char character, String word) {
        for (SuffixTreeNode n: children) {
            if (word.charAt(n.position) == character) {
                return n;
            }
        }
        return null;
    }

    /**
     * Update the node setting left diversity if proceeds
     * @param firstChar char to compare if it is left diverse
     * @param word string
     */
    public void updateLeftDiverse(int firstChar, String word) {
        char leftChar = (this.indexStartPath < 1) ? ' ' : word.charAt(this.indexStartPath - 1);
        char rightChar = word.charAt(firstChar - 1);
        if (leftChar != rightChar) {
            isLeftDiverse = true;
        }
    }

    @Override
    public String toString() {
        return  "position=" + position +
                ", isLeftDiverse=" + isLeftDiverse +
                ", indexStartPath=" + indexStartPath +
                ", character=" + character +
                ", words=" + listOfWords;
    }
}
