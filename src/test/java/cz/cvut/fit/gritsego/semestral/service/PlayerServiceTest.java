package cz.cvut.fit.gritsego.semestral.service;

import cz.cvut.fit.gritsego.semestral.dto.PlayerCreateDTO;
import cz.cvut.fit.gritsego.semestral.dto.PlayerDTO;
import cz.cvut.fit.gritsego.semestral.dto.PlayerCreateDTO;
import cz.cvut.fit.gritsego.semestral.dto.PlayerDTO;
import cz.cvut.fit.gritsego.semestral.entity.Player;
import cz.cvut.fit.gritsego.semestral.entity.Player;
import cz.cvut.fit.gritsego.semestral.entity.Sponsor;
import cz.cvut.fit.gritsego.semestral.entity.Team;
import cz.cvut.fit.gritsego.semestral.repository.PlayerRepository;
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
public class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;

    @MockBean
    private PlayerRepository playerRepositoryMock;
    @MockBean
    private TeamRepository teamRepository;

    @Test
    void create() throws Exception {
        Team teamToReturn = new Team("Name", 1, new ArrayList<Player>(), new ArrayList<Sponsor>());
        Player playerToReturn = new Player("Egor", "Gritsenko", 19, "GriTss", 1, false, teamToReturn);
        ReflectionTestUtils.setField(playerToReturn, "id", 1);
        PlayerCreateDTO playerCreateDTO = new PlayerCreateDTO("Egor", "Gritsenko", 19, "GriTss", 1, false, 0);
        BDDMockito.given(playerRepositoryMock.save(any(Player.class))).willReturn(playerToReturn);
        //BDDMockito.given(playerRepositoryMock.findById(1)).willReturn(Optional.of(playerToReturn));
        //BDDMockito.given(playerRepositoryMock.findByNickname(playerToReturn.getNickname())).willReturn(Optional.of(playerToReturn));
        BDDMockito.given(teamRepository.findById(0)).willReturn(Optional.of(teamToReturn));

        PlayerDTO returnedPlayerDTO = playerService.create(playerCreateDTO);

        PlayerDTO expectedPlayerDTO = new PlayerDTO(1 ,"Egor", "Gritsenko", 19, "GriTss", 1, false, 0);
        Assertions.assertEquals(expectedPlayerDTO.getFirstName(), returnedPlayerDTO.getFirstName());
        Assertions.assertEquals(expectedPlayerDTO.getLastName(), returnedPlayerDTO.getLastName());
        Assertions.assertEquals(expectedPlayerDTO.getAge(), returnedPlayerDTO.getAge());
        Assertions.assertEquals(expectedPlayerDTO.getNickname(), returnedPlayerDTO.getNickname());
        Assertions.assertEquals(expectedPlayerDTO.getRating(), returnedPlayerDTO.getRating());
        Assertions.assertEquals(expectedPlayerDTO.isBanned(), returnedPlayerDTO.isBanned());

        ArgumentCaptor<Player> argumentCaptor = ArgumentCaptor.forClass(Player.class);
        Mockito.verify(playerRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());

        Player playerProvidedToSave = argumentCaptor.getValue();
        Assertions.assertEquals("Egor", playerProvidedToSave.getFirstName());
        Assertions.assertEquals("Gritsenko", playerProvidedToSave.getLastName());
        Assertions.assertEquals(19, playerProvidedToSave.getAge());
        Assertions.assertEquals("GriTss", playerProvidedToSave.getNickname());
        Assertions.assertEquals(1, playerProvidedToSave.getRating());
        Assertions.assertFalse(playerProvidedToSave.isBanned());
    }

    @Test
    void update() throws Exception {
        Team teamToReturn = new Team("Name", 1, new ArrayList<Player>(), new ArrayList<Sponsor>());
        Player playerToReturn = new Player("Egor", "Gritsenko", 19, "GriTss", 1, false, teamToReturn);
        ReflectionTestUtils.setField(playerToReturn, "id", 1);
        PlayerCreateDTO playerCreateDTO = new PlayerCreateDTO("Tomas", "Gritsenko", 19, "GriTss", 1, false, 0);

        BDDMockito.given(playerRepositoryMock.findById(1)).willReturn(Optional.of(playerToReturn));

        PlayerDTO returnedPlayerDTO = playerService.update(1, playerCreateDTO);

        PlayerDTO expectedPlayerDTO = new PlayerDTO(1 ,"Tomas", "Gritsenko", 19, "GriTss", 1, false, 0);
        Assertions.assertEquals(expectedPlayerDTO.getFirstName(), returnedPlayerDTO.getFirstName());
        Assertions.assertEquals(expectedPlayerDTO.getLastName(), returnedPlayerDTO.getLastName());
        Assertions.assertEquals(expectedPlayerDTO.getAge(), returnedPlayerDTO.getAge());
        Assertions.assertEquals(expectedPlayerDTO.getNickname(), returnedPlayerDTO.getNickname());
        Assertions.assertEquals(expectedPlayerDTO.getRating(), returnedPlayerDTO.getRating());
        Assertions.assertEquals(expectedPlayerDTO.isBanned(), returnedPlayerDTO.isBanned());
    }

    @Test
    void delete() throws Exception {
        Player playerToReturn = new Player("Egor", "Gritsenko", 19, "GriTss", 1, false, null);
        ReflectionTestUtils.setField(playerToReturn, "id", 1);

        BDDMockito.given(playerRepositoryMock.findById(1)).willReturn(Optional.of(playerToReturn));
        BDDMockito.given(playerRepositoryMock.findByNickname("GriTss")).willReturn(Optional.of(playerToReturn));

        playerService.deleteById(playerToReturn.getId());
        BDDMockito.verify(playerRepositoryMock, Mockito.atLeastOnce()).deleteById(playerToReturn.getId());

        playerService.deleteByNickname(playerToReturn.getNickname());
        BDDMockito.verify(playerRepositoryMock, Mockito.atLeastOnce()).deleteByNickname(playerToReturn.getNickname());
    }
}
