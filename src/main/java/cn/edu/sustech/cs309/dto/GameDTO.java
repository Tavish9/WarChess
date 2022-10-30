package cn.edu.sustech.cs309.dto;

import java.io.Serializable;

public record GameDTO(Integer id, PlayerDTO player1, PlayerDTO player2, ShopDTO shopDTO, Integer round, Boolean currentPlayer) implements Serializable {
}
