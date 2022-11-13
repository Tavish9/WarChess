package cn.edu.sustech.cs309.dto;

import cn.edu.sustech.cs309.domain.CharacterClass;

import java.io.Serializable;

public record CharacterDTO(Integer id, String name, CharacterClass characterClass, int actionState, int actionRange,
                           int attack, int defense, int hp, int level, int x, int y, EquipmentDTO equipment,
                           MountDTO mount) implements Serializable {
}