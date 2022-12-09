package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.EquipmentRecord;
import cn.edu.sustech.cs309.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRecordRepository extends JpaRepository<EquipmentRecord, Integer> {
    EquipmentRecord findEquipmentRecordById(Integer id);

    List<EquipmentRecord> findEquipmentRecordsByPlayer(Player player);

    List<EquipmentRecord> findEquipmentRecordByPlayerAndUsed(Player player, Boolean used);
}