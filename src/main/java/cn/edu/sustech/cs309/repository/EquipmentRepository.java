package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    Equipment findEquipmentById(Integer id);

    Equipment findEquipmentByName(String name);
}