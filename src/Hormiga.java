
public class Hormiga {

    int[] vector;
    double coste = 0;
    boolean[] marcados;

    public Hormiga(int m, int n) {
        vector = new int[m];
        marcados = new boolean[n];

        for (int i = 0; i < m; i++) {
            vector[i] = 0;
        }

        for (int i = 0; i < n; i++) {
            marcados[i] = false;
        }
    }

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        this.coste = coste;
    }


    public void ramdonInit(int value) {
        this.vector[0] = value;
        this.marcados[value] = true;
    }

    public int getVectorIndex(int index) {
        return this.vector[index];
    }

    public void setVectorIndex(int index, int value) {
        this.vector[index] = value;
    }

    public boolean isIndexMarked(int index) {
        return this.marcados[index];
    }

    public void setMarked(int index, boolean value) {
        this.marcados[index] = value;
    }

    public boolean isBetterThan(Hormiga h) {
        return this.coste > h.coste;
    }
}
