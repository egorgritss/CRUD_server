package cz.cvut.fit.gritsego.semestral.rest_api;

import cz.cvut.fit.gritsego.semestral.dto.TeamCreateDTO;
import cz.cvut.fit.gritsego.semestral.dto.TeamDTO;
import cz.cvut.fit.gritsego.semestral.entity.Player;
import cz.cvut.fit.gritsego.semestral.entity.Sponsor;
import cz.cvut.fit.gritsego.semestral.entity.Team;
import cz.cvut.fit.gritsego.semestral.exeptions.TeamNotFoundException;
import cz.cvut.fit.gritsego.semestral.service.TeamService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@SpringBootTest
@AutoConfigureMockMvc
class TeamControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TeamService teamService;

    @Test
    void readOne() throws Exception {
        TeamDTO team = new TeamDTO(1, "Name", 1, new ArrayList<Integer>(), new ArrayList<Integer>());
        BDDMockito.given(teamService.findByIdAsDTO(team.getId())).willReturn(Optional.of(team));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/teams/{id}", team.getId())
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(team.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating", CoreMatchers.is(team.getRating())));
        Mockito.verify(teamService, Mockito.atLeastOnce()).findByIdAsDTO(team.getId());
    }

    @Test
    void readAll() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        ArrayList<TeamDTO> teams = new ArrayList<>();
        teams.add(new TeamDTO(1,"Name", 1, new ArrayList<Integer>(), new ArrayList<Integer>()));
        teams.add(new TeamDTO(2,"Name1", 2, new ArrayList<Integer>(), new ArrayList<Integer>()));
        Page<TeamDTO> pageTeams = new PageImpl<>(teams, pageable, teams.size());
        BDDMockito.given(teamService.readAll(pageable)).willReturn(pageTeams);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teams"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].name", CoreMatchers.is(teams.get(0).getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].rating", CoreMatchers.is(teams.get(0).getRating())))


                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].name", CoreMatchers.is(teams.get(1).getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].rating", CoreMatchers.is(teams.get(1).getRating())));

        Mockito.verify(teamService, Mockito.atLeastOnce()).readAll(pageable);
    }

    @Test
    void postNew() throws Exception {
        TeamDTO team = new TeamDTO(1, "Name", 1, new ArrayList<Integer>(), new ArrayList<Integer>());
        BDDMockito.given(teamService.create(any(TeamCreateDTO.class))).willReturn(team);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Name\", \"rating\": 1 }")
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(team.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating", CoreMatchers.is(team.getRating())));

        BDDMockito.given(teamService.create(any(TeamCreateDTO.class))).willThrow(InstanceAlreadyExistsException.class);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Name\", \"rating\": 1 }")
        ).andExpect(MockMvcResultMatchers.status().isConflict());
        Mockito.verify(teamService, Mockito.atLeast(2)).create(any(TeamCreateDTO.class));

    }

    @Test
    void update() throws Exception {
        TeamDTO updatedTeam = new TeamDTO(1, "Name", 1, new ArrayList<Integer>(), new ArrayList<Integer>());
        BDDMockito.given(teamService.update(any(Integer.class), any(TeamCreateDTO.class))).willReturn(updatedTeam);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/teams/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Name\", \"rating\": 1 }")
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(updatedTeam.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating", CoreMatchers.is(updatedTeam.getRating())));

        BDDMockito.given(teamService.update(any(Integer.class), any(TeamCreateDTO.class))).willThrow(InstanceAlreadyExistsException.class);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/teams/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Name\", \"rating\": 1 }")
        ).andExpect(MockMvcResultMatchers.status().isConflict());

        BDDMockito.given(teamService.update(any(Integer.class), any(TeamCreateDTO.class))).willThrow(TeamNotFoundException.class);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/teams/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Name\", \"rating\": 1 }")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(teamService, Mockito.atLeast(3)).update(any(Integer.class), any(TeamCreateDTO.class));
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/teams/{id}", 1)
        ).andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(teamService, Mockito.atLeastOnce()).delete(1);
    }

    @Test
    public void addPlayers() throws Exception {
        ArrayList<Integer> playersIds = new ArrayList<>();
        playersIds.add(1);
        playersIds.add(2);
        TeamDTO teamToReturn = new TeamDTO(1,  "Name", 1, playersIds, new ArrayList<Integer>());
        BDDMockito.given(teamService.addPlayers(1, playersIds)).willReturn(teamToReturn);

        mockMvc.perform(
                MockMvcRequestBuilders
                .post("/api/v1/teams/{id}/add_players", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1,2]")
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(teamService, Mockito.atLeastOnce()).addPlayers(1, playersIds);
    }

}