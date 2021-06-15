package cz.cvut.fit.gritsego.semestral.rest_api;

import cz.cvut.fit.gritsego.semestral.dto.TeamCreateDTO;
import cz.cvut.fit.gritsego.semestral.dto.TeamDTO;
import cz.cvut.fit.gritsego.semestral.entity.Team;
import cz.cvut.fit.gritsego.semestral.exeptions.PlayerIsBuisyException;
import cz.cvut.fit.gritsego.semestral.exeptions.PlayerNotFoundException;
import cz.cvut.fit.gritsego.semestral.exeptions.PlayersAmountBoundExceedException;
import cz.cvut.fit.gritsego.semestral.exeptions.TeamNotFoundException;
import cz.cvut.fit.gritsego.semestral.service.TeamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.hateoas.Link;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService TeamService) {
        this.teamService = TeamService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TeamDTO> create(@RequestBody TeamCreateDTO team) throws Exception {
        try {
            TeamDTO created = teamService.create(team);
            return  ResponseEntity
                    .created(Link.of("http://localhost:8080/api/v1/teams/" + created.getId()).toUri())
                    .body(created);
        }catch (InstanceAlreadyExistsException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

    }

    @GetMapping("/{id}")
    public TeamDTO readOne(@PathVariable int id) {
        Optional<TeamDTO> optionalTeam = teamService.findByIdAsDTO(id);
        return optionalTeam.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)) ;

    }

    @GetMapping
    public Page<TeamDTO> readAll(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        return teamService.readAll(PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TeamDTO> update(@PathVariable int id,@RequestBody TeamCreateDTO newTeam) throws Exception {
        try {
            TeamDTO updated = teamService.update(id, newTeam);
            return  ResponseEntity
                    .created(Link.of("http://localhost:8080/api/v1/teams/" + updated.getId()).toUri())
                    .body(updated);
        } catch (InstanceAlreadyExistsException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }catch (TeamNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) throws Exception {
        try {
            teamService.delete(id);
        }catch (TeamNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/{id}/add_players")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TeamDTO> addPlayers(@PathVariable int id,@RequestBody List<Integer> playersIds) throws Exception {
        try {
            TeamDTO updated = teamService.addPlayers(id, playersIds);
            return  ResponseEntity
                    .created(Link.of("http://localhost:8080/api/v1/teams/" + updated.getId()).toUri())
                    .body(updated);
        } catch (TeamNotFoundException | PlayerNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (PlayerIsBuisyException | PlayersAmountBoundExceedException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
