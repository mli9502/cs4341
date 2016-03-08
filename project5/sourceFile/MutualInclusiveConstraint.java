import java.util.ArrayList;

/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * Class represents the mutual inclusive constraint.
 * firstItems and secondItems keeps track of the items,
 * firstBagNames and secondBagNames keeps track of the bag names items should be in.
 *
 * e.g. If the constraint is A B c d
 * firstItems contains A, secondItems contains B, firstBagNames contains c and secondBagNames contains d.
 */
public class MutualInclusiveConstraint {
    ArrayList<Item> firstItems;
    ArrayList<Item> secondItems;
    ArrayList<String> firstBagNames;
    ArrayList<String> secondBagNames;

    public MutualInclusiveConstraint(){
        this.firstItems = new ArrayList<Item>();
        this.secondItems = new ArrayList<Item>();
        this.firstBagNames = new ArrayList<String>();
        this.secondBagNames = new ArrayList<String>();
    }
    // Add a mutual inclusive constraint from two items and two bag names.
    public void addConstraint(Item fi, Item si, String fbn, String sbn){
        this.firstItems.add(fi);
        this.secondItems.add(si);
        this.firstBagNames.add(fbn);
        this.secondBagNames.add(sbn);
    }
    // Given an Item - Bag pair, return a list of corresponding pairs according to the constraint.
    public ArrayList<Pair> getCorrPair(Pair pair, Bags bags){
        ArrayList<Pair> rtnPairs = new ArrayList<Pair>();
        // Check whether input item includes in first arraylist.
        if(!firstItems.contains(pair.getItem()) && !secondItems.contains(pair.getItem())){
            return null;
        }
        for(int i = 0; i < firstItems.size(); i ++){
            // If found a match.
            if(firstItems.get(i).equals(pair.getItem())){
                if(pair.getBag().getName() == firstBagNames.get(i)){  // If the other half is in solution and it's in one of the two bags.
                    rtnPairs.add(new Pair(secondItems.get(i), bags.getBagByName(secondBagNames.get(i))));
                }else if(pair.getBag().getName() == secondBagNames.get(i)){
                    rtnPairs.add(new Pair(secondItems.get(i), bags.getBagByName(firstBagNames.get(i))));
                }
            }
        }
        for(int i = 0; i < secondItems.size(); i ++){
            if(secondItems.get(i).equals(pair.getItem())){
                if(pair.getBag().getName() == firstBagNames.get(i)){  // If the other half is in solution and it's in one of the two bags.
                    rtnPairs.add(new Pair(firstItems.get(i), bags.getBagByName(secondBagNames.get(i))));
                }else if(pair.getBag().getName() == secondBagNames.get(i)){
                    rtnPairs.add(new Pair(firstItems.get(i), bags.getBagByName(firstBagNames.get(i))));
                }
            }
        }
        if(rtnPairs.size() == 0){
            return null;
        }else{
            return rtnPairs;
        }
    }
    // Checks whether this constraint is satisfied.
    public boolean satisfy(Pair pair, Solution sol){
//        System.out.println("++++++++++++++++++++++++++++++ In mic.satisfy()");
//        System.out.println("Pair is ");
//        System.out.println(pair);
//        System.out.println("Sol is ");
//        System.out.println(sol);
//        System.out.println("first item is ");
//        System.out.println(firstItems);
//        System.out.println("second item is ");
//        System.out.println(secondItems);
//        System.out.println("first bag names is ");
//        System.out.println(firstBagNames);
//        System.out.println("second bag names is ");
//        System.out.println(secondBagNames);
//        System.out.println("++++++++++++++++++++++++++++++");
        // Check whether input item includes in first arraylist.
        boolean firstRtn = false;
        boolean secondRtn = false;
        boolean eqFlg = false;
        // If the item is not found in constraint item lists, return true.
        if(!firstItems.contains(pair.getItem()) && !secondItems.contains(pair.getItem())){
            return true;
        }
        // Check all the items on the first list.
        for(int i = 0; i < firstItems.size(); i ++){
            // If found a match with the input item.
            if(firstItems.get(i).equals(pair.getItem())){
                // Get the bag for the corresponding item from solution.
                Bag b = sol.getBagByItem(secondItems.get(i));
                // If the other half is not in solution, return true.
                if(b == null){
                    return true;
                }else if(b.getName().equals(firstBagNames.get(i))){  // If the other half is in solution and corresponds to the bag in first bag list, set equal flag.
                    eqFlg = true;
                    if(pair.getBag().getName().equals(secondBagNames.get(i))){   // If the input bag corresponds to the one in second bag list, set firstRtn to true.
                        firstRtn |= true;
                    }else{
                        firstRtn |= false;
                    }
                }else if(b.getName().equals(secondBagNames.get(i))){
                    eqFlg = true;
                    if(pair.getBag().getName().equals(firstBagNames.get(i))){
                        firstRtn |= true;
                    }else{
                        firstRtn |= false;
                    }
                }
            }
        }
        // Check the second list.
        for(int i = 0; i < secondItems.size(); i ++){
            // If found a match.
            if(secondItems.get(i).equals(pair.getItem())){
                // Check whether the other half is in solution or not.
                Bag b = sol.getBagByItem(firstItems.get(i));
                // If the other half is not in solution, return true.
                if(b == null){
                    return true;
                }else if(b.getName().equals(firstBagNames.get(i))){  // If the other half is in solution and it's in one of the two bags.
                    eqFlg = true;
                    if(pair.getBag().getName().equals(secondBagNames.get(i))){   // If the bag in input pair matches the other bag, return true.
                        secondRtn |= true;
                    }else{
                        secondRtn |= false;
                    }
                }else if(b.getName().equals(secondBagNames.get(i))){
                    eqFlg = true;
                    // System.out.println("equals second bag name");
                    if(pair.getBag().getName().equals(firstBagNames.get(i))){
                        secondRtn |= true;
                    }else{
                        secondRtn |= false;
                    }
                }
            }
        }
        // If unable to find a equality while the other half is in solution, check whether the newly added half satisfies the constraint.
        // If not, return true, if satisfies, return false;
        if(!eqFlg){
            for(int i = 0; i < firstItems.size(); i ++){
                if(pair.getBag().getName().equals(firstBagNames.get(i)) ||
                        pair.getBag().getName().equals(secondBagNames.get(i))){
                    return false;
                }else{
                    return true;
                }
            }
        }
        return firstRtn | secondRtn;
    }
}
