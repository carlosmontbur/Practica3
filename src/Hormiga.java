
public class Hormiga {

    int[] vector;
    double coste = 0;
    boolean[] marcados;

    public Hormiga(int m) {
        vector = new int[m];
        marcados = new boolean[m];

        for (int i = 0; i < m; i++) {
            vector[i] = 0;
            marcados[i] = false;
        }
    }

    public int[] getVector() {
        return vector;
    }

    public void setVector(int[] vector) {
        this.vector = vector;
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
}
