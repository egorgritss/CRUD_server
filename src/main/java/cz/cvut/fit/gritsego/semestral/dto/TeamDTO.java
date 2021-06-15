package cz.cvut.fit.gritsego.semestral.dto;


import cz.cvut.fit.gritsego.semestral.entity.Player;
import cz.cvut.fit.gritsego.semestral.entity.Sponsor;

import java.util.List;

public class TeamDTO {


    private int id;
    private String name;
    private Integer rating;
    private List<Integer> playersIds;
    private List<Integer> sponsorsIds;

    public TeamDTO(int id, String name, Integer rating, List<Integer> playersIds, List<Integer> sponsorsIds) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.playersIds = playersIds;
        this.sponsorsIds = sponsorsIds;
    }

    public int getId() {
        return id;
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
