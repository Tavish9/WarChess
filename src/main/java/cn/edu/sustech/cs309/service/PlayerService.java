package cn.edu.sustech.cs309.service;

import cn.edu.sustech.cs309.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface PlayerService {
    PlayerDTO getAll(Integer playerId) throws JsonProcessingException;

    CharacterDTO updateItem(Integer playerId, Integer characterId, Integer itemId);

    CharacterDTO offEquipment(Integer playerId, Integer characterId, Integer equipmentId);

    CharacterDTO wearEquipment(Integer playerId, Integer characterId, Integer equipmentId);

    CharacterDTO offMount(Integer playerId, Integer characterId, Integer mountId);

    CharacterDTO wearMount(Integer playerId, Integer characterId, Integer mountId);

    List<String> getTechnologies(Integer playerId);

    List<EquipmentDTO> getEquipments(Integer playerId);

    List<MountDTO> getMounts(Integer playerId);

    List<ItemDTO> getItems(Integer playerId);

    EquipmentDTO buyEquipment(Integer playerId, Integer equipmentId);

    MountDTO buyMount(Integer playerId, Integer mountId);

    ItemDTO buyItem(Integer playerId, Integer itemId);
}
