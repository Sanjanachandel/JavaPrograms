import java.util.Scanner;
import java.util.Stack;

public class RobotNavigationSystem {
	static Stack<String> backStack = new Stack<>();
	static Stack<String> forwardStack = new Stack<>();
	static int position=0;
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		boolean flag = true;
		do {
			System.out.println("1. Move Forward");
			System.out.println("2. Move Backward");
			System.out.println("3. Undo Last Move");
			System.out.println("4. Redo Last Undone Move");
			System.out.println("5. Show Current Position");
			System.out.println("6. Exit Program");
			
			System.out.print("Enter choice: ");
			int choice = sc.nextInt();
			switch(choice) {
				case 1:
					moveForward();
					break;
				case 2:
					moveBackward();
					break;
				case 3:
					undoMove();
					break;
				case 4:
					redoMove();
					break;
				case 5:
					System.out.println("Current Position: "+position);
					break;
				case 6:
					System.out.println("Exiting Program...");
					flag = false;
					break;
					
			}
		}
		while(flag);
	}
	
	private static void moveForward() {
		System.out.print("Enter forward steps: ");
		int steps = sc.nextInt();
		String move = "Forward " + steps;
		
		backStack.push(move);
		forwardStack.clear();
		position += steps;
		
		System.out.println(move);
	}
	
	private static void moveBackward() {
		System.out.print("Enter backward steps: ");
		int steps = sc.nextInt();
		String move = "Backward " + steps;
		backStack.push(move);
		forwardStack.clear();
		position -= steps;
		
		System.out.println(move);
	}
	
	private static void undoMove() {
		if(backStack.empty()) {
			System.out.println("No moves to undo");
			return;
		}
		
		String lastMove = backStack.pop();
		String parts[] = lastMove.split(" ");
		String direction = parts[0];
		int steps = Integer.parseInt(parts[1]);
		
		if(direction.equals("Forward")) {
			position -= steps;
		}
		else {
			position += steps;
		}
		
		forwardStack.push(lastMove);
		System.out.println("Undo");
		
	}
	
	private static void redoMove() {
		if(forwardStack.empty()) {
			System.out.println("Nothing to redo");
			return;
		}
		
		String redoMove = forwardStack.pop();
		String parts[] = redoMove.split(" ");
		String direction = parts[0];
		int steps = Integer.parseInt(parts[1]);
		
		if(direction.equals("Forward")) {
			position += steps;
		}
		else {
			position -= steps;
		}
		
		backStack.push(redoMove);
		System.out.println("Redo");
		
	}
}