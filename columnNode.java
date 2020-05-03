
public class columnNode extends node{
	public int size; 

	public columnNode() {
		super();
		this.size = 0;
		this.header = this;
		this.index = -1;
	}

	//This method removes all Nodes from their respective up-down linked lists if they exist on the same row (i.e. left-right list) as the Nodes of the given column.
	public void cover() {
		this.separateLeftRight();
		node currColNode = this.down;
		while (currColNode != this) {
			node currRowNode = currColNode.right;
			while (currRowNode != currColNode) {
				currRowNode.separateUpDown();
				currRowNode.header.size--;
				currRowNode = currRowNode.right;
			}
			currColNode = currColNode.down;
		}
	}
	
	//This method will restore all Nodes that were removed in the Cover method to their respective up-down linked lists.
	public void uncover() {
		node currColNode = this.up;
		while (currColNode != this) {
			node currRowNode = currColNode.left;
			while (currRowNode != currColNode) {
				currRowNode.linkUpDown();
				currRowNode.header.size++;
				currRowNode = currRowNode.left;
			}
			currColNode = currColNode.up;
		}
		this.linkLeftRight();
	}
}
