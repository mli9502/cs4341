import java.util.ArrayList;

/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * Class for weight limit constraint.
 * Check whether the weight of the items exceeds the weight limit of the bag.
 */
public class WeightLimitConstraint {
    public WeightLimitConstraint(){}
    // Check whether a given bag satisfy the weight limit constraint.
    public boolean satisfy(Bag bag){
        if(bag.getItemWeight() > bag.getLimit()){
            return false;
        }
        return true;
    }
    // Check whether a set of bags all satisfy weight limit constraint.
    public boolean satisfy(Bags bags){
        for(int i = 0; i < bags.getBags().size(); i ++){
            if(!this.satisfy(bags.getBags().get(i))){
                return false;
            }
        }
        return true;
    }
    // Given an item that is about to be added, return a list of bags that the item can't added to.
    public ArrayList<Bag> getUnsatBags(Item item, Bags bags){
        ArrayList<Bag> rtn = new ArrayList<Bag>();
        for(int i = 0; i < bags.getSize(); i ++){
            if(item.getWeight() + bags.getBags().get(i).getItemWeight() > bags.getBags().get(i).getLimit()){
                rtn.add(bags.getBags().get(i));
            }
        }
        return rtn;
    }
}
