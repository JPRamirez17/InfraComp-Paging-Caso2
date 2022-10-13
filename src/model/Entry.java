package model;

import java.util.BitSet;

/**
 * Class that represents a standard paging entry (# page, # frame) + referenced bit + counter.
 */
public class Entry {

	// ----------------------------------------------------------------------------
	// CONSTANTS
	// ----------------------------------------------------------------------------

	/**
	 * Fixed aging counter size in bits.
	 */
	private final static int COUNTER_BIT_SIZE = 8;

	// ----------------------------------------------------------------------------
	// ATTRIBUTES
	// ----------------------------------------------------------------------------

	/**
	 * Virtual page number.
	 */
	private int page;

	/**
	 * Real frame number.
	 */
	private int frame;

	/**
	 * Keep track if the page was used recently.
	 */
	private boolean referenced;

	/**
	 * Aging algorithm counter (8 bits)
	 */
	private BitSet counter;

	// ----------------------------------------------------------------------------
	// CONSTRUCTOR
	// ----------------------------------------------------------------------------

	/**
	 * Creates a standard page entry (# page, # frame), initially not referenced and
	 * with counter zero.
	 * @param page Virtual page number. page >= -1.
	 * @param frame Real frame number. frame >= -1.
	 */
	public Entry(int page, int frame) {
		this.page = page;
		this.frame = frame;
		referenced = false;
		counter = new BitSet(COUNTER_BIT_SIZE);
	}

	// ----------------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------------

	/**
	 * Gets the entry's page number.
	 * @return Virtual page number.
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Change the page number with the specified number.
	 * @param page Virtual page number. page >= -1.
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * Gets the entry's frame number.
	 * @return Real frame number.
	 */
	public int getFrame() {
		return frame;
	}

	/**
	 * Change the frame number with the specified number.
	 * @param frame Real frame number. frame >= -1.
	 */
	public void setFrame(int frame) {
		this.frame = frame;
	}

	/**
	 * Determines whether the page was used recently.
	 * @return referenced bit, true if it was referenced, false if not.
	 */
	public boolean isReferenced() {
		return referenced;
	}

	/**
	 * Change the referenced bit.
	 * @param referenced New value for the referenced bit.
	 */
	public void setReferenced(boolean referenced) {
		this.referenced = referenced;
	}

	/**
	 * Returns the 8-bit aging counter.
	 * @return entry counter.
	 */
	public BitSet getCounter() {
		return counter;
	}
	
	/**
	 * Shifts the 8-bit aging counter to the right.
	 * Changes the leftmost bit to 1 in the 8-bit aging counter, if the entry was recently used.
	 * Resets the referenced bit.
	 */
	public void adjustAgingCounter() {
		long count = 0;
		try {
			count = counter.toLongArray()[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			count = 0; // empty bits equals zero
		}
		this.counter = BitSet.valueOf(new long[]{count/2}); // Shift right
		
		if (isReferenced())
			this.counter.set(COUNTER_BIT_SIZE-1, true);
		
		this.referenced = false;
	}

}
