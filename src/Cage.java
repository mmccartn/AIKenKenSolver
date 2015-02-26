package kenkenpuzzlesolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matt
 */
public class Cage {

    private int target, operator;
    private Map<Integer, Cell> CeMap;

    public Cage(int _target, int _operator, Integer[] _cellIs) {
        this.target = _target;
        this.operator = _operator;
        this.CeMap = new HashMap<Integer, Cell>();
        addCells(_cellIs);
    }

    public Cage(int _target, int _operator, Map<Integer, Cell> _CeMap) {
        this.target = _target;
        this.operator = _operator;
        this.CeMap = _CeMap;
    }

    public Cage(Cage aCage) {
        this(aCage.getTarget(), aCage.getOperator(), aCage.getCeMap());
    }

    private boolean addCells(Integer[] cellIs) {
        boolean result = true;
        for (int i = 0; i < cellIs.length; i++) {
            int cellI = cellIs[i];
            CeMap.put(cellI, new Cell(cellI));
        }
        return result;
    }

    public boolean canAcceptAssignment(int value) {
        boolean ret = false;

        switch (this.operator) {
            case KKPuzzle.N:
                ret = true;
                break;
            case KKPuzzle.M:
                ret = isMulVal(value);
                break;
            case KKPuzzle.D:
                ret = isDivVal(value);
                break;
            case KKPuzzle.A:
                ret = isAddVal(value);
                break;
            case KKPuzzle.S:
                ret = isSubVal(value);
                break;
        }

        return ret;
    }

    public boolean isSatisfied() {
        boolean ret = isComplete();
        if (ret) {
            switch (this.operator) {
                case KKPuzzle.N:
                    ret = true;
                    break;
                case KKPuzzle.M:
                    ret = isMulSat();
                    break;
                case KKPuzzle.D:
                    ret = isDivSat();
                    break;
                case KKPuzzle.A:
                    ret = isAddSat();
                    break;
                case KKPuzzle.S:
                    ret = isSubSat();
                    break;
            }
        }
        return ret;
    }

    public boolean isComplete() {
        boolean ret = true;
        for (int i : getCellIs()) {
            if (!CeMap.get(i).isAssigned()) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    private boolean isMulVal(int value) {
        boolean ret = true;
        Integer[] cellIs = getCellIs();
        int product = value;
        int ass = 0;

        for (int i = 0; i < cellIs.length; i++) {
            ass = CeMap.get(cellIs[i]).getAssignment();
            if (ass != 0) {
                product *= ass;
            }
        }

        if (product > this.target) {
            ret = false;
        }

        return ret;
    }

    private boolean isDivVal(int value) {
        if (numAssigned() == 0) {
            return true;
        }

        boolean ret = false;
        Integer[] cellIs = getCellIs();
        int a = CeMap.get(cellIs[0]).getAssignment();
        int b = CeMap.get(cellIs[1]).getAssignment();
        int ass = 0, quotient = 0;

        if (a == 0) {
            ass = b;
        } else {
            ass = a;
        }

        if (ass > value) {
            quotient = (int) (ass / value);
            if (quotient == target && (value * target) == ass) {
                ret = true;
            }
        } else {
            quotient = (int) (value / ass);
            if (quotient == target && (ass * target) == value) {
                ret = true;
            }
        }

        return ret;
    }

    private boolean isAddVal(int value) {
        boolean ret = true;
        Integer[] cellIs = getCellIs();
        int sum = value;

        for (int i = 0; i < cellIs.length; i++) {
            sum += CeMap.get(cellIs[i]).getAssignment();
        }

        if (sum > this.target) {
            ret = false;
        }

        return ret;
    }

    private boolean isSubVal(int value) {
        if (numAssigned() == 0) {
            return true;
        }

        boolean ret = false;

        Integer[] cellIs = getCellIs();
        int a = CeMap.get(cellIs[0]).getAssignment();
        int b = CeMap.get(cellIs[1]).getAssignment();
        int ass = 0, diff = 0;

        if (a == 0) {
            ass = b;
        } else {
            ass = a;
        }

        if (ass > value) {
            diff = ass - value;
            if (diff == target) {
                ret = true;
            }
        } else {
            diff = value - ass;
            if (diff == target) {
                ret = true;
            }
        }

        return ret;
    }

    private boolean isMulSat() {
        boolean ret = false;
        Integer[] cellIs = getCellIs();
        int product = CeMap.get(cellIs[0]).getAssignment();

        for (int i = 1; i < cellIs.length; i++) {
            product *= CeMap.get(cellIs[i]).getAssignment();
        }

        if (product == this.target) {
            ret = true;
        }

        return ret;
    }

    private boolean isDivSat() {
        boolean ret = false;
        Integer[] cellIs = getCellIs();
        int a = CeMap.get(cellIs[0]).getAssignment();
        int b = CeMap.get(cellIs[1]).getAssignment();
        int quotient = 0;

        if (a > b) {
            quotient = (int) (a / b);
            if (quotient == target && (b * target) == a) {
                ret = true;
            }
        } else {
            quotient = (int) (b / a);
            if (quotient == target && (a * target) == b) {
                ret = true;
            }
        }

        return ret;
    }

    private boolean isAddSat() {
        boolean ret = false;
        Integer[] cellIs = getCellIs();
        int sum = 0;

        for (int i = 0; i < cellIs.length; i++) {
            sum += CeMap.get(cellIs[i]).getAssignment();
        }

        if (sum == target) {
            ret = true;
        }

        return ret;
    }

    private boolean isSubSat() {
        boolean ret = false;
        Integer[] cellIs = getCellIs();
        int a = CeMap.get(cellIs[0]).getAssignment();
        int b = CeMap.get(cellIs[1]).getAssignment();
        int diff = 0;

        if (a > b) {
            diff = a - b;
        } else {
            diff = b - a;
        }
        if (diff == target) {
            ret = true;
        }

        return ret;
    }

    public Cell getCellRef(int cellI) {
        return CeMap.get(cellI);
    }

    public Map<Integer, Cell> getCeMap() {
        Map<Integer, Cell> CeMapB = new HashMap<Integer, Cell>();
        Integer[] keys = CeMap.keySet().toArray(new Integer[0]);
        for (int i = 0; i < keys.length; i++) {
            CeMapB.put(keys[i], new Cell(CeMap.get(keys[i])));
        }
        return CeMapB;
    }

    public void setCeMap(Map<Integer, Cell> CeMap) {
        this.CeMap = CeMap;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    private Integer[] getCellIs() {
        return CeMap.keySet().toArray(new Integer[0]);
    }

    public void assignToAll(Integer[] dom) {
        Integer[] cellIs = getCellIs();
        for (int i = 0; i < cellIs.length; i++) {
            CeMap.get(cellIs[i]).setDomain(dom);
        }
    }

    private int numAssigned() {
        int total = 0;
        Integer[] cellIs = getCellIs();
        for (int i = 0; i < cellIs.length; i++) {
            if (CeMap.get(cellIs[i]).isAssigned()) {
                total++;
            }
        }
        return total;
    }

}
