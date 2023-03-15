public class StatsAccumulatorTest {
    public static void main(String[] args) {
        StatsAccumulator s = new StatsAccumulatorImpl(); // как то создается

        System.out.println(s.getMax());
        System.out.println(s.getMin());


        s.add(1);
        s.add(2);

        System.out.println("1.5 == " + s.getAvg()); // 1.5 - среднее арифметическое элементов

        s.add(0);

        System.out.println("0 == " +s.getMin()); // 0 - минимальное из переданных значений

        s.add(3);
        s.add(8);

        System.out.println("8 == " +s.getMax()); // 8 - максимальный из переданных
        System.out.println("5 == " +s.getCount()); // 5 - количество переданных элементов
    }
}
