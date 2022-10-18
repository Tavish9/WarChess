package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player findPlayerById(Integer id);
}