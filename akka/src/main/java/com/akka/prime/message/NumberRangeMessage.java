package com.akka.prime.message;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class NumberRangeMessage.
 */
public class NumberRangeMessage implements Serializable
{
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The start number. */
    private long startNumber;
    
    /** The end number. */
    private long endNumber;

    /**
     * Instantiates a new number range message.
     *
     * @param startNumber the start number
     * @param endNumber the end number
     */
    public NumberRangeMessage(long startNumber, long endNumber) {
        this.startNumber = startNumber;
        this.endNumber = endNumber;
    }

    /**
     * Gets the start number.
     *
     * @return the start number
     */
    public long getStartNumber() {
        return startNumber;
    }

    /**
     * Sets the start number.
     *
     * @param startNumber the new start number
     */
    public void setStartNumber(long startNumber) {
        this.startNumber = startNumber;
    }

    /**
     * Gets the end number.
     *
     * @return the end number
     */
    public long getEndNumber() {
        return endNumber;
    }

    /**
     * Sets the end number.
     *
     * @param endNumber the new end number
     */
    public void setEndNumber(long endNumber) {
        this.endNumber = endNumber;
    }
}