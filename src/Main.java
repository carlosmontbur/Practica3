import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner capt = new Scanner(System.in);
        int option = -1;

        int num_file;
        int num_seed;
//        while (option != 0) {
//            printMainMenu();
//            option = capt.nextInt();
//
//            if (option == 0) {
//                System.out.print("Saliendo...");
//                break;
//            }
//        System.out.print("Inserte Nº Semilla [0-4]: ");
//        num_seed = capt.nextInt();
        num_seed = 0;
//        System.out.print("Inserte Nº Archivo [0-8]: ");
//        num_file = capt.nextInt();
//        System.out.println("");
        num_file = 0;


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


//            String filePath = Utils.getFilePath(num_file, num_seed, porcMPX, tamEli, cr);

        AlgHormigas.run(data, n, m, seed, evaluations, greedy, alfa, beta, tamPob, q0, p, fi, delta);
//        }

    }

    private static void printMainMenu() {
        System.out.println("*******  SELECCIONA UNA OPCION   *******");
        System.out.println("1. Volver a ejecutar");
        System.out.println("0. Salir");
        System.out.println("");
        System.out.print("Seleccione opción [1,0]:");
    }
}