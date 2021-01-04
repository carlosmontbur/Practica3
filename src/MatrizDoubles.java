public class MatrizDoubles {
    double[][] matriz;

    public MatrizDoubles(int n, double value) {
        matriz = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matriz[i][j] = value;
            }
        }
    }

    public MatrizDoubles(int n, double[][] data) {
        matriz = new double[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(data[i], 0, matriz[i], 0, n);
        }
    }

    public double getElement(int x, int y) {
        return this.matriz[x][y];
    }

    public void setElement(int x, int y, double value) {
        this.matriz[x][y] = value;
    }

    public void addToElement(int x, int y, double value) {
        this.matriz[x][y] += value;
    }

}
