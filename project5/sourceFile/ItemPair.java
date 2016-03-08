/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * This class is used to store a pair of item.
 */
public class ItemPair {
    private Item aItem;
    private Item bItem;

    public ItemPair(Item a, Item b){
        this.aItem = a;
        this.bItem = b;
    }
    public boolean containsItem(Item item){
        if(aItem.equals(item) || bItem.equals(item)){
            return true;
        }
        return false;
    }
    public Item getFirst(){
        return aItem;
    }
    public Item getSecond(){
        return bItem;
    }
}
