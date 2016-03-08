/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 * This class represents minimum fitting limit constraint.
 * The item count of a bag should be greater or equal to the minimum item count.
 */
public class MinFittingLimitConstraint {
    private int minItemCnt;

    public MinFittingLimitConstraint(int minItemCnt){
        this.minItemCnt = minItemCnt;
    }
    public boolean satisfy(Bag bag){
        // System.out.println("In mflc satisfy method");
        if(bag.getItemCnt() < minItemCnt){
            return false;
        }
        return true;
    }
}
