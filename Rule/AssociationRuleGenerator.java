/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rule;

import Trie.Trie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class implements the algorithm for mining association rules out of 
 * frequent itemsets.
 * 
 * @author Rodion "rodde" Efremov
 * Modified by Nisa Shadrina (Jul 21, 2020)
 * @version 1.6 (Apr 10, 2016)
 * @param <I> the actual item type.
 */
public class AssociationRuleGenerator<I> {
    
    public List<AssociationRule<I>> mineAssociationRules(FrequentItemset<I> data, double minimumConfidence) {
        Objects.requireNonNull(data, "The frequent itemset data is null.");
        checkMinimumConfidence(minimumConfidence);

        ArrayList<AssociationRule<I>> resultSet = new ArrayList<>();

        for (ArrayList<I> itemset : data.getFrequentItemsetList()) {
            if (itemset.size() < 2) {
                // Any association rule requires at least one item in the 
                // leftHS, and at least one item in the rightHS. An
                // itemset containing less than two items cannot satisfy this
                // requirement; skip it.
                continue;
            }

            // Generate the basic association rules out of current itemset.
            // An association rule is basic iff its rightHS contains only one
            // item.
            ArrayList<AssociationRule<I>> basicAssociationRuleSet = generateAllBasicAssociationRules(itemset, data);

            generateAssociationRules(itemset, basicAssociationRuleSet, data, minimumConfidence, resultSet);
        }

        return resultSet.stream()
        .sorted((a1, a2) -> Double.compare(a2.getConfidence(), a1.getConfidence()))
        .collect(Collectors.toList());
    }

    private void generateAssociationRules(ArrayList<I> itemset, ArrayList<AssociationRule<I>> ruleSet,
                                          FrequentItemset<I> data, double minimumConfidence,
                                          ArrayList<AssociationRule<I>> collector) {
        if (ruleSet.isEmpty()) {
            return;
        }

        // The size of the itemset.
        int k = itemset.size(); 
        // The size of the rightHS of the input rules.
        int m = ruleSet.iterator().next().getRightHS().size();

        // Test whether we can pull one more item from the leftHS to 
        // rightHS.
        if (k > m + 1) {
            ArrayList<AssociationRule<I>> nextRules = moveOneItemToRightHS(itemset, ruleSet, data);

            Iterator<AssociationRule<I>> iterator = nextRules.iterator();

            while (iterator.hasNext()) {
                AssociationRule<I> rule = iterator.next();

                if (rule.getConfidence() >= minimumConfidence) {
                    collector.add(rule);
                } else {
                    iterator.remove();
                }
            }

            generateAssociationRules(itemset, nextRules, data, minimumConfidence, collector);
        }
    }

    private ArrayList<AssociationRule<I>> moveOneItemToRightHS(ArrayList<I> itemset, ArrayList<AssociationRule<I>> ruleSet, FrequentItemset<I> data) {
        ArrayList<AssociationRule<I>> output = new ArrayList<>();
        ArrayList<I> leftHS = new ArrayList<>();
        ArrayList<I> rightHS = new ArrayList<>();
        double itemsetSupportCount = data.getSupportCountMap().get(itemset);

        // For each rule ...
        for (AssociationRule<I> rule : ruleSet) {
            leftHS.clear();
            rightHS.clear();
            leftHS.addAll(rule.getLeftHS());
            rightHS.addAll(rule.getRightHS());

            // ... move one item from its leftHS to its consequnt.
            for (I item : rule.getLeftHS()) {
                leftHS.remove(item);
                rightHS.add(item);

                int leftHSSupportCount = data.getSupportCountMap().get(leftHS);
                AssociationRule<I> newRule = new AssociationRule<>(leftHS, rightHS,
                                            itemsetSupportCount / leftHSSupportCount);

                output.add(newRule);
                leftHS.add(item);
                rightHS.remove(item);
            }
        }
        return output;
    }

   /**
    * Given a frequent itemset of size {@code n}, generates and returns all 
    * {@code n} possible association rules with rightHS of size one.
    * 
    * @param itemset the itemset.
    * @return a set of association rules with rightHS of size one.
    */
    private ArrayList<AssociationRule<I>> generateAllBasicAssociationRules(ArrayList<I> itemset,
                                         FrequentItemset<I> data){
        ArrayList<AssociationRule<I>> basicAssociationRuleSet = new ArrayList<>(itemset.size());
        
        Trie trie = new Trie();
        ArrayList<I> leftHS = new ArrayList<>(itemset);
        ArrayList<I> rightHS = new ArrayList<>(1);

        for (I item : itemset) {
            leftHS.remove(item);
            rightHS.add(item);

            int itemsetSupportCount = data.getSupportCountMap().get(itemset);
            int leftHSSupportCount = data.getSupportCountMap().get(rightHS);

            double confidence = 1.0 * itemsetSupportCount / leftHSSupportCount;

            basicAssociationRuleSet.add(new AssociationRule(leftHS, rightHS, confidence));
            leftHS.add(item);
            rightHS.remove(item);
        }
        return basicAssociationRuleSet;
    }

    private void checkMinimumConfidence(double minimumConfidence) {
        if (Double.isNaN(minimumConfidence)) {
            throw new IllegalArgumentException(
                    "The input minimum confidence is NaN.");
        }

        if (minimumConfidence < 0.0) {
            throw new IllegalArgumentException(
                    "The input minimum confidence is negative: " + 
                    minimumConfidence + ". " +
                    "Must be at least zero.");
        }

        if (minimumConfidence > 1.0) {
            throw new IllegalArgumentException(
                    "The input minimum confidence is too large: " +
                    minimumConfidence + ". " +
                    "Must be at most 1.");
        }
    }
}

