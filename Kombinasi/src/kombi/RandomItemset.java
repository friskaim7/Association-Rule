package kombi;

/*
 * Nama  : Nisa Shadrina
 * NIM   : 191524053
 * Kelas : 1B - D4
 */


/**
 *
 * @author feuillelisse
 */
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomItemset{
    private static int column[];
    private static ArrayList<Integer> combination;
    private static ArrayList<ArrayList<Integer>> storage;
    private static int amountOfElement;
    private static final int maxRandom = 1000000;
    private static ArrayList<String> goods;
    private static ArrayList<String[]> transaction;
    
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
    
    public static void GenerateTransaction(int amount){
        final String[] products = {"apple", "baby diaper", "biscuit", "bread", "broccoli", "cake",
                                    "chicken", "coke", "egg", "fish", "grape", "meat", "milk", "noodle", 
                                    "orange juice", "paprika", "salt", "shampoo", "soap", "sugar"};
        int count = -1;
        transaction = new ArrayList<String[]>();
        for(int i = 0; i < amount; i++){
            Random rand = new Random(); 
            int random = rand.nextInt(maxRandom);
            combination = storage.get(random);
            
            goods = new ArrayList<String>();
            for(int j = 0; j < combination.size(); j++){
                int idx = combination.get(j) - 1;
                goods.add(products[idx]);
            }
            count += 1;
            String[] listOfTransaction = GetStringArray(goods);
            transaction.add(count, listOfTransaction);
        }
        writeCSV(transaction);
    }
    
    public static void writeCSV(ArrayList<String[]> transaction){

        File file = new File("transaction10m++.csv");
        try{
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile, ',', 
                                         CSVWriter.NO_QUOTE_CHARACTER, 
                                         CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                                         CSVWriter.DEFAULT_LINE_END); 

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[]{"Transactions"});
            for (int i=0; i < transaction.size() ; i++) {
                data.add(transaction.get(i));
            }
            writer.writeAll(data);
            
            writer.close(); 
        }catch (IOException e){
            e.printStackTrace(); 
        }
    }
    
    public static void main(String[] args){
        ArrayList<ArrayList<Integer>> hasil = GenerateCombination(20);
        GenerateTransaction(10000000);
        System.out.println("Make 10m transactions. \nMax item per transaction is 20 item.");
    }
}
