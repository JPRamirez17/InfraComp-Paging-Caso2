package model;

import java.util.ArrayList;

/**
 * Page table with replacement using Aging algorithm.
 */
public class PT extends ETable {

	// ----------------------------------------------------------------------------
	// ATTRIBUTES
	// ----------------------------------------------------------------------------

	/**
	 * Frames assigned as available space in RAM to the process.
	 */
	private int assignedRam;

	/**
	 * Frames already used in available space in RAM assigned to the process.
	 */
	private int ramSpaceUsed;

	// ----------------------------------------------------------------------------
	// CONSTRUCTOR
	// ----------------------------------------------------------------------------

	/**
	 * Creates an empty PT, no entries and size given.
	 * Implements table as array (faster page lookup).
	 */
	public PT(int size, int assignedRam) {
		this.assignedRam = assignedRam;
		this.ramSpaceUsed = 0;

		this.size = size;
		table = new ArrayList<Entry>(size);
		for (int i = 0; i < size; i++)
			table.add(new Entry(i, -1));
	}

	// ----------------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------------

	/**
	 * Replaces entry during PT miss.
	 * If PT has not assigned all the frames available to the process, it assigns the next available to the new page entry.
	 * If frames avaiable to the process are full, it replaces with aging algorithm, replaces the entry with minimum counter.
	 * @param page Virtual page number to update in the PT entry. page >= null.
	 */
	public synchronized int replace(int page) {
		int newFrame = -1;
		if (ramSpaceUsed < assignedRam) { // Assigns avaiable space in RAM
			newFrame = ramSpaceUsed;
			ramSpaceUsed ++;
		} else { // Replacement with aging algorithm
			long minCounter = (long) Math.pow(2, Entry.COUNTER_BIT_SIZE); // 2^8 max counter
			int oldPage = 0;
			for (int i = 0; i < table.size(); i++) {
				Entry actual = table.get(i);
				long counter = actual.getCounter();
				int currentFrame = actual.getFrame();
				if (currentFrame != -1 && counter <= minCounter) {
					oldPage = i;
					newFrame = currentFrame;
					minCounter = counter;
				}
			}
			table.get(oldPage).setFrame(-1);
			
		}
		table.get(page).setFrame(newFrame);
		table.get(page).setReferenced(true);
		return newFrame;
	}

	/**
	 * Updates the counter of each PT entry, according to the aging algorithm.
	 */
	public synchronized void updateAgingCounters() {
		for (Entry actual: table) {
			actual.adjustAgingCounter();
		}
	}

	/**
	 * Tries to find the frame number, given a page number in the PT.
	 * @param page Number of page to find in the PT. 0 <= page < size.
	 * @return frame number of the corresponding page if in RAM, -1 if not.
	 */
	public synchronized int consult(int page) {
		int frame = -1;
		Entry pagEntry = table.get(page);
		if (pagEntry.getFrame() != -1) {
			frame = pagEntry.getFrame();
			pagEntry.setReferenced(true);
		}
		return frame;
	}

}
