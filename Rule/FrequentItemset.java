/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rule;

import Trie.Trie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author hp
 */
public class FrequentItemset<I>{
    private final List<ArrayList<I>> frequentItemsetList;
    private final Map<ArrayList<I>, Integer> supportCountMap;
    private final double threshold;
    private final int numberOfTransactions;
    
    public FrequentItemset(List<ArrayList<I>> frequentItemsetList, Map<ArrayList<I>, Integer> supportCountMap,
                        double threshold, int transactionNumber) {
        this.frequentItemsetList = frequentItemsetList;
        this.supportCountMap = supportCountMap;
        this.threshold = threshold;
        this.numberOfTransactions = transactionNumber;
    }
    
    public List<ArrayList<I>> getFrequentItemsetList() {
        return frequentItemsetList;
    }

    public Map<ArrayList<I>, Integer> getSupportCountMap() {
        return supportCountMap;
    }

    public double getThreshold() {
        return threshold;
    }

    public int getTransactionNumber() {
        return numberOfTransactions;
    }

    public double getSupport(ArrayList<I> itemset) {
        return 1.0 * supportCountMap.get(itemset) / numberOfTransactions;
    }
    
}
