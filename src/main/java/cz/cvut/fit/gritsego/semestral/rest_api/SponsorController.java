package cz.cvut.fit.gritsego.semestral.rest_api;

import cz.cvut.fit.gritsego.semestral.dto.SponsorCreateDTO;
import cz.cvut.fit.gritsego.semestral.dto.SponsorDTO;
import cz.cvut.fit.gritsego.semestral.entity.Sponsor;
import cz.cvut.fit.gritsego.semestral.exeptions.SponsorNotFoundException;
import cz.cvut.fit.gritsego.semestral.service.SponsorService;
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
@RequestMapping("/api/v1/sponsors")
public class SponsorController {
    private final SponsorService sponsorService;

    public SponsorController(SponsorService SponsorService) {
        this.sponsorService = SponsorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SponsorDTO> create(@RequestBody SponsorCreateDTO sponsor) throws Exception {
        try {
            SponsorDTO created = sponsorService.create(sponsor);
            return  ResponseEntity
                    .created(Link.of("http://localhost:8080/api/v1/sponsors/" + created.getId()).toUri())
                    .body(created);
        }catch (InstanceAlreadyExistsException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

    }

    @GetMapping("/{id}")
    public SponsorDTO readOne(@PathVariable int id) {
        Optional<SponsorDTO> optionalSponsor = sponsorService.findByIdAsDTO(id);
        return optionalSponsor.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)) ;

    }

    @GetMapping
    public Page<SponsorDTO> readAll(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        return sponsorService.readAll(PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SponsorDTO> update(@PathVariable int id,@RequestBody SponsorCreateDTO newSponsor) throws Exception {
        try {
            SponsorDTO updated = sponsorService.update(id, newSponsor);
            return  ResponseEntity
                    .created(Link.of("http://localhost:8080/api/v1/sponsors/" + updated.getId()).toUri())
                    .body(updated);
        } catch (InstanceAlreadyExistsException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }catch (SponsorNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) throws Exception {
        try {
            sponsorService.deleteById(id);
        }catch (SponsorNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }


    }
}
