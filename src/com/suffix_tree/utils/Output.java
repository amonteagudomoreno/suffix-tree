package com.suffix_tree.utils;

import com.suffix_tree.Main;
import com.suffix_tree.tree.CompactSuffixTree;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.System.out;

public class Output {

    /**
     * Prints all maximals found in the tree
     * @param maximals list where maximals are stored
     */
    public static void printMaximals(ArrayList<String> maximals) {
        if (!maximals.isEmpty()) {
            StringBuilder sb = new StringBuilder("Maximals are:\n\t");
            for (int i = 1; i <= maximals.size(); i++) {
                sb.append(maximals.get(i - 1));
                if (!(i == maximals.size())) {
                    sb.append(", ");
                }
                if (i % 5 == 0) {
                    sb.append('\n').append('\t');
                }
            }
            out.println(sb + "\n");
        } else out.println("No maximals\n");
    }

    /**
     * Print a comparing table between different algorithm strategies on tree construction.
     */
    public static void printComparingTable(ArrayList<String> words) {
        Timer timer = Timer.start();

        AtomicReference<CompactSuffixTree> tree = new AtomicReference<>();

        String aux = "+-----------+-------+--------+-------------+";

        StringBuilder table = new StringBuilder(aux).append('\n');
        table.append(String.format("%1s%10s%2s%5s%3s%6s%3s%8s%6s","|","ALGORITHM", "|", "ms",
                "|", "us", "|", "ns", "|")).append('\n');
        table.append(aux).append('\n');

        try {
            tree.set(new CompactSuffixTree(words.toArray(new String [0]), Main.AlgorithmFeatures.N2));
            long elapsed_ns = timer.time();
            long elapsed_ms = timer.convertTo(TimeUnit.MILLISECONDS, elapsed_ns);
            long elapsed_us = timer.convertTo(TimeUnit.MICROSECONDS, elapsed_ns);

            table.append(String.format("%1s%10s%2s%5d%3s%7d%2s%10d%4s", "|", "N2", "|", elapsed_ms,
                    "|", elapsed_us, "|", elapsed_ns, "|")).append('\n');
        } catch (OutOfMemoryError ex) {
            System.out.println("Exceeded limit in garbage collector N2, try shorter texts.");
        }

        timer.reset();

        try {
            tree.set(new CompactSuffixTree(words.toArray(new String [0]), Main.AlgorithmFeatures.NLGN));
            long elapsed_ns = timer.time();
            long elapsed_ms = timer.convertTo(TimeUnit.MILLISECONDS, elapsed_ns);
            long elapsed_us = timer.convertTo(TimeUnit.MICROSECONDS, elapsed_ns);

            table.append(String.format("%1s%10s%2s%5d%3s%7d%2s%10d%4s", "|", "NLGN", "|",
                    elapsed_ms, "|", elapsed_us, "|", elapsed_ns, "|")).append('\n');
            table.append(aux).append('\n');
        } catch (OutOfMemoryError ex) {
            System.out.println("Exceeded limit in garbage collector NLGN, try shorter texts.");
        }

        System.out.println(table);
    }
}
