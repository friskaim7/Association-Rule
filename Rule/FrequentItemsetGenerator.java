package Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class implements the 
 * <a href="https://en.wikipedia.org/wiki/Apriori_algorithm">Apriori algorithm</a> 
 * for frequent itemset generation.
 * 
 * @author Rodion "rodde" Efremov 
 * Modified by Nisa Shadrina (Jul 21, 2020)
 * @version 1.6 (Sep 14, 2015)
 * @param <I> the actual item type.
 */
public class FrequentItemsetGenerator<I>{
    /**
     * Generates the frequent itemset data.
     * 
     * @param transactionList the list of transactions to mine.
     * @param threshold  the minimum support.
     * @return the object describing the result of this task.
     */
    public FrequentItemset<I> generate(List<ArrayList<I>> transactionList, double threshold) {
        Objects.requireNonNull(transactionList, "The itemset list is empty.");
        checkSupport(threshold);

        if (transactionList.isEmpty()) {
            return null;
        }
        
        // Maps each itemset to its support count. Support count is simply the 
        // number of times an itemset appeares in the transaction list.
        Map<ArrayList<I>, Integer> supportCountMap = new HashMap<>();
        
        // Get the list of 1-itemsets that are frequent.
        List<ArrayList<I>> frequentItemList = findFrequentItems(transactionList, supportCountMap, threshold);

        // Maps each 'k' to the list of frequent k-itemsets.
        Map<Integer, List<ArrayList<I>>> map = new HashMap<>();
        map.put(1, frequentItemList);

        // 'k' denotes the cardinality of itemsets processed at each iteration
        // of the following loop.
        int k = 1;
        do{
            ++k;
            // First generate the candidates.
            List<ArrayList<I>> candidateList = generateCandidates(map.get(k - 1));

            for (ArrayList<I> transaction : transactionList) {
                List<ArrayList<I>> candidateList2 = subset(candidateList, transaction);

                for (ArrayList<I> itemset : candidateList2) {
                    supportCountMap.put(itemset, supportCountMap.getOrDefault(itemset, 0) + 1);
                }
            }

            map.put(k, getNextItemsets(candidateList, supportCountMap, threshold, transactionList.size()));
        }while (!map.get(k).isEmpty());
        return new FrequentItemset<>(extractFrequentItemsets(map), supportCountMap, threshold, transactionList.size());
    }
    
    
    /**
     * This method simply concatenates all the lists of frequent itemsets into
     * one list.
     * 
     * @param  map the map mapping an itemset size to the list of frequent
     *             itemsets of that size.
     * @return the list of all frequent itemsets.
     */
    private List<ArrayList<I>> extractFrequentItemsets(Map<Integer, List<ArrayList<I>>> map){
        
        List<ArrayList<I>> allFreqItem = new ArrayList<>();

        for (List<ArrayList<I>> itemsetList : map.values()) {
            allFreqItem.addAll(itemsetList);
        }
        return allFreqItem;
    }

    /**
     * This method gathers all the frequent candidate itemsets into a single 
     * list.
     * 
     * @param candidateList   the list of candidate itemsets.
     * @param supportCountMap the map mapping each itemset to its support count.
     * @param minimumSupport  the minimum support.
     * @param transactions    the total number of transactions.
     * @return a list of frequent itemset candidates.
     */
    private List<ArrayList<I>> getNextItemsets(List<ArrayList<I>> candidateList,
                                         Map<ArrayList<I>, Integer> supportCountMap,
                                         double minimumSupport, int transactions){
        List<ArrayList<I>> itemsetCandidates = new ArrayList<>(candidateList.size());

        for (ArrayList<I> itemset : candidateList) {
            if (supportCountMap.containsKey(itemset)) {
                int supportCount = supportCountMap.get(itemset);
                double support = 1.0 * supportCount / transactions;
                if (support >= minimumSupport) {
                    itemsetCandidates.add(itemset);
                }
            }
        }
        return itemsetCandidates;
    }

    /**
     * Computes the list of itemsets that are all subsets of 
     * {@code transaction}.
     * 
     * @param candidateList the list of candidate itemsets.
     * @param transaction   the transaction to test against.
     * @return the list of itemsets that are subsets of {@code transaction}
     *         itemset.
     */
    private List<ArrayList<I>> subset(List<ArrayList<I>> candidateList, ArrayList<I> transaction) {
        List<ArrayList<I>> subsets = new ArrayList<>(candidateList.size());

        for (ArrayList<I> candidate : candidateList) {
            if (transaction.containsAll(candidate)) {
                subsets.add(candidate);
            }
        }
        return subsets;
    }

    /**
     * Generates the next candidates. This is so called F_(k - 1) x F_(k - 1) 
     * method.
     * 
     * @param itemsetList the list of source itemsets, each of size <b>k</b>.
     * @return the list of candidates each of size <b>k + 1</b>.
     */
    private List<ArrayList<I>> generateCandidates(List<ArrayList<I>> itemsetList) {
        List<List<I>> list = new ArrayList<>(itemsetList.size());

        for (ArrayList<I> itemset : itemsetList) {
            List<I> l = new ArrayList<>(itemset);
            Collections.<I>sort(l, ITEM_COMPARATOR);
            list.add(l);
        }

        int listSize = list.size();

        List<ArrayList<I>> listofCandidates = new ArrayList<>(listSize);

        for (int i = 0; i < listSize; ++i) {
            for (int j = i + 1; j < listSize; ++j) {
                ArrayList<I> candidate = tryMergeItemsets(list.get(i), list.get(j));

                if (candidate != null) {
                    listofCandidates.add(candidate);
                }
            }
        }

        return listofCandidates;
    }

    /**
     * Attempts the actual construction of the next itemset candidate.
     * @param itemset1 the list of elements in the first itemset.
     * @param itemset2 the list of elements in the second itemset.
     * 
     * @return a merged itemset candidate or {@code null} if one cannot be 
     *         constructed from the input itemsets.
     */
    private ArrayList<I> tryMergeItemsets(List<I> itemset1, List<I> itemset2) {
        int length = itemset1.size();

        for (int i = 0; i < length - 1; ++i) {
            if (!itemset1.get(i).equals(itemset2.get(i))) {
                return null;
            }
        }

        if (itemset1.get(length - 1).equals(itemset2.get(length - 1))) {
            return null;
        }

        ArrayList<I> mergedItemset = new ArrayList<>(length + 1);

        for (int i = 0; i < length - 1; ++i) {
            mergedItemset.add(itemset1.get(i));
        }

        mergedItemset.add(itemset1.get(length - 1));
        mergedItemset.add(itemset2.get(length - 1));
        return mergedItemset;
    }

    private static final Comparator ITEM_COMPARATOR = new Comparator() {

        @Override
        public int compare(Object o1, Object o2) {
            return ((Comparable) o1).compareTo(o2);
        }

    };

    /**
     * Computes the frequent itemsets of size 1.
     * 
     * @param itemsetList     the entire database of transactions.
     * @param supportCountMap the support count map to which to write the 
     *                        support counts of each item.
     * @param minimumSupport  the minimum support.
     * @return                the list of frequent one-itemsets.
     */
    private List<ArrayList<I>> findFrequentItems(List<ArrayList<I>> itemsetList,
                                           Map<ArrayList<I>, Integer> supportCountMap,
                                           double minimumSupport) {
        Map<I, Integer> map = new HashMap<>();

        // Count the support counts of each item.
        for (ArrayList<I> itemset : itemsetList) {
            for (I item : itemset) {
                ArrayList<I> tmp = new ArrayList<>(1);
                tmp.add(item);

                supportCountMap.put(tmp, supportCountMap.getOrDefault(tmp, 0) + 1);
                map.put(item, map.getOrDefault(item, 0) + 1);
            }
        }

        List<ArrayList<I>> frequentItemsetList = new ArrayList<>();

        for (Map.Entry<I, Integer> entry : map.entrySet()) {
            if (1.0 * entry.getValue() / itemsetList.size() >= minimumSupport) {
                ArrayList<I> itemset = new ArrayList<>(1);
                itemset.add(entry.getKey());
                frequentItemsetList.add(itemset);
            }
        }
        return frequentItemsetList;
    }

    private void checkSupport(double support) {
        if (Double.isNaN(support)) {
            throw new IllegalArgumentException("The input support is NaN.");
        }

        if (support > 1.0) {
            throw new IllegalArgumentException(
                    "The input support is too large: " + support + ", " +
                    "should be at most 1.0");
        }

        if (support < 0.0) {
            throw new IllegalArgumentException(
                    "The input support is too small: " + support + ", " +
                    "should be at least 0.0");
        }
    }
}
