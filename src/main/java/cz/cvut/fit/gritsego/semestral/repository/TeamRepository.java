package cz.cvut.fit.gritsego.semestral.repository;

import cz.cvut.fit.gritsego.semestral.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Optional<Team> findByName(String title);
    Optional<Team> findByRating(int id);
}
