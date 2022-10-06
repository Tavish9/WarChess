package cn.edu.sustech.cs309.dto;

import java.io.Serializable;
import java.util.List;

public record PlayerDTO(String name, Long exp, Long maxExp, Integer level, Long coins,
                        List<CharacterDTO> characterDTOS) implements Serializable {

}
