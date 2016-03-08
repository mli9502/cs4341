import java.util.ArrayList;

/**
 * @author: Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * Which player can connect more live 2-pieces horizontally.
 */
public class HorizontalOne implements Feature{
    public HorizontalOne(){}

    @Override
    public String getFeatureName(){
        return "Horizontal connected one";
    }

    @Override
    public int getFeature(ArrayList<Integer> board){
        Helper h = new Helper();
        return h.horizontalN(h.arrListtoArr(board), 1);
    }
}
