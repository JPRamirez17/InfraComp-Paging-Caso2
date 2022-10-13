package model;

import java.util.ArrayList;

public class PT extends ETable {

	private int assignedRam;
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
	 * Replaces entry during PT miss with aging algorithm, replaces the entry with minimum counter.
	 * @param repEntry New entry that is going to replace the oldest used page. repEntry != null.
	 */
	public int replace(int page) {

		int newFrame = -1;
		if (ramSpaceUsed < assignedRam) {
			newFrame = ramSpaceUsed;
			table.get(page).setFrame(newFrame);
			ramSpaceUsed ++;
		} else {
			long minCounter = 256; // 2^8 max counter
			int oldPage = 0;
			for (int i = 0; i < table.size(); i++) {
				Entry actual = table.get(i);
				long counter = 0;
				try {
					counter = actual.getCounter().toLongArray()[0];
				} catch (ArrayIndexOutOfBoundsException e) {
					counter = 0; // empty bits equals zero
				}
				int currentFrame = actual.getFrame();
				if (currentFrame != -1 && counter <= minCounter) {
					oldPage = i;
					newFrame = currentFrame;
					minCounter = counter;
				}
			}
			table.get(oldPage).setFrame(-1);
			table.get(page).setFrame(newFrame);
		}
		return newFrame;
	}

	/**
	 * Tries to find the frame number, given a page number in the PT.
	 * @param page Number of page to find in the PT. 0 <= page < size.
	 * @return frame number of the corresponding page if in RAM, -1 if not.
	 */
	public int consult(int page) {
		int frame = -1;
		Entry pagEntry = table.get(page);
		if (pagEntry.getFrame() != -1) {
			frame = pagEntry.getFrame();
			pagEntry.setReferenced(true);
		}
		return frame;
	}

}
