public class strategy {
	
	public boolean backTracking(board b) {
		for (int row = 0; row < b.getSize(); row++) {
			for (int column = 0; column < b.getSize(); column++) {
				if (b.getBoard()[row][column] == 0) {
					for (int number = 1; number <= b.getSize(); number++) {
						if (b.finished(row, column, number)) {
							b.getBoard()[row][column] = number;
							if (backTracking(b)) {
								return true;
							} else {
								b.getBoard()[row][column] = 0;
							}
						}
					}
					return false;
				}
			}
		}  
		return true;
	}
}
