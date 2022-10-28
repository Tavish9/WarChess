package cn.edu.sustech.cs309.dto;

import java.io.Serializable;

public record ArchiveDTO(Integer id, Integer gameId, PlayerDTO player) implements Serializable {
}
