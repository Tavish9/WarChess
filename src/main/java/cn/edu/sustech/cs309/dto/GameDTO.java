package cn.edu.sustech.cs309.dto;

import java.io.Serializable;
import java.util.List;

public record GameDTO(Integer id, PlayerDTO player1, PlayerDTO player2, ShopDTO shop, Integer round, Boolean currentPlayer,
                      List<StructureDTO> structures, int[][] map) implements Serializable {
}
