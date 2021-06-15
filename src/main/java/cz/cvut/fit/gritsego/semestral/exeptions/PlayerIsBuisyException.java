package cz.cvut.fit.gritsego.semestral.exeptions;

public class PlayerIsBuisyException extends Exception{
    private final String message;
    public PlayerIsBuisyException(String errorMessage){
        super(errorMessage);
        this.message = errorMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
