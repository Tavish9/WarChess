package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.Player;
import cn.edu.sustech.cs309.domain.ShopRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRecordRepository extends JpaRepository<ShopRecord, Integer> {
    ShopRecord findShopRecordById(Integer id);

    List<ShopRecord> findShopRecordsByPlayerAndRound(Player player, Integer round);
}