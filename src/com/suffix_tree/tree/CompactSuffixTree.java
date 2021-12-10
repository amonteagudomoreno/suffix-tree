package com.suffix_tree.tree;

import com.suffix_tree.Main;
import com.suffix_tree.node.CompactSuffixTreeNode;
import com.suffix_tree.node.SuffixTreeNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *  Implementation for compacted suffix tree.
 *
 *  @authors Silvia Usón: 681721 at unizar dot es
 *           Álvaro Monteagudo: 681060 at unizar dot es
 *
 *  @version 1.0
 *
 */
public class CompactSuffixTree {

    // String to save current word to be added to the tree
    private String string;

    // Root node of the tree
    public CompactSuffixTreeNode root;

    // List with nodes marked as maximals
    private final ArrayList<CompactSuffixTreeNode> maximals;

    // Index of the word where longest repeated substring starts
    private int indexLongestSubstring = 0;

    // Node where longest repeated substring starts
    private CompactSuffixTreeNode nodeLongestSubstring = null;

    private int currentWord;


    /**
     * Constructor for compacted suffix tree
     * @param words from which tree is built
     */
    public CompactSuffixTree(String[] words, Main.AlgorithmFeatures feature) {
        maximals = new ArrayList<>();
        currentWord = 0;

        // N squared
        if (feature == Main.AlgorithmFeatures.N2) {
            string = "$" + words[0] + "$";
            SuffixTree tree = new SuffixTree(string);
            for (int i = 1; i < words.length; i++) {
                tree.addWord(words[i], i);
            }
            root = generateCompactSuffixTree(tree.getRoot(), 0);
        } else {
            // N lg n
            root = new CompactSuffixTreeNode(-1, -1, false, 0);
            for (String word : words) {
                string = "$" + word + "$";
                for (int i = 1; i < string.length() - 1; i++) {
                    insertSuffix(root, i, i);
                }
                currentWord++;
            }
        }
    }

    /**
     * Method to look for a certain pattern within the tree
     * @param current node to look in
     * @param pattern to be looked for
     * @param pos current position in the pattern string
     * @return a set of integers representing in which words the pattern was found, empty set if not found.
     */
    public Set<Integer> search(CompactSuffixTreeNode current, String pattern, int pos) {

        CompactSuffixTreeNode matchedNode = null;

        int aux = pos;

        for (CompactSuffixTreeNode child : current.children) {
            int i = 0;
            pos = aux;
            while (pos < pattern.length() && i < child.substring.length()
                    && pattern.charAt(pos) == child.substring.charAt(i)) { // Match character
                //System.out.println(pattern.charAt(pos) + " " + child.substring.charAt(i));
                i++;
                pos++;
            }

            if (pos == pattern.length())
                return child.listOfWords;
            else if (i == child.substring.length()) {
                matchedNode = child;
                break;
            }
        }
        return (matchedNode != null) ? search(matchedNode, pattern, pos) : new HashSet<>();
    }

    /**
     * Insert a new suffix to the tree
     * @param current node to check case of insertion
     * @param pos in the current word to be added to the tree
     * @param indexInWord extra parameter for internal management
     */
    private void insertSuffix(CompactSuffixTreeNode current, int pos, int indexInWord) {
        CompactSuffixTreeNode matchedNode = null;
        int inTree = 0;
        int idx = -1;

        ArrayList<CompactSuffixTreeNode> children = current.children;
        // Check how many charcters are already in the tree
        for (int i = 0; i < children.size(); i++) {
            CompactSuffixTreeNode child = children.get(i);

            if (string.charAt(pos + inTree) == child.substring.charAt(inTree)) {
                matchedNode = child;
                idx = i;
                while ((pos + inTree) < string.length() - 1 // Actual pos less then string length
                        && inTree < child.substring.length()  // Does not match all the node substring
                        && string.charAt(pos + inTree) == child.substring.charAt(inTree)) { // Match character
                    inTree++;
                }
            }

            // We found a node that fits the full suffix
            if ((pos + inTree) == string.length() - 1) {
                assert matchedNode != null;
                matchedNode.listOfWords.add(currentWord);
                return; // Already in tree
            } else if (matchedNode != null) {	// If we already found a child that matchs in any of the possible way, break the loop
                break;
            }
        }

        // If child was not found, add new full branch with suffix
        if (matchedNode == null) {
            CompactSuffixTreeNode newNode = new CompactSuffixTreeNode(pos, string.length() - 1, false,
                    indexInWord, string.substring(pos, string.length() - 1), new HashSet<>() {{
                add(currentWord);
            }});
            current.children.add(newNode);
        } else if (inTree < matchedNode.substring.length()) { // Child found but does not match the full branch
            // We have to split this branch to include the new one
            int end = pos + inTree;
            boolean flag = false;
            if (currentWord == 0) {
                char leftChar = (current.indexStartPath < 1) ? ' ' : string.charAt(current.indexStartPath - 1);
                char rightChar = (indexInWord < 1) ? ' ' : string.charAt(indexInWord - 1);
                flag = current.isLeftDiverse || leftChar != rightChar;
            }


            // New node with all characters that matched
            CompactSuffixTreeNode newNode = new CompactSuffixTreeNode(pos, end - 1, flag,
                  indexInWord, matchedNode.substring.substring(0, inTree), new HashSet<>(matchedNode.listOfWords));

            if (currentWord == 0) {
                if (newNode.isLeftDiverse) {
                    maximals.add(newNode);
                }

                int newDepth = end - indexInWord;

                if (newDepth > indexLongestSubstring) {
                    indexLongestSubstring = newDepth;
                    nodeLongestSubstring = newNode;
                }
            }

            // Matched node update
            matchedNode.begin += inTree;
            // Now contains the substring from the first character that did not match
            matchedNode.substring = matchedNode.substring.substring(inTree);
            newNode.listOfWords.add(currentWord);

            // Set node to the new one and add updated matched node as child to this new one
            current.children.set(idx, newNode);
            newNode.children.add(matchedNode);

            insertSuffix(newNode, end, indexInWord);

        } else { // Matched a full branch, insert what is left of the suffix, starting from the matched node
            if (currentWord == 0) {
                char leftChar = (current.indexStartPath < 1) ? ' ' : string.charAt(current.indexStartPath - 1);
                char rightChar = (indexInWord < 1) ? ' ' : string.charAt(indexInWord - 1);
                if (!current.isLeftDiverse && leftChar != rightChar) {
                    current.isLeftDiverse = true;
                    maximals.add(current);
                }
            }
        	matchedNode.listOfWords.add(currentWord);
            insertSuffix(matchedNode, pos + inTree, indexInWord);
        }
    }

    /**
     * Creation of compacted tree
     * @param node root of suffix tree
     * @param depth we are in the tree
     * @return root of the compacted suffix tree
     */
    private CompactSuffixTreeNode generateCompactSuffixTree(SuffixTreeNode node, int depth) {
        CompactSuffixTreeNode result;

        int start = node.position;
        StringBuilder sb = new StringBuilder("" + node.character);

        // Compact into one single node all consecutive nodes with one children
        while (node.children.size() == 1) {
            node = node.children.get(0);
            sb.append(node.character);
        }

        int end = node.position;

        result = new CompactSuffixTreeNode(start, end, node.isLeftDiverse, node.indexStartPath, sb.toString(), node.listOfWords);

        if (depth != 0 && node.children.size() > 0 && node.isLeftDiverse) {
            maximals.add(result);
        }

        int newDepth = depth + end - start;

        for (SuffixTreeNode children: node.children) {
            result.children.add(generateCompactSuffixTree(children, newDepth + 1));
        }

        if (result.children.size() != 0) {
            if (newDepth > indexLongestSubstring) {
                indexLongestSubstring = newDepth;
                nodeLongestSubstring = result;
            }
        }

        return result;
    }

    /**
     * @return longest repeated substring stored in the tree
     */
    public String getLongestSubstring() {
        if (nodeLongestSubstring != null) {
            int start = nodeLongestSubstring.indexStartPath;
            int end =  nodeLongestSubstring.end;
            int len = end - start + 1;
            return string.substring(start, start + len);
        } else {
            return "";
        }
    }

    /**
     * @return list with all the maximals of the tree
     */
    public ArrayList<String> getMaximals() {
        ArrayList<String> result = new ArrayList<>();
        for (CompactSuffixTreeNode node: maximals) {
            int start = node.indexStartPath;
            int end =  node.end;
            int len = end - start + 1;
            result.add(string.substring(start, start + len));
        }
        return result;
    }
}