import java.util.ArrayList;

/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * This class represetns bags (values).
 */
public class Bags {
    private ArrayList<Bag> bags;

    public Bags(){
        bags = new ArrayList<Bag>();
    }
    // Copy constructor for bags.
    public Bags(Bags bags){
        this.bags = new ArrayList<Bag>();
        for(int i = 0; i < bags.getSize(); i ++){
            this.bags.add(new Bag(bags.getBags().get(i)));
        }
    }
    // Add a bag to bag list.
    public void addBag(Bag bag){
        this.bags.add(bag);
    }
    // Get a bag by given id.
    public Bag getBagById(int id){
        return bags.get(id);
    }
    // Get a bag by given name.
    public Bag getBagByName(String name){
        for(int i = 0; i < bags.size(); i ++){
            if(bags.get(i).getName().equals(name)){
                return bags.get(i);
            }
        }
        return null;
    }
    // Get the ids of bags.
    public ArrayList<Integer> getBagsIds(){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for(int i = 0; i < this.bags.size(); i ++){
            ids.add(this.bags.get(i).getId());
        }
        return ids;
    }
    // Get bags by given list of names.
    public ArrayList<Bag> getBagsByNames(ArrayList<String> names){
        ArrayList<Bag> rtnBags = new ArrayList<Bag>();
        for(int i = 0; i < names.size(); i ++){
            for(int j = 0; j < bags.size(); j ++){
                if(bags.get(j).getName().equals(names.get(i))){
                    rtnBags.add(bags.get(j));
                }
            }
        }
        return rtnBags;
    }
    // Get all the bags.
    public ArrayList<Bag> getBags(){
        return this.bags;
    }
    // Update a bag by given bag id and given item.
    // By doing this, the item is placed in the bag.
    public void updateBag(int bagId, Item item){
        this.getBagById(bagId).addItem(item);
        return;
    }
    public int getSize(){
        return bags.size();
    }
    @Override
    public String toString(){
        return bags.toString();
    }
}
