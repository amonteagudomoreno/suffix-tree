package com.suffix_tree.utils;

import java.util.concurrent.TimeUnit;

/**
 *  Implementation for a nanosecond precision timer
 *
 *  @author Álvaro Monteagudo: 681060 at unizar dot es
 *
 *  @version 1.0
 *
 */
public class Timer {

    private long starts;

    /**
     * com.suffix_tree.utils.Timer start
     * @return an initialized timer
     */
    public static Timer start() {
        return new Timer();
    }

    /**
     * Default com.suffix_tree.utils.Timer constructor
     */
    private Timer() {
        reset();
    }

    /**
     * Reset times
     */
    public void reset() {
        starts = System.nanoTime();
    }

    /**
     * Get time
     * @return time elapsed in nanoseconds
     */
    public long time() {
        long end = System.nanoTime();
        return end - starts;
    }

    /**
     * Get time
     * @param unit to get time in
     * @return time in unit
     */
    public long time(TimeUnit unit) {
        return unit.convert(time(), TimeUnit.NANOSECONDS);
    }

    /**
     * Converts time from a unit to another
     * @param unit to convert time to
     * @param value to be converted
     * @return value converted to new unit value
     */
    public long convertTo(TimeUnit unit, long value) { return unit.convert(value, TimeUnit.NANOSECONDS); }

}
