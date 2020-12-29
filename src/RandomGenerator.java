public class RandomGenerator {

    public RandomGenerator() {
    }

    long seed = 0L;

    final long MASK = 2147483647;
    final long PRIME = 65539;
    final float SCALE = (float) 0.4656612875e-9;

    public void setSeed(long seed) {
        this.seed = seed;
    }

    double getRandom() {
        return ((this.seed = ((this.seed * PRIME) & MASK)) * SCALE);
    }

    public int getRandomInt(int low, int high) {
        return (int) (low + (high - (low) + 1) * getRandom());
    }

    public float getRandomFloat(float low, float high) {
        return (float) (low + (high - (low)) * getRandom());
    }

}
