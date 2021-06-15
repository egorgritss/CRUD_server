package cz.cvut.fit.gritsego.semestral.service;

import cz.cvut.fit.gritsego.semestral.dto.SponsorCreateDTO;
import cz.cvut.fit.gritsego.semestral.dto.SponsorDTO;
import cz.cvut.fit.gritsego.semestral.entity.Sponsor;
import cz.cvut.fit.gritsego.semestral.exeptions.SponsorNotFoundException;
import cz.cvut.fit.gritsego.semestral.repository.SponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SponsorService implements CrudService {


    private final SponsorRepository sponsorRepository;

    @Autowired
    public SponsorService(SponsorRepository sponsorRepository) {
        this.sponsorRepository = sponsorRepository;
    }
    
    public List<SponsorDTO> findAll(){
        return sponsorRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Page<SponsorDTO> readAll(Pageable pageable) {
        List<SponsorDTO> sponsors = findAll();
        return new PageImpl<SponsorDTO>(sponsors, pageable, sponsors.size());
    }

    public List<Sponsor> findByIds(List<Integer> ids) {
        return sponsorRepository.findAllById(ids);
    }

    public Optional<Sponsor> findById(int id) {
        return sponsorRepository.findById(id);
    }

    public Optional<SponsorDTO> findByIdAsDTO(int id) {
        return toDTO(findById(id));
    }

    public Optional<Sponsor> findByName(String name) {
        return sponsorRepository.findByName(name);
    }

    public Optional<SponsorDTO> findByNameAsDTO(String name) {
        return toDTO(findByName(name));
    }



    public SponsorDTO create(SponsorCreateDTO sponsorDTO) throws Exception {
        if (sponsorRepository.findByName(sponsorDTO.getName()).isPresent())
            throw new InstanceAlreadyExistsException("Sponsor with name " + sponsorDTO.getName() + " already exists");
        return toDTO(sponsorRepository.save(
                new Sponsor(
                        sponsorDTO.getName()
                )
        ));
    }

    @Transactional
    public SponsorDTO update(int id, SponsorCreateDTO sponsorDTO) throws Exception {
        Optional<Sponsor> optionalSponsor = sponsorRepository.findById(id);
        if (optionalSponsor.isEmpty())
            throw new Exception("Sponsor with id " + id + " not found.");
        Sponsor sponsor = optionalSponsor.get();
        sponsor.setName(sponsorDTO.getName());
        return toDTO(sponsor);
    }

    @Transactional
    public void deleteById(int id) throws Exception{
        Optional<Sponsor> optionalSponsor = sponsorRepository.findById(id);
        if (optionalSponsor.isEmpty())
            throw new SponsorNotFoundException("Sponsor with id " + id + " not found.");
        sponsorRepository.deleteById(id);
    }

    @Transactional
    public void deleteByName(String name) throws Exception{
        Optional<Sponsor> optionalSponsor = sponsorRepository.findByName(name);
        if (optionalSponsor.isEmpty())
            throw new SponsorNotFoundException("Sponsor with name " + name + " not found.");
        sponsorRepository.deleteByName(name);
    }


    private SponsorDTO toDTO(Sponsor sponsor) {
        return new SponsorDTO(sponsor.getId(), sponsor.getName());
    }
    
    private Optional<SponsorDTO> toDTO(Optional<Sponsor> sponsor) {
        if (sponsor.isEmpty())
            return Optional.empty();
        return Optional.of(toDTO(sponsor.get()));
    }


}
