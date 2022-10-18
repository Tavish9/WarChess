package cn.edu.sustech.cs309.dto;

import java.io.Serializable;

public record MountDTO(Integer id, String name,
                       int attack, int defense, int actionRange,
                       String description) implements Serializable {
}
