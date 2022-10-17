package model;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.TestCase;
import model.AgingThread;
import model.Entry;
import model.PT;

public class TestPT extends TestCase {
	
	private PT pt;
	
	public void setUp1() {
		try {
			pt = new PT(64, 2);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void setUp2() {
		try {
			pt = new PT(64, 2);
			pt.replace(0);
			pt.replace(1);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void testPT() {
		setUp1();
		assertNotNull("The pt must exist.", pt);
	}

	public void testReplace() {
		setUp1();
		assertEquals("New frame is not correct.", 0, pt.replace(0));
		pt.updateAgingCounters();
		assertEquals("New frame is not correct.", 1, pt.replace(1));
		pt.updateAgingCounters();
		assertEquals("New frame is not correct.", 0, pt.replace(2));
		assertEquals("New frame is not correct.", -1, pt.getTable().get(0).getFrame());
	}
	
	public void testConsult() {
		setUp2();
		assertEquals("Frame is not correct.", 0, pt.consult(0));
		assertEquals("Frame is not correct.", 1, pt.consult(1));
		assertEquals("Frame is not correct.", -1, pt.consult(2));
	}
	
	
}
