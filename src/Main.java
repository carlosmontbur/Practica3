import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        int num_file = 0;
        int num_seed = 0;

        // LEER PARAMETROS
        ParameterReader parameterReader = new ParameterReader(args[0]);
        File file = new File(parameterReader.getFiles().get(num_file));
        Long seed = parameterReader.getSeeds().get(num_seed);
        int evaluations = parameterReader.getEvaluations();
        int tamPob = parameterReader.getTamPob();
        double greedy = parameterReader.getGreedy().get(num_file);
        int alfa = 1;
        int beta = 1;
        double q0 = parameterReader.getQ0();
        double p = parameterReader.getP();
        double fi = parameterReader.getFi();
        double delta = parameterReader.getDelta();


        // CREAR MATRIZ
        Scanner scanner = new Scanner(file);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        double[][] data = new double[n][n];
        Utils.readFileAndStoreFloatValues(scanner, data);
        File dir = new File("logs");
        dir.mkdirs();
        String filePath = Utils.getFilePath(num_file, num_seed, alfa, beta);

        AlgHormigas.run(data, n, m, seed, evaluations, greedy, alfa, beta, tamPob, q0, p, fi, delta, filePath);
    }
}