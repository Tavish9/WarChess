package cn.edu.sustech.cs309.dto;

public record ItemDTO(Integer id, String name,
                      int attack, int defense, int hp,
                      String description) {
}
