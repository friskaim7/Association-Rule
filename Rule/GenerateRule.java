/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rule;

import Trie.Trie;
import Trie.TrieNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hp
 */
public class GenerateRule {
    public static void MakingAssociationRule(String fileName){
        FrequentItemsetGenerator<String> generator = new FrequentItemsetGenerator<>(); 
        List<ArrayList<String>> itemsetList = new ArrayList<>();
        
        Trie transactionTrie = new Trie();
        transactionTrie.generateTransactionTrie(fileName);

        ArrayList<String> emptyTemporaryList = new ArrayList<>();
        transactionTrie.convertTrietoArrayList(transactionTrie.getRoot(), emptyTemporaryList);
        for(int i = 0; i < transactionTrie.getTotalTransaction() - 1; i++){
            itemsetList.add(i, transactionTrie.getAllTransactions().get(i));
        }
        
        FrequentItemset<String> data = generator.generate(itemsetList, 0.5);

        System.out.println("--- Frequent itemsets ---");

        for (ArrayList<String> itemset : data.getFrequentItemsetList()) {
            System.out.printf("%s with support: %1.1f\n", itemset, data.getSupport(itemset));
        }

        List<AssociationRule<String>> associationRuleList = new AssociationRuleGenerator<String>()
                                      .mineAssociationRules(data, 0.5);
        System.out.println();
        System.out.println("--- Association rules ---");

        for (AssociationRule<String> rule : associationRuleList) {
            System.out.printf("%s\n", rule);
        }
    }
}
