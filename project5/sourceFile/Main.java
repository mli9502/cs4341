import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * Main class.
 * Read in the file and initialize items, bags and constraints here.
 * After initialization, start search.
 * After the search is finished, print out the solution if any.
 * If solution is not found, "No solution found!" is printed out.
 */
public class Main {
    public static void main(String[] args){
        MaxFittingLimitConstraint maxflc = null;
        MinFittingLimitConstraint minflc = null;
        MinWeightLimitConstraint mwlc = new MinWeightLimitConstraint();
        WeightLimitConstraint wlc = new WeightLimitConstraint();
        BinaryEqualConstraint bec = null;
        BinaryNotEqualConstraint bnec = null;
        MutualInclusiveConstraint mic = null;

        Matrices matrices = new Matrices();
        ArrayList<String> fileLines = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            String line = br.readLine();
            while(line != null){
                fileLines.add(line);
                line = br.readLine();
            }
            br.close();
        }catch(IOException e){
            System.out.println("IO exception.");
            e.printStackTrace();
        }
        // Process file lines.
        int sectionCnt = 0;
        // ArrayList<Item> items = new ArrayList<Item>();
        // ArrayList<Bag> bags = new ArrayList<Bag>();
        Items items = new Items();
        Bags bags = new Bags();

        for(int i = 0; i < fileLines.size(); i ++){
            if(fileLines.get(i).contains("#####")){
                sectionCnt ++;
                if(sectionCnt == 3){
                    matrices = new Matrices(items.getSize());
                    matrices.initialize(items.getItems(), bags.getSize());
                }
                continue;
            }
            // Items.
            if(sectionCnt == 1){
                String[] tokens = fileLines.get(i).split(" ");
                Item item = new Item(tokens[0], items.getSize(), Integer.parseInt(tokens[1]));
                items.addItem(item);
                // System.out.println(items);
            }else if(sectionCnt == 2) {  // Values.
                String[] tokens = fileLines.get(i).split(" ");
                Bag bag = new Bag(tokens[0], bags.getSize(), Integer.parseInt(tokens[1]));
                bags.addBag(bag);
            }else if(sectionCnt == 3){  // Fitting limit.
                // Set the fitting limit.
                String[] tokens = fileLines.get(i).split(" ");
                // Construct min and max fitting limit constraint.
                minflc = new MinFittingLimitConstraint(Integer.parseInt(tokens[0]));
                maxflc = new MaxFittingLimitConstraint(Integer.parseInt(tokens[1]));
            }else if(sectionCnt == 4){  // Unary inclusive.
                String[] tokens = fileLines.get(i).split(" ");
                ArrayList<String> tkList = new ArrayList<String>(Arrays.asList(tokens));
                String itemName = tkList.get(0);
                // Increase constraint count.
                items.getItemByName(itemName).increaseConsCnt();
                tkList.remove(0);
                matrices.setMatrixInclusive(items.getItemByName(itemName), bags.getBagsByNames(tkList));
            }else if(sectionCnt == 5){
                String[] tokens = fileLines.get(i).split(" ");
                ArrayList<String> tkList = new ArrayList<String>(Arrays.asList(tokens));
                String itemName = tkList.get(0);
                items.getItemByName(itemName).increaseConsCnt();
                tkList.remove(0);
                matrices.setMatrixExclusive(items.getItemByName(itemName), bags.getBagsByNames(tkList));
            }else if(sectionCnt == 6){
                String[] tokens = fileLines.get(i).split(" ");
                matrices.setMatrixBinaryEqual(items.getItemByName(tokens[0]), items.getItemByName(tokens[1]));
                if(bec == null){
                    bec = new BinaryEqualConstraint();
                }
                bec.addItemPair(items.getItemByName(tokens[0]), items.getItemByName(tokens[1]));
                items.getItemByName(tokens[0]).increaseConsCnt();
                items.getItemByName(tokens[1]).increaseConsCnt();
            }else if(sectionCnt == 7){
                String[] tokens = fileLines.get(i).split(" ");
                matrices.setMatrixBinaryNotEqual(items.getItemByName(tokens[0]), items.getItemByName(tokens[1]));
                if(bnec == null){
                    bnec = new BinaryNotEqualConstraint();
                }
                bnec.addItemPair(items.getItemByName(tokens[0]), items.getItemByName(tokens[1]));
                items.getItemByName(tokens[0]).increaseConsCnt();
                items.getItemByName(tokens[1]).increaseConsCnt();
            }else{
                String[] tokens = fileLines.get(i).split(" ");
                if(mic == null){
                    mic = new MutualInclusiveConstraint();
                }
                mic.addConstraint(items.getItemByName(tokens[0]), items.getItemByName(tokens[1]), tokens[2], tokens[3]);
                items.getItemByName(tokens[0]).increaseConsCnt();
                items.getItemByName(tokens[1]).increaseConsCnt();
            }
        }
        // Back tracking algorithm.
        BackTrack bt = new BackTrack(matrices, items, bags, maxflc, minflc, mwlc, wlc, bec, bnec, mic);
        Solution s = bt.backtrackingSearch();
        if(s.isEmpty()){
            System.out.println("No solution found!");
        }else{
            s.printSolution(bags, items);
        }
    }
}
