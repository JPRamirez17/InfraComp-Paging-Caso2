package model;


import org.junit.Test;

import junit.framework.TestCase;
import model.Entry;
import model.TLB;

public class TestTLB extends TestCase {
	
	private TLB tlb;
	
	public void setUp() {
		tlb = new TLB(32);
	}
	
	@Test
	public void testTLB() {
		setUp();
		assertNotNull("The TLB must exist.", tlb);
	}
	
	public void testReplaceTLB() {
		setUp();
		Entry entry = new Entry(0, 0);
		tlb.replace(entry);
		assertEquals("The entry must exist.", tlb.consult(0), 0);
	}

	public void testConsultTLB() {
		setUp();
		Entry entry = new Entry(0, 0);
		tlb.replace(entry);
		assertEquals("The entry must exist.", tlb.consult(0), 0);
		tlb.consult(-1);
		assertEquals("The entry must not exist.", tlb.consult(-1), -1);
	}
}
