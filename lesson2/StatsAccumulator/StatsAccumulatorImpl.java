public class StatsAccumulatorImpl implements StatsAccumulator {
    private int min;
    private int max;
    private int count;
    private long sum;
    private Double avg;

    @Override
    public void add(int value) {
        if (value < min) {
            min = value;
        } else if (value > max) {
            max = value;
        }
        sum += value;
        ++count;
        avg = (sum / (double) count);
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Double getAvg() {
        return avg;
    }
}
