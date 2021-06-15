package cz.cvut.fit.gritsego.semestral.service;

import cz.cvut.fit.gritsego.semestral.dto.PlayerCreateDTO;
import cz.cvut.fit.gritsego.semestral.dto.PlayerDTO;
import cz.cvut.fit.gritsego.semestral.entity.Player;
import cz.cvut.fit.gritsego.semestral.entity.Team;
import cz.cvut.fit.gritsego.semestral.exeptions.PlayerNotFoundException;
import cz.cvut.fit.gritsego.semestral.exeptions.TeamNotFoundException;
import cz.cvut.fit.gritsego.semestral.repository.PlayerRepository;
import cz.cvut.fit.gritsego.semestral.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService implements CrudService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    
    @Autowired
    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    public List<PlayerDTO> findAll() {
        return playerRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Page<PlayerDTO> readAll(Pageable pageable) {
        List<PlayerDTO> players = findAll();
        return new PageImpl<PlayerDTO>(players, pageable, players.size());
    } 

    public List<Player> findByIds(List<Integer> ids) {
        return playerRepository.findAllById(ids);
    }

    public Optional<Player> findById(int id) {
        return playerRepository.findById(id);
    }

    public Optional<PlayerDTO> findByIdAsDTO(int id) {
        return toDTO(findById(id));
    }

    public Optional<Player> findByNickname(String nickname) {
        return playerRepository.findByNickname(nickname);
    }

    public Optional<PlayerDTO> findByNickNameAsDTO(String nickname) {
        return toDTO(findByNickname(nickname));
    }

    public Optional<Player> findByRating(int id) {
        return playerRepository.findByRating(id);
    }

    public Player save(Player player) {return playerRepository.save(player);}

    public PlayerDTO create(PlayerCreateDTO playerDTO) throws Exception{
        if (playerRepository.findByNickname(playerDTO.getNickname()).isPresent())
            throw new InstanceAlreadyExistsException("Player with nickname " + playerDTO.getNickname() + " already exists");

        Optional<Player> optionalPlayer = playerRepository.findByRating(playerDTO.getRating());
        if (optionalPlayer.isPresent() && optionalPlayer.get().getRating() != 0)
            throw new InstanceAlreadyExistsException("Player with rating " + playerDTO.getRating() + " already exists");

        Optional<Team> optionalTeam = teamRepository.findById(playerDTO.getTeamId());

        if(optionalTeam.isEmpty())
            throw new TeamNotFoundException("Team with id " + playerDTO.getTeamId() + " not found.");
        return toDTO(playerRepository.save(
                new Player(
                        playerDTO.getFirstName(),
                        playerDTO.getLastName(),
                        playerDTO.getAge(),
                        playerDTO.getNickname(),
                        playerDTO.getRating(),
                        playerDTO.isBanned(),
                        optionalTeam.get())
                )
        );
    }

    @Transactional
    public PlayerDTO update(int id, PlayerCreateDTO playerDTO) throws Exception {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        Optional<Player> optionalPlayerByNickname = playerRepository.findByNickname(playerDTO.getNickname());
        Optional<Player> optionalPlayerByRating = playerRepository.findByRating(playerDTO.getRating());
        if (optionalPlayer.isEmpty())
            throw new PlayerNotFoundException("Player with id " + id + " not found.");
        if (optionalPlayerByNickname.isPresent() && optionalPlayerByNickname.get().getId()!=id)
            throw new InstanceAlreadyExistsException("Nickname " + '"' + playerDTO.getNickname() + '"' + " already token." );
        if (optionalPlayerByRating.isPresent() && optionalPlayerByRating.get().getId()!=id)
            throw new InstanceAlreadyExistsException("Player with rating " + '"' + playerDTO.getRating() + '"' + " already exists." );
        Player player = optionalPlayer.get();
        player.setFirstName(playerDTO.getFirstName());
        player.setLastName(playerDTO.getLastName());
        player.setAge(playerDTO.getAge());
        player.setNickname(playerDTO.getNickname());
        player.setBanned(playerDTO.isBanned());
        player.setRating(playerDTO.getRating());

        return toDTO(player);
    }

    @Transactional
    public void deleteById(int id) throws Exception{
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        if (optionalPlayer.isEmpty())
            throw new PlayerNotFoundException("Player with id " + id + " not found.");
        playerRepository.deleteById(id);
    }

    @Transactional
    public void deleteByNickname(String nickname) throws Exception{
        Optional<Player> optionalPlayer = playerRepository.findByNickname(nickname);
        if (optionalPlayer.isEmpty())
            throw new PlayerNotFoundException("Player with Nickname " + nickname + " not found.");
        playerRepository.deleteByNickname(nickname);
    }
    
    private PlayerDTO toDTO(Player player) {
        Integer teamId = null;
        if(player.getTeam() != null){
            teamId = player.getTeam().getId();

        }

        return new PlayerDTO(player.getId(),
                player.getFirstName(),
                player.getLastName(),
                player.getAge(),
                player.getNickname(),
                player.getRating(),
                player.isBanned(),
                teamId
                );
    }

    private Optional<PlayerDTO> toDTO(Optional<Player> player) {
        if (player.isEmpty())
            return Optional.empty();
        return Optional.of(toDTO(player.get()));
    }


}
