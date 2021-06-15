package cz.cvut.fit.gritsego.semestral.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Column(unique = true)
    private String name;

    private Integer rating;

    @OneToMany(targetEntity = Player.class)
    @JoinColumn(name = "team_id")
    private List<Player> players;

    @ManyToMany
    @JoinTable(name = "team_sposor",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "sponsor_id")
    )
    private List<Sponsor> sponsors;

    public Team() {
    }

    public Team(String name, Integer rating, List<Player> players, List<Sponsor> sponsors) {
        this.name = name;
        this.rating = rating;
        this.players = players;
        this.sponsors = sponsors;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Sponsor> getSponsors() {
        return sponsors;
    }

    public void setSponsors(List<Sponsor> sponsors) {
        this.sponsors = sponsors;
    }

    public void addPlayer(Player player) { this.players.add(player); }

    public void removePlayer(Player player) { this.players.remove(player); }

    public void addSponsor(Sponsor sponsor) { this.sponsors.add(sponsor); }

    public void removeSponsor(Sponsor sponsor) { this.sponsors.remove(sponsor); }

}
