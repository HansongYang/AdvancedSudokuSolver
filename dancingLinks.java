import java.util.*;

public class dancingLinks {
	private columnNode header;
	private boolean[][] coverMatrix;
	private int[][] board;
	private int BOARD_SIZE; //The size of the board
	private int MAX_VALUES; //The maximum value in this board
	private int BOX_SIZE; //The size of each box
	private int NUM_CONSTRAINTS = 4; //4 constraints : cell, line, column, boxes
	
	public dancingLinks(int [][] board, board b) {
		this.board = board;
		BOARD_SIZE = b.getSize();
		MAX_VALUES = b.getSize();
		BOX_SIZE = b.getBoxSize();
		this.coverMatrix = new boolean[BOARD_SIZE * BOARD_SIZE * MAX_VALUES][BOARD_SIZE * BOARD_SIZE * NUM_CONSTRAINTS];
		this.header = new columnNode();
		initialize();
		createDancingLinks();
	}
	
	public void createDancingLinks() {
		ArrayList<columnNode> columnList = new ArrayList<>();
		columnNode currHeader = header;
		int numCols = coverMatrix[0].length;
		for (int i = 0; i < numCols; i++) {
			columnNode newHeader = new columnNode();
			columnList.add(newHeader);
			currHeader.addNodeRight(newHeader);
			currHeader = newHeader;
		}
		header.size = numCols;
		
		int numRows = coverMatrix.length;
		for (int r = 0; r < numRows; r++) {
			node prevNode = null;
			for (int c = 0; c < numCols; c++) {
				if (coverMatrix[r][c] == true) {
					columnNode header = columnList.get(c);
					node newNode = new node(header, r);
					
					header.up.addNodeDown(newNode);
					header.size++;
					
					if (prevNode == null) prevNode = newNode;
					prevNode.addNodeRight(newNode);
					prevNode = newNode;
				}
			}
		}
	}
	
	//This method transforms a Sudoku board instance into a cover matrix.
	public void initialize() {
		initializeMatrix();
		applyRowConstraint(BOARD_SIZE*BOARD_SIZE);
		applyColumnConstraint(BOARD_SIZE*BOARD_SIZE*2);
		applyBoxConstraint(BOARD_SIZE*BOARD_SIZE*3);
		updateCoverMatrix();
	}
	
	public void initializeMatrix() {
		for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
			for (int j = 0; j < MAX_VALUES; j++) {
				coverMatrix[i * MAX_VALUES + j][i] = true;
			}					
		}
	}
	
	public void applyRowConstraint(int total) {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				for (int k = 0; k < MAX_VALUES; k++) {
					coverMatrix[i * MAX_VALUES*BOARD_SIZE + j * MAX_VALUES + k][total + i * BOARD_SIZE + k] = true;
				}		
			}
		}
	}
	
	public void applyColumnConstraint(int total) {
		for (int i = 0; i < BOARD_SIZE; i++) { 
			for (int j = 0; j < BOARD_SIZE; j++) { 
				for (int k = 0; k < MAX_VALUES; k++) {
					coverMatrix[i * MAX_VALUES * BOARD_SIZE + j * MAX_VALUES + k][total + j * MAX_VALUES + k] = true;
				}
			}
		}
	}
	
	public void applyBoxConstraint(int total) {
		for (int i = 0; i < BOX_SIZE; i++) {
			for (int j = 0; j < BOX_SIZE; j++) {
				for (int k = 0; k < BOX_SIZE; k++) {
					diagonals(i * MAX_VALUES * BOARD_SIZE * BOX_SIZE + j * MAX_VALUES*BOARD_SIZE + k * MAX_VALUES * BOX_SIZE,
							total + i * MAX_VALUES*BOX_SIZE + k * MAX_VALUES);
				}
			}
		}
	}
	
	public void diagonals(int startR, int startC) {
		for (int i = 0; i < BOX_SIZE; i++) {
			for (int j = 0; j < MAX_VALUES; j++) {
				coverMatrix[startR + i * MAX_VALUES + j][startC + j] = true;
			}
		}
	}
	
	public void updateCoverMatrix() {
		for (int r = 0; r < BOARD_SIZE; r++) {
			for (int c = 0; c < BOARD_SIZE; c++) {
				int value = board[r][c];
				if (value != 0) {
					updateCoverRows(r, c, value);
				}
			}
		}
	}
	
	public void updateCoverRows(int row, int col, int value) {
		for (int i = 1; i <= MAX_VALUES; i++) {
			if (i != value) {
				int coverRowIndex = getRowInCoverMatrix(row, col, i);
				Arrays.fill(coverMatrix[coverRowIndex], false);
			}
		}
	}
	
	public int getRowInCoverMatrix(int row, int col, int value) {
		return ((row * BOARD_SIZE * BOARD_SIZE) + (col * BOARD_SIZE) + (value - 1));
	}
	
	private void coverRow(node node) {
		node currNode = node.right;
		while (currNode != node) {
			currNode.header.cover();
			currNode = currNode.right;
		}
	}

	private void uncoverRow(node node) {
		node currNode = node.left;
		while (currNode != node) {
			currNode.header.uncover();
			currNode = currNode.left;
		}
	}
	
	public int[][] solve() {
		ArrayList<node> results = new ArrayList<node>();
		algorithmX(results);
		applySolution(results);
		return board;
	}
	
	private boolean algorithmX(ArrayList<node> results) {
		if (header.right == header) return true;
		columnNode node = determineHeuristic();
		node.cover();
		node rowHead = node.down;
		while (rowHead != node) {
			coverRow(rowHead);
			results.add(rowHead);
			if (algorithmX(results)) return true;	
			results.remove(results.size() - 1);
			uncoverRow(rowHead);
			rowHead = rowHead.down;
		}
		node.uncover();
		return false;	
	}
	
	private columnNode determineHeuristic() {
		columnNode node = null;
		int minSize = Integer.MAX_VALUE;
		columnNode currNode = (columnNode) header.right;
		while (currNode != header) {
			if (currNode.size < minSize) {
				minSize = currNode.size;
				node = currNode;
			}
			currNode = (columnNode) currNode.right;
		}
		return node;
	}
	
	private void applySolution(ArrayList<node> results) {
		for (node node : results) {
			int index = node.index;
			int row = index / (BOARD_SIZE * BOARD_SIZE);
			int col = (index - (row * BOARD_SIZE * BOARD_SIZE)) / BOARD_SIZE;
			int val = (index % BOARD_SIZE) + 1;
			board[row][col] = val;
		}
	}
}
