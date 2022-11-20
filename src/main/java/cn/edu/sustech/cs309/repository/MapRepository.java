package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface MapRepository extends JpaRepository<Map, Integer> {
    @Transactional
    Map findMapById(Integer id);

    @Query(value = "select count(*) from map", nativeQuery = true)
    int countAll();
}