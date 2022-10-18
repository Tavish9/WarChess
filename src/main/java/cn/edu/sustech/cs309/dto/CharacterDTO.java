package cn.edu.sustech.cs309.dto;

import cn.edu.sustech.cs309.domain.CharacterClass;

import java.io.Serializable;
import java.util.List;

public record CharacterDTO(Integer id, String name, CharacterClass characterClass, int attack, int defense,
                           int hp, int level, EquipmentDTO equipmentDTO, MountDTO mountDTO) implements Serializable {
}