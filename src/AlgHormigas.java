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
        MayorMenorDistancia mayorMenor = new MayorMenorDistancia();
        double[] ferxHeu;

        while (cont < iterations && iterationEndTime < 600000) {
            iterationStartTime = System.currentTimeMillis();

            coloniaHormigas.cargaAleatoria(rnd, n);

            for (int component = 1; component < m; component++) {
                for (int h = 0; h < tamPob; h++) {
                    double[] distancias = new double[n];

                    Hormiga hormiga = coloniaHormigas.getHormiga(h);
                    calculateDistances(data, n, component, distancias, hormiga);

                    calculateMayorMenorDistancia(n, mayorMenor, distancias, hormiga);

                    calculateLRC(n, delta, LRC, mayorMenor, distancias, hormiga);

                    ferxHeu = new double[LRC.size()];

                    for (int i = 0; i < LRC.size(); i++) {
                        ferxHeu[i] = 0;
                        for (int j = 0; j < component; j++) {
                            ferxHeu[i] += Math.pow(heuristica.getElement(j, LRC.get(i)), beta) *
                                    Math.pow(feromona.getElement(j, LRC.get(i)), alfa);
                        }
                    }

                    double denominador = 0;
                    double argMax = 0;
                    int posArgMax = -1;
                    for (int i = 0; i < LRC.size(); i++) {
                        denominador += ferxHeu[i];
                        if (ferxHeu[i] > argMax) {
                            argMax = ferxHeu[i];
                            posArgMax = LRC.get(i);
                        }
                    }

                    // FUNCION DE TRANSICION
                    int elegido = -1;
                    double numerador = 0;
                    double[] prob = new double[LRC.size()];
                    for (int i = 0; i < LRC.size(); i++) {
                        prob[i] = 0;
                    }
                    double q = rnd.getRandomFloat(0, (float) 1.01);

                    if (q0 <= q) {
                        elegido = posArgMax;
                    } else {
                        for (int i = 0; i < LRC.size(); i++) {
                            numerador = ferxHeu[i];
                            prob[i] = numerador / denominador;
                        }

                        double uniforme = rnd.getRandomFloat(0, 1);
                        double acumulado = 0;
                        for (int i = 0; i < LRC.size(); i++) {
                            acumulado += prob[i];
                            if (uniforme <= acumulado) {
                                elegido = LRC.get(i);
                                break;
                            }
                        }
                    }
                    Hormiga horm = coloniaHormigas.getHormiga(h);
                    horm.setVectorIndex(component, elegido);
                    horm.setMarked(elegido, true);


                    LRC.clear();
                }

                for (int h = 0; h < tamPob; h++) {
                    Hormiga hrm = coloniaHormigas.getHormiga(h);
                    for (int i = 0; i < component; i++) {
                        double value = ((1 - fi) * feromona.getElement(hrm.getVectorIndex(i), hrm.getVectorIndex(component)) + (fi * greedy));
                        feromona.setElement(hrm.getVectorIndex(i), hrm.getVectorIndex(component), value);
                    }
                }
            }

            bestActualHormiga = coloniaHormigas.getBestHormiga(data, m);

            if (bestActualHormiga.isBetterThan(bestGlobalHormiga)) {
                bestGlobalHormiga = bestActualHormiga;
            }

            // APLICAR EL DEMONIO
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (bestActualHormiga.getVectorIndex(i) != j) {
                        feromona.addToElement(bestActualHormiga.getVectorIndex(i), j, p * bestActualHormiga.getCoste());
                        feromona.addToElement(j, bestActualHormiga.getVectorIndex(i), p * bestActualHormiga.getCoste());
                    }
                }
            }

            //
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        double value = (1 - p) * feromona.getElement(i, j);
                        feromona.setElement(i, j, value);
                        feromona.setElement(j, i, value);
                    }
                }
            }

            coloniaHormigas = new ColoniaHormigas(m, tamPob, n);
            ++cont;
            iterationEndTime += System.currentTimeMillis() - iterationStartTime;
            System.out.println("Iteracion " + cont + " Coste: " + bestGlobalHormiga.getCoste() + " Tiempo: " +
                    iterationEndTime);
        }


        globalEndTime = System.currentTimeMillis() - globalStartTime;
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
