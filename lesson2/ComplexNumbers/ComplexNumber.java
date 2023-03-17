public class ComplexNumber {
    double real;
    double imaginary;

    ComplexNumber(double real) {
        this.real = real;
    }

    ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber sum(ComplexNumber complexNumber) {
        return new ComplexNumber(this.real + complexNumber.real, this.imaginary + complexNumber.imaginary);
    }

    public ComplexNumber subtraction(ComplexNumber complexNumber) {
        return new ComplexNumber(this.real - complexNumber.real, this.imaginary - complexNumber.imaginary);
    }

    public ComplexNumber multiplication(ComplexNumber complexNumber) {
        double x1 = this.real * complexNumber.real;
        double x2 = this.imaginary * complexNumber.imaginary;

        double x3 = this.real * complexNumber.imaginary;
        double x4 = this.imaginary * complexNumber.real;

        return new ComplexNumber(x1 - x2, x3 + x4);
    }

    public double modul() {
        return Math.sqrt(this.real * this.real + this.imaginary * this.imaginary);
    }

    @Override
    public String toString() {
        return "(" + real + " + (" + imaginary + ")i)";
    }
}
//3. Сложение
//4. Вычитание
//5. Умножение
//6. Операция получения модуля
//7. преобразование в строку (toString)
//(арифметические действия должны создавать новый экземпляр класса)
//}
