package cn.edu.sustech.cs309.dto;

import java.io.Serializable;
import java.util.List;

public record AccountDTO(String username,
                         List<PlayerDTO> playerDTOS) implements Serializable {
}
