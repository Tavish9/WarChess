package cn.edu.sustech.cs309.dto;

import java.io.Serializable;

public record CharacterDTO(String name, String characterClass, double attack, double defense,
                           double hp, Long exp, Long maxExp, Integer level) implements Serializable {
}


