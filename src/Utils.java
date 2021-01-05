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
            data[j][i] = value;
        }
    }

    public static double calculateCost(double[][] data, Hormiga hormiga, int m) {
        double result = 0;
        for (int i = 0; i < m - 1; i++) {
            for (int j = i + 1; j < m; j++) {
                result += hormiga.getVectorIndex(i) < hormiga.getVectorIndex(j)
                        ? data[hormiga.getVectorIndex(i)][hormiga.getVectorIndex(j)]
                        : data[hormiga.getVectorIndex(j)][hormiga.getVectorIndex(i)];
            }
        }
        return result;
    }

    public static String getFilePath(int num_file, int num_seed, int alfa, int beta) {
        return "logs/" + "File" + num_file + "_Seed" + num_seed + "_Alfa" + alfa + "_Beta" + beta;
    }

}
