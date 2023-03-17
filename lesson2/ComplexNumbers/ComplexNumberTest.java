public class ComplexNumberTest {
    public static void main(String[] args) {
        ComplexNumber num1 = new ComplexNumber(42, 13);
        ComplexNumber num2 = new ComplexNumber(-65, 3);
        ComplexNumber num3 = new ComplexNumber(-2, 5);

        System.out.print(num1 + " + " + num2 + " = ");
        System.out.println(num1.sum(num2).toString());

        System.out.print(num2 + " * " + num3 + " = ");
        System.out.println(num2.multiplication(num3).toString());

        System.out.print(num1 + " - " + num2 + " = ");
        System.out.println(num1.subtraction(num2).toString());

        System.out.print(num1 + " modul is ");
        System.out.println(num1.modul());
    }
}
