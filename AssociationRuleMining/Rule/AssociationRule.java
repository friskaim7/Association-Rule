/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class holds an association rule and its confidence.
 * 
 * @author Rodion "rodde" Efremov
 * Modified by Nisa Shadrina (Jul 21, 2020)
 * @version 1.6 (Apr 10, 2016)
 * @param <I> the actual item type.
 */
public class AssociationRule<I> {

    private final ArrayList<I> leftHS = new ArrayList<>();
    private final ArrayList<I> rightHS = new ArrayList<>();
    private double confidence;

    public AssociationRule(ArrayList<I> leftHS, ArrayList<I> rightHS, double confidence) {
        Objects.requireNonNull(leftHS, "The rule LHS is null.");
        Objects.requireNonNull(rightHS, "The rule RHS is null.");
        this.leftHS.addAll(leftHS);
        this.rightHS.addAll(rightHS);
        this.confidence = confidence;
    }

    public AssociationRule(ArrayList<I> leftHS, ArrayList<I> rightHS) {
        this(leftHS, rightHS, Double.NaN);
    }

    public List<I> getLeftHS() {
        return Collections.<I>unmodifiableList(leftHS);
    }

    public List<I> getRightHS() {
        return Collections.<I>unmodifiableList(rightHS);
    }

    public double getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(Arrays.toString(leftHS.toArray()));
        sb.append(" -> ");
        sb.append(Arrays.toString(rightHS.toArray()));
        sb.append(": ");
        sb.append(confidence);

        return sb.toString();
    }

    @Override
    public int hashCode() {
        return leftHS.hashCode() ^ rightHS.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        AssociationRule<I> other = (AssociationRule<I>) obj;
        return leftHS.equals(other.leftHS) &&
               rightHS.equals(other.rightHS);
    }
}
