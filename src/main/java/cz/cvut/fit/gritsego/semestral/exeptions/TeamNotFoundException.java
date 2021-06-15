package cz.cvut.fit.gritsego.semestral.exeptions;

public class TeamNotFoundException extends Exception {
    public TeamNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
