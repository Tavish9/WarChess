package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.Mount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MountRepository extends JpaRepository<Mount, Integer> {
    Mount findMountById(Integer id);
}