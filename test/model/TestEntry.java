package model;

import junit.framework.TestCase;

/**
 * Class to test the implementation of the table entries.
 */
public class TestEntry extends TestCase {

	// ----------------------------------------------------------------------------
	// ATTRIBUTES
	// ----------------------------------------------------------------------------

	/**
	 * Test table entry.
	 */
	private Entry entry;

	//--------------------------------------------------------------------------
	// METHODS
	//--------------------------------------------------------------------------

	/**
	 * Creates a new entry (0, 0).
	 */
	public void setUp1() {
		try {
			entry = new Entry(0, 0);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Verifies the constructor method of the entry.<br>
	 * <b> Methods to test:</b><br>
	 * Entry() <br>
	 * <b> Objective:</b><br>
	 * Prove that the constructor method Entry() is capable of creating a new table entry. 
	 * <b> Expected results:</b><br>
	 * 1. Entry object not null.
	 */
	public void testEntry() {
		setUp1();
		assertNotNull("The entry must exist.", entry);
	}
	
	/**
	 * Verifies the getter and setter for the page number of an entry.<br>
	 * <b> Methods to test:</b><br>
	 * getPage() <br>
	 * setPage() <br>
	 * <b> Objective:</b><br>
	 * Prove that the getters and setters work correctly. 
	 * <b> Expected results:</b><br>
	 * 1. Get the correct page number. <br>
	 * 1. Change the page number correctly.
	 */
	public void testGetSetPage() {
		setUp1();
		assertEquals("Page number is not correct.", 0, entry.getPage());
		entry.setPage(-1);
		assertEquals("Page number is not correct.", -1, entry.getPage());
	}
	
	/**
	 * Verifies the getter and setter for the frame number of an entry.<br>
	 * <b> Methods to test:</b><br>
	 * getFrame() <br>
	 * setFrame() <br>
	 * <b> Objective:</b><br>
	 * Prove that the getters and setters work correctly. 
	 * <b> Expected results:</b><br>
	 * 1. Get the correct frame number. <br>
	 * 1. Change the frame number correctly.
	 */
	public void testGetSetFrame() {
		setUp1();
		assertEquals("Frame number is not correct.", 0, entry.getFrame());
		entry.setFrame(-1);
		assertEquals("Frame number is not correct.", -1, entry.getFrame());
	}
	
	/**
	 * Verifies the getter and setter for the referenced bit of an entry.<br>
	 * <b> Methods to test:</b><br>
	 * isReferenced() <br>
	 * setReferenced() <br>
	 * <b> Objective:</b><br>
	 * Prove that the getters and setters work correctly. 
	 * <b> Expected results:</b><br>
	 * 1. Get the correct value for the referenced bit. <br>
	 * 1. Change the referenced bit value correctly.
	 */
	public void testGetSetReferenced() {
		setUp1();
		assertFalse("Referenced bit is not correct.", entry.isReferenced());
		entry.setReferenced(true);
		assertTrue("Referenced bit is not correct.", entry.isReferenced());
	}
	
	/**
	 * Verifies the aging counter update of an entry.<br>
	 * <b> Methods to test:</b><br>
	 * getCounter() <br>
	 * adjustAgingCounter() <br>
	 * <b> Objective:</b><br>
	 * Prove that the aging algorithm to the counter of an entry is correct. 
	 * <b> Expected results:</b><br>
	 * 1. Get the correct counter number. <br>
	 * 1. Apply the aging algorithm correctly, shift counter right and add leftmost one if referenced.
	 */
	public void testAgingCounter() {
		setUp1();
		assertEquals("Counter is not correct.", 0, entry.getCounter());
		entry.setReferenced(true);
		entry.adjustAgingCounter(); // 10000000000000000000000000000000
		assertEquals("Counter is not correct.", (long) Math.pow(2, Entry.COUNTER_BIT_SIZE-1), entry.getCounter());
		entry.setReferenced(true);
		entry.adjustAgingCounter(); // 11000000000000000000000000000000
		assertEquals("Counter is not correct.", 3221225472L, entry.getCounter());
		entry.setReferenced(false);
		entry.adjustAgingCounter(); // 01100000000000000000000000000000
		assertEquals("Counter is not correct.", 1610612736L, entry.getCounter());
	}
}
