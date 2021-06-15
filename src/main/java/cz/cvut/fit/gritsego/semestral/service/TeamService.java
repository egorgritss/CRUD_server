package cz.cvut.fit.gritsego.semestral.service;

import cz.cvut.fit.gritsego.semestral.dto.TeamCreateDTO;
import cz.cvut.fit.gritsego.semestral.dto.TeamDTO;
import cz.cvut.fit.gritsego.semestral.entity.Player;
import cz.cvut.fit.gritsego.semestral.entity.Sponsor;
import cz.cvut.fit.gritsego.semestral.entity.Team;
import cz.cvut.fit.gritsego.semestral.exeptions.*;
import cz.cvut.fit.gritsego.semestral.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService implements CrudService {

    private final TeamRepository teamRepository;
    private final PlayerService playerService;
    private final SponsorService sponsorService;

    @Autowired
    public TeamService(TeamRepository teamRepository, PlayerService playerService, SponsorService sponsorService) {
        this.teamRepository = teamRepository;
        this.playerService = playerService;
        this.sponsorService = sponsorService;
    }


    public List<TeamDTO> findAll() {
        return teamRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Page<TeamDTO> readAll(Pageable pageable) {
        List<TeamDTO> teams = findAll();
        return new PageImpl<TeamDTO>(teams, pageable, teams.size());
    }

    public Optional<Team> findById(int id) {
        return teamRepository.findById(id);
    }

    public Optional<TeamDTO> findByIdAsDTO(int id) {
        return toDTO(findById(id));
    }

    public Optional<TeamDTO> findByName(String name) {
        return toDTO(teamRepository.findByName(name));
    }

    public Optional<TeamDTO> findByRating(int rating) {
        return toDTO(teamRepository.findByRating(rating));
    }

    @Transactional
    public TeamDTO create(TeamCreateDTO teamDTO) throws Exception {
        if (teamRepository.findByName(teamDTO.getName()).isPresent())
            throw new InstanceAlreadyExistsException("Team with name " + teamDTO.getName() + " already exists");
        Optional<Team> optionalTeam = teamRepository.findByRating(teamDTO.getRating());

        if (optionalTeam.isPresent() && optionalTeam.get().getRating() != 0)
            throw new InstanceAlreadyExistsException("Team with rating " + teamDTO.getRating() + " already exists");
        List<Sponsor> sponsors = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        if (teamDTO.getSponsorsIds() != null) {
            sponsors = sponsorService.findByIds(teamDTO.getSponsorsIds());
            if (sponsors.size() != teamDTO.getSponsorsIds().size())
                throw new SponsorNotFoundException("Some sponsors not found.");
        }
        if (teamDTO.getPlayersIds() != null) {
            players = playerService.findByIds(teamDTO.getPlayersIds());
            if (players.size() != teamDTO.getPlayersIds().size())
                throw new PlayerNotFoundException("Some players not found.");
        }

        Team team = new Team(teamDTO.getName(), teamDTO.getRating(), players, sponsors);

        return toDTO(teamRepository.save(team));
    }

    @Transactional
    public TeamDTO update(int id, TeamCreateDTO teamDTO) throws Exception {
        Optional<Team> optionalTeam = findById(id);
        if (optionalTeam.isEmpty())
            throw new TeamNotFoundException("Team with id " + id + " not found.");
        Team team = optionalTeam.get();

        Optional<TeamDTO> optionalTeamByName = findByName(teamDTO.getName());
        if (optionalTeamByName.isPresent() && optionalTeamByName.get().getId() != id) {
            throw new InstanceAlreadyExistsException("Team with name " + teamDTO.getName() + " already token.");
        }
        Optional<TeamDTO> optionalTeamByRating = findByRating(teamDTO.getRating());
        if (optionalTeamByRating.isPresent() && optionalTeamByRating.get().getId() != id) {
            throw new InstanceAlreadyExistsException("Team with rating " + teamDTO.getName() + " already exists.");
        }
        team.setName(teamDTO.getName());
        team.setRating(teamDTO.getRating());
        List<Sponsor> sponsors = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        if (teamDTO.getSponsorsIds() != null && teamDTO.getSponsorsIds().size() != 0) {
            sponsors = sponsorService.findByIds(teamDTO.getSponsorsIds());
            if (sponsors.size() != teamDTO.getSponsorsIds().size())
                throw new SponsorNotFoundException("Some sponsors not found.");
        } else {
            team.setSponsors(sponsors);
        }
        if (teamDTO.getPlayersIds() != null && teamDTO.getPlayersIds().size() != 0) {
            players = playerService.findByIds(teamDTO.getPlayersIds());
            if (players.size() != teamDTO.getPlayersIds().size())
                throw new PlayerNotFoundException("Some players not found.");
            addPlayers(id, teamDTO.getPlayersIds());
        } else {
            team.setPlayers(players);
        }
        return toDTO(team);
    }

    @Transactional
    public void delete(int id) throws Exception {
        Optional<Team> optionalSponsor = teamRepository.findById(id);
        if (optionalSponsor.isEmpty())
            throw new TeamNotFoundException("Team with id " + id + " not found.");
        teamRepository.deleteById(id);
    }

    @Transactional
    public TeamDTO addPlayers(Integer teamId, List<Integer> playersIds) throws Exception {
        Optional<Team> optionalTeam = findById(teamId);
        if (optionalTeam.isEmpty()) {
            throw new TeamNotFoundException("Team with id " + teamId + " not found");
        }
        Team team = optionalTeam.get();
        for (int id : playersIds) {
            Optional<Player> optionalPlayer = playerService.findById(id);
            if (optionalPlayer.isEmpty()) {
                throw new PlayerNotFoundException("Player with id " + id + " not found");
            }
            Player player = optionalPlayer.get();
            if (player.getTeam() != null && player.getTeam().getId() != teamId) {
                throw new PlayerIsBuisyException("Player with id " + id + " is already playing for another team.");
            }
            if (team.getPlayers().size() == 5) {
                throw new PlayersAmountBoundExceedException("Max amount of players is 5.");
            }
            if (!team.getPlayers().contains(player)) {
                player.setTeam(team);
                team.addPlayer(player);
            }
        }
        return toDTO(teamRepository.save(team));
    }




    private TeamDTO toDTO(Team team) {
        return new TeamDTO(
                team.getId(),
                team.getName(),
                team.getRating(),
                team.getPlayers().stream().map(Player::getId).collect(Collectors.toList()),
                team.getSponsors().stream().map(Sponsor::getId).collect(Collectors.toList())
        );
    }

    private Optional<TeamDTO> toDTO(Optional<Team> team) {
        if (team.isEmpty())
            return Optional.empty();
        return Optional.of(toDTO(team.get()));
    }


}
