package com.suffix_tree.utils;

import java.util.Random;

/**
 *  Implementation of a string random generator.
 *
 *  @authors Silvia Usón: 681721 at unizar dot es
 *           Álvaro Monteagudo: 681060 at unizar dot es
 *
 *  @version 1.0
 *
 */
public class RandomGenerator {

    private static Random rnd;
    private static final char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray(); // Letters array
    private static final char[] numbers = "0123456789".toCharArray(); // Numbers array

    // Character array
    private static final char[] alphanumeric = (String.valueOf(chars) + String.valueOf(numbers)).toCharArray();

    /**
     * Default constructor
     */
    public RandomGenerator() {
        rnd = new Random();
    }

    /**
     * @param n characters to generate
     * @return string with n letters randomly selected
     */
    public String stringRandom(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(chars[rnd.nextInt(chars.length)]);
        }
        return sb.toString();
    }

    /**
     * @param n characters to generate
     * @return string with n numbers randomly selected
     */
    public String numberStringRandom(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(numbers[rnd.nextInt(numbers.length)]);
        }
        return sb.toString();
    }

    /**
     * @param n characters to generate
     * @return string with n characters randomly selected
     */
    public String alphanumericRandom(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(alphanumeric[rnd.nextInt(alphanumeric.length)]);
        }
        return sb.toString();
    }
}
