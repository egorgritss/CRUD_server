package cz.cvut.fit.gritsego.semestral.dto;


import cz.cvut.fit.gritsego.semestral.entity.Player;
import cz.cvut.fit.gritsego.semestral.entity.Sponsor;

import java.util.ArrayList;
import java.util.List;

public class TeamCreateDTO {

    private String name;
    private Integer rating;
    private List<Integer> playersIds;
    private List<Integer> sponsorsIds;

    public TeamCreateDTO(String name, Integer rating, List<Integer> playersIds, List<Integer> sponsorsIds) {

        this.name = name;
        this.rating = rating;
        this.playersIds = playersIds;
        this.sponsorsIds = sponsorsIds;
    }

    public String getName() {
        return name;
    }

    public Integer getRating() {
        return rating;
    }

    public List<Integer> getPlayersIds() {
        return playersIds;
    }

    public List<Integer> getSponsorsIds() {
        return sponsorsIds;
    }
}
