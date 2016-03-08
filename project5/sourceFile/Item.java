import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * This class is used to represent Item (variable).
 */
public class Item implements Comparable<Item>{
    private String name;
    private int id;
    private int weight;
    // Constraint count.
    private int consCnt;
    // Domain is represented by bag Id.
    private ArrayList<Integer> domain;

    public Item(String name, int id, int weight){
        this.name = name;
        this.id = id;
        this.weight = weight;
        this.consCnt = 0;
        domain = new ArrayList<Integer>();
    }
    // Copy constructor for item.
    public Item(Item item){
        this.name = item.getName();
        this.id = item.getId();
        this.weight = item.getWeight();
        this.consCnt = item.getConsCnt();
        this.domain = new ArrayList<Integer>();
        for(int i = 0; i < item.getDomain().size(); i ++){
            this.domain.add(item.getDomain().get(i));
        }
    }

    public String getName(){
        return this.name;
    }
    public int getId(){
        return this.id;
    }
    public int getWeight(){
        return this.weight;
    }
    public int getConsCnt(){
        return this.consCnt;
    }
    // Increase constraint count.
    public void increaseConsCnt(){
        this.consCnt ++;
        return;
    }
    // Get domain from matrices.
    public ArrayList<Integer> updateAndGetDomain(Matrices matrices){
        setDomain(matrices);
        return this.domain;
    }
    public ArrayList<Integer> getDomain(){
        return this.domain;
    }
    // Set domain from matrices.
    public void setDomain(Matrices matrices){
        this.domain = matrices.getDomain(this);
        return;
    }
    // Sort the domain according to Least Constraining Value.
    public ArrayList<Integer> sortDomain(Matrices matrices){
        this.setDomain(matrices);
        ArrayList<Integer> rtnList = new ArrayList<Integer>();
        // Get the total available count list.
        ArrayList<Integer> tacList = new ArrayList<Integer>();
        for(int i = 0; i < domain.size(); i ++){
            int tac = matrices.getTotalAvalCnt(this, domain.get(i));
            tacList.add(tac);
        }
        // System.out.println(tacList);
        ArrayList<Wrapper> wList = new ArrayList<Wrapper>();
        for(int i = 0; i < domain.size(); i ++){
            Wrapper w = new Wrapper(domain.get(i), tacList.get(i));
            wList.add(w);
        }
        Collections.sort(wList);
        for(int i = 0; i < wList.size(); i ++){
            rtnList.add(wList.get(i).getA());
        }
        Collections.reverse(rtnList);
        return rtnList;
    }
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Item){
            if(((Item)obj).getId() == this.id){
                return true;
            }
            return false;
        }
        return false;
    }
    @Override
    public String toString(){
        String rtn = "";
        rtn += "item name is: " + name + " item id is: " + id + " item weight is: " + weight + " item constraint count is: " + consCnt + " remaining value is: " + this.domain.size() + "\n";
        return rtn;
    }
    @Override
    public int compareTo(Item item){
        if(this.domain.size() > item.getDomain().size()){
            return 1;
        }else if(this.domain.size() < item.getDomain().size()){
            return -1;
        }else{
            return 0;
        }
    }
}
