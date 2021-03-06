import java.util.ArrayList;

public class AlgHormigas {

    private static final CustomLogger logger = new CustomLogger();

    public static void run(double[][] data, int n, int m, long seed, long iterations, double greedy, int alfa, int beta, int tamPob,
                           double q0, double p, double fi, double delta, String filePath) {
        long globalStartTime = System.currentTimeMillis();
        long globalEndTime;
        MatrizDoubles feromona = new MatrizDoubles(n, greedy);
        MatrizDoubles heuristica = new MatrizDoubles(n, data);
        ArrayList<Integer> LRC = new ArrayList<>();
        ColoniaHormigas coloniaHormigas = new ColoniaHormigas(m, tamPob, n);

        Hormiga bestGlobalHormiga = new Hormiga(m, n);
        Hormiga bestActualHormiga;

        int cont = 0;
        long iterationStartTime;
        long iterationEndTime = 0;
        RandomGenerator rnd = new RandomGenerator();
        rnd.setSeed(seed);
        double[] aportes;
        MayorMenorAporte mayorMenor = new MayorMenorAporte();
        double[] HxF; // Heuristicas x Feromonas
        double sumHxF;
        double argumentMax;
        int posArgumentMax;

        while (cont < iterations && iterationEndTime < 600000) {
            iterationStartTime = System.currentTimeMillis();

            coloniaHormigas.cargaAleatoria(rnd, n);

            for (int component = 1; component < m; component++) {
                for (Hormiga hormiga : coloniaHormigas.getHormigas()) {
                    aportes = new double[n];

                    calculateAportes(data, n, component, aportes, hormiga);

                    calculateMayorMenorAporte(n, mayorMenor, aportes, hormiga);

                    calculateLRC(n, delta, LRC, mayorMenor, aportes, hormiga);

                    HxF = calculateHxF(alfa, beta, feromona, heuristica, LRC, component);

                    sumHxF = 0;
                    argumentMax = 0;
                    posArgumentMax = 0;
                    for (int i = 0; i < LRC.size(); i++) {
                        sumHxF += HxF[i];
                        if (HxF[i] > argumentMax) {
                            argumentMax = HxF[i];
                            posArgumentMax = LRC.get(i);
                        }
                    }

                    calculateChosenAndSetToHormiga(q0, LRC, rnd, HxF, sumHxF, posArgumentMax, component, hormiga);

                    LRC.clear();
                }

                updateFeromonaLocal(greedy, fi, feromona, coloniaHormigas, component);
            }

            bestActualHormiga = coloniaHormigas.getBestHormiga(data, m);

            if (bestActualHormiga.isBetterThan(bestGlobalHormiga)) {
                bestGlobalHormiga = bestActualHormiga;
            }

            updateFeromonaGlobal(n, m, p, feromona, bestActualHormiga);
            applyDemon(n, p, feromona);

            coloniaHormigas = new ColoniaHormigas(m, tamPob, n);
            ++cont;
            iterationEndTime += System.currentTimeMillis() - iterationStartTime;
            iterationLog(filePath, bestGlobalHormiga, cont, iterationEndTime);
        }

        globalEndTime = System.currentTimeMillis() - globalStartTime;

        finalLog(filePath, globalEndTime, bestGlobalHormiga, cont);

    }

    private static void updateFeromonaGlobal(int n, int m, double p, MatrizDoubles feromona, Hormiga bestActualHormiga) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (bestActualHormiga.getVectorIndex(i) != j) {
                    feromona.addToElement(bestActualHormiga.getVectorIndex(i), j, p * bestActualHormiga.getCoste());
                    feromona.addToElement(j, bestActualHormiga.getVectorIndex(i), p * bestActualHormiga.getCoste());
                }
            }
        }
    }

    private static void applyDemon(int n, double p, MatrizDoubles feromona) {
        double value;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    value = (1 - p) * feromona.getElement(i, j);
                    feromona.setElement(i, j, value);
                    feromona.setElement(j, i, value);
                }
            }
        }
    }

    private static void updateFeromonaLocal(double greedy, double fi, MatrizDoubles feromona, ColoniaHormigas coloniaHormigas, int component) {
        int x, y;
        for (Hormiga hormiga : coloniaHormigas.getHormigas()) {
            for (int i = 0; i < component; i++) {
                x = hormiga.getVectorIndex(i);
                y = hormiga.getVectorIndex(component);
                feromona.setElement(x, y, ((1 - fi) * feromona.getElement(x, y) + (fi * greedy)));
            }
        }
    }

    private static void calculateChosenAndSetToHormiga(double q0, ArrayList<Integer> LRC, RandomGenerator rnd, double[] hxF, double sumHxF, int posArgumentMax, int component, Hormiga hormiga) {
        int chosen = transition(q0, LRC, rnd, hxF, sumHxF, posArgumentMax);
        hormiga.setVectorIndex(component, chosen);
        hormiga.setMarked(chosen, true);
    }

    private static int transition(double q0, ArrayList<Integer> LRC, RandomGenerator rnd, double[] hxF, double sumHxF, int posArgumentMax) {
        double probAM;
        int chosen;
        double randomFloat = rnd.getRandomFloat(0, 1);
        double sumProbLRC = 0;
        double[] probLRC;
        chosen = 0;
        probLRC = createArrayProbLRC(LRC);

        probAM = rnd.getRandomFloat(0, (float) 1.01);
        if (probAM < q0) {
            for (int i = 0; i < LRC.size(); i++) {
                probLRC[i] = hxF[i] / sumHxF;
            }

            for (int i = 0; i < LRC.size(); i++) {
                sumProbLRC += probLRC[i];
                if (randomFloat <= sumProbLRC) {
                    chosen = LRC.get(i);
                    break;
                }
            }
        } else {
            chosen = posArgumentMax;
        }
        return chosen;
    }

    private static double[] createArrayProbLRC(ArrayList<Integer> LRC) {
        double[] probLRC;
        probLRC = new double[LRC.size()];
        for (int i = 0; i < LRC.size(); i++) {
            probLRC[i] = 0;
        }
        return probLRC;
    }

    private static double[] calculateHxF(int alfa, int beta, MatrizDoubles feromona, MatrizDoubles heuristica, ArrayList<Integer> LRC, int component) {
        double[] HxF;
        HxF = new double[LRC.size()];
        for (int i = 0; i < LRC.size(); i++) {
            HxF[i] = 0;
            for (int j = 0; j < component; j++) {
                HxF[i] += Math.pow(heuristica.getElement(j, LRC.get(i)), beta) *
                        Math.pow(feromona.getElement(j, LRC.get(i)), alfa);
            }
        }
        return HxF;
    }

    private static void calculateLRC(int n, double delta, ArrayList<Integer> LRC, MayorMenorAporte mayorMenor, double[] aportes, Hormiga hormiga) {
        for (int i = 0; i < n; i++) {
            if (!hormiga.isIndexMarked(i) && (aportes[i] >= (mayorMenor.getMenor() +
                    (delta * (mayorMenor.getMayor() - mayorMenor.getMenor()))))
            ) {
                LRC.add(i);
            }
        }
    }

    private static void calculateMayorMenorAporte(int n, MayorMenorAporte mayorMenor, double[] aportes, Hormiga hormiga) {
        mayorMenor.reset();
        for (int i = 0; i < n; i++) {
            if (!hormiga.isIndexMarked(i)) {
                if (aportes[i] < mayorMenor.getMenor()) {
                    mayorMenor.setMenor(aportes[i]);
                }
                if (aportes[i] > mayorMenor.getMayor()) {
                    mayorMenor.setMayor(aportes[i]);
                }
            }
        }
    }

    private static void calculateAportes(double[][] data, int n, int component, double[] aportes, Hormiga hormiga) {
        for (int i = 0; i < n; i++) {
            aportes[i] = 0;
            if (!hormiga.isIndexMarked(i)) {
                for (int j = 0; j < component; j++) {
                    aportes[i] += data[i][hormiga.getVectorIndex(j)];
                }
            }
        }
    }

    private static void finalLog(String filePath, long globalEndTime, Hormiga bestGlobalHormiga, int cont) {
        logger.addStringToLog(filePath, "----------------------------------------------------LOG FINAL--------------------------------------------\n" +
                "Numero iteraciones realizadas " + cont + ", Tiempo transcurrido: " + globalEndTime + "(ms), Mejor Coste: " + bestGlobalHormiga.getCoste() + "\n" +
                "-----------------------------------------------LOG FINAL-------------------------------------------------\n");
    }

    private static void iterationLog(String filePath, Hormiga bestGlobalHormiga, int cont, long iterationEndTime) {
        logger.addStringToLog(filePath, "Iteracion " + cont + ", Tiempo transcurrido: " + iterationEndTime + "(ms), Coste: " + bestGlobalHormiga.getCoste());
    }
}
