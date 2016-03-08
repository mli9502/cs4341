import java.util.ArrayList;

/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * Constraint matrix.
 * The row and col count of the matrix equals to the number of bags since bags are values.
 * rowItem and colItem represents the two variables this constraint matrix represents.
 */
public class Matrix {
    Item rowItem;
    Item colItem;
    int[][] matrix;

    public Matrix(Item rowItem, Item colItem, int bagCnt){
        this.rowItem = rowItem;
        this.colItem = colItem;
        matrix = new int[bagCnt][bagCnt];
        // Initialize matrix.
        for(int i = 0; i < bagCnt; i ++){
            for(int j = 0; j < bagCnt; j ++){
                matrix[i][j] = 1;
            }
        }
    }
    // Copy constructor for Matrix.
    public Matrix(Matrix mtx){
        this.rowItem = new Item(mtx.getRowItem());
        this.colItem = new Item(mtx.getColItem());
        this.matrix = new int[mtx.getMatrix().length][mtx.getMatrix()[0].length];
        for(int i = 0; i < mtx.getMatrix().length; i ++){
            for(int j = 0; j < mtx.getMatrix()[0].length; j ++){
                this.matrix[i][j] = mtx.getMatrix()[i][j];
            }
        }
    }

    public int[][] getMatrix(){
        return this.matrix;
    }
    public Item getRowItem(){
        return this.rowItem;
    }
    public Item getColItem(){
        return this.colItem;
    }
    // Get the avaliable count for an item given its assigned bag id.
    // This method is used when doing Least Constraining Value heuristic.
    public int getAvalCnt(Item item, int bagId){
        int rtnCnt = 0;
        if(item.equals(rowItem)){
            for(int i = 0; i < matrix[0].length; i ++){
                if(matrix[bagId][i] == 1){
                    rtnCnt ++;
                }
            }
            return rtnCnt;
        }else if(item.equals(colItem)){
            for(int i = 0; i < matrix.length; i ++){
                if(matrix[i][bagId] == 1){
                    rtnCnt ++;
                }
            }
            return rtnCnt;
        }
        return rtnCnt;
    }
    // Check whether the matrix contains an Item.
    public boolean containsItem(Item item){
        if(item.equals(this.rowItem) || item.equals(this.colItem)){
            return true;
        }
        return false;
    }
    // Set a row or column given by bagId to 0.
    // Row or column is determined by the item passed in.
    public void setInvalid(Item item, int bagId){
        // If it is a row item.
        if(item.equals(rowItem)){
            for(int i = 0; i < matrix[0].length; i ++){
                matrix[bagId][i] = 0;
            }
        }else if(item.equals(colItem)){
            for(int i = 0; i < matrix.length; i ++){
                matrix[i][bagId] = 0;
            }
        }else{
            System.out.println("Item not contained in the matrix.");
            return;
        }
    }
    // Set all the rows or cols to 0 except for one specified by bagId.
    public void setValid(Item item, int bagId){
        // If it is a row item.
        if(item.equals(rowItem)){
            for(int i = 0; i < matrix.length; i ++){
                if(i == bagId){
                    continue;
                }
                for(int j = 0; j < matrix[0].length; j ++){
                    matrix[i][j] = 0;
                }
            }
        }else if(item.equals(colItem)){
            for(int j = 0; j < matrix[0].length; j ++){
                if(j == bagId){
                    continue;
                }
                for(int i = 0; i < matrix.length; i ++){
                    matrix[i][j] = 0;
                }
            }
        }else{
            System.out.println("Item not contained in the matrix.");
            return;
        }
    }
    // Set all the rows or cols to 0 except for one specified by bagIds.
    public void setValid(Item item, ArrayList<Integer> bagIds){
        // If it is a row item.
        if(item.equals(rowItem)){
            for(int i = 0; i < matrix.length; i ++){
                if(bagIds.contains(new Integer(i))){
                    continue;
                }
                for(int j = 0; j < matrix[0].length; j ++){
                    matrix[i][j] = 0;
                }
            }
        }else if(item.equals(colItem)){
            for(int j = 0; j < matrix[0].length; j ++){
                if(bagIds.contains(new Integer(j))){
                    continue;
                }
                for(int i = 0; i < matrix.length; i ++){
                    matrix[i][j] = 0;
                }
            }
        }else{
            System.out.println("Item not contained in the matrix.");
            return;
        }
    }
    // Set the diagnol to 0. Used for not equals constraint.
    public void setDiagnolNotValid(){
        for(int i = 0; i < matrix.length; i ++){
            for(int j = 0; j < matrix[0].length; j ++){
                if(i == j){
                    matrix[i][j] = 0;
                }
            }
        }
        return;
    }
    // Set all spots other than diagnol to 0.
    // Used for binary equals contraint.
    public void setNotDiagnolNotValid(){
        for(int i = 0; i < matrix.length; i ++){
            for(int j = 0; j < matrix[0].length; j ++){
                if(i != j){
                    matrix[i][j] = 0;
                }
            }
        }
        return;
    }
    // Get the valid bag id for the input item.
    public ArrayList<Integer> getValidBagId(Item item){
        ArrayList<Integer> rtn = new ArrayList<Integer>();
        if(!this.containsItem(item)){
            System.out.println("Item not found in this matrix.");
            return rtn;
        }
        if(item.equals(this.rowItem)){
            for(int i = 0; i < matrix.length; i ++){
                if(checkRowAval(i)){
                    rtn.add(i);
                }
            }
            return rtn;
        }else{
            for(int j = 0; j < matrix[0].length; j ++){
                if(checkColAval(j)){
                    rtn.add(j);
                }
            }
            return rtn;
        }
    }
    // Check whether row contains 1.
    public boolean checkRowAval(int row){
        for(int j = 0; j < matrix[0].length; j ++){
            if(matrix[row][j] == 1){
                return true;
            }
        }
        return false;
    }
    // Check whether col contains 1.
    public boolean checkColAval(int col){
        for(int i = 0; i < matrix.length; i ++){
            if(matrix[i][col] == 1){
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString(){
        String rtn = "";
        rtn += "Items: \n";
        rtn += rowItem.toString() + "\n";
        rtn += colItem.toString() + "\n";
        for(int i = 0; i < matrix.length; i ++){
            for(int j = 0; j < matrix[0].length; j ++){
                rtn += matrix[i][j] + " ";
            }
            rtn += "\n";
        }
        return rtn;
    }
}
