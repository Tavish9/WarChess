package cn.edu.sustech.cs309.dto;

import cn.edu.sustech.cs309.domain.CharacterClass;
import cn.edu.sustech.cs309.domain.Player;
import cn.edu.sustech.cs309.domain.StructureClass;
import cn.edu.sustech.cs309.domain.StructureRecord;

import java.io.Serializable;

public record StructureDTO(Integer id, PlayerDTO playerDTO, StructureClass structureClass,Integer level,Integer hp,Integer remainingRound,Integer value,Integer x,Integer y,String character) implements Serializable {
}