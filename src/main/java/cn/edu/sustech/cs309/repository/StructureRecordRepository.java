package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.StructureRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StructureRecordRepository extends JpaRepository<StructureRecord, Integer> {
    StructureRecord findStructureRecordById(Integer id);
}