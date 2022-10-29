package cn.edu.sustech.cs309.dto;

import cn.edu.sustech.cs309.domain.CharacterClass;

import java.io.Serializable;
import java.util.List;

public record CharacterDTO(Integer id, String name, CharacterClass characterClass, int actionRange,int attack, int defense,
                           int hp, int level, EquipmentDTO equipment, MountDTO mount) implements Serializable {
}