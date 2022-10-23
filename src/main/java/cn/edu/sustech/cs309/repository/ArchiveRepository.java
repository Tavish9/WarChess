package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveRepository extends JpaRepository<Archive, Integer> {
    Archive findArchiveById(Integer id);
}
