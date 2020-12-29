import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class ParameterReader {
    private final ArrayList<String> files;
    private final ArrayList<Long> seeds;
    private int evaluations = 0;
    private double probCr = 0;
    private double probMut = 0;
    private double porcMPX = 0;
    private int tamPob = 0;

    ParameterReader(String ruta) {
        files = new ArrayList<>();
        seeds = new ArrayList<>();

        String linea;
        FileReader fileReader;
        try {
            fileReader = new FileReader(ruta);
            BufferedReader b = new BufferedReader(fileReader);
            while ((linea = b.readLine()) != null) {
                String[] split = linea.split("=");
                switch (split[0]) {
                    case "files" -> {
                        String[] vFiles = split[1].split(" ");
                        files.addAll(Arrays.asList(vFiles));
                    }
                    case "seeds" -> {
                        String[] vSeeds = split[1].split(" ");
                        for (String vSeed : vSeeds) {
                            seeds.add(Long.parseLong(vSeed));
                        }
                    }
                    case "evaluations" -> {
                        evaluations = Integer.parseInt(split[1]);
                    }
                    case "probCr" -> {
                        probCr = Double.parseDouble(split[1]);
                    }
                    case "probMut" -> {
                        probMut = Double.parseDouble(split[1]);
                    }
                    case "porcMPX" -> {
                        porcMPX = Double.parseDouble(split[1]);
                    }
                    case "tamPob" -> {
                        tamPob = Integer.parseInt(split[1]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public ArrayList<Long> getSeeds() {
        return seeds;
    }

    public int getEvaluations() {
        return evaluations;
    }

    public double getProbCr() {
        return probCr;
    }

    public int getTamPob() {
        return tamPob;
    }

    public double getProbMut() {
        return probMut;
    }

    public double getPorcMPX() {
        return porcMPX;
    }
}
