/*
 * Nama  : Nisa Shadrina
 * NIM   : 191524053
 * Kelas : 1B - D4
 */
package RandomItemset;
/**
 *
 * @author feuillelisse, friskaim7
 */

import CSVTool.CSVTool;
import java.util.ArrayList;
import java.util.Random;

public class RandomItemset{
    private static int column[];
    private static ArrayList<Integer> combination;
    private static ArrayList<ArrayList<Integer>> storage;
    private static int amountOfElement;
    private static final int maxRandom = 50;
    private static ArrayList<String> goods;
    private static ArrayList<String[]> transaction;
    public static String[] products = {"apple", "baby diaper", "biscuit", "bread", "broccoli", "cake",
                                    "chicken", "coke", "egg", "fish", "grape", "meat", "milk", "noodle", 
                                    "orange juice", "paprika", "salt", "shampoo", "soap", "sugar"};
    
    public static void Combinations(int set, int order, int n){
        column[set] = column[set - 1]; 
        while(column[set] < n - order + set){
            column[set] = column[set] + 1;
            if(set < order){
                Combinations(set + 1, order, n);
            }else{
                combination = new ArrayList<Integer>();
                for(int j = 1; j <= order; j++){
                    combination.add(column[j]);
                }
                amountOfElement += 1;
                storage.add(amountOfElement, combination);
                combination = storage.get(amountOfElement);
            }
        }
    }
    
    public static ArrayList<ArrayList<Integer>> GenerateCombination(int n){
        column = new int[n+1];
        storage = new ArrayList<ArrayList<Integer>>();
        amountOfElement = -1;
        
        for(int r = 1; r <= n; r++){
            column[0] = 0;
            Combinations(1, r, n);
        }
        
        for(int i = 0; i < storage.size(); i++){
            combination = storage.get(i);
        }
        return storage;
    }
    
    public static String[] GetStringArray(ArrayList<String> array){ 
        String list[] = new String[array.size()];
        for (int j = 0; j < array.size(); j++){
            list[j] = array.get(j); 
        }
        return list; 
    }
    
    public static void GenerateTransaction(int amount, String fileName){
        transaction = new ArrayList<String[]>();
        for(int i = 0; i < amount; i++){
            Random rand = new Random(); 
            int random = rand.nextInt(maxRandom);
            combination = storage.get(random + (random*100*(100*random % 99)));
            
            goods = new ArrayList<String>();
            for(int j = 0; j < combination.size(); j++){
                int idx = combination.get(j) - 1;
                goods.add(products[idx]);
            }
            String[] listOfTransaction = GetStringArray(goods);
            transaction.add(listOfTransaction);
        }
        CSVTool.writeCSV(transaction, fileName);
    }
    
    public static void main(String[] args){
        ArrayList<ArrayList<Integer>> hasil = GenerateCombination(20);
        GenerateTransaction(100, "transactions_100.csv");
    }
}
