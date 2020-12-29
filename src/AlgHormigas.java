import java.util.ArrayList;

public class AlgHormigas {


    void run(double[][] data, int n, int m, long seed, long iterations, double greedy, int alfa, int beta, int tamPob,
             double q0, double p, double fi, double delta) {
        long globalStartTime = System.currentTimeMillis();
        long globalEndTime;
        MatrizDoubles feromona = new MatrizDoubles(n, greedy);
        MatrizDoubles heuristica = new MatrizDoubles(n, data);
        ArrayList<Integer> LRC = new ArrayList<>();
        ColoniaHormigas coloniaHormigas = new ColoniaHormigas(m, tamPob);

        Hormiga bestGlobalHormiga = new Hormiga(m);
        Hormiga bestActualHormiga = new Hormiga(m);

        int cont = 0;
        long iterationStartTime;
        long iterationEndTime = 0;
        RandomGenerator rnd = new RandomGenerator();
        rnd.setSeed(seed);

        while (cont < iterations && iterationEndTime < 600000) {
            iterationStartTime = System.currentTimeMillis();

            coloniaHormigas.cargaAleatoria(rnd, n);







            iterationEndTime = System.currentTimeMillis() - iterationStartTime;
        }


        globalEndTime = System.currentTimeMillis() - globalStartTime;
    }
}
