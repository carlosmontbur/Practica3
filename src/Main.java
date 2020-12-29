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
            System.out.print("Inserte Nº Semilla [0-4]: ");
            num_seed = capt.nextInt();
            System.out.print("Inserte Nº Archivo [0-8]: ");
            num_file = capt.nextInt();
            System.out.println("");

            ParameterReader parameterReader = new ParameterReader(args[0]);
            File file = new File(parameterReader.getFiles().get(num_file));

            Long seed = parameterReader.getSeeds().get(num_seed);
            int evaluations = parameterReader.getEvaluations();
            double probCr = parameterReader.getProbCr();
            double probMut = parameterReader.getProbMut();
            RandomGenerator rnd = new RandomGenerator();
            rnd.setSeed(seed);
            int porcMPX = rnd.getRandomInt(20, 80);
            int tamPob = parameterReader.getTamPob();
            int tamEli = 2;
            int cr = 1;
            Scanner scanner = new Scanner(file);
            int n = scanner.nextInt();
            int m = scanner.nextInt();

            double[][] data = new double[n][n];
            File dir = new File("logs");
            dir.mkdirs();
            Utils.readFileAndStoreFloatValues(scanner, data);


            String filePath = Utils.getFilePath(num_file, num_seed, porcMPX, tamEli, cr);

//            AGG.run(data, n, m, cromosomaResult, tamPob, seed, evaluations, probCr, probMut, tamEli, cr, porcMPX, filePath);
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