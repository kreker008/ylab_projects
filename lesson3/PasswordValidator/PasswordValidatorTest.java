public class PasswordValidatorTest {
    public static void main(String[] args) {
        System.out.println(PasswordValidator.validator("ABC", "123", "123"));
        System.out.println(PasswordValidator.validator("ABCjenviie43erhbejrvjenrvjwevevenrvjeerververre", "123", "123"));
        System.out.println(PasswordValidator.validator("ABC", "1234", "123"));
        System.out.println(PasswordValidator.validator("ABC!", "123", "123"));
        System.out.println(PasswordValidator.validator("ABC", "123!", "123"));

    }
}
