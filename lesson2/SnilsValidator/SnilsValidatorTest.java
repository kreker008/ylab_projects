public class SnilsValidatorTest {
    public static void main(String[] args) {
        System.out.println(new SnilsValidatorImpl().validate("01468870570")); //false
        System.out.println(new SnilsValidatorImpl().validate("90114404441")); //true

        System.out.println(new SnilsValidatorImpl().validate("90114404441111")); //false
        System.out.println(new SnilsValidatorImpl().validate("90114404")); //false
        System.out.println(new SnilsValidatorImpl().validate("9011440444!")); //false

        System.out.println(new SnilsValidatorImpl().validate("63559509125")); //true
        System.out.println(new SnilsValidatorImpl().validate("17529713295")); //true
        System.out.println(new SnilsValidatorImpl().validate("83204190161")); //true
    }
}
