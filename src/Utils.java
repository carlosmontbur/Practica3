import java.util.Scanner;

public class Utils {

    public static void readFileAndStoreFloatValues(Scanner scanner, double[][] data) {
        int i, j;
        double value;

        while (scanner.hasNext()) {
            i = scanner.nextInt();
            j = scanner.nextInt();
            value = scanner.nextFloat();
            data[i][j] = value;
        }
    }

    public static void print(int tam, double[][] data) {
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                System.out.print(data[i][j]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

//    public static double calculateCost(double[][] data, Cromosoma cromosoma, int m) {
//        double result = 0;
//        for (int i = 0; i < m - 1; i++) {
//            for (int j = i + 1; j < m; j++) {
//                result += cromosoma.getGen(i) < cromosoma.getGen(j)
//                        ? data[cromosoma.getGen(i)][cromosoma.getGen(j)]
//                        : data[cromosoma.getGen(j)][cromosoma.getGen(i)];
//            }
//        }
//        return result;
//    }
//
//    public static void randomInit(Cromosoma cromosoma, int n, int m, RandomGenerator rnd) {
//        int x, j, i = 0;
//        while (i < m) {
//            x = rnd.getRandomInt(0, n - 1);
//            j = 0;
//            while (cromosoma.getGen(j) != x && j < i) {
//                j++;
//            }
//            if (j == i) {
//                cromosoma.setGen(i, x);
//                i++;
//            }
//        }
//    }

    public static boolean hasValue(int[] vSol, int value) {
        for (int j : vSol) {
            if (value == j) {
                return true;
            }
        }
        return false;
    }

    public static String getFilePath(int num_file, int num_seed, int porcMPX, int tamEli, int cr) {
        return "logs/" + getCrossName(cr) + "E" + tamEli + "_File" + num_file + "_Seed" + num_seed + "_PorcMPX" + porcMPX;
    }

    private static String getCrossName(int option) {
        return option == 0 ? "2P_" : "MPX_";
    }
}
