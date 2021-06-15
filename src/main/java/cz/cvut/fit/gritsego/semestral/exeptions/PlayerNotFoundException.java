package cz.cvut.fit.gritsego.semestral.exeptions;

public class PlayerNotFoundException extends Exception{
    private final String message;
    public PlayerNotFoundException(String errorMessage){
        super(errorMessage);
        this.message = errorMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
