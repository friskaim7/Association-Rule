import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import Trie.*;

public class MainClass {
    private final static String EXIT = "0";
    private final static String READ_FILE = "1";

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        showMenu();
        String userChoice = getUserInput();
        switchMenu(userChoice);
    }

    public static void switchMenu(String userChoice) {
        switch (userChoice) {
            case READ_FILE:
                System.out.print("\nEnter file name : ");
                String file = getUserInput();
                if (checkFileExistence(file)) {
                    doGenerateAssociationRule(file);
                } else {
                    System.out.println("Invalid file. Please try again. . .\n");
                    showMenu();
                    switchMenu(getUserInput());
                }
                break;

            case EXIT:
                System.out.println("Thank you. . .");
                break;

            default:
                System.out.println("Invalid menu. Please try again. . .\n");
                showMenu();
                switchMenu(getUserInput());
                break;
        }
    }

    public static void doGenerateAssociationRule(String file) {
        Trie transcationTrie = new Trie();
        transcationTrie.generateTransactionTrie(file);
        System.out.println("Total Transaction : " + transcationTrie.getTotalTransaction());
        transcationTrie.preorderPrint(transcationTrie.getRoot());

        System.out.println("\n\n");

        ArrayList<TrieNode> emptyTemporaryList = new ArrayList<>();
        transcationTrie.convertTrietoArrayList(transcationTrie.getRoot(), emptyTemporaryList);
        for (ArrayList<TrieNode> transaction : transcationTrie.getAllTransactions()) {
            for (TrieNode trieNode : transaction) {
                System.out.print("(" + trieNode.getProduct() + " " + trieNode.getFrequency() + ") ");
            }
            System.out.println("\n");
        }

    }

    public static void showMenu() {
        System.out.println("-------------------------------");
        System.out.println("   Association  Rule  Mining");
        System.out.println("-------------------------------");
        System.out.println("1. Generate Association Rule");
        System.out.println("0. Exit");
        System.out.print("\nYour choice : ");
    }

    public static String getUserInput() {
        return scanner.next();
    }

    public static boolean checkFileExistence(String filePath) {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            return true;
        }
        return false;
    }
}