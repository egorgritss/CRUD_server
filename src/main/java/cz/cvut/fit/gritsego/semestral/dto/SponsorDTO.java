package cz.cvut.fit.gritsego.semestral.dto;

public class SponsorDTO {

    private final int id;
    private final String name;

    public SponsorDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
