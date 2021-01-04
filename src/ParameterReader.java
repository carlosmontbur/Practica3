import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class ParameterReader {
    private final ArrayList<String> files;
    private final ArrayList<Long> seeds;
    private int evaluations = 0;
    private int tamPob = 0;
    private double q0 = 0;
    private double p = 0;
    private double fi = 0;
    private double delta = 0;
    private final ArrayList<Double> greedy;

    ParameterReader(String ruta) {
        files = new ArrayList<>();
        seeds = new ArrayList<>();
        greedy = new ArrayList<>();

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
                    case "tamPob" -> {
                        tamPob = Integer.parseInt(split[1]);
                    }
                    case "q0" -> {
                        q0 = Double.parseDouble(split[1]);
                    }
                    case "p" -> {
                        p = Double.parseDouble(split[1]);
                    }
                    case "fi" -> {
                        fi = Double.parseDouble(split[1]);
                    }
                    case "delta" -> {
                        delta = Double.parseDouble(split[1]);
                    }
                    case "greedy" -> {
                        String[] vGreedy = split[1].split(" ");
                        for (String vGreedyFile : vGreedy) {
                            greedy.add(Double.parseDouble(vGreedyFile));
                        }
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

    public int getTamPob() {
        return tamPob;
    }

    public double getQ0() {
        return q0;
    }

    public double getP() {
        return p;
    }

    public double getFi() {
        return fi;
    }

    public double getDelta() {
        return delta;
    }

    public ArrayList<Double> getGreedy() {
        return greedy;
    }
}
