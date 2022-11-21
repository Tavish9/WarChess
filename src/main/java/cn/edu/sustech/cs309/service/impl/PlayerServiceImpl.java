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
import java.util.Arrays;
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

    @Autowired
    private ShopRecordRepository shopRecordRepository;

    @Override
    public CharacterDTO updateItem(Integer playerId, Integer characterId, Integer itemId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord == null)
            throw new RuntimeException("Character does not exist");
        if (characterRecord.getHp() <= 0)
            throw new RuntimeException("Character is dead");
        ItemRecord itemRecord = itemRecordRepository.findItemRecordById(itemId);
        if (itemRecord == null)
            throw new RuntimeException("Item does not exist");
        if (itemRecord.getUsed())
            throw new RuntimeException("Item has been used");
        Item item = itemRecord.getItem();
        characterRecord.setAttack(Math.min(characterRecord.getAttack() + item.getAttack(),50));
        characterRecord.setDefense(Math.min(characterRecord.getDefense() + item.getDefense(),50));
        characterRecord.setHp(Math.min(characterRecord.getHp() + item.getHp(),50));
        characterRecordRepository.save(characterRecord);
        itemRecord.setUsed(true);
        itemRecordRepository.save(itemRecord);
        log.debug("character " + characterId + "use item " + itemId);
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
        if (characterRecord.getHp() <= 0)
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
        if (characterRecord.getHp() <= 0)
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
        if (characterRecord.getHp() <= 0)
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
        if (characterRecord.getHp() <= 0)
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
    public int[][] getTechnologies(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        log.debug("play " + playerId + "get techTree");
        return player.getTech();
    }

    @Override
    public List<EquipmentDTO> getEquipments(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        List<EquipmentRecord> equipmentRecords = equipmentRecordRepository.findEquipmentRecordByPlayerAndUsed(player, false);
        log.debug("player " + playerId + "get equipment");
        return DTOUtil.toEquipmentDTOs(equipmentRecords);
    }

    @Override
    public List<MountDTO> getMounts(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        List<MountRecord> mountRecords = mountRecordRepository.findMountRecordByPlayerAndUsed(player, false);
        log.debug("player " + playerId + "get mount");
        return DTOUtil.toMountDTOs(mountRecords);
    }

    @Override
    public List<ItemDTO> getItems(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        List<ItemRecord> itemRecords = itemRecordRepository.findItemRecordByPlayerAndUsed(player, false);
        log.debug("player " + playerId + "get item");
        return DTOUtil.toItemDTOs(itemRecords);
    }

    @Override
    public EquipmentDTO buyEquipment(Integer playerId, Integer shopId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        ShopRecord shopRecord = shopRecordRepository.findShopRecordById(shopId);
        if (shopRecord == null)
            throw new RuntimeException("ShopId is not valid");
        EquipmentRecord equipmentRecord = equipmentRecordRepository.findEquipmentRecordById(shopRecord.getPropid());
        if (equipmentRecord == null)
            throw new RuntimeException("Equipment does not exist");
        if (player.getStars() < shopRecord.getCost())
            throw new RuntimeException("Player does not have enough stars");
        equipmentRecord.setPlayer(player);
        equipmentRecordRepository.save(equipmentRecord);
        player.setStars(player.getStars() - shopRecord.getCost());
        playerRepository.save(player);
        log.debug("player " + playerId + " buy equipment" + equipmentRecord.getId());
        return DTOUtil.toEquipmentDTO(equipmentRecord);
    }

    @Override
    public MountDTO buyMount(Integer playerId, Integer shopId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        ShopRecord shopRecord = shopRecordRepository.findShopRecordById(shopId);
        if (shopRecord == null)
            throw new RuntimeException("ShopId is not valid");
        MountRecord mountRecord = mountRecordRepository.findMountRecordById(shopRecord.getPropid());
        if (mountRecord == null)
            throw new RuntimeException("Mount does not exist");
        if (player.getStars() < shopRecord.getCost())
            throw new RuntimeException("Player does not have enough stars");
        mountRecord.setPlayer(player);
        mountRecordRepository.save(mountRecord);
        player.setStars(player.getStars() - shopRecord.getCost());
        playerRepository.save(player);
        log.debug("player " + playerId + " buy mount" + mountRecord.getId());
        return DTOUtil.toMountDTO(mountRecord);
    }

    @Override
    public ItemDTO buyItem(Integer playerId, Integer shopId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("Player does not exist");
        ShopRecord shopRecord = shopRecordRepository.findShopRecordById(shopId);
        if (shopRecord == null)
            throw new RuntimeException("ShopId is not valid");
        ItemRecord itemRecord = itemRecordRepository.findItemRecordById(shopRecord.getPropid());
        if (itemRecord == null)
            throw new RuntimeException("Item does not exist");
        if (player.getStars() < shopRecord.getCost())
            throw new RuntimeException("Player does not have enough stars");
        itemRecord.setPlayer(player);
        itemRecordRepository.save(itemRecord);
        player.setStars(player.getStars() - shopRecord.getCost());
        playerRepository.save(player);
        log.debug("player " + playerId + " buy item" + itemRecord.getId());
        return DTOUtil.toItemDTO(itemRecord);
    }
}
