package cn.edu.sustech.cs309.dto;

import cn.edu.sustech.cs309.domain.ItemClass;

import java.io.Serializable;

public record ItemDTO(Integer id, String name, ItemClass itemClass,
                      int attack, int defense, int hp,
                      String description) implements Serializable {
}
