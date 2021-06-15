package cz.cvut.fit.gritsego.semestral.entity;

import cz.cvut.fit.gritsego.semestral.repository.PlayerRepository;
import cz.cvut.fit.gritsego.semestral.repository.SponsorRepository;
import cz.cvut.fit.gritsego.semestral.repository.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TeamEntityTest {

    @Autowired
    private final TeamRepository teamRepository;

    @Autowired
    private final PlayerRepository playerRepository;

    @Autowired
    private final SponsorRepository sponsorRepository;

    @Autowired
    public TeamEntityTest(TeamRepository teamRepository, PlayerRepository playerRepository, SponsorRepository sponsorRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.sponsorRepository = sponsorRepository;
    }

    @Test
    void addPlayer(){
        Team team = new Team("Name", 1, new ArrayList<Player>(), null );
        Player player = new Player();

        Assertions.assertEquals(0, team.getPlayers().size());
        team.addPlayer(player);
        Assertions.assertEquals(1, team.getPlayers().size());
    }

    @Test
    void removePlayer(){
        Team team = new Team("Name", 1, new ArrayList<Player>(), null );
        Player player = new Player();

        team.addPlayer(player);
        team.removePlayer(player);
        Assertions.assertEquals(0,team.getPlayers().size());
    }

    @Test
    void addSponsor(){
        Team team = new Team("Name", 1, null, new ArrayList<Sponsor>());
        Sponsor sponsor = new Sponsor();

        Assertions.assertEquals(0, team.getSponsors().size());
        team.addSponsor(sponsor);
        Assertions.assertEquals(1, team.getSponsors().size());
    }

    @Test
    void removeSponsor(){
        Team team = new Team("Name", 1, null, new ArrayList<Sponsor>());
        Sponsor sponsor = new Sponsor();

        team.addSponsor(sponsor);
        team.removeSponsor(sponsor);
        Assertions.assertEquals(0, team.getSponsors().size());
    }

}
