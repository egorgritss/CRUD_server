package cz.cvut.fit.gritsego.semestral.entity;

import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Sponsor {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Column(unique = true)
    private String name;

    public Sponsor() {
    }

    public Sponsor(String name) {
        this.name = name;
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
}
