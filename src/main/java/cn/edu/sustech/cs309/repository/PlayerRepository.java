package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.Game;
import cn.edu.sustech.cs309.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player findPlayerById(Integer id);

    List<Player> findPlayersByGameOrderById(Game game);
}