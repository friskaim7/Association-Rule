package Trie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import CSVTool.*;

public class Trie {

    private TrieNode root; // the only TrieNode with null product field
    private int totalTransaction;
    private ArrayList<ArrayList<TrieNode>> allTransactions;

    public static void main(String[] args) {
        Trie transcationTrie = new Trie();
        transcationTrie.generateTransactionTrie("transactions.csv");
        System.out.println("Total Transaction : " + transcationTrie.totalTransaction);
        transcationTrie.preorderPrint(transcationTrie.root);

        System.out.println("\n\n");

        ArrayList<TrieNode> emptyTemporaryList = new ArrayList<>();
        transcationTrie.convertTrietoArrayList(transcationTrie.root, emptyTemporaryList);
        for (ArrayList<TrieNode> transaction : transcationTrie.allTransactions) {
            for (TrieNode trieNode : transaction) {
                System.out.print("(" + trieNode.product + " " + trieNode.frequency + ") ");
            }
            System.out.println("\n");
        }
    }

    public Trie() {
        this.root = new TrieNode();
        this.totalTransaction = 0;
        this.allTransactions = new ArrayList<ArrayList<TrieNode>>();
    }

    public TrieNode getRoot() {
        return root;
    }

    public ArrayList<ArrayList<TrieNode>> getAllTransactions() {
        return allTransactions;
    }

    public int getTotalTransaction() {
        return totalTransaction;
    }

    public void setRoot(TrieNode root) {
        this.root = root;
    }

    public void setAllTransactions(ArrayList<ArrayList<TrieNode>> allTransactions) {
        this.allTransactions = allTransactions;
    }

    public void setTotalTransaction(int totalTransaction) {
        this.totalTransaction = totalTransaction;
    }

    public void convertTrietoArrayList(TrieNode node, ArrayList<TrieNode> toFillList) {
        if (node.product != null) {
            toFillList.add(node);
        }
        if (!node.isLeaf()) {
            Iterator<TrieNode> nodeChildrenIterator = node.childrens.iterator();
            while (nodeChildrenIterator.hasNext()) {
                TrieNode nextNode = nodeChildrenIterator.next();
                ArrayList<TrieNode> temp = (ArrayList<TrieNode>) toFillList.clone();

                convertTrietoArrayList(nextNode, temp);
            }
        }
        if (node.isLeaf()) {
            this.allTransactions.add(toFillList);
        }
    }

    public void preorderPrint(TrieNode node) {
        if (node != null) {
            System.out.print("(" + node.product + " " + node.frequency + ") ");

            Iterator<TrieNode> childrensIterator = node.childrens.iterator();
            while (childrensIterator.hasNext()) {
                node = childrensIterator.next();
                preorderPrint(node);
            }
        }
    }

    public static void sort(String[] arrayToSort) {
        for (int j = 0; j < arrayToSort.length - 1; j++) {
            for (int i = j + 1; i < arrayToSort.length; i++) {
                if (arrayToSort[i].compareToIgnoreCase(arrayToSort[j]) < 0) {
                    String temp = arrayToSort[i];
                    arrayToSort[i] = arrayToSort[j];
                    arrayToSort[j] = temp;
                }
            }
        }
    }

    public void generateTransactionTrie(String fileName) {
        CSVTool csvTool = new CSVTool(fileName);
        List<String[]> allData = csvTool.readDataOnly();
        TrieNode currentNode = new TrieNode();

        for (String[] row : allData) {
            sort(row);
            currentNode = root;
            for (String cell : row) {
                if (cell.length() > 0) {
                    TrieNode productNode = new TrieNode(cell.trim().toLowerCase());

                    if (currentNode.addChild(productNode)) {
                        System.out.println("add " + productNode.product + " success. child of " + currentNode.product);
                    } else {
                        productNode = currentNode.searchChild(cell);
                        if (productNode.product.equals(cell)) {
                            System.out.println(productNode.product + " found. child of " + currentNode.product);
                        }
                    }
                    currentNode = productNode;
                }
            }
            if (currentNode.product != null) {
                currentNode.addFrequency();
                this.totalTransaction++;
            }
            System.out.println("Frequency : " + currentNode.frequency);
            System.out.println("--------------------------");
        }
    }
}