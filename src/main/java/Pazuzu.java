import java.util.Scanner;

public class Pazuzu {
    private static String[] items = new String[100];
    private static int itemCount = 0;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Hello! I'm Pazuzu!\nWhat can I do for you?\n");
        
        while (true) {
            String input = scanner.nextLine();
            
            if (input.equals("bye")) {
                System.out.println("Bye.");
                break;
            } else if (input.equals("List")) {
                printList();
            } else {
                addToList(input);
                System.out.println("added: " + input);
            }
            
            System.out.println("===========");
        }
        
        scanner.close();
    }
    
    /**
     * Adds a new item to the list if there is space available.
     * The item is stored in the internal array and the item count is incremented.
     * If the array is full (100 items), the item will not be added.
     * 
     * @param item the string item to be added to the list
     */
    private static void addToList(String item) {
        if (itemCount < 100) {
            items[itemCount] = item;
            itemCount++;
        }
    }
    
    /**
     * Prints all items currently stored in the list.
     * Each item is displayed with a numbered format starting from 1.
     * If the list is empty, nothing will be printed.
     */
    private static void printList() {
        for (int i = 0; i < itemCount; i++) {
            System.out.println((i + 1) + ". " + items[i]);
        }
    }
}
