import java.util.Scanner;

public class Stars {
    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            String template = scanner.next();
            // ваш код здесь
            for(; n > 0; --n)
                for (int i = 0; i < m; ++i)
                    if (i + 1 != m) System.out.printf("%s ", template); else System.out.println(template);
        }
    }
}