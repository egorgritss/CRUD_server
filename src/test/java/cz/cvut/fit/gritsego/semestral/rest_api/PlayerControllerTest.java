package cz.cvut.fit.gritsego.semestral.rest_api;

import cz.cvut.fit.gritsego.semestral.dto.PlayerCreateDTO;
import cz.cvut.fit.gritsego.semestral.dto.PlayerDTO;
import cz.cvut.fit.gritsego.semestral.entity.Player;
import cz.cvut.fit.gritsego.semestral.exeptions.PlayerNotFoundException;
import cz.cvut.fit.gritsego.semestral.service.PlayerService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@SpringBootTest
@AutoConfigureMockMvc
class PlayerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @Test
    void readOne() throws Exception {
        PlayerDTO player = new PlayerDTO(1,"Egor", "Gritsenko", 19, "GriTss", 1, false, 1);
        BDDMockito.given(playerService.findByIdAsDTO(player.getId())).willReturn(Optional.of(player));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/players/{id}", player.getId())
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(player.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(player.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", CoreMatchers.is(player.getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nickname", CoreMatchers.is(player.getNickname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating", CoreMatchers.is(player.getRating())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.banned", CoreMatchers.is(player.isBanned())));
        Mockito.verify(playerService, Mockito.atLeastOnce()).findByIdAsDTO(player.getId());
    }

    @Test
    void readAll() throws Exception {
        Pageable pageable = PageRequest.of(0,10);
        ArrayList<PlayerDTO> players = new ArrayList<>();
        players.add(new PlayerDTO(1,"Egor", "Gritsenko", 19, "GriTss", 1, false, 1));
        players.add(new PlayerDTO(2,"Egor", "Gritsenko", 19, "GriTss1", 2, false, 1));
        Page<PlayerDTO> pagePlayers = new PageImpl<>(players, pageable, players.size());
        BDDMockito.given(playerService.readAll(pageable)).willReturn(pagePlayers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/players"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].firstName", CoreMatchers.is(players.get(0).getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].lastName", CoreMatchers.is(players.get(0).getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].nickname", CoreMatchers.is(players.get(0).getNickname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].age", CoreMatchers.is(players.get(0).getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].rating", CoreMatchers.is(players.get(0).getRating())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].banned", CoreMatchers.is(players.get(0).isBanned())))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].firstName", CoreMatchers.is(players.get(1).getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].lastName", CoreMatchers.is(players.get(1).getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].nickname", CoreMatchers.is(players.get(1).getNickname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].age", CoreMatchers.is(players.get(1).getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].rating", CoreMatchers.is(players.get(1).getRating())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].banned", CoreMatchers.is(players.get(1).isBanned())));

        Mockito.verify(playerService, Mockito.atLeastOnce()).readAll(pageable);
    }

    @Test
    void postNew() throws Exception {
        PlayerDTO player = new PlayerDTO(1, "Egor", "Gritsenko", 19, "GriTss", 1, false, 1);
        //PlayerCreateDTO playerCreateDTO = new PlayerCreateDTO("Egor", "Gritsenko", 19, "GriTss", 1, false);
        BDDMockito.given(playerService.create(any(PlayerCreateDTO.class))).willReturn(player);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"Egor\", \"lastname\":\"Gritsenko\", \"age\": 19, \"nickname\":\"GriTss\",\"rating\": 1,\"banned\": false, \"teamId\":1}")
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(player.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(player.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", CoreMatchers.is(player.getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nickname", CoreMatchers.is(player.getNickname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating", CoreMatchers.is(player.getRating())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.banned", CoreMatchers.is(player.isBanned())));

        BDDMockito.given(playerService.create(any(PlayerCreateDTO.class))).willThrow(InstanceAlreadyExistsException.class);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"Egor\", \"lastname\":\"Gritsenko\", \"age\": 19, \"nickname\":\"GriTss\",\"rating\": 1,\"banned\": false, \"teamId\":1}")
        ).andExpect(MockMvcResultMatchers.status().isConflict());
        Mockito.verify(playerService, Mockito.atLeast(2)).create(any(PlayerCreateDTO.class));

    }

    @Test
    void update() throws Exception {
        PlayerDTO updatedPlayer = new PlayerDTO(1,"Egor", "Gritsenko", 19, "GriTss", 1, false, 1);
        BDDMockito.given(playerService.update(any(Integer.class), any(PlayerCreateDTO.class))).willReturn(updatedPlayer);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/players/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"Egor\", \"lastname\":\"Gritsenko\", \"age\": 19, \"nickname\":\"GriTss\",\"rating\": 1,\"banned\": false, \"teamId\":1}")
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updatedPlayer.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(updatedPlayer.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", CoreMatchers.is(updatedPlayer.getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nickname", CoreMatchers.is(updatedPlayer.getNickname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating", CoreMatchers.is(updatedPlayer.getRating())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.banned", CoreMatchers.is(updatedPlayer.isBanned())));

        BDDMockito.given(playerService.update(any(Integer.class), any(PlayerCreateDTO.class))).willThrow(InstanceAlreadyExistsException.class);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/players/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"Egor\", \"lastname\":\"Gritsenko\", \"age\": 19, \"nickname\":\"GriTss\",\"rating\": 1,\"banned\": false, \"teamId\":1}")
        ).andExpect(MockMvcResultMatchers.status().isConflict());

        BDDMockito.given(playerService.update(any(Integer.class), any(PlayerCreateDTO.class))).willThrow(PlayerNotFoundException.class);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/players/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"Egor\", \"lastname\":\"Gritsenko\", \"age\": 19, \"nickname\":\"GriTss\",\"rating\": 1,\"banned\": false, \"teamId\":1}")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(playerService, Mockito.atLeast(3)).update(any(Integer.class), any(PlayerCreateDTO.class));
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/players/{id}", 1)
        ).andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(playerService, Mockito.atLeastOnce()).deleteById(1);
    }
}