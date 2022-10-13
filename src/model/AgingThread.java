package model;

import main.App;

/**
 * Class that represents the thread that updates PT entries' counter according to the 
 * aging replacement algorithm.
 */
public class AgingThread extends Thread {

	// ----------------------------------------------------------------------------
	// CONSTANTS
	// ----------------------------------------------------------------------------

	/**
	 * Milliseconds to simulate a clock tick.
	 */
	private final static long CLOCK_TICK_MS = 1;

	// ----------------------------------------------------------------------------
	// ATTRIBUTES
	// ----------------------------------------------------------------------------

	/**
	 * Page table to recalculate aging counter.
	 */
	private PT pt; // MONITOR

	// ----------------------------------------------------------------------------
	// CONSTRUCTOR
	// ----------------------------------------------------------------------------
	/**
	 * Creates a new aging thread with the corresponding page table.
	 * @param pt Page table to update. pt != null.
	 */
	public AgingThread(PT pt) {
		this.pt = pt;
	}

	// ----------------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------------

	/**
	 * At each clock tick (1ms), the thread updates the aging counter of each PT entry 
	 * according to the aging algorithm.
	 */
	@Override
	public void run() {
		while(!App.isFinished()) {
			// Simulate clock tick
			try {
				Thread.sleep(CLOCK_TICK_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Updates the counter of each PT entry, according to the aging algorithm
			for (Entry actual: pt.getTable()) {
				actual.adjustAgingCounter();
			}
		}
	}
}
