public class SnilsValidatorImpl implements SnilsValidator {
    @Override
    public boolean validate(String snils) {
        if(snils.length() != 11){
            return false;
        }
        for(int i = 0; i < 11; ++i){
            if(!Character.isDigit(snils.charAt(i))){
                return false;
            }
        }

        int sum = 0;
        for (var i = 0; i < 9; i++) {
            sum += Character.getNumericValue(snils.charAt(i)) * (9 - i);
        }
        int checkDigit = 0;
        if (sum < 100) {
            checkDigit = sum;
        } else if (sum > 101) {
            checkDigit = sum % 101;
            if (checkDigit == 100) {
                checkDigit = 0;
            }
        }
        return (Character.getNumericValue(snils.charAt(9)) * 10 + Character.getNumericValue(snils.charAt(10))) == checkDigit;
    }
}
