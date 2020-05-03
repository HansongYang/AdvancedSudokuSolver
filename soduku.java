import java.util.*;

public class soduku {
	
	public static void main(String[] args) {
		int choice = 0;
		int strategy = 0;
		int generate = 0;
		int level = 0;
		int remove = 0;
		board b = new board();
		strategy s = new strategy();
		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome to the advanced Soduku solver.");
		while(true) {
			System.out.println("Please choose the size of your soduku board. 1. 9*9  2. 16*16  3. 25*25  4. 36*36  5. 49*49");
			choice = sc.nextInt();
			if(choice > 0 && choice < 6) {
				break;
			}
			System.out.println("Invalid input, please enter again.");
		}
		
		int size = 0;
		switch(choice) {
		case 1:
			size = 9;
			break;
		case 2: 
			size = 16;
			break;
		case 3:
			size = 25;
			break;
		case 4:
			size = 36;
			break;
		case 5: 
			size = 49;
			break;
		}
		
		while(true) {
			System.out.println("Do you want to 1. generate board or 2. input board?");
			generate = sc.nextInt();
			if(generate == 1 || generate == 2) {
				break;
			}
			System.out.println("Invalid input, please enter again.");
		}
		
		if(generate == 1) {
			while(true) {
				System.out.println("Please choose the level of difficulty. 1.Easy 2.Medium 3.Hard ");
				level = sc.nextInt();
				if(level > 0 && level < 4) {
					break;
				}
				System.out.println("Invalid input, please enter again.");
			}
			
			if(level == 1) {
				remove = 20;
			} else if(level == 2) {
				remove = 40;
			} else if(level == 3) {
				remove = 60;
			}
			remove = remove * choice;
		}
		
		if(generate == 1) {
			System.out.println("\nGenerating board....");
			b.generateBoard(size, remove);
			b.printSudoku();
		} else if(generate == 2) {
			if(!b.inputBoard(size)) {
				return;
			}
			System.out.println("\nThe input board: ");
			b.printSudoku();
		}

		while(true) {
			System.out.println("Please choose the algorithm for solving the soduku. 1. backtracking 2. Algorithm X (advanced backtracking)");
			strategy = sc.nextInt();
			if(strategy > 0 && strategy < 3) {
				break;
			}
			System.out.println("Invalid input, please enter again.");
		}
		
		long start = System.currentTimeMillis();
		System.out.println("\nThe board after solving.");
		if(strategy == 1) {
			s.backTracking(b);
		} else {
			dancingLinks dl = new dancingLinks(b.getBoard(), b);
			b.setBoard(dl.solve());
		}
		long end = System.currentTimeMillis();
		b.printSudoku();
		if(strategy == 1) {
			System.out.println("Time taken by backtracking algorithm: " + (end-start) + " milliseconds");
		} else {
			System.out.println("Time taken by Algorithm X: " + (end-start) + " milliseconds");
		}
	}
}
