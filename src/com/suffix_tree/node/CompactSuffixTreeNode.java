package com.suffix_tree.node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CompactSuffixTreeNode {

    // Values of the node
    public int begin, end;

    // List containing all the children of this node
    public ArrayList<CompactSuffixTreeNode> children = new ArrayList<>();

    // Flag if node is left diverse
    public boolean isLeftDiverse;

    // Helpful parameter to determine if a node is left diverse
    public int indexStartPath;

    public String substring;

    public Set<Integer> listOfWords = new HashSet<>();


    /**
     * Constructor for compacted suffix tree node
     * @param begin value of the node
     * @param end value of the node
     * @param isLeftDiverse flag
     * @param indexStartPath extra parameter for internal management
     */
    public CompactSuffixTreeNode(int begin, int end, boolean isLeftDiverse, int indexStartPath) {
        this.begin = begin;
        this.end = end;
        this.isLeftDiverse = isLeftDiverse;
        this.indexStartPath = indexStartPath;
    }

    /**
     * Constructor for compacted suffix tree node
     * @param begin value of the node
     * @param end value of the node
     * @param isLeftDiverse flag
     * @param indexStartPath extra parameter for internal management
     * @param substring fragment of the word to keep in the node
     * @param listOfWords list of words the node belongs to
     */
    public CompactSuffixTreeNode(int begin, int end, boolean isLeftDiverse, int indexStartPath, String substring, Set<Integer> listOfWords) {
        this.begin = begin;
        this.end = end;
        this.isLeftDiverse = isLeftDiverse;
        this.indexStartPath = indexStartPath;
        this.substring = substring;
        this.listOfWords = listOfWords;
    }

    @Override
    public String toString() {
        return "" +
                "begin=" + begin +
                ", end=" + end +
                ", isLeftDiverse=" + isLeftDiverse +
                ", indexStartPath=" + indexStartPath +
                ", substring=" + substring +
                ", words=" + listOfWords;
    }
}
