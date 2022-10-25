package cn.edu.sustech.cs309.service.impl;

import cn.edu.sustech.cs309.domain.*;
import cn.edu.sustech.cs309.dto.*;
import cn.edu.sustech.cs309.repository.*;
import cn.edu.sustech.cs309.service.PlayerService;
import cn.edu.sustech.cs309.utils.DTOUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CharacterRecordRepository characterRecordRepository;

    @Autowired
    private ItemRecordRepository itemRecordRepository;

    @Autowired
    private EquipmentRecordRepository equipmentRecordRepository;

    @Autowired
    private MountRecordRepository mountRecordRepository;

    @Override
    public PlayerDTO getAll(Integer playerId) throws JsonProcessingException {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        log.debug("find player");
        return DTOUtil.toPlayerDTO(player);
    }

    @Override
    public CharacterDTO updateItem(Integer playerId, Integer characterId, Integer itemId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord == null)
            throw new RuntimeException("Character does not exist");
        if (characterRecord.getHp() == 0)
            throw new RuntimeException("Character is dead");
        ItemRecord itemRecord = itemRecordRepository.findItemRecordById(itemId);
        if (itemRecord == null)
            throw new RuntimeException("Item does not exist");
        // TODO
        return DTOUtil.toCharacterDTO(characterRecord);
    }

    @Override
    public CharacterDTO offEquipment(Integer playerId, Integer characterId, Integer equipmentId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord == null)
            throw new RuntimeException("Character does not exist");
        if (characterRecord.getHp() == 0)
            throw new RuntimeException("Character is dead");
        EquipmentRecord equipmentRecord = characterRecord.getEquipmentRecord();
        if (equipmentRecord == null)
            throw new RuntimeException("Character is not wearing equipment");
        if (!equipmentRecord.getId().equals(equipmentId))
            throw new RuntimeException("Character is not wearing this equipment");
        characterRecord.setEquipmentRecord(null);
        characterRecordRepository.save(characterRecord);
        log.debug("character " + characterId + " off equipment " + equipmentId);
        return DTOUtil.toCharacterDTO(characterRecord);
    }

    @Override
    public CharacterDTO wearEquipment(Integer playerId, Integer characterId, Integer equipmentId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord == null)
            throw new RuntimeException("Character does not exist");
        if (characterRecord.getHp() == 0)
            throw new RuntimeException("Character is dead");
        EquipmentRecord equipmentRecord = characterRecord.getEquipmentRecord();
        if (equipmentRecord != null)
            throw new RuntimeException("Character is wearing equipment");
        EquipmentRecord equipmentRecord1 = equipmentRecordRepository.findEquipmentRecordById(equipmentId);
        if (equipmentRecord1 == null)
            throw new RuntimeException("Equipment does not exist");
        characterRecord.setEquipmentRecord(equipmentRecord1);
        characterRecordRepository.save(characterRecord);
        log.debug("character " + characterId + " wear equipment " + equipmentId);
        return DTOUtil.toCharacterDTO(characterRecord);
    }

    @Override
    public CharacterDTO offMount(Integer playerId, Integer characterId, Integer mountId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord == null)
            throw new RuntimeException("Character does not exist");
        if (characterRecord.getHp() == 0)
            throw new RuntimeException("Character is dead");
        MountRecord mountRecord = characterRecord.getMountRecord();
        if (mountRecord == null)
            throw new RuntimeException("Character does not have a mount");
        if (!mountRecord.getId().equals(mountId))
            throw new RuntimeException("Character does not have this mount");
        characterRecord.setMountRecord(null);
        characterRecordRepository.save(characterRecord);
        log.debug("character " + characterId + " off mount " + mountId);
        return DTOUtil.toCharacterDTO(characterRecord);
    }

    @Override
    public CharacterDTO wearMount(Integer playerId, Integer characterId, Integer mountId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord == null)
            throw new RuntimeException("Character does not exist");
        if (characterRecord.getHp() == 0)
            throw new RuntimeException("Character is dead");
        MountRecord mountRecord = characterRecord.getMountRecord();
        if (mountRecord != null)
            throw new RuntimeException("Character has a mount");
        MountRecord mountRecord1 = mountRecordRepository.findMountRecordById(mountId);
        if (mountRecord1 == null)
            throw new RuntimeException("Mount does not exist");
        characterRecord.setMountRecord(mountRecord1);
        characterRecordRepository.save(characterRecord);
        log.debug("character " + characterId + " has mount " + mountId);
        return DTOUtil.toCharacterDTO(characterRecord);
    }

    @Override
    public List<String> getTechnologies(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        List<String> technology = new ArrayList<>();
        technology.add(player.getTechtreeLight());
        technology.add(player.getTechtreeFeasible());
        technology.add(player.getTechtreeRemainRound());
        log.debug("play " + playerId + "get techTree");
        return technology;
    }

    @Override
    public List<EquipmentDTO> getEquipments(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        List<EquipmentRecord> equipmentRecords = equipmentRecordRepository.findEquipmentRecordByPlayerAndUsed(player, false);
        log.debug("player " + playerId + "get equipment");
        if (equipmentRecords == null)
            return null;
        return DTOUtil.toEquipmentDTOs(equipmentRecords);
    }

    @Override
    public List<MountDTO> getMounts(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        List<MountRecord> mountRecords = mountRecordRepository.findMountRecordByPlayerAndUsed(player, false);
        log.debug("player " + playerId + "get mount");
        if (mountRecords == null)
            return null;
        return DTOUtil.toMountDTOs(mountRecords);
    }

    @Override
    public List<ItemDTO> getItems(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        List<ItemRecord> itemRecords = itemRecordRepository.findItemRecordByPlayerAndUsed(player, false);
        log.debug("player " + playerId + "get item");
        if (itemRecords == null)
            return null;
        return DTOUtil.toItemDTOs(itemRecords);
    }

    @Override
    public EquipmentDTO buyEquipment(Integer playerId, Integer equipmentId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        EquipmentRecord equipmentRecord = equipmentRecordRepository.findEquipmentRecordById(equipmentId);
        if (equipmentRecord == null)
            throw new RuntimeException("Equipment does not exist");
        equipmentRecord.setPlayer(player);
        equipmentRecordRepository.save(equipmentRecord);
        log.debug("player " + playerId + " buy equipment" + equipmentId);
        return DTOUtil.toEquipmentDTO(equipmentRecord);
    }

    @Override
    public MountDTO buyMount(Integer playerId, Integer mountId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        MountRecord mountRecord = mountRecordRepository.findMountRecordById(mountId);
        if (mountRecord == null)
            throw new RuntimeException("Mount does not exist");
        mountRecord.setPlayer(player);
        mountRecordRepository.save(mountRecord);
        log.debug("player " + playerId + " buy mount" + mountRecord);
        return DTOUtil.toMountDTO(mountRecord);
    }

    @Override
    public ItemDTO buyItem(Integer playerId, Integer itemId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        ItemRecord itemRecord = itemRecordRepository.findItemRecordById(itemId);
        if (itemRecord == null)
            throw new RuntimeException("Item does not exist");
        itemRecord.setPlayer(player);
        itemRecordRepository.save(itemRecord);
        log.debug("player " + playerId + " buy item" + itemRecord);
        return DTOUtil.toItemDTO(itemRecord);
    }
}
