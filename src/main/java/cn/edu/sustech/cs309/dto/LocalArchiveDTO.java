package cn.edu.sustech.cs309.dto;

import java.io.Serializable;
import java.util.List;

public record LocalArchiveDTO(Integer id, PlayerDTO player1, PlayerDTO player2, List<EquipmentDTO> equipmentDTOS,
                                List<MountDTO> mountDTOS,List<ItemDTO>itemDTOS, Integer round, Boolean currentPlayer,
                              List<StructureDTO> structures, int[][] map) implements Serializable {
}
