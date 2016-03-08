/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * The class represents an Item - Bag pair.
 */
public class Pair {
    private Item item;
    private Bag bag;

    public Pair(Item item, Bag bag){
        this.item = item;
        this.bag = bag;
    }

    public Item getItem(){
        return this.item;
    }
    public Bag getBag(){
        return this.bag;
    }
    // Check whether the pair contains an item or not.
    public boolean containsItem(Item i){
        if(this.item.equals(i)){
            return true;
        }
        return false;
    }
    // Method used to check whether two pairs are equal.
    @Override
    public boolean equals(Object o){
        if(o instanceof Pair){
            Pair p = (Pair)o;
            if(this.item.equals(p.getItem()) &&
                    this.bag.equals(p.getBag())){
                return true;
            }
            return false;
        }
        return false;
    }
    @Override
    public String toString(){
        return this.item.toString() + this.bag.toString();
    }
}
