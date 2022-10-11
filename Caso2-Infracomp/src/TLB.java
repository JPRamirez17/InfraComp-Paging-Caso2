
public class TLB {

	private Register top;

	private Register last;

	private int size;

	public TLB() {
		top = null;
		last = null;
		size = 0;
	}

	public void insert(Register r) {

		Register oldLast = last;
		last = r;
		if(size==0) {
			top = last; 
		} else {
			oldLast.setNext(last);
		}
		size ++;
	}

	public Register get() {
		if( top == null ){
			return null;
		}
		top = top.getNext();
		size --;
		if(size == 0) { 
			last = null;
		}
		return top;
	}
	
	public boolean consult(Register r) {	
		Register actual = top;
		for ( int i=0; i<this.size; i++ ) {
			if( actual == r ) {
				return true;
			}
		}
		return false;
	}
	
	public int size() {
		return size;
	}


}
