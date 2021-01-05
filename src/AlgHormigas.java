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
        Hormiga bestActualHormiga = new Hormiga(m, n);

        int cont = 0;
        long iterationStartTime;
        long iterationEndTime = 0;
        RandomGenerator rnd = new RandomGenerator();
        rnd.setSeed(seed);

        while (cont < iterations && iterationEndTime < 600000) {
            iterationStartTime = System.currentTimeMillis();

            coloniaHormigas.cargaAleatoria(rnd, n);

            for (int comp = 1; comp < m; comp++) {
                for (int h = 0; h < tamPob; h++) {
                    double[] distancias = new double[n];

                    // REFACTOR DISTANCIAS A COLONIA TODO
                    for (int i = 0; i < n; i++) {
                        double d = 0;
                        Hormiga hormiga = coloniaHormigas.getHormiga(h);
                        if (!hormiga.isIndexMarked(i)) {
                            for (int k = 0; k < comp; k++) {
                                d += data[i][coloniaHormigas.getHormiga(h).getVectorIndex(k)];
                            }
                            distancias[i] = d;
                        }
                    }
                    // HACER EL CALCULO TODO
                    double mayorDistancia = Double.MIN_VALUE;
                    double menorDistancia = Double.MAX_VALUE;
                    for (int i = 0; i < n; i++) {
                        if (!coloniaHormigas.getHormiga(h).isIndexMarked(i)) {
                            if (distancias[i] < menorDistancia) {
                                menorDistancia = distancias[i];
                            }
                            if (distancias[i] > mayorDistancia) {
                                mayorDistancia = distancias[i];
                            }
                        }
                    }

                    for (int i = 0; i < n; i++) {
                        if (!coloniaHormigas.getHormiga(h).isIndexMarked(i) &&
                                (distancias[i] >= (menorDistancia + (delta * (mayorDistancia - menorDistancia))))
                        ) {
                            LRC.add(i);
                        }
                    }

                    double[] ferxHeu = new double[LRC.size()];

                    for (int i = 0; i < LRC.size(); i++) {
                        ferxHeu[i] = 0;
                        for (int j = 0; j < comp; j++) {
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
                    double numerador = 0 ;
                    double[] prob = new double[LRC.size()];
                    for (int i=0; i<LRC.size(); i++) {
                        prob[i] = 0;
                    }
                    double q = rnd.getRandomFloat(0, (float) 1.01);

                    if (q0 <= q ) {
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
                    horm.setVectorIndex(comp, elegido);
                    if (elegido == -1) {
                        System.out.println(2);
                    }
                    horm.setMarked(elegido, true);


                    LRC.clear();
                }

                for (int h = 0; h < tamPob; h++) {
                    Hormiga hrm = coloniaHormigas.getHormiga(h);
                    for (int i = 0; i < comp; i++) {
                        double value = ((1 - fi) * feromona.getElement(hrm.getVectorIndex(i), hrm.getVectorIndex(comp)) + (fi * greedy));
                        feromona.setElement(hrm.getVectorIndex(i), hrm.getVectorIndex(comp), value);
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
            iterationEndTime = System.currentTimeMillis() - iterationStartTime;
            System.out.println("Iteracion " + cont + " Coste: " + bestGlobalHormiga.getCoste() + " Tiempo: " +
                    iterationEndTime);
        }


        globalEndTime = System.currentTimeMillis() - globalStartTime;
    }
}
