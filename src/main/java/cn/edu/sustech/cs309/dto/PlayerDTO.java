package cn.edu.sustech.cs309.dto;

import java.io.Serializable;
import java.util.List;

public record PlayerDTO(Integer id, Long stars, Integer prosperityDegree, Integer peaceDegree,
                        int[][] technologyTree, int[][] vision, ShopDTO shopDTO, List<CharacterDTO> characters,
                        List<EquipmentDTO> equipments, List<MountDTO> mounts, List<ItemDTO> items,
                        List<StructureDTO> structures) implements Serializable {

}
