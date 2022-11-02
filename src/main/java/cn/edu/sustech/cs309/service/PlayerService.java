package cn.edu.sustech.cs309.service;

import cn.edu.sustech.cs309.dto.*;

import java.util.List;

public interface PlayerService {
    CharacterDTO updateItem(Integer playerId, Integer characterId, Integer itemId);

    CharacterDTO offEquipment(Integer playerId, Integer characterId, Integer equipmentId);

    CharacterDTO wearEquipment(Integer playerId, Integer characterId, Integer equipmentId);

    CharacterDTO offMount(Integer playerId, Integer characterId, Integer mountId);

    CharacterDTO wearMount(Integer playerId, Integer characterId, Integer mountId);

    int[][] getTechnologies(Integer playerId);

    List<EquipmentDTO> getEquipments(Integer playerId);

    List<MountDTO> getMounts(Integer playerId);

    List<ItemDTO> getItems(Integer playerId);

    EquipmentDTO buyEquipment(Integer playerId, Integer shopId);

    MountDTO buyMount(Integer playerId, Integer shopId);

    ItemDTO buyItem(Integer playerId, Integer shopId);
}
