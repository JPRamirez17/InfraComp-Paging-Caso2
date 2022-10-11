
public class Register {
	
	private int page;
	private int frame;
	private Register next;
	
	public Register(int page, int frame) {
		this.page= page;
		this.frame = frame;
	}
	
	public void setNext(Register next) {
		this.next = next; 
	}

	public Register getNext() {
		return next;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

}
