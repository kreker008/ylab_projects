public class SequencesTest {
    private static final SequenceGenerator sequenceGenerator = new SequencesImpl();

    public static void main(String[] args) {
        test(10);
        test(1);
        test(2);
        test(-1);
    }

    private static void test(int n) {
//      A. 2, 4, 6, 8, 10...
        System.out.printf("n is %d\nA. 2, 4, 6, 8, 10...\nFunction out:\n", n);
        sequenceGenerator.a(n);
        System.out.println();

//      B. 1, 3, 5, 7, 9...
        System.out.printf("n is %d\nB. 1, 3, 5, 7, 9...\nFunction out:\n", n);
        sequenceGenerator.b(n);
        System.out.println();

//      C. 1, 4, 9, 16, 25...
        System.out.printf("n is %d\nC. 1, 4, 9, 16, 25...\nFunction out:\n", n);
        sequenceGenerator.c(n);
        System.out.println();

//      D. 1, 8, 27, 64, 125...
        System.out.printf("n is %d\nD. 1, 8, 27, 64, 125...\nFunction out:\n", n);
        sequenceGenerator.d(n);
        System.out.println();

//      E. 1, -1, 1, -1, 1, -1...
        System.out.printf("n is %d\nE. 1, -1, 1, -1, 1, -1...\nFunction out:\n", n);
        sequenceGenerator.e(n);
        System.out.println();

//      F. 1, -2, 3, -4, 5, -6...
        System.out.printf("n is %d\nF. 1, -2, 3, -4, 5, -6...\nFunction out:\n", n);
        sequenceGenerator.f(n);
        System.out.println();

//      G. 1, -4, 9, -16, 25....
        System.out.printf("n is %d\nG. 1, -4, 9, -16, 25....\nFunction out:\n", n);
        sequenceGenerator.g(n);
        System.out.println();

//      H. 1, 0, 2, 0, 3, 0, 4....
        System.out.printf("n is %d\nH. 1, 0, 2, 0, 3, 0, 4....\nFunction out:\n", n);
        sequenceGenerator.h(n);
        System.out.println();

//      I. 1, 2, 6, 24, 120, 720...
        System.out.printf("n is %d\nI. 1, 2, 6, 24, 120, 720...\nFunction out:\n", n);
        sequenceGenerator.i(n);
        System.out.println();

//      J. 1, 1, 2, 3, 5, 8, 13, 21…
        System.out.printf("n is %d\nJ. 1, 1, 2, 3, 5, 8, 13, 21…\nFunction out:\n", n);
        sequenceGenerator.j(n);
        System.out.println();
    }
}
