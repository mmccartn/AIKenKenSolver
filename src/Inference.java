/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kenkenpuzzlesolver;

/**
 *
 * @author McMatt
 */
public class Inference {

    private Integer[][] cellDoms;

    public Inference(KKPuzzle KKP) {
        Integer[] cellIs = KKP.getCellIs();
        Integer[] dom;
        cellDoms = new Integer[cellIs.length][];
        for (int cellI : cellIs) {
            dom = KKP.getCellDom(cellI);
            cellDoms[cellI] = new Integer[dom.length];
            for (int i = 0; i < dom.length; i++) {
                cellDoms[cellI][i] = dom[i];
            }
            //System.arraycopy(dom, 0, cellDoms[cellI], 0, dom.length);
        }
    }

    public Integer[] getDom(int i) {
        return cellDoms[i];
    }
}
