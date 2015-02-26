package kenkenpuzzlesolver;

/**
 *
 * @author McMatt
 */
public class KenKenPuzzleSolver {

    private static final String USAGE = "Usage: KenKenPuzzleSolver SOURCE DEST";
    private static String iFileS, oFileS;
    private static KKPuzzle KKP = new KKPuzzle();
    private static KKFIO KKFIO = new KKFIO();
    private static KKSolver KKS = new KKSolver();

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println(USAGE);
            System.exit(1);
        }
        iFileS = args[0];
        oFileS = args[1];

        boolean read = KKFIO.readFromFile(iFileS, KKP);

        if (read) {
            System.out.print("Successfully read file " + iFileS + ". \nNow solving.");
            KKPuzzle[] KKPS = KKS.solvePuzzle(KKP, false);
            System.out.println("");
            System.out.println("Found " + KKPS.length + " solutions.");
            if (KKPS.length > 0 && KKFIO.writeToFile(oFileS, KKPS)) {
                System.out.println("Success.");
                System.exit(0);
            } else {
                System.exit(0);
            }
        } else {
            System.err.println("Error Reading: input file is damaged or puzzle is invalid.");
            System.exit(1);
        }
    }
}
