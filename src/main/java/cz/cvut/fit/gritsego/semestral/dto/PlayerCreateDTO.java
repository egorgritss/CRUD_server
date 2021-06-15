package cz.cvut.fit.gritsego.semestral.dto;


public class PlayerCreateDTO {
    private String firstname;
    private String lastname;
    private Integer age;
    private String nickname;
    private Integer rating;
    private boolean banned;

    private int teamId;

    public PlayerCreateDTO(String firstname, String lastname, Integer age, String nickname, Integer rating, boolean banned, Integer teamId) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.nickname = nickname;
        this.rating = rating;
        this.banned = banned;
        this.teamId = teamId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public Integer getAge() {
        return age;
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getRating() {
        return rating;
    }

    public boolean isBanned() {
        return banned;
    }
}
