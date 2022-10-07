package cn.edu.sustech.cs309.dto;

import cn.edu.sustech.cs309.domain.EquipmentClass;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

public record EquipmentDTO(Integer id, String name, EquipmentClass equipmentClass,
                           double attack, double defense, double hp,
                           String description) implements Serializable {
}
