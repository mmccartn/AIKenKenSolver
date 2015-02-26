package kenkenpuzzlesolver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author McMatt
 */
public class KKFIO {

    private static final int TAI = 0, OPI = 1, LOS = 2;

    public KKFIO() {
    }

    public boolean readFromFile(String iFileS, KKPuzzle KKP) {
        boolean succ = true;
        try {
            FileInputStream fin = new FileInputStream(iFileS);
            DataInputStream din = new DataInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(din));

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(" ");
                if (!addDataToPuzzle(data, KKP)) {
                    succ = false;
                    break;
                }
            }

            succ = KKP.setRCLen();
            din.close();
        } catch (Exception e) {
            succ = false;
            System.err.println("KKFIO: Read Exception " + e.getMessage());
        }
        return succ;
    }

    public boolean writeToFile(String oFileS, KKPuzzle[] KKPS) {
        boolean ret = true;
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(oFileS, false));
            System.out.println("Writing to file " + oFileS + ".");
            for (KKPuzzle KKP : KKPS) {
                out.write(KKP.toStringAss() + "\n");
            }
            out.close();
        } catch (IOException e) {
            System.err.println("Failed to write solutions to " + oFileS);
            ret = false;
        }
        return ret;
    }

    private boolean addDataToPuzzle(String[] data, KKPuzzle KKP) {
        boolean succ = false;

        if (data.length > 2) {
            int target = Integer.parseInt(data[TAI]);
            int operator = -1;
            switch (data[OPI].charAt(0)) {
                case '.':
                    operator = KKPuzzle.N;
                    break;
                case '*':
                    operator = KKPuzzle.M;
                    break;
                case '/':
                    operator = KKPuzzle.D;
                    break;
                case '+':
                    operator = KKPuzzle.A;
                    break;
                case '-':
                    operator = KKPuzzle.S;
                    break;
            }
            if (target > 0 && operator > -1) {
                int numCells = data.length - LOS;
                Integer[] cellIs = new Integer[numCells];
                for (int i = LOS; i < numCells + LOS; i++) {
                    cellIs[i - LOS] = Integer.parseInt(data[i]);
                }
                succ = KKP.addCage(target, operator, cellIs);
            }
        }
        return succ;
    }

    public String writeToFile(String oFileS, KKPuzzle KKP) {
        String out = "";
        int rcLen = KKP.getRCLen(), c = 0;

        for (int i = 0; i < rcLen; i++) {
            for (int j = 0; j < rcLen; j++) {
                out += KKP.getCellRef(c++).getCellI() + " ";
            }
            out += "\n";
        }
        return out;
    }
}
