package cn.edu.sustech.cs309.dto;

import cn.edu.sustech.cs309.domain.MountClass;

import java.io.Serializable;

public record MountDTO(Integer id, String name, MountClass mountClass,
                       int attack, int defense, int actionRange,
                       String description) implements Serializable {
}
