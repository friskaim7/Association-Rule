import TransactionMaker.TransactionMaker;
import java.util.Scanner;

/**
 *
 * @author hp
 */
public class MakingTransaction {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        menu();
    }
    
    public static void menu() {
        TransactionMaker transaction = new TransactionMaker();
        System.out.println("Enter Maximum Transaction : ");
        int total = scanner.nextInt();
        System.out.println("Enter File Name : ");
        String file = getUserInput();
        transaction.GenerateTransaction(total, file);
    }
    
    public static String getUserInput() {
        return scanner.next();
    }
}
