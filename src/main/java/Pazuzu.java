import java.util.Scanner;

public class Pazuzu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Hello! I'm Pazuzu!\nWhat can I do for you?\n");
        
        while (true) {
            String input = scanner.nextLine();
            
            if (input.equals("bye")) {
                System.out.println("Bye.");
                break;
            }
            
            System.out.println(input);
            System.out.println("===========");
        }
        
        scanner.close();
    }
}
