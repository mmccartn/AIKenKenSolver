package kenkenpuzzlesolver;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Matt
 */
public class Cell {

    private int cellI, assignment;
    private ArrayList<Integer> Domain;

    /*** Construction ***/
    public Cell(int _cellI) {
        cellI = _cellI;
        assignment = 0;
        Domain = new ArrayList<Integer>();
    }

    public Cell(Cell aCell) {
        this(aCell.getCellI(), aCell.getAssignment(), aCell.getDomain());
    }

    public Cell(int _cellI, int _assignment, ArrayList<Integer> _Domain) {
        cellI = _cellI;
        assignment = _assignment;
        Domain = new ArrayList<Integer>();
    }

    /*** Assignment ***/
    public boolean isAssigned() {
        boolean ret = true;
        if (this.assignment == 0) {
            ret = false;
        }
        return ret;
    }

    public int getAssignment() {
        return this.assignment;
    }

    public boolean isAssignedValue(int value) {
        boolean ret = false;
        if (this.assignment == value) {
            ret = true;
        }
        return ret;
    }

    public void resetAssignment() {
        this.assignment = 0;
    }

    public void setAssignment(int value) {
        this.assignment = value;
    }

    /*** Domain ***/
    public ArrayList<Integer> getDomain() {
        ArrayList<Integer> DomainB = new ArrayList<Integer>(this.Domain.size());
        DomainB.addAll(this.Domain);
        return DomainB;
    }

    public void setDomain(Integer[] dom) {
        //this.Domain = new ArrayList<Integer>(Arrays.asList(dom));
        this.Domain = new ArrayList<Integer>(dom.length);
        this.Domain.addAll(Arrays.asList(dom));
    }
    
    //TODO: Least Constrained Value:
    //The domain elelment which appears the lease in the adjecent row and column
    //sorted to the one that appears the most
    public Integer[] getOrderedDomain() {
        return this.getDomain().toArray(new Integer[0]);
    }

    /*** Cell Indexes ***/
    public int getCellI() {
        return cellI;
    }

    public void setCellI(int cellI) {
        this.cellI = cellI;
    }

    public void removeFromDom(int ass) {
        if (Domain.contains(ass)) {
            Domain.remove(Domain.indexOf(ass));
//            if (Domain.size() == 1) {
//                setAssignment(Domain.get(0));
//            }
        }
    }
    
    public int getDomainSize() {
        return Domain.size();
    }

    public boolean domContains(int ele) {
        return Domain.contains(ele);
    }
    
}
