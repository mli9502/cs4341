import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yihong on 2/28/2016.
 */
public class Node implements Cloneable {
    private int numOfParents;
    private String name;
    private String[] parents;
    private float[] probabilities;
    private Node[] parentNodes;
    private float value;
    private int status;
    private int restriction;

    Node(String name,int numOfParents, float[] probabilities,String[] parents){
        this.name = name;
        this.numOfParents = numOfParents;
        this.probabilities = probabilities;
        this.parents = parents;
        this.value = -1;
        this.restriction = -1;
        this.status = 999;
    }

    Node(Node node){
        this.numOfParents = node.getNumOfParents();
        this.name = node.getName();
        this.probabilities = new float[node.getPossibilities().length];
        if(node.hasParent()){
            this.parents = new String[node.getParent().length];
            for(int i = 0; i<node.getParent().length; i++ ){
                this.parents[i] = node.getParent()[i];
            }
        }
        for(int i = 0; i<node.getPossibilities().length; i++ ){
            this.probabilities[i] = node.getPossibilities()[i];
        }
        this.value = node.getValue();
        this.status = node.getStatus();
        this.restriction = node.getRestriction();

    }

    void addParents(Map<String,Node> hm){
        if(numOfParents == 1){
            parentNodes = new Node[1];
            parentNodes[0] = hm.get(parents[0]);
        }
        if(numOfParents == 2){
            parentNodes = new Node[2];
            parentNodes[0] = hm.get(parents[0]);
            parentNodes[1] = hm.get(parents[1]);
        }
    }

    boolean hasParent(){
        if(numOfParents == 0){
            return false;
        }
        else{
            return true;
        }
    }

    //return null if no parents
    Node[] getParentNode(){
        if(numOfParents == 0){
            return null;
        }
        else{
            return parentNodes;
        }
    }

    String[] getParent(){
        if(numOfParents == 0){
            return null;
        }
        else{
            return parents;
        }
    }

    int getNumOfParents(){
        return this.numOfParents;
    }

    String getName(){
        return this.name;
    }


    float[] getPossibilities(){
        return this.probabilities;
    }


    void setValue(float value){
        this.value = value;
    }

    float getValue(){
        return this.value;
    }

    void setStatus(int status){
        this.status = status;
    }

    int getStatus(){
        return this.status;
    }

    //t return 1, f return 0, - return -1, q return 2
    int getRestriction(){
        return this.restriction;
    }

    void setRestriction(int restriction){
        this.restriction = restriction;
    }

    public Node clone() throws CloneNotSupportedException {
        try{
            return (Node) super.clone();
        }
        catch(CloneNotSupportedException e){
            System.out.println("Node clone fail!");
            return null;
        }
    }
}
