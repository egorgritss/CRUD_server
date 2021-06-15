package cz.cvut.fit.gritsego.semestral.repository;

import cz.cvut.fit.gritsego.semestral.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Integer> {
    Optional<Sponsor> findByName(String name);

    void deleteByName(String name);
}
