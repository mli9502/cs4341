import java.util.ArrayList;

/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * This class represents Binary Not Equal Constraint.
 */
public class BinaryNotEqualConstraint {
    private ArrayList<ItemPair> ips;

    public BinaryNotEqualConstraint(){
        this.ips = new ArrayList<ItemPair>();
    }
    // Add item pair to item pair list.
    public void addItemPair(Item a, Item b){
        this.ips.add(new ItemPair(a, b));
        return;
    }
    // Get the corresponding item for the given pair.
    public Item getCorrItem(Pair pair){
        int rtn = this.containsItem(pair.getItem());
        if(rtn == -1){
            return null;
        }
        ItemPair ip = ips.get(rtn);
        if(ip.getFirst().equals(pair.getItem())){
            return ip.getSecond();
        }else{
            return ip.getFirst();
        }
    }
    // Check whether the constraint is satisfied by the given Item - Bag pair and solution.
    public boolean satisfy(Pair pair, Solution sol){
        int rtn = this.containsItem(pair.getItem());
        // System.out.println("rtn is " + rtn);
        if(rtn != -1){
            ItemPair ip = ips.get(rtn);
            Bag b = sol.getBagByItemPair(ip);
            if(b == null){
                return true;
            }
            if(b.getId() == pair.getBag().getId()){
                return false;
            }
            return true;
        }
        return true;
    }
    public boolean satisfy(ItemPair ip, int fbId, int sbId){
        if(fbId == sbId){
            return false;
        }
        return true;
    }
    // Check whether constraint contains a given item.
    private int containsItem(Item item){
        for(int i = 0; i < ips.size(); i ++){
            if(ips.get(i).containsItem(item)){
                return i;
            }
        }
        return -1;
    }
}
