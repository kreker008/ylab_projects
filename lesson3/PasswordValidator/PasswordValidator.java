public class PasswordValidator {
    private static boolean isOnlyLatinNumOrUnderscore(String string){
        char symbol;
        for(int i = 0; i < string.length(); ++i){
            symbol = string.charAt(i);
            if(!((symbol >= 'a' && symbol <= 'z') ||
                (symbol >= 'A' && symbol <= 'Z') ||
                (symbol >= '0' && symbol <= '9') ||
                symbol == '_')) {
                return false;
            }
        }
        return true;
    }
    public static boolean validator(String login, String password, String confirmPassword){
        if(!isOnlyLatinNumOrUnderscore(login)){
            try {
                throw new WrongLoginException();
            }catch(WrongLoginException e){
                System.out.println("Логин содержит недопустимые символы");
            }
            return false;
        }

        if(login.length() >= 20){
            try {
                throw new WrongLoginException();
            }catch(WrongLoginException e){
                System.out.println("Логин слишком длинный");
            }
            return false;
        }

        if(!isOnlyLatinNumOrUnderscore(password)){
            try {
                throw new WrongPasswordException();
            }catch(WrongPasswordException e){
                System.out.println("Пароль содержит недопустимые символы");
            }
            return false;
        }

        if(password.length() >= 20){
            try {
                throw new WrongPasswordException();
            }catch(WrongPasswordException e){
                System.out.println("Пароль слишком длинный");
            }
            return false;
        }

        if(!password.equals(confirmPassword)){
            try {
                throw new WrongPasswordException();
            }catch(WrongPasswordException e){
                System.out.println("Пароль и подтверждение не совпадают");
            }
            return false;
        }

        return true;
    }
}
