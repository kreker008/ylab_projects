public class RateLimitedPrinterTest {
    public static void main(String[] args) throws InterruptedException {
        RateLimitedPrinter rateLimitedPrinter = new RateLimitedPrinter(5 * 1000);

        rateLimitedPrinter.print("first hello,");
        rateLimitedPrinter.print("hello");
        rateLimitedPrinter.print("hello");
        rateLimitedPrinter.print("hello");
        rateLimitedPrinter.print("hello");

        Thread.sleep(5 * 1000);

        rateLimitedPrinter.print("now its worked...");

        rateLimitedPrinter.print("hello");
        rateLimitedPrinter.print("hello");
        rateLimitedPrinter.print("hello");
        rateLimitedPrinter.print("hello");
        rateLimitedPrinter.print("hello");

        Thread.sleep(5 * 1000);

        rateLimitedPrinter.print(" and now too");

        for (int i = 0; i < 1_000_000_000; i++) {
            rateLimitedPrinter.print(String.valueOf(i));
        }
    }
}
