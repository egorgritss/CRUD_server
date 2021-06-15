package cz.cvut.fit.gritsego.semestral.service;

import cz.cvut.fit.gritsego.semestral.dto.TeamCreateDTO;
import cz.cvut.fit.gritsego.semestral.dto.TeamDTO;
import cz.cvut.fit.gritsego.semestral.entity.Player;
import cz.cvut.fit.gritsego.semestral.entity.Sponsor;
import cz.cvut.fit.gritsego.semestral.entity.Team;
import cz.cvut.fit.gritsego.semestral.repository.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class TeamServiceTest {
    
    @Autowired
    private TeamService teamService;
    
    @MockBean
    private TeamRepository teamRepositoryMock;

    @Test
    void create() throws Exception {
        Team teamToReturn = new Team("Name", 1, new ArrayList<Player>(), new ArrayList<Sponsor>());
        ReflectionTestUtils.setField(teamToReturn, "id", 1);
        TeamCreateDTO teamCreateDTO = new TeamCreateDTO("Name", 1, new ArrayList<Integer>(), new ArrayList<Integer>());

        BDDMockito.given(teamRepositoryMock.save(any(Team.class))).willReturn(teamToReturn);

        TeamDTO returnedTeamDTO = teamService.create(teamCreateDTO);

        TeamDTO expectedTeamDTO = new TeamDTO(1,"Name", 1, new ArrayList<Integer>(), new ArrayList<Integer>());
        Assertions.assertEquals(expectedTeamDTO.getName(), returnedTeamDTO.getName());
        Assertions.assertEquals(expectedTeamDTO.getRating(), returnedTeamDTO.getRating());
        Assertions.assertEquals(0,returnedTeamDTO.getSponsorsIds().size());
        Assertions.assertEquals(0,returnedTeamDTO.getPlayersIds().size());
        Assertions.assertEquals(expectedTeamDTO.getRating(), returnedTeamDTO.getRating());

        ArgumentCaptor<Team> argumentCaptor = ArgumentCaptor.forClass(Team.class);
        Mockito.verify(teamRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());

        Team teamProvidedToSave = argumentCaptor.getValue();
        Assertions.assertEquals("Name", teamProvidedToSave.getName());
        Assertions.assertEquals(1, teamProvidedToSave.getRating());
    }

    @Test
    void update() throws Exception {
        Team teamToReturn = new Team("Name", 1, new ArrayList<Player>(), new ArrayList<Sponsor>());
        ReflectionTestUtils.setField(teamToReturn, "id", 1);
        TeamCreateDTO teamCreateDTO = new TeamCreateDTO("NewName", 2, new ArrayList<Integer>(), new ArrayList<Integer>());

        BDDMockito.given(teamRepositoryMock.findById(1)).willReturn(Optional.of(teamToReturn));

        TeamDTO returnedTeamDTO = teamService.update(1, teamCreateDTO);

        TeamDTO expectedTeamDTO = new TeamDTO(1,"NewName", 2, new ArrayList<Integer>(), new ArrayList<Integer>());
        Assertions.assertEquals(expectedTeamDTO.getName(), returnedTeamDTO.getName());
        Assertions.assertEquals(expectedTeamDTO.getRating(), returnedTeamDTO.getRating());
        Assertions.assertEquals(0,returnedTeamDTO.getSponsorsIds().size());
        Assertions.assertEquals(0,returnedTeamDTO.getPlayersIds().size());
        Assertions.assertEquals(expectedTeamDTO.getRating(), returnedTeamDTO.getRating());
    }

    @Test
    void delete() throws Exception{
        Team teamToReturn = new Team("Name", 1, new ArrayList<Player>(), new ArrayList<Sponsor>());
        ReflectionTestUtils.setField(teamToReturn, "id", 1);

        BDDMockito.given(teamRepositoryMock.findById(1)).willReturn(Optional.of(teamToReturn));

        teamService.delete(teamToReturn.getId());

        BDDMockito.verify(teamRepositoryMock, Mockito.atLeastOnce()).deleteById(teamToReturn.getId());
    }
}
