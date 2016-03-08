import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 * wen on 2/6/2016.
 * Read in the files from .csv file, add attributes and store to another .csv file.
 */
public class Main {
    public static void main(String[] args){
        if(args.length != 2) {
            System.out.println("Usage: java -jar project3_featureSelect.jar <input file name> <output file name>");
            return;
        }
        String fileIn = args[0];
        String fileOut = args[1];
        // Check input, output file validability.
        for(int i = 0; i < args.length; i ++){
            System.out.println(args[i]);
            String[] tmp = args[i].split("\\.");
            System.out.println(tmp[0]);
            if(!tmp[tmp.length - 1].equals("csv")){
                System.out.println("Input, output file needs to end with .csv");
                return;
            }
        }
        ArrayList<ArrayList<Integer>> fileEntry = new ArrayList<ArrayList<Integer>>();
        String attributes = "";
        try {
            Scanner scanner = new Scanner(new File(fileIn));
            boolean firstLine = true;
            while(scanner.hasNextLine()){
                if(firstLine){
                    attributes = scanner.nextLine();
                    firstLine = false;
                }else{
                    String[] locs = scanner.nextLine().split(",");
                    ArrayList<Integer> intLocs = new ArrayList<Integer>();
                    for(int i = 0; i < locs.length; i ++){
                        intLocs.add(Integer.parseInt(locs[i]));
                    }
                    fileEntry.add(intLocs);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Can't open file!");
            e.printStackTrace();
        }
        System.out.println("Number of entries are: " + fileEntry.size());
        // Feature 1: Which player has a piece at the bottom left corner.
        attributes = addFeature(attributes, fileEntry, new PieceLeftCorner());
        // Feature 2: Which player has more pieces in the center region.
        attributes = addFeature(attributes, fileEntry, new CenterControl());
        // Feature 3: Which player has more conitnuous pieces (live 2-piece and live 3-piece) vertically.
        attributes = addFeature(attributes, fileEntry, new VerticalTwo());
        // Feature 4: Which player has more continuous pieces (live 2-piece and live 3-piece) horizontally.
        attributes = addFeature(attributes, fileEntry, new HorizontalTwo());
        // Feature 5: Which player has more continuous pieces in both vertical and horizontal lines.
        attributes = addFeature(attributes, fileEntry, new Combine());
        // Feature 6: Which player can connect more live 2-pieces vertically.
        attributes = addFeature(attributes, fileEntry, new VerticalOne());
        // Feature 7: Which player can connect more live 2-pieces horizontally.
        attributes = addFeature(attributes, fileEntry, new HorizontalOne());

        // Write to output file.
        Writer writer = null;
        try{
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOut), "utf-8"));
            writer.write(attributes);
            writer.write("\n");
            writer.write(boardListToStr(fileEntry));
        }catch(IOException e){
            System.out.println("Can't open output file!");
            e.printStackTrace();
        }finally{
            try{
                writer.close();
            }catch(Exception e){
                System.out.println("Can't close output file!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Function used to add features.
     * @param attrs
     * @param boards
     * @param feature
     * @return
     */
    public static String addFeature(String attrs, ArrayList<ArrayList<Integer>> boards, Feature feature){
        // Update attributes.
        attrs = attrs + "," + feature.getFeatureName();
        // Update column data.
        for(int i = 0; i < boards.size(); i ++){
            System.out.println(i);
            boards.get(i).add(feature.getFeature(boards.get(i)));
        }
        return attrs;
    }

    /**
     * Function used to convert an integer arraylist to string.
     * @param intList
     * @return
     */
    public static String intListToStr(ArrayList<Integer> intList){
        String rtnStr = "";
        for(int i = 0; i < intList.size(); i ++){
            rtnStr += intList.get(i);
            if(i != intList.size() - 1){
                rtnStr += ",";
            }
        }
        return rtnStr;
    }

    /**
     * Function used to convert a board to string.
     * @param boardList
     * @return
     */
    public static String boardListToStr(ArrayList<ArrayList<Integer>> boardList){
        String rtnStr = "";
        for(int i = 0; i < boardList.size(); i ++){
            rtnStr += intListToStr(boardList.get(i));
            rtnStr += "\n";
        }
        return rtnStr;
    }
}
