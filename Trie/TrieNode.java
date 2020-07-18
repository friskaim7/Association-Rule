package Trie;

import java.util.Iterator;
import java.util.TreeSet;

public class TrieNode implements Comparable<TrieNode> {
    protected TreeSet<TrieNode> childrens;
    protected int frequency; // frequency of the path from root to this TrieNode
    protected String product;

    public TrieNode() {
        this.childrens = new TreeSet<>();
        this.frequency = 0;
        this.product = null;
    }

    public TrieNode(String productName) {
        this();
        this.product = productName;
    }

    public TreeSet<TrieNode> getChildrens() {
        return childrens;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getProduct() {
        return product;
    }

    public void setChildrens(TreeSet<TrieNode> childrens) {
        this.childrens = childrens;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public int compareTo(TrieNode o) {
        return this.product.compareTo(o.product);
    }

    public boolean isRoot() {
        return this.product == null;
    }

    public boolean isSameProduct(String otherProduct) {
        return this.product == otherProduct;
    }

    public boolean addChild(TrieNode childNode) {
        return this.childrens.add(childNode);
    }

    public void addFrequency() {
        this.frequency++;
    }

    public TrieNode searchChild(String productName) {
        TrieNode currentNode = new TrieNode();
        Iterator<TrieNode> childrensIterator = this.childrens.iterator();

        // currentNode = childrensIterator.next();
        while (childrensIterator.hasNext()) {
            currentNode = childrensIterator.next();
            if (currentNode.product.equals(productName)) {
                break;
            }
        }

        return currentNode;
    }

}