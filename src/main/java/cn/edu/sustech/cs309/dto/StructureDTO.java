package cn.edu.sustech.cs309.dto;

import cn.edu.sustech.cs309.domain.StructureClass;

import java.io.Serializable;
import java.util.List;

public record StructureDTO(Integer id, StructureClass structureClass, Integer level, Integer hp, Integer remainingRound,
                           Integer value, Integer x, Integer y, List<CharacterDTO> characterDTOS) implements Serializable {
}
