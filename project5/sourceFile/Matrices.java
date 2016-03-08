import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * This class is used to represent the matrices for all the combination of variables.
 * The matrices are represented in 2-D array.
 */
public class Matrices {
    private Matrix matrices[][];

    public Matrices(){
        this.matrices = null;
    }

    public Matrices(int itemCnt){
        this.matrices = new Matrix[itemCnt][itemCnt];
        for(int i = 0; i < itemCnt; i ++){
            for(int j = 0; j <= i; j ++){
                matrices[i][j] = null;
            }
        }
    }
    // Copy constructor for Matrices.
    public Matrices(Matrices mtcs){
        this.matrices = new Matrix[mtcs.getMatrices().length][mtcs.getMatrices()[0].length];
        for(int i = 0; i < mtcs.getMatrices().length; i ++){
            for(int j = 0; j < mtcs.getMatrices()[0].length; j ++){
                if(i < j){
                    matrices[i][j] = new Matrix(mtcs.getMatrices()[i][j]);
                }else{
                    matrices[i][j] = null;
                }
            }
        }
    }

    public Matrix[][] getMatrices(){
        return this.matrices;
    }
    // Initialize the matrices.
    public void initialize(ArrayList<Item> items, int bagCnt){
        for(int i = 0; i < items.size(); i ++){
            for(int j = i + 1; j < items.size(); j ++){
                this.consMatrix(items.get(i), items.get(j), bagCnt);
            }
        }
    }

    // Get the matrix for the given two items.
    public Matrix getMatrix(Item aItem, Item bItem){
        int a = aItem.getId();
        int b = bItem.getId();
        int row = a > b ? b : a;
        int col = a > b ? a : b;
        return matrices[row][col];
    }
    // Get the matrix for the given row and col.
    public Matrix getMatrix(int a, int b){
        int row = a > b ? b : a;
        int col = a > b ? a : b;
        return matrices[row][col];
    }
    // Construct a single matrix for the given two items.
    public void consMatrix(Item aItem, Item bItem, int bagCnt){
        int a = aItem.getId();
        int b = bItem.getId();
        int row = a > b ? b : a;
        int col = a > b ? a : b;
        this.matrices[row][col] = new Matrix(aItem, bItem, bagCnt);
        return;
    }

    // Get the domain for a given item.
    public ArrayList<Integer> getDomain(Item item){
        ArrayList<Integer> rtn = new ArrayList<Integer>();
        int itemId = item.getId();
        for(int i = 0; i < matrices.length; i ++){
            if(i == itemId){
                continue;
            }
            // System.out.println("In getDomain");
            // System.out.println("i is " + i + " itemId is " + itemId);
            ArrayList<Integer> tmpList = this.getMatrix(i, itemId).getValidBagId(item);
            rtn.addAll(tmpList);
            // Remove duplicate.
            Set tmpSet = new LinkedHashSet<Integer>(rtn);
            rtn.clear();
            rtn.addAll(tmpSet);
        }
        return rtn;
    }
    // Get the total available count for given bag id.
    public int getTotalAvalCnt(Item item, int bagId){
        int itemId = item.getId();
        int rtnCnt = 0;
        for(int i = 0; i < matrices.length; i ++){
            if(i == itemId){
                continue;
            }
            rtnCnt += this.getMatrix(i, itemId).getAvalCnt(item, bagId);
        }
        return rtnCnt;
    }

    // Set the matrix for the given item for unary constraint.
    public void setMatrixExclusive(Item item, ArrayList<Bag> bags){
        int itemId = item.getId();
        for(int i = 0; i < matrices.length; i ++){
            if(i == itemId){
                continue;
            }
            for(int j = 0; j < bags.size(); j ++){
                this.getMatrix(i, itemId).setInvalid(item, bags.get(j).getId());
            }
        }
        return;
    }
    // Set the row or col other than given bags to 0 for the given item.
    public void setMatrixInclusive(Item item, ArrayList<Bag> bags){
        // System.out.println(item);
        // System.out.println(bags);
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for(int i = 0; i < bags.size(); i ++){
            ids.add(bags.get(i).getId());
        }
        int itemId = item.getId();
        for(int i = 0; i < matrices.length; i ++){
            if(i == itemId){
                continue;
            }
            this.getMatrix(i, itemId).setValid(item, ids);
        }
        return;
    }
    // Update matrices according to the input pair (Item and Bag).
    public void updateMatrices(Pair pair){
        ArrayList<Bag> tmpList = new ArrayList<Bag>();
        tmpList.add(pair.getBag());
        this.setMatrixInclusive(pair.getItem(), tmpList);
    }
    // Set spots other than the diagonal of the matrix for given two items to 0.
    public void setMatrixBinaryEqual(Item aItem, Item bItem){
        int aId = aItem.getId();
        int bId = bItem.getId();
        this.getMatrix(aId, bId).setNotDiagnolNotValid();
        return;
    }
    // Set the dianonal of the matrix for given two items to 0.
    public void setMatrixBinaryNotEqual(Item aItem, Item bItem){
        int aId = aItem.getId();
        int bId = bItem.getId();
        this.getMatrix(aId, bId).setDiagnolNotValid();
        return;
    }

    @Override
    public String toString(){
        String rtn = "";
        for(int i = 0; i < matrices.length; i ++){
            for(int j = 0; j < matrices[0].length; j ++){
                rtn += "row is " + i + " col is " + j + "\n";
                if(matrices[i][j] == null) {
                    rtn += "null\n";
                }else{
                    rtn += matrices[i][j].toString();
                }
            }
        }
        return rtn;
    }
}
