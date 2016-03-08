/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 * This class represents minimum weight limit constraint.
 * The item weight in bag should be more than 60% of the bag's limit.
 */
public class MinWeightLimitConstraint {
    public MinWeightLimitConstraint(){}

    public boolean satisfy(Bag bag){
        if(bag.getItemWeight() >= Math.floor((double)bag.getLimit() * 0.9)){
            return true;
        }
        return false;
    }
}
