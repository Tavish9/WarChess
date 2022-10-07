package cn.edu.sustech.cs309.dto;

import cn.edu.sustech.cs309.domain.CharacterClass;

import java.io.Serializable;
import java.util.List;

public record CharacterDTO(Integer id, String name, CharacterClass characterClass, double attack, double defense,
                           double hp, Long exp, Long maxExp, Integer level, List<EquipmentDTO> equipmentDTOS) implements Serializable {
}


