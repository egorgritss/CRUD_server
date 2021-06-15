package cz.cvut.fit.gritsego.semestral.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SponsorCreateDTO {

    private final String name;

    @JsonCreator
    public SponsorCreateDTO( String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
