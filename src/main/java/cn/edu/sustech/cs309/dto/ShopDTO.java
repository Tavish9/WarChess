package cn.edu.sustech.cs309.dto;

import java.util.List;

public record ShopDTO(List<EquipmentDTO> equipments, List<ItemDTO> items, List<MountDTO> mounts, int[][] index) {
}
