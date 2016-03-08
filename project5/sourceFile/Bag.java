/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * This class represetns Bag (value).
 */
public class Bag{
    private String name;
    private int id;
    private int weightLimit;
    // Item count. Records how many items are in the bag.
    private int itemCnt;
    // Item weight. Records the total item weight that is placed in the bag.
    private int itemWeight;

    public Bag(String name, int id, int weightLimit){
        this.id = id;
        this.name = name;
        this.weightLimit = weightLimit;
        this.itemCnt = 0;
        this.itemWeight = 0;
    }
    // Copy constructor for bag.
    public Bag(Bag bag){
        this.name = bag.getName();
        this.id = bag.getId();
        this.weightLimit = bag.getLimit();
        this.itemCnt = bag.getItemCnt();
        this.itemWeight = bag.getItemWeight();
    }
    // Add an item to bag.
    public void addItem(Item item){
        this.itemCnt ++;
        this.itemWeight += item.getWeight();
        return;
    }
    // Check whether two bags are equal.
    public boolean equals(Bag b){
        if(this.id == b.getId()){
            return true;
        }else{
            return false;
        }
    }
    public String getName(){
        return this.name;
    }
    public int getId(){
        return this.id;
    }
    public int getLimit(){
        return this.weightLimit;
    }
    public int getItemCnt(){
        return this.itemCnt;
    }
    public int getItemWeight(){
        return this.itemWeight;
    }
    @Override
    public String toString(){
        String rtn = "";
        rtn += "bag name is: " + name + " bag id is: " + id + " bag weightlimit is: " + weightLimit + " bag item count is: " + itemCnt + " bag item weight is: " + itemWeight + "\n";
        return rtn;
    }
}
