package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Item findItemById(Integer id);
}