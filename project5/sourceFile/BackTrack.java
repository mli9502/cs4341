import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * This class has algorithm that does simple backtracking,
 * backtracking + MRV + degree heuristic, backtracking + MRV + degree heuristic + LCV,
 * backtracking + MRV + degree heuristic + LCV + Forward Checking (default).
 *
 * To switch between these methods, see line 47.
 */
public class BackTrack {
    private Matrices matrices;
    private Items items;
    private Bags bags;
    private MaxFittingLimitConstraint maxflc;
    private MinFittingLimitConstraint minflc;
    private MinWeightLimitConstraint mwlc;
    private WeightLimitConstraint wlc;
    private BinaryEqualConstraint bec;
    private BinaryNotEqualConstraint bnec;
    private MutualInclusiveConstraint mic;

    public BackTrack(Matrices matrices,
                     Items items,
                     Bags bags,
                     MaxFittingLimitConstraint maxflc,
                     MinFittingLimitConstraint minflc,
                     MinWeightLimitConstraint mwlc,
                     WeightLimitConstraint wlc,
                     BinaryEqualConstraint bec,
                     BinaryNotEqualConstraint bnec,
                     MutualInclusiveConstraint mic){
        this.matrices = matrices;
        this.items = items;
        this.bags = bags;
        this.maxflc = maxflc;
        this.minflc = minflc;
        this.mwlc = mwlc;
        this.wlc = wlc;
        this.bec = bec;
        this.bnec = bnec;
        this.mic = mic;
    }

    /**
     * For just backtracking: comment out line 63 and uncomment line 57.
     * For backtracking + MRV + degree heuristic: comment out line 63 and uncomment line 59.
     * For backtracking + MRV + degree heuristic + LCV: comment out line 63 and uncomment line 61.
     * backtracking + MRV + degree heuristic + LCV + Forward Checking is the default method.
     * @return
     */
    public Solution backtrackingSearch(){
        // AC3 preprocessing. Partially works.
        // this.ac3();
        Solution s = new Solution();
        // Ordinary back tracking search.
        // return backtrack(s, items, bags, matrices);
        // Back tracking search with MRV + degree heuristic.
        // return this.backtrackMRV(s, items, bags, matrices);
        // Back tracking search with MRV + degree heuristic + LCV.
        // return this.backtrackMrvLcv(s, items, bags, matrices);
        // Back tracking searth with MRV + degree heuristic + LCV + forward checking.
        return this.backtrackMrvLcvFc(s, items, bags, matrices);
    }
    // Just backtracking.
    public Solution backtrack(Solution s, Items items, Bags bags, Matrices matrices){
        // If solution contains all items, finish.
        if(s.containsAllItem(items)){
            // Check for global constraints.
            if(this.finishConsistant(bags)){
                return s;
            }
            return new Solution();
        }
        // Get an unused item according to solution.
        Item varItem = items.getUnusedItem(s);
        // Get the domain for this item.
        ArrayList<Integer> values = varItem.updateAndGetDomain(matrices);
        if(items.getSize() == 1){
            values = bags.getBagsIds();
        }
        for(int i = 0; i < values.size(); i ++){
            Bags tmpBags = new Bags(bags);
            // Update bag with item.
            tmpBags.updateBag(values.get(i), varItem);
            // Check for constraints.
            if(consistant(tmpBags)){
                Pair pair = new Pair(varItem, bags.getBagById(values.get(i)));
                // Check for binary constraints.
                if(!this.binaryConsistant(pair, s)){
                    continue;
                }
                // Add to solution.
                s.addPair(pair);
                Solution tmpSolution = backtrack(s, items, tmpBags, matrices);
                // If no solution found, remove this pair and try next value.
                if(tmpSolution.isEmpty()){
                    s.removePair(pair);
                }else{
                    // If found a solution, return.
                    return tmpSolution;
                }
            }
        }
        // If all values are tried and no solution found, return empty.
        return new Solution();
    }

    // Backtrack algorithm + Minimum remaining value and breaking ties with the degree heuristic.
    public Solution backtrackMRV(Solution s, Items items, Bags bags, Matrices matrices){
        if(s.containsAllItem(items)){
            if(this.finishConsistant(bags)){
                return s;
            }
            return new Solution();
        }
        // Using MRV and degree heuristic to get unused value.
        // This method is defined in Items class.
        Item varItem = items.getUnusedItemMRV(s, matrices);
        ArrayList<Integer> values = varItem.getDomain();
        if(items.getSize() == 1){
            values = bags.getBagsIds();
        }
        for(int i = 0; i < values.size(); i ++){
            Bags tmpBags = new Bags(bags);
            tmpBags.updateBag(values.get(i), varItem);
            if(consistant(tmpBags)){
                Pair pair = new Pair(varItem, bags.getBagById(values.get(i)));
                if(!this.binaryConsistant(pair, s)){
                    continue;
                }
                s.addPair(pair);
                Solution tmpSolution = backtrackMRV(s, items, tmpBags, matrices);
                if(tmpSolution.isEmpty()){
                    s.removePair(pair);
                }else{
                    return tmpSolution;
                }
            }
        }
        return new Solution();
    }
    // backtracking + MRV + degree heuristic + LCV
    public Solution backtrackMrvLcv(Solution s, Items items, Bags bags, Matrices matrices){
        if(s.containsAllItem(items)){
            if(this.finishConsistant(bags)){
                return s;
            }
            return new Solution();
        }
        // Using MRV and degree heuristic to get unused value.
        // This method is defined in Items class.
        Item varItem = items.getUnusedItemMRV(s, matrices);
        // Using LCV to sort the values.
        // This method is defined in Item class.
        ArrayList<Integer> values = varItem.sortDomain(matrices);
        if(items.getSize() == 1){
            values = bags.getBagsIds();
        }
        for(int i = 0; i < values.size(); i ++){
            Bags tmpBags = new Bags(bags);
            tmpBags.updateBag(values.get(i), varItem);
            if(consistant(tmpBags)){
                Pair pair = new Pair(varItem, bags.getBagById(values.get(i)));
                if(!this.binaryConsistant(pair, s)){
                    continue;
                }
                s.addPair(pair);
                Solution tmpSolution = backtrackMrvLcv(s, items, tmpBags, matrices);
                if(tmpSolution.isEmpty()){
                    s.removePair(pair);
                }else{
                    return tmpSolution;
                }
            }
        }
        return new Solution();
    }
    // backtracking + MRV + degree heuristic + LCV + Forward Checking
    public Solution backtrackMrvLcvFc(Solution s, Items items, Bags bags, Matrices matrices){
        if(s.containsAllItem(items)){
            if(this.finishConsistant(bags)){
                return s;
            }
            return new Solution();
        }
        // Using MRV and degree heuristic to get unused value.
        // This method is defined in Items class.
        Item varItem = items.getUnusedItemMRV(s, matrices);
        // Using LCV to sort the values.
        // This method is defined in Item class.
        ArrayList<Integer> values = varItem.sortDomain(matrices);
        if(items.getSize() == 1){
            values = bags.getBagsIds();
        }
        for(int i = 0; i < values.size(); i ++){
            Bags tmpBags = new Bags(bags);
            tmpBags.updateBag(values.get(i), varItem);
            if(consistant(tmpBags)){
                Pair pair = new Pair(varItem, bags.getBagById(values.get(i)));
                if(!this.binaryConsistant(pair, s)){
                    continue;
                }
                // Do forward checking by updating the matrices according to current taken item, its bag and constraints.
                Matrices tmpMatrices = new Matrices(matrices);
                tmpMatrices.updateMatrices(pair);
                this.updateMatricesCons(tmpMatrices, pair, items, tmpBags);
                s.addPair(pair);
                Solution tmpSolution = backtrackMrvLcvFc(s, items, tmpBags, tmpMatrices);
                if(tmpSolution.isEmpty()){
                    s.removePair(pair);
                }else{
                    return tmpSolution;
                }
            }
        }
        return new Solution();
    }

    // Check for global constraints including Minimum Fitting Limit and Minimum Weight Limit constraints.
    private boolean finishConsistant(Bags bags){
        boolean minflcFlg = true;
        boolean mwlcFlg = true;
        if(this.minflc != null){
            for(int i = 0; i < bags.getSize(); i ++){
                if(!this.minflc.satisfy(bags.getBags().get(i))){
                    // System.out.println(bags.getBags().get(i));
                    minflcFlg = false;
                    break;
                }
            }
        }
        if(this.mwlc != null){
            for(int i = 0; i < bags.getSize(); i ++){
                if(!this.mwlc.satisfy(bags.getBags().get(i))){
                    // System.out.println(bags.getBags().get(i));
                    mwlcFlg = false;
                    break;
                }
            }
        }
        return minflcFlg && mwlcFlg;
    }
    // Check for binary constraints including Binary Equality, Binary Not Equal and Mutual Inclusive constraints.
    private boolean binaryConsistant(Pair pair, Solution s){
        boolean becFlg = false;
        boolean bnecFlg = false;
        boolean micFlg = false;
        if(this.bec == null){
            becFlg = true;
        }else if(this.bec.satisfy(pair, s)){
            becFlg = true;
        }else{
            becFlg = false;
        }
        if(this.bnec == null){
            bnecFlg = true;
        }else if(this.bnec.satisfy(pair, s)){
            bnecFlg = true;
        }else{
            bnecFlg = false;
        }
        if(this.mic == null){
            micFlg = true;
        }else if(this.mic.satisfy(pair, s)){
            micFlg = true;
        }else{
            micFlg = false;
        }
        return becFlg && bnecFlg && micFlg;
    }
    // Check for other global constraints that need to be kept as the algorithm goes.
    // Including Max Fitting Limit and Weight Limit constraints.
    private boolean consistant(Bags bags){
        if(this.maxflc == null){
            if(this.wlc.satisfy(bags)){
                return true;
            }
            return false;
        }
        if(this.maxflc.satisfy(bags) &&
                this.wlc.satisfy(bags)){
            return true;
        }
        return false;
    }
    // Method used to update matrices according to constraints when conducting forward checking.
    private void updateMatricesCons(Matrices matrices, Pair pair, Items items, Bags bags){
        // WeightLimitConstraint
        for(int i = 0; i < items.getSize(); i ++){
            // Go through all bags for an item.
            if(items.getItems().get(i).equals(pair.getItem())){
                continue;
            }
            ArrayList<Bag> unsatBags = wlc.getUnsatBags(items.getItems().get(i), bags);
            matrices.setMatrixExclusive(items.getItems().get(i), unsatBags);
        }
        // MaxFittingLimitConstraint
        if(this.maxflc != null){
            for(int i = 0; i < items.getSize(); i ++){
                if(items.getItems().get(i).equals(pair.getItem())){
                    continue;
                }
                ArrayList<Bag> unsatBags = this.maxflc.getUnsatBags(items.getItems().get(i), bags);
                matrices.setMatrixExclusive(items.getItems().get(i), unsatBags);
            }
        }
        // BinaryEqualConstraint
        if(this.bec != null){
            Item corrItem = this.bec.getCorrItem(pair);
            if(corrItem != null){
                ArrayList<Bag> satBag = new ArrayList<Bag>();
                satBag.add(pair.getBag());
                matrices.setMatrixInclusive(corrItem, satBag);
            }
        }
        // BinaryNotEqualConstraint
        if(this.bnec != null){
            Item corrItem = this.bnec.getCorrItem(pair);
            if(corrItem != null){
                ArrayList<Bag> unsatBag = new ArrayList<Bag>();
                unsatBag.add(pair.getBag());
                matrices.setMatrixExclusive(corrItem, unsatBag);
            }
        }
        // MutualInclusiveConstraint
        if(this.mic != null){
            ArrayList<Pair> nps = this.mic.getCorrPair(pair, bags);
            if(nps != null){
                for(int i = 0; i < nps.size(); i ++){
                    ArrayList<Bag> satBag = new ArrayList<Bag>();
                    satBag.add(nps.get(i).getBag());
                    matrices.setMatrixInclusive(nps.get(i).getItem(), satBag);
                }
            }
        }
    }

    // AC3 consistency checking.
    public boolean ac3(){
        Queue<ItemPair> ipq = new LinkedList<ItemPair>();
        for(int i = 0; i < items.getSize(); i ++){
            for(int j = i + 1; j < items.getSize(); j ++){
                ipq.add(new ItemPair(items.getItems().get(i), items.getItems().get(j)));
            }
        }
        while(!ipq.isEmpty()){
            ItemPair ip = ipq.remove();
            System.out.println("Before reviese");
            boolean revRtn = revise(ip);
            System.out.println(revRtn);
            System.out.println("After reviese");
            if(revRtn){
                if(ip.getFirst().getDomain().size() == 0){
                    return false;
                }
                for(int i = 0; i < items.getSize(); i ++){
                    if(items.getItems().get(i).equals(ip.getFirst()) ||
                            items.getItems().get(i).equals(ip.getSecond())){
                        continue;
                    }
                    ipq.add(new ItemPair(items.getItems().get(i), ip.getFirst()));
                }
            }
        }
        return true;
    }

    public boolean revise(ItemPair ip){
        boolean revised  = false;
        boolean s1 = false;
        boolean s2 = false;
        ArrayList<Integer> domainFir = ip.getFirst().updateAndGetDomain(matrices);
        ArrayList<Integer> domainSec = ip.getSecond().updateAndGetDomain(matrices);
        System.out.println(domainFir.size());
        System.out.println(domainSec.size());
        for(int i = 0; i < domainFir.size(); i ++){
            for(int j = 0; j < domainSec.size(); j ++){
                if(this.bnec != null){
                    if(this.bnec.satisfy(ip, domainFir.get(i), domainSec.get(j))) {
                        s1 = true;
                    }
                }
                if(this.bec != null){
                    if(this.bec.satisfy(ip, domainFir.get(i), domainSec.get(j))){
                        s2 = true;
                    }
                }
            }
            if((s1 == false) || (s2 == false)){
                Matrix mtx = matrices.getMatrix(ip.getFirst().getId(), ip.getSecond().getId());
                if(mtx.getRowItem().equals(ip.getFirst())){
                    for(int j = 0; j < mtx.getMatrix()[0].length; j ++){
                        mtx.getMatrix()[i][j] = 0;
                    }
                }else{
                    for(int j = 0; j < mtx.getMatrix().length; j ++){
                        mtx.getMatrix()[j][i] = 0;
                    }
                }
                revised = true;
            }
        }
        return revised;
    }
}
