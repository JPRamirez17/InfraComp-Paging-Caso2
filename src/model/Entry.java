package model;

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
	public final static int COUNTER_BIT_SIZE = 32;

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
	 * Aging algorithm counter
	 */
	private long counter;

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
		counter = 0;
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
	public long getCounter() {
		return counter;
	}
	
	/**
	 * Shifts the 8-bit aging counter to the right.
	 * Changes the leftmost bit to 1 in the 8-bit aging counter, if the entry was recently used.
	 * Resets the referenced bit.
	 */
	public void adjustAgingCounter() {
		counter /= 2; // Shift right
		
		if (isReferenced())
			counter += (long) Math.pow(2, COUNTER_BIT_SIZE-1);
		
		this.referenced = false;
	}

}
