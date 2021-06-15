package cz.cvut.fit.gritsego.semestral.rest_api;

import cz.cvut.fit.gritsego.semestral.dto.PlayerCreateDTO;
import cz.cvut.fit.gritsego.semestral.dto.PlayerDTO;
import cz.cvut.fit.gritsego.semestral.entity.Player;
import cz.cvut.fit.gritsego.semestral.exeptions.PlayerNotFoundException;
import cz.cvut.fit.gritsego.semestral.service.PlayerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.hateoas.Link;

import javax.management.InstanceAlreadyExistsException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PlayerDTO> create(@RequestBody PlayerCreateDTO player) throws Exception {
        try {
            PlayerDTO created = playerService.create(player);
            return  ResponseEntity
                    .created(Link.of("http://localhost:8080/api/v1/players/" + created.getId()).toUri())
                    .body(created);
        }catch (InstanceAlreadyExistsException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public PlayerDTO readOne(@PathVariable int id) {
        Optional<PlayerDTO> optionalPlayer = playerService.findByIdAsDTO(id);
        return optionalPlayer.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)) ;

    }

    @GetMapping()
    public Page<PlayerDTO> readAll(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        return playerService.readAll(PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> update(@PathVariable int id, @RequestBody PlayerCreateDTO newPlayer) throws Exception {
        try {
            PlayerDTO updated = playerService.update(id, newPlayer);
            return  ResponseEntity
                    .created(Link.of("http://localhost:8080/api/v1/players/" + updated.getId()).toUri())
                    .body(updated);
        } catch (InstanceAlreadyExistsException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (PlayerNotFoundException e){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) throws Exception {
        try {
            playerService.deleteById(id);
        }catch (PlayerNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }


    }
}
