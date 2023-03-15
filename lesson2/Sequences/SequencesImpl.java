public class SequencesImpl implements SequenceGenerator {
    //    A. 2, 4, 6, 8, 10...
    @Override
    public void a(int n) {
        if (!isValidArg(n)) {
            return;
        }
        long num = 0;

        for (int i = 0; i < n; ++i) {
            num += 2;
            System.out.println(num);
        }
    }

    //    B. 1, 3, 5, 7, 9...
    @Override
    public void b(int n) {
        if (!isValidArg(n)) {
            return;
        }
        long num = 1;

        for (int i = 0; i < n; ++i) {
            System.out.println(num);
            num += 2;
        }
    }

    //    C. 1, 4, 9, 16, 25...
    @Override
    public void c(int n) {
        if (!isValidArg(n)) {
            return;
        }
        long num;

        for (int i = 1; i <= n; ++i) {
            num = (long) i * i;
            System.out.println(num);
        }
    }

    //    D. 1, 8, 27, 64, 125...
    @Override
    public void d(int n) {
        if (!isValidArg(n)) {
            return;
        }
        long num;

        for (int i = 1; i <= n; ++i) {
            num = (long) i * i * i;
            System.out.println(num);
        }
    }

    //    E. 1, -1, 1, -1, 1, -1...
    @Override
    public void e(int n) {
        if (!isValidArg(n)) {
            return;
        }
        long num = 1;

        for (int i = 0; i < n; ++i) {
            System.out.println(num);
            num *= -1;
        }
    }

    //    F. 1, -2, 3, -4, 5, -6...
    @Override
    public void f(int n) {
        if (!isValidArg(n)) {
            return;
        }
        byte flag = 1;

        for (int num = 1; num <= n; ++num) {
            System.out.println(num * flag);
            flag *= -1;
        }
    }

    //    G. 1, -4, 9, -16, 25....
    @Override
    public void g(int n) {
        if (!isValidArg(n)) {
            return;
        }
        long num;
        byte flag = 1;

        for (int i = 1; i <= n; ++i) {
            num = (long) i * i * flag;
            flag *= -1;
            System.out.println(num);
        }
    }

    //    H. 1, 0, 2, 0, 3, 0, 4....
    @Override
    public void h(int n) {
        if (!isValidArg(n)) {
            return;
        }
        int num = 1;

        for (int i = 0; i < n; ++i) {
            if (i % 2 == 0) {
                System.out.println(num);
                ++num;
            } else {
                System.out.println(0);
            }
        }
    }

    //    I. 1, 2, 6, 24, 120, 720...
    @Override
    public void i(int n) {
        if (!isValidArg(n)) {
            return;
        }
        int num = 1;

        for (int i = 1; i <= n; ++i) {
            num *= i;
            System.out.println(num);
        }
    }

    //    J. 1, 1, 2, 3, 5, 8, 13, 21…
    @Override
    public void j(int n) {
        if (!isValidArg(n)) {
            return;
        }
        long next;
        long last = 1;
        long prelast = 1;

        if (n == 1) {
            System.out.println(last);
        } else if (n >= 2) {
            System.out.println(prelast);
            System.out.println(last);
        }

        for (int i = 2; i < n; ++i) {
            next = last + prelast;
            System.out.println(next);
            prelast = last;
            last = next;
        }
    }

    private boolean isValidArg(int n) {
        if (n <= 0 || n > 20) {
//            System.err.println();
            System.out.println("Invalid arg: please use 0 < n <= 10 ");
            return false;
        }
        return true;
    }
}
