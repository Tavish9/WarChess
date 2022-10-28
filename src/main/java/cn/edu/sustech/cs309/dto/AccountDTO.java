package cn.edu.sustech.cs309.dto;

import java.io.Serializable;
import java.util.List;

public record AccountDTO(Integer id, String username, List<ArchiveDTO> archives) implements Serializable {
}
