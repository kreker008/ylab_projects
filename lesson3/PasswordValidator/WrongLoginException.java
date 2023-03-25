public class WrongLoginException extends Exception{
    public WrongLoginException(){};
    public WrongLoginException(String stringErr){
        super(stringErr);
    }
}
