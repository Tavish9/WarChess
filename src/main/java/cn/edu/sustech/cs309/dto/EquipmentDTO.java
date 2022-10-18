package cn.edu.sustech.cs309.dto;

import cn.edu.sustech.cs309.domain.EquipmentClass;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

public record EquipmentDTO(Integer id, String name, EquipmentClass equipmentClass,
                           int attack, int defense, int hp, int attackRange,
                           String description) implements Serializable {
}
