package cz.cvut.fit.gritsego.semestral.exeptions;

public class PlayersAmountBoundExceedException extends Exception{
    private final String message;
    public PlayersAmountBoundExceedException(String errorMessage){
        super(errorMessage);
        this.message = errorMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
