package cz.cvut.fit.gritsego.semestral.repository;

import cz.cvut.fit.gritsego.semestral.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> findByNickname(String nickname);
    Optional<Player> findByRating(int rating);
    void deleteByNickname(String nickname);
}
