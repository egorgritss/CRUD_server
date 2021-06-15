package cz.cvut.fit.gritsego.semestral.exeptions;

public class SponsorNotFoundException extends Exception {
    public SponsorNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
