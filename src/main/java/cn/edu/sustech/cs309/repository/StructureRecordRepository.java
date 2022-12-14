package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.Game;
import cn.edu.sustech.cs309.domain.Player;
import cn.edu.sustech.cs309.domain.StructureRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StructureRecordRepository extends JpaRepository<StructureRecord, Integer> {
    StructureRecord findStructureRecordById(Integer id);

    List<StructureRecord> findStructureRecordsByPlayer(Player player);

    @Query(value = "select sum(level) from structure_record where player_id = ?1", nativeQuery = true)
    Integer getSumLevel(Integer playerId);

    Integer countByPlayer(Player player);

    @Query(value = "select count(*) from structure_record where player_id = ?1 and structure_class = ?2", nativeQuery = true)
    Integer countByPlayerAndStructureClass(Integer playerId, String structureClass);

    List<StructureRecord> findStructureRecordsByGameAndPlayer(Game game, Player player);
}