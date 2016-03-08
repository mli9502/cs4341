import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * This class represents the items (variables).
 */
public class Items {
    private ArrayList<Item> items;

    public Items(){
        items = new ArrayList<Item>();
    }
    // Copy constructor for items.
    public Items(Items items){
        for(int i = 0; i < items.getItems().size(); i ++){
            items.addItem(new Item(items.getItems().get(i)));
        }
    }
    // Add an item to item list.
    public void addItem(Item item){
        this.items.add(item);
    }
    // Remove an item from item list.
    public void removeItem(Item item){
        for(int i = 0; i < items.size(); i ++){
            if(items.get(i).equals(item)){
                items.remove(i);
            }
        }
    }
    // Get an item by given id.
    public Item getItemById(int id){
        return items.get(id);
    }
    // Get an item by given name.
    public Item getItemByName(String name){
        for(int i = 0; i < items.size(); i ++){
            if(items.get(i).getName().equals(name)){
                return items.get(i);
            }
        }
        return null;
    }
    // Get an unused item according to solution.
    public Item getUnusedItem(Solution s){
        ArrayList<Item> usedItems = s.getUsedItems();
        for(int i = 0; i < items.size(); i ++){
            int j = 0;
            for(j = 0; j < usedItems.size(); j ++){
                if(items.get(i).equals(usedItems.get(j))){
                    break;
                }
            }
            if(j == usedItems.size()){
                return items.get(i);
            }
        }
        return null;
    }
    // Get an unused item according to Minimum remaining value heuristic.
    // If remaining values are the same, use degree heuristic.
    public Item getUnusedItemMRV(Solution sol, Matrices matrices){
        ArrayList<Item> usedItems = sol.getUsedItems();
        ArrayList<Item> tmpList = new ArrayList<Item>();
        for(int i = 0; i < items.size(); i ++){
            tmpList.add(new Item(items.get(i)));
        }
        tmpList.removeAll(usedItems);
        // Get domain for all unused items.
        for(int i = 0; i < tmpList.size(); i ++){
            tmpList.get(i).updateAndGetDomain(matrices);
        }
        // Sort the list.
        Collections.sort(tmpList);
        int minRemVal = tmpList.get(0).getDomain().size();
        ArrayList<Item> equalList = new ArrayList<Item>();
        for(int i = 0; i < tmpList.size(); i ++){
            if(tmpList.get(i).getDomain().size() == minRemVal){
                equalList.add(tmpList.get(i));
            }
        }
        if(equalList.size() == 1){
            return equalList.get(0);
        }else{  // Apply degree heuristic for items with same domain count.
            int maxCons = Integer.MIN_VALUE;
            int rtnInd = 0;
            for(int i = 0; i < equalList.size(); i ++){
                int consCont = equalList.get(i).getConsCnt();
                if(consCont > maxCons){
                    maxCons = consCont;
                    rtnInd = i;
                }
            }
            if(rtnInd == 0){
                int maxWeight = Integer.MIN_VALUE;
                for(int i = 0; i < equalList.size(); i ++){
                    if(equalList.get(i).getWeight() > maxWeight){
                        maxWeight = equalList.get(i).getWeight();
                        rtnInd = i;
                    }
                }
            }
            return equalList.get(rtnInd);
        }
    }
    // Get the size of item list.
    public int getSize(){
        return this.items.size();
    }
    // Get the items in ArrayList form.
    public ArrayList<Item> getItems(){
        return this.items;
    }
    @Override
    public String toString(){
        return items.toString();
    }
}
