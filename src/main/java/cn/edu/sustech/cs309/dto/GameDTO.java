package cn.edu.sustech.cs309.dto;

import java.io.Serializable;

public record GameDTO(PlayerDTO player1, PlayerDTO player2, Integer round, Boolean currentPlayer) implements Serializable {
}
