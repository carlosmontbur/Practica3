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
            hormiga.ramdonInit(rnd.getRandomInt(0, n-1));
        }
    }

}
