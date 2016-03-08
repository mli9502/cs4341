import java.util.Random;

/**
 * Created by Yihong on 2/29/2016.
 */
public class LikelyhoodWeighting {
    private float value;// its actually weight here
    private static float weight = 1;
    private int numOfSampleUsed = 0;
    LikelyhoodWeighting(){};

    void likelyhoodWeighting(Node node){
        //don't have value
        if(node.getValue() == -1.0){
            if(node.hasParent()){
                //if only has one parent
                if(node.getNumOfParents() == 1){
                    //parent don't have value
                    if(node.getParentNode()[0].getValue() == -1.0) {
                        //produce parent values
                        likelyhoodWeighting(node.getParentNode()[0]); ////////////////////////////
                    }
                    //check restirction then decide if assign random value and its status
                    //if node is evidence and it is asking to be false
                    if(node.getRestriction() == 0){
                        //parent is false
                        if(node.getParentNode()[0].getStatus() == 0){
                            value = node.getPossibilities()[0];
                            node.setValue(value);
                            node.setStatus(0);
                            weight *= value;
                        }
                        //parent is true
                        else{
                            value = node.getPossibilities()[2];
                            node.setValue(value);
                            node.setStatus(0);
                            weight *= value;
                        }
                    }
                    //if node is evidence and it is asking to be true
                    else if(node.getRestriction() == 1){
                        //parent is false
                        if(node.getParentNode()[0].getStatus() == 0){
                            value = node.getPossibilities()[1];
                            node.setValue(value);
                            node.setStatus(1);
                            weight *= value;
                        }
                        //parent is true
                        else{
                            value = node.getPossibilities()[3];
                            node.setValue(value);
                            node.setStatus(1);
                            weight *= value;
                        }
                    }
                    //if node is not evidence,
                    else{
                        value = new Random().nextFloat();
                        numOfSampleUsed ++;
                        node.setValue(value);
                        //base on parent status assgin node's status
                        //parent is false
                        //set status, check restirction
                        if(node.getParentNode()[0].getStatus() == 0){
                            //node is false
                            if(value < node.getPossibilities()[0]){
                                node.setStatus(0);
                            }
                            else{
                                node.setStatus(1);
                            }
                        }
                        //parent is true
                        else{
                            //node is false
                            if(value < node.getPossibilities()[2]){
                                node.setStatus(0);
                            }
                            else{
                                node.setStatus(1);
                            }
                        }

                    }
                }
                //if it has two parents
                if(node.getNumOfParents() == 2){
                    //if any of the parent don't have value
                    //if parent 1 don't have value
                    if(node.getParentNode()[0].getValue() == -1.0) {
                        likelyhoodWeighting(node.getParentNode()[0]);
                    }
                    //if parent2 don't have value
                    if(node.getParentNode()[1].getValue() == -1.0) {
                        likelyhoodWeighting(node.getParentNode()[1]);
                    }
                    //both parent have value now
                    //check restriction, node is going to be false
                    if(node.getRestriction() == 0){
                        if(node.getParentNode()[0].getStatus() == 0 && node.getParentNode()[1].getStatus() == 0){
                            value = node.getPossibilities()[0];
                            node.setValue(value);
                            node.setStatus(0);
                            weight *= value;
                        }
                        if(node.getParentNode()[0].getStatus() == 0 && node.getParentNode()[1].getStatus() == 1){
                            value = node.getPossibilities()[2];
                            node.setValue(value);
                            node.setStatus(0);
                            weight *= value;
                        }
                        if(node.getParentNode()[0].getStatus() == 1 && node.getParentNode()[1].getStatus() == 0){
                            value = node.getPossibilities()[4];
                            node.setValue(value);
                            node.setStatus(0);
                            weight *= value;
                        }
                        //both parent are true
                        else{
                            value = node.getPossibilities()[6];
                            node.setValue(value);
                            node.setStatus(0);
                            weight *= value;
                        }
                    }
                    //node is going to be true
                    else if(node.getRestriction() == 1){
                        if(node.getParentNode()[0].getStatus() == 0 && node.getParentNode()[1].getStatus() == 0){
                            value = node.getPossibilities()[1];
                            node.setValue(value);
                            node.setStatus(1);
                            weight *= value;
                        }
                        if(node.getParentNode()[0].getStatus() == 0 && node.getParentNode()[1].getStatus() == 1){
                            value = node.getPossibilities()[3];
                            node.setValue(value);
                            node.setStatus(1);
                            weight *= value;
                        }
                        if(node.getParentNode()[0].getStatus() == 1 && node.getParentNode()[1].getStatus() == 0){
                            value = node.getPossibilities()[5];
                            node.setValue(value);
                            node.setStatus(1);
                            weight *= value;
                        }
                        //both parent are true
                        else{
                            value = node.getPossibilities()[7];
                            node.setValue(value);
                            node.setStatus(1);
                            weight *= value;
                        }
                    }
                    //node is not evidence, random assign value
                    else{
                        value = new Random().nextFloat();
                        numOfSampleUsed ++;
                        node.setValue(value);
                        //parent1 is false
                        //set status, check restirction
                        if(node.getParentNode()[0].getStatus() == 0){
                            //parent2 is false
                            if(node.getParentNode()[1].getStatus() == 0){
                                //node is false
                                if(value < node.getPossibilities()[0]){
 //                                   System.out.println("node.getPossibilities()[0]:"+node.getPossibilities()[0]+"  "+node.getName());
                                    node.setStatus(0);
                                }
                                //node is true
                                else{
                                    node.setStatus(1);
                                }
                            }
                            //parent 2 is true
                            else{
                                //node is false
                                if(value < node.getPossibilities()[2]){
                                    node.setStatus(0);
                                }
                                //node is true
                                else{
                                    node.setStatus(1);
                                }
                            }
                        }
                        //parent1 is true
                        else{
                            //parent2 is false
                            if(node.getParentNode()[1].getStatus() == 0){
                                //node is false
                                if(value < node.getPossibilities()[4]){
                                    node.setStatus(0);
                                }
                                //node is true
                                else{
                                    node.setStatus(1);
                                }
                            }
                            //parent 2 is true
                            else{
                                //node is false
                                if(value < node.getPossibilities()[6]){
                                    node.setStatus(0);
                                }
                                //node is true
                                else{
                                    node.setStatus(1);
                                }
                            }
                        }
                    }
                }
            }
            //i am parent!
            else{
                //check if i am evidence
                //the node is asking to be false
                if(node.getRestriction() == 0){
                    value = node.getPossibilities()[0];
                    node.setValue(value);
                    node.setStatus(0);
                    weight *= value;
                }
                // node is asking to be true
                else if(node.getRestriction() == 1){
                    value = node.getPossibilities()[1];
                    node.setValue(value);
                    node.setStatus(1);
                    weight *= value;
                }
                //node is not evidence
                else{
                    //set value,set state,look at restirction and  return
                    value = new Random().nextFloat();
                    numOfSampleUsed ++;
                    node.setValue(value);
                    //status false
                    if(value < node.getPossibilities()[0]){
                        node.setStatus(0);
                    }
                    //status true
                    else{
                        node.setStatus(1);
                    }
                }
            }
        }
    }


    float getWeight(){
        return this.weight;
    }
    void setWeight(float weight){
        this.weight = weight;
    }
    int getNumOfSampleUsed(){
        return this.numOfSampleUsed;
    }
}
