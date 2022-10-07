package cn.edu.sustech.cs309.dto;

import java.io.Serializable;
import java.util.List;

public record PlayerDTO(Integer id, Long coins,
                        List<CharacterDTO> characterDTOS) implements Serializable {

}
