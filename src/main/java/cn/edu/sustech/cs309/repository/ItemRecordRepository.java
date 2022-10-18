package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.ItemRecord;
import cn.edu.sustech.cs309.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRecordRepository extends JpaRepository<ItemRecord, Integer> {
    ItemRecord findItemRecordById(Integer id);

    List<ItemRecord> findItemRecordByPlayerAndUsed(Player player, Boolean used);
}