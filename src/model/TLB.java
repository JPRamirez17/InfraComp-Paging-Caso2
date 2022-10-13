package model;

import java.util.LinkedList;

/**
 * Class that represents the TLB cache as a queue with a FIFO replacement algorithm.
 */
public class TLB extends ETable {

	// ----------------------------------------------------------------------------
	// CONSTRUCTOR
	// ----------------------------------------------------------------------------

	/**
	 * Creates an empty TLB, no entries and size given.
	 * Implements table as linked list (faster FIFO replacement - more frequent misses).
	 */
	public TLB(int size) {
		this.size = size;
		table = new LinkedList<Entry>();
		for (int i = 0; i < size; i++)
			table.add(new Entry(-1, -1));
	}

	// ----------------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------------

	/**
	 * Deletes the first entry that went into the TLB queue (top), and adds a new last entry.
	 * @param repEntry New last entry that is going to replace the first one. repEntry != null.
	 */
	public void replace(Entry repEntry) {
		((LinkedList<Entry>) table).pollFirst();
		table.add(repEntry);
	}
	
	/**
	 * Tries to find the frame number, given a page number in the TLB.
	 * @param page Number of page to find in the TLB. 0 <= page < size.
	 * @return frame number of the corresponding page if found, -1 if not found.
	 */
	public int consult(int page) {
		int frame = -1;
		for (Entry actual: table) {
			if (actual.getPage() == page) {
				frame = actual.getFrame();
			}
		}
		return frame;
	}

}
