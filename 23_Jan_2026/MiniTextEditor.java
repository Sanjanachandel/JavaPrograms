package Day23;



import java.util.Scanner;
import java.util.Stack;

public class MiniTextEditor {
	static Scanner sc = new Scanner(System.in);
	static StringBuilder text = new StringBuilder();
	static Stack<String> undoStack = new Stack<>();
	static Stack<String> redoStack = new Stack<>();
	
	public static void main(String[] args) {
		System.out.println("=== MINI TEXT EDITOR ===");
		System.out.println("1. Add Text");
		System.out.println("2. Delete Text");
		System.out.println("3. Search Word");
		System.out.println("4. Replace Word");
		System.out.println("5. Undo");
		System.out.println("6. Redo");
		System.out.println("7. Display Document");
		System.out.println("8. Exit");
		int choice = 0;
		boolean flag;
		do {
			System.out.print("Choose: ");
			choice = sc.nextInt();
			sc.nextLine();
			flag = true;
			switch(choice) {
				case 1:
					System.out.print("Enter text: ");
					String s = sc.nextLine();
					addText(s);
					break;
				case 2:
					System.out.print("Enter number of characters to delete: ");
					int num = sc.nextInt();
					deleteText(num);
					sc.nextLine();
					break;
				case 3:
					System.out.println("Enter word to search: ");
					String searchWord = sc.nextLine();
					searchWord(searchWord);
					break;
				case 4:
					System.out.print("Enter word to replace: ");
					String oldWord = sc.nextLine();
					System.out.print("Enter replacement: ");
					String newWord = sc.nextLine();
					replaceWord(oldWord,newWord);
					break;
				case 5:
					undo();
					break;
				case 6:
					redo();
					break;
				case 7:
					display();
					break;
				case 8:
					System.out.println("Exiting editor...");
					flag = false;
			}
		}
		while(flag);
	}
	
	private static void saveStateForUndo() {
		undoStack.push(text.toString());
	}
	
	public static void addText(String s) {
		text.append(s).append(" ");
		saveStateForUndo();
		System.out.println("Text added successfully!");
	}
	
	public static void deleteText(int n) {
		if(n > text.length()) {
			System.out.println("Not enough characters to delete");
			return;
		}
		saveStateForUndo();
		text.delete(text.length()-n,text.length());
		System.out.println("Text deleted successfully!");
	}
	
	public static void searchWord(String s) {
		int presentIdx = text.indexOf(s);
		if(presentIdx == -1) {
			System.out.println("Word is not present");
			return;
		}
		System.out.println(s + " is present at "+presentIdx);
	}
	
	public static void replaceWord(String oldWord, String newWord) {
		if(text.indexOf(oldWord) == -1) {
			System.out.println("Word is not present to replace");
			return;
		}
		saveStateForUndo();
		text = new StringBuilder(text.toString().replace(oldWord, newWord));
		System.out.println("Text replaced successfully!");
		
	}
	
	public static void undo() {
		if(undoStack.empty()) {
			System.out.println("Nothing to undo");
			return;
		}
		redoStack.push(text.toString());
		text = new StringBuilder(undoStack.pop());
		System.out.println("Undo successful!");
		
	}
	
	public static void redo() {
		if(redoStack.empty()) {
			System.out.println("Nothing to redo");
			return;
		}
		undoStack.push(text.toString());
		text = new StringBuilder(redoStack.pop());
		System.out.println("Redo successful!");
	}
	
	public static void display() {
		System.out.println("Current Document:");
		System.out.println(text);
	}
	
}
