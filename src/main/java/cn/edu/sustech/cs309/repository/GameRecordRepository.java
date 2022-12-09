package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.Game;
import cn.edu.sustech.cs309.domain.GameRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface GameRecordRepository extends JpaRepository<GameRecord, Integer> {
    GameRecord findGameRecordById(Integer id);

    GameRecord findFirstByGameOrderByIdDesc(Game game);

    GameRecord findGameRecordByGameAndRound(Game game, Integer round);

    @Transactional
    void deleteById(Integer id);
}