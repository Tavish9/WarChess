package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.CharacterRecord;
import cn.edu.sustech.cs309.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRecordRepository extends JpaRepository<CharacterRecord, Integer> {
    CharacterRecord findCharacterRecordById(Integer id);

    List<CharacterRecord> findCharacterRecordsByPlayer(Player player);

    Integer countByPlayer(Player player);

    Integer countByPlayerAndHpEquals(Player player, Integer hp);
}