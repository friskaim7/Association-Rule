/*This file contains methods that on progress, these methods is inspired by nirajdewani on Github*/

public class OnProgress{
    public void combinations(String input){
        int inputSupport = getSupportFromTrie(this.root, input);
        ArrayList<String> itemset = new ArrayList<String>();
        String[] temp = input.split(",");
        for (int k = 0; k < temp.length; k++){
            itemset.add(temp[k]);
        }
        
        int countOfSubsets = ((int) (Math.pow(2, itemset.size()))) - 2; 
        int longestBinaryString = Integer.toBinaryString(countOfSubsets).length();
        String formatString = "%" + longestBinaryString + "s";
        
        int i, j;
        
        String[] setOfLHS = new String[countOfSubsets];
        String[] setOfRHS = new String[countOfSubsets];
        String[] binarySequence = new String[countOfSubsets];
        
        for (i = 0; i < countOfSubsets; i++){
                binarySequence[i] = String.format(formatString, Integer.toBinaryString(i+1)).replace(' ', '0');
        }
        
        StringBuffer tempSB = new StringBuffer();
        
        //generating LHS of rules
        for (i = 0; i < countOfSubsets; i++){
            tempSB.setLength(0);
            for (j = 0; j < longestBinaryString; j++){
                if (binarySequence[i].charAt(j) == '1'){
                    tempSB.append(itemset.get(j) + ",");
                }
            }
            setOfLHS[i] = tempSB.toString();
        }
        
        //generating RHS of rules
        for (i = 0; i < countOfSubsets; i++){
                tempSB.setLength(0);
                for (j = 0; j < itemset.size(); j++){	
            String[] LHSitems = setOfLHS[i].split(",");
            String currentRHSitem = itemset.get(j);
            boolean isPresent = false;
                    for (int k = 0; k < LHSitems.length; k++){
                        if (LHSitems[k].equals(currentRHSitem)){
                isPresent = true;
                break;
                        }
            }
            if(!isPresent){
                        tempSB.append(currentRHSitem + ",");
            }
                
                }
                setOfRHS[i] = tempSB.toString();
        }	
        
        for (i = 0; i < countOfSubsets; i++){
                int lhsSupport = getSupportFromTrie(this.root, setOfLHS[i]);
                int rhsSupport = getSupportFromTrie(this.root, setOfRHS[i]);
                double confidence = (double)inputSupport/lhsSupport;
                int productOfLhsRhs = lhsSupport * rhsSupport;
                double lift = (double)(inputSupport)/(productOfLhsRhs);
                double leverage = (double)(inputSupport - (productOfLhsRhs));
                this.listOfRules.add(new Rule(setOfLHS[i].substring(0,setOfLHS[i].length()-1) + " --> " + setOfRHS[i].substring(0,setOfRHS[i].length() - 1), confidence, lift, inputSupport, leverage));
        }		
    }
    
    public int getSupportFromTrie(TrieNode node, String itemset){
    	TrieNode current = node;
    	String[] items = itemset.split(",");
        Iterator<TrieNode> it = node.childrens.iterator();
    	while(it.hasNext()){   		
            current = it.next();
    	}
    	return current.frequency;
    }
    
    public void sortRules(){
    	Collections.sort(this.listOfRules);
    }
    
    public void printRules(){
    	DecimalFormat df = new DecimalFormat("#.##");
        for (int i = 0; i < this.listOfRules.size(); i++){
            System.out.println(this.listOfRules.get(i).rule + " (" + "Conf: " + df.format((this.listOfRules.get(i).confidence * 100)) + "% " + "Lift: " + df.format((this.listOfRules.get(i).lift * 100)) + "% " + "Lev: " + (this.listOfRules.get(i).leverage) + ")");
        }
    }
}