import java.util.Scanner;

public class Pell {
    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            // ваш код здесь
            long next = n;
            for(long prelast = 0, last = 1, temp; n > 1; --n){
                next = last * 2 + prelast;
                temp = last;
                last = next;
                prelast = temp;
            }
            System.out.print(next);
        }
    }
}