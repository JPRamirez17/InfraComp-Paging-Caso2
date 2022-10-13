package model;

import java.util.List;

/**
 * Class that represents an abstract table of page entries of fixed size.
 */
public abstract class ETable {

	// ----------------------------------------------------------------------------
	// ATTRIBUTES
	// ----------------------------------------------------------------------------

	/**
	 * Table as a list of entries.
	 */
	protected List<Entry> table;

	/**
	 * Size of the table, total number of entries.
	 */
	protected int size;

	// ----------------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------------

	/**
	 * Returns table of entries.
	 * @return table.
	 */
	public List<Entry> getTable() {
		return table;
	}
	
	/**
	 * Tries to find the frame number, given a page number in the table.
	 * @param page Number of page to find in the table. 0 <= page < size.
	 * @return frame number of the corresponding page if found, -1 if not found.
	 */
	public abstract int consult(int page);
}
