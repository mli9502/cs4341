import java.util.Random;

/**
 * Created by Yihong on 2/28/2016.
 */
public class RejectSampling {
    private float value;
    private int numofSampleUsed = 0;
    RejectSampling(){}

    //return true if node reject, false if not reject
    boolean checkRestriction(Node node){
        if(node.getRestriction() != 2 && node.getRestriction() != -1){
            if(node.getStatus() != node.getRestriction()){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

    //return if it needs to be rejected
    boolean rejectSampling(Node node){
        //don't have value
        if(node.getValue() == -1.0){
            if(node.hasParent()){
                //if only has one parent
                if(node.getNumOfParents() == 1){
                    //parent don't have value
                    if(node.getParentNode()[0].getValue() == -1.0) {
                        //rejected
                        if (rejectSampling(node.getParentNode()[0])) {
                            return true;
                        }
                    }
                    //parent have value now
                    //parent not rejected
                    value = new Random().nextFloat();
                    numofSampleUsed++;
                    node.setValue(value);
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
                        return checkRestriction(node);
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
                        return checkRestriction(node);
                    }
                }
                //if it has two parents
                if(node.getNumOfParents() == 2){
                    //if any of the parent don't have value
                    //if parent 1 don't have value
                    if(node.getParentNode()[0].getValue() == -1.0) {
//                        System.out.println("kakkak"+node.getName());
                        //rejected
                        if (rejectSampling(node.getParentNode()[0])) {
                            return true;
                        }
                    }
                    //if parent2 don't have value
                    if(node.getParentNode()[1].getValue() == -1.0) {
//                        System.out.println("hahhaha"+node.getName());
                        //rejected
                        if (rejectSampling(node.getParentNode()[1])) {
                            return true;
                        }
                    }
                    //both parent have value now
                    //And both parent not rejected
                    value = new Random().nextFloat();
                    numofSampleUsed ++;
                    node.setValue(value);
                    //parent1 is false
                    //set status, check restirction
                    if(node.getParentNode()[0].getStatus() == 0){
                        //parent2 is false
                        if(node.getParentNode()[1].getStatus() == 0){
                            //node is false
                            if(value < node.getPossibilities()[0]){
//                                System.out.println("node.getPossibilities()[0]:"+node.getPossibilities()[0]+"  "+node.getName());
                                node.setStatus(0);
                            }
                            //node is true
                            else{
                                node.setStatus(1);
                            }
                            return checkRestriction(node);
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
                            return checkRestriction(node);
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
                            return checkRestriction(node);
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
                            return checkRestriction(node);
                        }
                    }
                }
            }
            //i am parent!
            else{
                //set value,set state,look at restirction and  return
                value = new Random().nextFloat();
                numofSampleUsed ++;
                node.setValue(value);
                //status false
                if(value < node.getPossibilities()[0]){
                    node.setStatus(0);
                }
                //status true
                else{
                    node.setStatus(1);
                }
                //check if can be rejected
                //if node is not '-' or 'q'
                return checkRestriction(node);
            }
        }
        //node has a value
        return checkRestriction(node);
    }

    int getNumofSampleUsed(){
        return this.numofSampleUsed;
    }
}
