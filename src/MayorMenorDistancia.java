public class MayorMenorDistancia {

    double mayor;

    double menor;

    public MayorMenorDistancia() {
        this.reset();
    }

    public double getMayor() {
        return mayor;
    }

    public void setMayor(double mayor) {
        this.mayor = mayor;
    }

    public double getMenor() {
        return menor;
    }

    public void setMenor(double menor) {
        this.menor = menor;
    }

   public void reset() {
        this.mayor = Double.MIN_VALUE;
        this.menor = Double.MAX_VALUE;
   }
}
