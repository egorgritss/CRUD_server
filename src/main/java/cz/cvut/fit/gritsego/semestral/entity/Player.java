package cz.cvut.fit.gritsego.semestral.entity;

import com.sun.istack.NotNull;
import net.bytebuddy.implementation.bind.annotation.Default;

import javax.persistence.*;

@Entity
public class Player {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private Integer age;

    @NotNull
    @Column(unique = true)
    private String nickname;

    private Integer rating;

    @ManyToOne(targetEntity = Team.class)
    @JoinColumn(name = "team_id")
    private Team team;

    @NotNull
    private boolean banned = false;

    public Player() {
    }


    public Player(String firstname, String lastname, Integer age, String nickname, Integer rating, boolean banned, Team team) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.nickname = nickname;
        this.rating = rating;
        this.banned = banned;
        this.team = team;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
