package kenkenpuzzlesolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author McMatt
 */
public class KKPuzzle {

    public static final int N = 0, M = 1, D = 2, A = 3, S = 4;
    private int gridSize, rcLen;
    private ArrayList<Cage> Cages;
    private Map<Integer, Integer> CeCaMap;

    /*** Construction ***/
    public KKPuzzle() {
        gridSize = 0;
        Cages = new ArrayList<Cage>();
        CeCaMap = new HashMap<Integer, Integer>();
    }

    public KKPuzzle(int _gridSize, int _rcLen,
            ArrayList<Cage> _Cages, Map<Integer, Integer> _CeMap) {
        this.gridSize = _gridSize;
        this.rcLen = _rcLen;
        this.Cages = _Cages;
        this.CeCaMap = _CeMap;
    }

    public KKPuzzle(KKPuzzle aKKP) {
        this(aKKP.getGridSize(), aKKP.getRCLen(), aKKP.getCages(), aKKP.getCeCaMap());
    }

    /*** Cage ***/
    public boolean addCage(int target, int operator, Integer[] cellIs) {
        boolean result = Cages.add(new Cage(target, operator, cellIs));
        if (result) {
            int cageI = Cages.size() - 1;
            for (int i = 0; i < cellIs.length; i++) {
                CeCaMap.put(cellIs[i], cageI);
                if (gridSize < cellIs[i]) {
                    gridSize = cellIs[i];
                }
            }
        }
        return result;
    }

    public int getNumCages() {
        return Cages.size();
    }

    public Cage getCageRef(int cageI) {
        return Cages.get(cageI);
    }

    public ArrayList<Cage> getCages() {
        ArrayList<Cage> CB = new ArrayList<Cage>(Cages.size());
        for (int i = 0; i < Cages.size(); i++) {
            CB.add(i, new Cage(Cages.get(i)));
        }
        return CB;
    }

    public void setCages(ArrayList<Cage> Cages) {
        this.Cages = Cages;
    }

    /*** Output ***/
    public String toStringDom() {
        int cell = 0;
        String ret = "";
        for (int r = 0; r < this.rcLen; r++) {
            for (int c = 0; c < this.rcLen; c++) {
                cell = (r * this.rcLen) + c;
                ret += String.format("%1$20s", getCellRef(cell).getDomain().toString());
            }
            ret += "\n";
        }
        return ret;
    }

    public String toStringAss() {
        int cell = 0;
        String ret = "";
        for (int r = 0; r < this.rcLen; r++) {
            for (int c = 0; c < this.rcLen; c++) {
                cell = (r * this.rcLen) + c;
                ret += String.format("%1$-2s", getCellRef(cell).getAssignment());
            }
            ret += "\n";
        }
        return ret;
    }

    public String toStringCells() {
        int cell = 0;
        String ret = "";
        for (int r = 0; r < this.rcLen; r++) {
            for (int c = 0; c < this.rcLen; c++) {
                cell = (r * this.rcLen) + c;
                ret += String.format("%1$10s", getCellRef(cell).getCellI());
            }
            ret += "\n";
        }
        return ret;
    }

    public String toStringCages() {
        int cell = 0;
        String ret = "";
        for (int r = 0; r < this.rcLen; r++) {
            for (int c = 0; c < this.rcLen; c++) {
                cell = (r * this.rcLen) + c;
                ret += String.format("%1$10s", CeCaMap.get(cell));
            }
            ret += "\n";
        }
        return ret;
    }

    /*** Map Cells to Cages ***/
    public Map<Integer, Integer> getCeCaMap() {
        Map<Integer, Integer> CeCaMapB = new HashMap<Integer, Integer>();
        CeCaMapB.putAll(this.CeCaMap);
        return CeCaMapB;
    }

    public void setCeCaMap(Map<Integer, Integer> CeCaMap) {
        this.CeCaMap = CeCaMap;
    }

    /*** Cell ***/
    public Integer[] getCellIs() {
        return CeCaMap.keySet().toArray(new Integer[0]);
    }

    public Cell getCellRef(int cellI) {
        return Cages.get(CeCaMap.get(cellI)).getCellRef(cellI);
    }

    //choose the one with the smallest domain
    public int selectUnassignedVariable(boolean MRV) {
        Cell c = null, t = null;
        Integer[] cellIs = getCellIs();

        for (int i : cellIs) {
            t = getCellRef(i);
            if (!t.isAssigned()) {
                if (MRV) {
                    //Most constrained variable := <
                    //Least constrained variable := >
                    if (c == null || t.getDomainSize() < c.getDomainSize()) {
                        c = new Cell(t);
                    }
                } else {
                    c = t;
                    break;
                }
            }
        }
        int ret = 0;
        if (c != null) {
            ret = c.getCellI();
        }
        return ret;
    }

    public boolean valIsConsWAss(int value, int cellI) {
        boolean ret = false;

        if (valueIsRCValid(value, cellI)) {
            ret = valueFitsInCage(value, cellI);
        }

        return ret;
    }

    private boolean valueFitsInCage(int value, int cellI) {
        Cage cage = Cages.get(CeCaMap.get(cellI));
        return cage.canAcceptAssignment(value);
    }

    private boolean valueIsRCValid(int value, int cellI) {
        boolean ret = true;

        for (int c : getColIs(cellI)) {
            if (getCellRef(c).isAssignedValue(value)) {
                ret = false;
                break;
            }
        }

        if (ret == true) {
            for (int r : getRowIs(cellI)) {
                if (getCellRef(r).isAssignedValue(value)) {
                    ret = false;
                    break;
                }
            }
        }

        return ret;
    }

    private Integer[] getRowIs(int cellI) {
        int initRowI = getColI(cellI);
        int finaRowI = (gridSize + 1) - (rcLen - initRowI);
        Integer[] rowIs = new Integer[rcLen - 1];

        for (int r = initRowI, i = 0; r <= finaRowI; r += rcLen) {
            if (r != cellI) {
                rowIs[i++] = r;
            }
        }
        return rowIs;
    }

    private int getRowI(int cellI) {
        return (int) Math.floor((double) cellI / (double) this.rcLen);
    }

    private Integer[] getColIs(int cellI) {
        int initColI = getRowI(cellI) * rcLen;
        int finaColI = initColI + rcLen;
        Integer[] colIs = new Integer[rcLen - 1];

        for (int c = initColI, i = 0; c < finaColI; c++) {
            if (c != cellI) {
                colIs[i++] = c;
            }
        }
        return colIs;
    }

    private int getColI(int cellI) {
        return cellI % this.rcLen;
    }

    /*** KKP attributes ***/
    public boolean isComplete() {
        boolean ret = true;

        for (Cage g : Cages) {
            if (!g.isComplete()) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public boolean isSatisfied() {
        boolean ret = true;

        for (Cage g : Cages) {
            if (!g.isSatisfied()) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public boolean setRCLen() {
        double square = (double) gridSize + 1.0;
        rcLen = (int) Math.sqrt(square);
        return ((rcLen * rcLen) == (int) square);
    }

    public int getGridSize() {
        return gridSize;
    }

    public int getRCLen() {
        return rcLen;
    }

    public Integer[] getCellDom(int cellI) {
        return this.getCellRef(cellI).getOrderedDomain();
    }

    public void restoreDom(Inference domSave) {
        Integer[] cellIs = this.getCellIs();
        for (int i : cellIs) {
            this.getCellRef(i).setDomain(domSave.getDom(i));
        }
    }

    public void refineDoms(int cellI) {
        Cell cell = getCellRef(cellI);
        int ass = cell.getAssignment();
        cell.setDomain(new Integer[]{ass});

        for (int col : getColIs(cellI)) {
            cell = getCellRef(col);
            cell.removeFromDom(ass);
        }

        for (int row : getRowIs(cellI)) {
            cell = getCellRef(row);
            cell.removeFromDom(ass);
        }

    }

    //TODO: Least Constrained Value:
    //The domain elelment which appears the lease in the adjecent row and column
    //sorted to the one that appears the most
    public Integer[] getOrderedDomainBaf(Cell cell) {
        ArrayList<Integer> domEles = cell.getDomain();
        ArrayList<Integer> eleOccs = new ArrayList<Integer>(domEles.size());
        for (int j = 0; j < domEles.size(); j++) {
            eleOccs.add(0);
        }
        Integer[] sorted = new Integer[domEles.size()];

        for (int col : getColIs(cell.getCellI())) {
            for (int i = 0; i < domEles.size(); i++) {
                if (getCellRef(col).domContains(domEles.get(i))) {
                    eleOccs.set(i, eleOccs.get(i) + 1);
                }
            }
        }

        for (int row : getRowIs(cell.getCellI())) {
            for (int i = 0; i < domEles.size(); i++) {
                if (getCellRef(row).domContains(domEles.get(i))) {
                    eleOccs.set(i, eleOccs.get(i) + 1);
                }
            }
        }

        int min = 0, i = 0;
        while (eleOccs.size() > 0) {
            min = Collections.min(eleOccs);
            sorted[i++] = domEles.get(eleOccs.indexOf(min));
            eleOccs.remove(eleOccs.indexOf(min));
        }
        return sorted;
    }

    public Integer[] getOrderedDomain(Cell cell) {

        ArrayList<Integer> domEles = cell.getDomain();
        int size = domEles.size(), ele = 0;
        Integer[] sorted = new Integer[size];
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (int i = 0; i < size; i++) {
            ele = domEles.get(i);
            map.put(ele, 0);
        }

        for (int col : getColIs(cell.getCellI())) {
            for (int i = 0; i < size; i++) {
                ele = domEles.get(i);
                if (getCellRef(col).domContains(ele)) {
                    map.put(ele, map.get(ele) + 1);
                }
            }
        }

        for (int row : getRowIs(cell.getCellI())) {
            for (int i = 0; i < domEles.size(); i++) {
                ele = domEles.get(i);
                if (getCellRef(row).domContains(ele)) {
                    map.put(ele, map.get(ele) + 1);
                }
            }
        }

        //Least constraining Value :=
        SortedSet<Entry<Integer, Integer>> sortedEntries = entriesSortedByValuesAscending(map);

        //Most constraining Value :=
        //SortedSet<Entry<Integer, Integer>> sortedEntries = entriesSortedByValuesDescending(map);

        int i = 0;
        Integer[] sortedKeys = new Integer[size];
        for (Entry<Integer, Integer> lol : sortedEntries) {
            sortedKeys[i++] = lol.getKey();
        }

        return sortedKeys;
    }

    //Least constraining Value heuristic
    //Taken from:
    //http://stackoverflow.com/a/4702335
    private static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValuesAscending(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {

                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                });
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    //Most constraining Value heuristic
    //Taken from:
    //http://stackoverflow.com/a/4702335
    private static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValuesDescending(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {

                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        if (res == 1 || res == 0) {
                            res = -1;
                        } else if (res == -1) {
                            res = 1;
                        }
                        return res;
                    }
                });
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}
