
public class node {
	protected node left, right, up, down; 
	protected columnNode header;
	protected int index;
	
	protected node() {
		this.left = this;
		this.right = this;
		this.up = this;
		this.down = this;
	}
	
	public node(columnNode header, int index) {
		this();
		this.header = header;
		this.index = index;
	}

	protected void separateLeftRight() {
		this.left.right = this.right;
		this.right.left = this.left;
	}

	protected void linkLeftRight() {		
		this.left.right = this;
		this.right.left = this;
	}
	
	protected void separateUpDown() {
		this.up.down = this.down;
		this.down.up = this.up;		
	}

	protected void linkUpDown() {
		this.up.down = this;
		this.down.up = this;
	}

	public void addNodeRight(node node) {	
		node.right = this.right;
		node.right.left = node;
		node.left = this;
		this.right = node;
	}
	
	public void addNodeDown(node node) {	
		node.down = this.down;
		node.down.up = node;
		node.up = this;
		this.down = node;
	}
}