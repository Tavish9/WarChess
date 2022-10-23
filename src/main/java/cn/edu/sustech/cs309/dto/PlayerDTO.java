package cn.edu.sustech.cs309.dto;

import java.io.Serializable;
import java.util.List;

public record PlayerDTO(Integer id, Long stars, Integer prosperityDegree, Integer peaceDegree,
                        List<CharacterDTO> characterDTOS, List<EquipmentDTO> equipmentDTOS,
                        List<MountDTO> mountDTOS, List<ItemDTO> itemDTOS,
                        List<StructureDTO> structureDTOS) implements Serializable {

}
