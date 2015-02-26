package kenkenpuzzlesolver;

import java.util.ArrayList;
//import org.apache.commons.lang3.mutable.MutableInt;

/**
 * @author WrightEdge LSFL 007
 */
public class KKSolver {

    public KKSolver() {
    }

    public KKPuzzle[] solvePuzzle(KKPuzzle KKP, boolean useHeu) {
        inferInitDomForAllCages(KKP);
        ArrayList<KKPuzzle> KKPS = new ArrayList<KKPuzzle>();
        //MutableInt explored = new MutableInt(0);
        //MutableInt exploredTot = new MutableInt(0);
        //backtrack(KKP, KKPS, explored, exploredTot, useHeu);
        backtrack(KKP, KKPS, useHeu);
        return KKPS.toArray(new KKPuzzle[0]);
    }

    //    private KKPuzzle backtrack(KKPuzzle KKP, ArrayList<KKPuzzle> KKPS, MutableInt explored, MutableInt exploredTot, boolean useHeu) {
    private KKPuzzle backtrack(KKPuzzle KKP, ArrayList<KKPuzzle> KKPS, boolean useHeu) {
        Cell cell = null;
        Inference domSave = null;
        Integer[] domain;

        if (KKP.isComplete()) {
//            System.out.println("Using Heuristics : " + useHeu);
//            System.out.println("Solution #" + (KKPS.size() + 1) + " took:\n" 
//                    + explored.toString() + " assignments from previous solution, and\n"
//                    + exploredTot.toString() + " total assignments from start.");
//            System.out.println(KKP.toStringAss());
            System.out.print(".");
            KKPS.add(new KKPuzzle(KKP));
            //explored.setValue(0);
        }

        cell = KKP.getCellRef(KKP.selectUnassignedVariable(useHeu));
        if (cell != null) {
            if (!useHeu) {
                domain = cell.getOrderedDomain();
            } else {
                domain = KKP.getOrderedDomain(cell);
            }
            for (int value : domain) {

                if (KKP.valIsConsWAss(value, cell.getCellI())) {

//                    for (int i = 0; i < cell.getCellI(); i++) {
//                        System.out.print("\t");
//                    }
//                    System.out.print("c=" + cell.getCellI() + " v=" + value + "\n");

                    domSave = new Inference(KKP);
                    cell.setAssignment(value);
                    KKP.refineDoms(cell.getCellI());

                    //explored.increment();
                    //exploredTot.increment();
                    //KKPuzzle result = backtrack(KKP, KKPS, explored, exploredTot, useHeu);
                    KKPuzzle result = backtrack(KKP, KKPS, useHeu);
                    if (result != null) {
                        return result;
                    }

                    KKP.restoreDom(domSave);
                    cell.resetAssignment();
                }
            }
        }
        return null;
    }

    private void inferInitDomForAllCages(KKPuzzle KKP) {
        int gs = KKP.getGridSize();
        int rc = KKP.getRCLen();
        Cage CG;

        for (int i = 0; i < KKP.getNumCages(); i++) {
            CG = KKP.getCageRef(i);
            switch (CG.getOperator()) {
                case KKPuzzle.N:
                    CG.assignToAll(new Integer[]{CG.getTarget()});
                    break;
                case KKPuzzle.M:
                    CG.assignToAll(inferCageDomAll(rc, CG));
                    break;
                case KKPuzzle.D:
                    CG.assignToAll(inferCageDomAll(rc, CG));
                    break;
                case KKPuzzle.A:
                    CG.assignToAll(inferCageDomAll(rc, CG));
                    break;
                case KKPuzzle.S:
                    CG.assignToAll(inferCageDomAll(rc, CG));
                    break;
            }
        }
    }

    private Integer[] inferCageDomAll(int rc, Cage CG) {
        int maxi = rc;
        Integer[] dom = new Integer[maxi];
        for (int i = 0; i < maxi; i++) {
            dom[i] = i + 1;
        }
        return dom;
    }

    //int add := 1 for addition, 0 for multiplication or div
    private Integer[] inferCageDomNonSub(int rc, Cage CG, int add) {
        int maxi = Math.max(rc, CG.getTarget() - add);
        if (maxi > rc) {
            maxi = rc;
        }
        Integer[] dom = new Integer[maxi];
        for (int i = 1; i <= dom.length;) {
            dom[i - 1] = i++;
        }
        return dom;
    }

    //target < rc
    private Integer[] inferCageDomSub(int rc, Cage CG) {
        int maxi = Math.max(CG.getTarget() - 1, rc - 1);
        if (maxi > rc) {
            maxi = rc;
        }
        Integer[] dom = new Integer[maxi];
        for (int i = 1; i <= dom.length;) {
            dom[i - 1] = i++;
        }
        return dom;
    }
}
