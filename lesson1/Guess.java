import java.util.Random;
import java.util.Scanner;

public class Guess {
    public static void main(String[] args) throws Exception {
        int number = new Random().nextInt(100); // здесь загадывается число от 1 до 99
        int maxAttempts = 10; // здесь задается количество попыток
        System.out.println("Я загадал число. У тебя " + maxAttempts + " попыток угадать.");
        // ваш код здесь
        try (Scanner scanner = new Scanner(System.in)) {
            int user_try = 0;
            while(maxAttempts > user_try){
                int user_num = scanner.nextInt();
                ++user_try;
                if(number == user_num){
                    System.out.printf("Ты угадал c %d попытки!", user_try);
                    return;
                } else if (number > user_num)
                    System.out.printf("Мое число больше! Осталось %d попыток\n", maxAttempts - user_try);
                else
                    System.out.printf("Мое число меньше! Осталось %d попыток\n", maxAttempts - user_try);
            }
            System.out.print("Ты не угадал");
        }
    }
}