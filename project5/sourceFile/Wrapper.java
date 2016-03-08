/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * Wrapper class is used when trying to sort the values by constraining count.
 */
public class Wrapper implements Comparable<Wrapper>{
    // Target value.
    private int a;
    // Value used to help sorting.
    private int b;

    public Wrapper(int a, int b){
        this.a = a;
        this.b = b;
    }
    public int getA(){
        return this.a;
    }
    public int getB(){
        return this.b;
    }
    @Override
    public int compareTo(Wrapper w){
        if(b > w.getB()){
            return 1;
        }else if(b < w.getB()){
            return -1;
        }else{
            return 0;
        }
    }

}
