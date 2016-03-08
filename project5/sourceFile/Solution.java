import java.util.ArrayList;

/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * Class used to store the solutions as the algorithm goes.
 */
public class Solution {
    ArrayList<Pair> solution;

    public Solution(){
        this.solution = new ArrayList<Pair>();
    }
    // Add an Item - Bag pair to solution.
    public void addPair(Pair p){
        solution.add(p);
    }
    // Remove an Item - Bag pair from solution.
    public void removePair(Pair p){
        solution.remove(p);
    }
    // Check whether the solution contains all items.
    // If it does, the algorithm can return.
    public boolean containsAllItem(Items items){
        if(solution.size() == items.getSize()){
            return true;
        }
        return false;
    }
    // Get the corresponding bag given an item.
    // Returns null when the item is not in solution.
    public Bag getBagByItem(Item item){
        for(int i = 0; i < solution.size(); i ++){
            if(solution.get(i).containsItem(item)){
                return solution.get(i).getBag();
            }
        }
        return null;
    }
    // Given an item pair with one in solution, one not, returns the
    // corresponding bag with the item that is in solution.
    public Bag getBagByItemPair(ItemPair ip){
        Bag b = getBagByItem(ip.getFirst());
        if(b == null){
            return getBagByItem(ip.getSecond());
        }
        return b;
    }
    // Get used items from solution.
    public ArrayList<Item> getUsedItems(){
        ArrayList<Item> items = new ArrayList<Item>();
        for(int i = 0; i < solution.size(); i ++){
            items.add(solution.get(i).getItem());
        }
        return items;
    }
    // Check whether a solution is empty.
    public boolean isEmpty(){
        return this.solution.isEmpty();
    }
    @Override
    public String toString(){
        String rtn = "";

        for(int i = 0; i < solution.size(); i ++){
            rtn += "~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
            rtn += solution.get(i).toString();
            rtn += "~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
        }
        return rtn;
    }
    // Format the solution and print it out.
    public void printSolution(Bags bags, Items items){
        String rtn = "";
        for(int i = 0; i < bags.getSize(); i ++){
            rtn += bags.getBags().get(i).getName() + " ";
            int totalWeight = 0;
            int itemCnt = 0;
            for(int j = 0; j < solution.size(); j ++){
                if(solution.get(j).getBag().equals(bags.getBags().get(i))){
                    rtn += solution.get(j).getItem().getName() + " ";
                    totalWeight += solution.get(j).getItem().getWeight();
                    itemCnt += 1;
                }
            }
            rtn += "\nnumber of items: " + itemCnt +
                    "\ntotal weight: " + totalWeight + "/" + bags.getBags().get(i).getLimit() +
                    "\nwasted capacity: " + (bags.getBags().get(i).getLimit() - totalWeight) + "\n";
        }
        System.out.println(rtn);
    }
}
