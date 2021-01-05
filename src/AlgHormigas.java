import java.util.ArrayList;

public class AlgHormigas {


    public static void run(double[][] data, int n, int m, long seed, long iterations, double greedy, int alfa, int beta, int tamPob,
                           double q0, double p, double fi, double delta) {
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
        double[] distancias;
        MayorMenorDistancia mayorMenor = new MayorMenorDistancia();
        double[] HxF; // Heuristicas x Feromonas
        double sumHxF;
        double argumentMax;
        int posArgumentMax;

        while (cont < iterations && iterationEndTime < 600000) {
            iterationStartTime = System.currentTimeMillis();

            coloniaHormigas.cargaAleatoria(rnd, n);

            for (int component = 1; component < m; component++) {
                for (Hormiga hormiga : coloniaHormigas.getHormigas()) {
                    distancias = new double[n];

                    calculateDistances(data, n, component, distancias, hormiga);

                    calculateMayorMenorDistancia(n, mayorMenor, distancias, hormiga);

                    calculateLRC(n, delta, LRC, mayorMenor, distancias, hormiga);

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

                updateFeromona(greedy, fi, feromona, coloniaHormigas, component);
            }

            bestActualHormiga = coloniaHormigas.getBestHormiga(data, m);

            if (bestActualHormiga.isBetterThan(bestGlobalHormiga)) {
                bestGlobalHormiga = bestActualHormiga;
            }

            updateFeromonaWithBestActualHormiga(n, m, p, feromona, bestActualHormiga);

            coloniaHormigas = new ColoniaHormigas(m, tamPob, n);
            ++cont;
            iterationEndTime += System.currentTimeMillis() - iterationStartTime;
            System.out.println("Iteracion " + cont + " Coste: " + bestGlobalHormiga.getCoste() + " Tiempo: " + iterationEndTime);
        }

        globalEndTime = System.currentTimeMillis() - globalStartTime;
    }

    private static void updateFeromonaWithBestActualHormiga(int n, int m, double p, MatrizDoubles feromona, Hormiga bestActualHormiga) {
        double value;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (bestActualHormiga.getVectorIndex(i) != j) {
                    feromona.addToElement(bestActualHormiga.getVectorIndex(i), j, p * bestActualHormiga.getCoste());
                    feromona.addToElement(j, bestActualHormiga.getVectorIndex(i), p * bestActualHormiga.getCoste());
                }
            }
        }

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

    private static void updateFeromona(double greedy, double fi, MatrizDoubles feromona, ColoniaHormigas coloniaHormigas, int component) {
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

    private static void calculateLRC(int n, double delta, ArrayList<Integer> LRC, MayorMenorDistancia mayorMenor, double[] distancias, Hormiga hormiga) {
        for (int i = 0; i < n; i++) {
            if (!hormiga.isIndexMarked(i) && (distancias[i] >= (mayorMenor.getMenor() +
                    (delta * (mayorMenor.getMayor() - mayorMenor.getMenor()))))
            ) {
                LRC.add(i);
            }
        }
    }

    private static void calculateMayorMenorDistancia(int n, MayorMenorDistancia mayorMenor, double[] distancias, Hormiga hormiga) {
        mayorMenor.reset();
        for (int i = 0; i < n; i++) {
            if (!hormiga.isIndexMarked(i)) {
                if (distancias[i] < mayorMenor.getMenor()) {
                    mayorMenor.setMenor(distancias[i]);
                }
                if (distancias[i] > mayorMenor.getMayor()) {
                    mayorMenor.setMayor(distancias[i]);
                }
            }
        }
    }

    private static void calculateDistances(double[][] data, int n, int component, double[] distancias, Hormiga hormiga) {
        for (int i = 0; i < n; i++) {
            distancias[i] = 0;
            if (!hormiga.isIndexMarked(i)) {
                for (int k = 0; k < component; k++) {
                    distancias[i] += data[i][hormiga.getVectorIndex(k)];
                }
            }
        }
    }
}
