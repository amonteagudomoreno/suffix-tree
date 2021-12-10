package com.suffix_tree;

import com.suffix_tree.tree.CompactSuffixTree;
import com.suffix_tree.utils.RandomGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

import static com.suffix_tree.utils.Output.printComparingTable;
import static com.suffix_tree.utils.Output.printMaximals;
import static java.lang.System.*;

/**
  * Implementation for basic and compacted tree nodes.
  *
  *  @authors Silvia Usón: 681721 at unizar dot es
  *           Álvaro Monteagudo: 681060 at unizar dot es
  *
  *  @version 1.0

 */
public class Main {


    /**
     * Strategies for tree construction
     */
    public enum AlgorithmFeatures {
        N2, NLGN
    }

    private static boolean getLongest = false;
    private static boolean getMaximals = false;
    private static boolean time = false;
    private static boolean caseSensitive = false;
    private static String treeWord = "";
    private static final ArrayList<String> words = new ArrayList<>();
    private static final ArrayList<String> files = new ArrayList<>();
    private static AlgorithmFeatures feature = AlgorithmFeatures.NLGN;

    /**
     * Prints instructions of use
     */
    private static void printUsage() {
        out.println("./com.suffix_tree.Main [-h] [-time] [-cost] [-longest] [-maximals] [-random] [-file] ");
        out.println("""
                This program builds the corresponding compacted suffix tree for a word or a bunch of words.
                Tree are created from multiple files, words found on a specified file, or with a random word,
                if file or random not supplied a prompt will ask for a word to create the tree.
                There are two available implementations: n squared cost and logarithmic.\s
                Once created, tree is ready to look for as many patterns as user wants to.
                For a single word tree there are available two features: longest repeated substring and list of maximals
                Visualization of Ukkonen's algorithm step by step, visit the next webpage: http://brenden.github.io/ukkonen-animation/
                All credits to: Brenden Kokoszka. Git user: https://github.com/brenden""");
        out.println("Available options:");
        out.println("\t-time: prints a comparision table of tree's construction time, n squared vs n log n.");
        out.println("\t-cost <STRING>: n2 or nlgn tree construction, nlgn by default.");
        out.println("\t-longest: get the longest repeated substring.");
        out.println("\t-case_sensitive: to make difference between upper and lower case letters. To be effective it has to be passed before -file argument.");
        out.println("\t-maximals: get all maximal repetitions in the string.");
        out.println("\t-random <INTEGER>: generate a random word with n characters.");
        out.println("\t-file <INTEGER> [<STRING>]+: number of files to read from, -1 to read all the files.");
        out.println("\t-h: this helpful message.");
        out.println();
    }

    /**
     * com.suffix_tree.Main function
     * @param args containing features for concrete algorithm
     */
    public static void main(String[] args) {

        parseArguments(args);

        out.print("Arguments: ");
        for (String arg : args) {
            out.print(arg + " ");
        }
        out.println();

        Scanner keyboard = new Scanner(in);

        if (treeWord.equals("") && words.isEmpty()) {
            out.print("Enter text: ");
            treeWord = keyboard.nextLine();
            treeWord = removeSpecialChars(treeWord);
            words.add(treeWord);
            out.println();
        }

        if (time) printComparingTable(words);


        out.println("Creating tree.");

        CompactSuffixTree compactTree = null;
        try {
            compactTree = new CompactSuffixTree(words.toArray(new String [0]), feature);
        } catch (OutOfMemoryError ex) {
            System.out.println("Exceeded limit in garbage collector " + feature.toString() +", try shorter texts.");
            exit(-1);
        }

        searchPatterns(compactTree);

        if (words.size() > 1 && (getLongest || getMaximals)) {
            System.out.println("Longest substring and maximals only available with one word tree.");
        } else {
            if (getLongest) out.println("Longest repeated substring: " + compactTree.getLongestSubstring() + "\n");
            if (getMaximals) printMaximals(compactTree.getMaximals());
        }
    }

    /**
     * Set algorithm parameters
     * @param args array where options for algorithm are stored
     */
    private static void parseArguments(String[] args) {
        for (int i = 0; i < args.length ; i++) {
            switch (args[i]) {
                case "-cost" -> {
                    ++i;
                    switch (args[i]) {
                        case "n2" -> feature = AlgorithmFeatures.N2;
                        case "nlgn" -> feature = AlgorithmFeatures.NLGN;
                        default -> System.out.println("Feature not available. Try: n2 or nlgn.");
                    }
                }
                case "-case_sensitive" -> caseSensitive = true;
                case "-time" -> time = true;
                case "-longest" -> getLongest = true;
                case "-maximals" -> getMaximals = true;
                case "-random" -> {
                    ++i;
                    try {
                        int randomChars = Integer.parseInt(args[i]);
                        treeWord = (treeWord.equals("")) ? new RandomGenerator().stringRandom(randomChars) : treeWord;
                        words.add(treeWord);
                    } catch (NumberFormatException | NullPointerException e) {
                        err.println(e.getMessage());
                    }
                }
                case "-file" -> {
                    ++i;
                    try {
                        int number = Integer.parseInt(args[i]);
                        while (number > 0) {
                            ++i;
                            String filename = args[i];
                            words.add(readFile(filename));
                            --number;
                        }

                    } catch (NumberFormatException | NullPointerException ex) {
                        err.println("Trace: " + Arrays.toString(ex.getStackTrace()) + ". Message: " + ex.getMessage());
                        exit(-1);
                    }
                }
                default -> {
                    printUsage();
                    exit(0);
                }
            }
        }
    }

    /**
     * Read alphanumeric words from file [filename]
     * @param filename file where words are
     * @return list with all words read
     */
    private static String readFile(String filename){
        StringBuilder sb = new StringBuilder();
        File f = new File(filename);
        if (f.exists()) {
            files.add(filename);
            Scanner s;
            try {
                s = new Scanner(f);

                while (s.hasNext()) {
                    String word = removeSpecialChars(s.next());
                    sb.append(word);
                }
                s.close();
            }
            catch (Exception ex) {
                err.println("Trace: " + Arrays.toString(ex.getStackTrace()) + ". Message: " + ex.getMessage());
            }
        } else System.out.println("File does not exist");
        return sb.toString();
    }

    /**
     * Remove special characters from words
     * @param s word
     * @return word without special characters
     */
    private static String removeSpecialChars(String s) {
        if (caseSensitive) {
            return s.replaceAll("[^a-zA-Z0-9áéíóúÁÉÍÓÚ]+","");
        }
        else return s.replaceAll("[^a-zA-Z0-9áéíóúÁÉÍÓÚ]+","").toLowerCase();
    }

    /**
     * Search patterns until end condition satisfies
     * @param tree compacted tree where patterns ae looked in
     */
    private static void searchPatterns(CompactSuffixTree tree) {
        Scanner keyboard = new Scanner(in);
        out.print("Enter pattern (0 to exit): ");
        String pattern = keyboard.nextLine();
        File f = new File(pattern);
        String patternSearch = (f.exists()) ? removeSpecialChars(readFile(pattern)) : removeSpecialChars(pattern);

        while(!patternSearch.equals("0")){
            Set<Integer> listOfTexts = tree.search(tree.root, patternSearch, 0);
            if (listOfTexts.isEmpty()) System.out.println("Pattern not found in tree\n");
            else {
                StringBuilder sb = new StringBuilder("Pattern found in text/s\n");
                if (!files.isEmpty()) {
                    for (int index : listOfTexts) {
                        sb.append(files.get(index)).append('\n');
                    }
                }
                out.println(sb);
            }
            out.print("Enter pattern (0 to exit): ");
            pattern = keyboard.nextLine();
            patternSearch = removeSpecialChars(pattern);
        }
    }
}
