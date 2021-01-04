public class ColoniaHormigas {
    Hormiga[] hormigas;

    public ColoniaHormigas(int tam, int tamPob) {
        hormigas = new Hormiga[tamPob];

        for (int i = 0; i < tamPob; i++) {
            hormigas[i] = new Hormiga(tam);
        }
    }

    public void cargaAleatoria(RandomGenerator rnd, int n) {
        for (Hormiga hormiga : hormigas) {
            hormiga.ramdonInit(rnd.getRandomInt(0, n - 1));
        }
    }

    public Hormiga getHormiga(int index) {
        return this.hormigas[index];
    }

    public Hormiga getBestHormiga() {
        int index = -1;
        double maxCost = Double.MIN_VALUE;
        for (int i = 0; i < this.hormigas.length; i++) {
            if (this.hormigas[i].getCoste() > maxCost) {
                maxCost = this.hormigas[i].getCoste();
                index = i;
            }
        }
        return this.hormigas[index];
    }

}
