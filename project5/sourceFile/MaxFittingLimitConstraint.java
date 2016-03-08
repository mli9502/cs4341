import java.util.ArrayList;

/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * This class represents the maximum fitting limit constraint.
 * The item count in a bag should not exceed the max item limit.
 */
public class MaxFittingLimitConstraint {
    private int maxItemNum;
    
    public MaxFittingLimitConstraint(int maxItemNum){
        this.maxItemNum = maxItemNum;
    }
    public boolean satisfy(Bag bag){
        if(bag.getItemCnt() > maxItemNum){
            return false;
        }
        return true;
    }
    public boolean satisfy(Bags bags){
        for(int i = 0; i < bags.getBags().size(); i ++){
            if(!this.satisfy(bags.getBags().get(i))){
                return false;
            }
        }
        return true;
    }
    public ArrayList<Bag> getUnsatBags(Item item, Bags bags){
        ArrayList<Bag> rtn = new ArrayList<Bag>();
        for(int i = 0; i < bags.getSize(); i ++){
            if(1 + bags.getBags().get(i).getItemCnt() > this.maxItemNum){
                rtn.add(bags.getBags().get(i));
            }
        }
        return rtn;
    }
}
