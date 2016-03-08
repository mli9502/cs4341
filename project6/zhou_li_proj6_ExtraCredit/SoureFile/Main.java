import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yihong on 2/28/2016.
 */
public class Main {
    static String[] files = new String[2];
    static int input = 0;
    static String sCurrentLine;
    static ArrayList<String> networksLines = new ArrayList<String>();
    static String query = "";
    static int numOfParents;
    static int numOfProbability;
    static Map<String,Node> map = new HashMap<String,Node>();
    static ArrayList<String> trueSet = new ArrayList<String>();
    static ArrayList<String> falseSet = new ArrayList<String>();
    static String asking = "";

    public static void main(String[] args){
        //reading inputs
        if(args.length != 3){
            System.out.println("Parameters num is not correct!"+ args.length);
            return;
        }
        for(int i = 0;  i < args.length; i++ ){
            if(i == 0){
                files[0] = args[i];
            }
            if (i == 1){
                files[1] = args[i];
            }
            if(i == 2){
                input = Integer.parseInt(args[i]);
            }
        }

        //reading files
        readingFile(files);

        //initialize a node and add info
        nodeCreate(files);

        //add parent to each node
        for(Map.Entry<String, Node> nodeEntry : map.entrySet()){
            nodeEntry.getValue().addParents(map);
        }

        //run rejectSampling
        float resultR = rejectSample(input,map,asking);
        System.out.println("rejectSampling:"+resultR);


        //run likelyhood weighting
        float resultL = likelyWeight(input,map,trueSet,asking);
        System.out.println("likelyWeight:"+resultL);

        //run convergence test
        float resultC1 = convergenceTest1(input);
        System.out.println("convergenceTest rejectSampling:"+resultC1);

        float resultC2 = convergenceTest2(input);
        System.out.println("convergenceTest likelyWeight:"+resultC2);

    }

    public static float convergenceTest1(int input){
        int testNum = input;
        float result = 0;
        float mean = 0;
        float variance = 2;
        int count = 0;
        float[] values;
        float sumOfvariance = 0;

        while(variance > 0.001){
            count = 0;
            mean = 0;
            sumOfvariance = 0;
            values = new float[testNum];
            while(count < testNum){
                result = rejectSample(testNum,map,asking);
                values[count] = result;
                mean += result;
                count ++;
            }
            mean = mean/(float)count;

            for(int i = 0; i<values.length; i++){
                sumOfvariance += (values[i] - mean)*(values[i] - mean);
            }
            variance = (float)((float)sumOfvariance/(float)count);
            testNum += 3000;
 //           System.out.println("variance:"+variance);
 //          System.out.println("testNum:"+testNum);
        }
        return mean;
    }
    public static float convergenceTest2(int input){
        int testNum = input;
        float result = 0;
        float mean = 0;
        float variance = 2;
        int count = 0;
        float[] values;
        float sumOfvariance = 0;

        while(variance > 0.0073){
            count = 0;
            mean = 0;
            sumOfvariance = 0;
            values = new float[testNum];
            while(count < testNum){
                result = likelyWeight(input, map, trueSet, asking);
                values[count] = result;
                mean += result;
                count ++;
            }
            mean = mean/(float)count;

            for(int i = 0; i<values.length; i++){
                sumOfvariance += (values[i] - mean)*(values[i] - mean);
            }
            variance = (float)((float)sumOfvariance/(float)count);
            testNum += 3000;
 //           System.out.println("variance:"+variance);
 //           System.out.println("testNum:"+testNum);
        }
        return mean;
    }



    public static float likelyWeight(int input,Map<String, Node> map, ArrayList<String> trueSet,String asking){
        float valid = 0; //both query and evidence should be right
        float evidenceTrue = 0; //only evidence right
        float weight;
        boolean evidenceValid = true;
        int numOfSample = input;


        while(numOfSample > 0) {
            //copy a hashmap
            Map<String, Node> copyMap;
            copyMap = constructCopyMap(map);

            //new a likelyhood weighting
            LikelyhoodWeighting lw = new LikelyhoodWeighting();
            for(Map.Entry<String, Node> nodeEntry : copyMap.entrySet()){
                lw.likelyhoodWeighting(nodeEntry.getValue());
            }
            weight = lw.getWeight();
            for(String name: trueSet){
                //check if any evidence not valid
                if(copyMap.get(name).getStatus() != 1) {
                    evidenceValid = false;
                }
            }
            if(evidenceValid && (( numOfSample - lw.getNumOfSampleUsed()) >= 0) ){
                if(copyMap.get(asking).getStatus() == 1){
                    valid += weight;
                }
                evidenceTrue += weight;
            }
            evidenceValid = true;
            lw.setWeight(1);

            numOfSample -= lw.getNumOfSampleUsed();
        }
        return (float)valid/(float)evidenceTrue;
    }


    public static float rejectSample(int input,Map<String, Node> map, String asking){
        int rejectNum = 0;
        int validNum = 0;
        boolean rejectFlag;
        int numOfSample = input;
        int totalModelCount = 0;

        while(numOfSample > 0) {
            //copy a hashmap
            Map<String, Node> copyMap;
            copyMap = constructCopyMap(map);

            //new a rejection
            RejectSampling rs = new RejectSampling();
            rejectFlag = false;

            for(Map.Entry<String, Node> nodeEntry : copyMap.entrySet()){
                //node is assigned "t" or "f" or "q"
               if(nodeEntry.getValue().getRestriction() != -1){
                   //have reject
                    if(rs.rejectSampling(nodeEntry.getValue()) && ((numOfSample - rs.getNumofSampleUsed()) >= 0) ){
                        rejectNum ++;
                        rejectFlag = true;
                        break;
                    }
                   else{
                        if((numOfSample - rs.getNumofSampleUsed()) < 0){
                            break;
                        }
                    }
                }
            }
            if(!rejectFlag){
                //check q is valid
                if(copyMap.get(asking).getStatus() == 1){
                    validNum ++;
                }
            }
            rejectFlag = false;

            numOfSample -= rs.getNumofSampleUsed();
            totalModelCount++;
        }
        return (float)validNum/((float)totalModelCount-(float)rejectNum);
    }


    public static Map constructCopyMap(Map<String, Node> map){
        Map<String, Node> copyMap = new HashMap<String, Node>();
        for (Map.Entry<String, Node> nodeEntry : map.entrySet()) {
            Node tmpNode = new Node(nodeEntry.getValue());
            copyMap.put(nodeEntry.getKey(), tmpNode);
        }
        for(Map.Entry<String, Node> nodeEntry : copyMap.entrySet()){
            nodeEntry.getValue().addParents(copyMap);
        }
        return copyMap;
    }


    public static void readingFile(String[] files){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(files[0]));
            while ((sCurrentLine = br.readLine()) != null) {
                networksLines.add(sCurrentLine);
            }
            br = new BufferedReader(new FileReader(files[1]));
            while ((sCurrentLine = br.readLine()) != null) {
                query = sCurrentLine;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (br != null) {
                    br.close();
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void nodeCreate(String[] files){
        //parse query
        String[] expect = query.split(",");
        //parse network file
        for(int i = 0; i < networksLines.size(); i++){
            //get node name
            String[] token = networksLines.get(i).split(":");
            String name = token[0];

            //get parents
            String[] info = token[1].split("(\\[)|(\\])");
            numOfParents = 999;
            if(info[1].equals("")){
                numOfParents = 0;
            }
            String[] parents = info[1].split(" ");
            if(parents.length == 1){
                if(numOfParents == 0){
                    numOfParents = 0;
                }
                else{
                    numOfParents = 1;
                }
            }
            else{
                numOfParents = 2;
            }

            //get probabilities
            String[] prob = info[3].split(" ");
            numOfProbability = prob.length;
            float[] probability = new float[numOfProbability];
            for(int j = 0; j < numOfProbability; j ++) {
                probability[j] = Float.parseFloat(prob[j]);
            }

            //Initialize nodes and change observation
            Node node = new Node(name,numOfParents,probability,parents);
            //get observation
            if(expect[i].equals("t")){
                trueSet.add(node.getName());
                node.setRestriction(1);
            }
            else if(expect[i].equals("f") ){
                falseSet.add(node.getName());
                node.setRestriction(0);
            }
            else if(expect[i].equals("q") ){
                asking = node.getName();
                node.setRestriction(2);
            }
            else{
                node.setRestriction(-1);
            }
            map.put(name,node);

        }

    }

}
