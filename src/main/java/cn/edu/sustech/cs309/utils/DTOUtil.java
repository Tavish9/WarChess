package cn.edu.sustech.cs309.utils;

import cn.edu.sustech.cs309.domain.*;
import cn.edu.sustech.cs309.dto.*;
import cn.edu.sustech.cs309.repository.EquipmentRecordRepository;
import cn.edu.sustech.cs309.repository.ItemRecordRepository;
import cn.edu.sustech.cs309.repository.MountRecordRepository;
import cn.edu.sustech.cs309.repository.StructureRecordRepository;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DTOUtil {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EquipmentRecordRepository equipmentRecordRepository;

    @Autowired
    private ItemRecordRepository itemRecordRepository;

    @Autowired
    private MountRecordRepository mountRecordRepository;

    @Autowired
    private StructureRecordRepository structureRecordRepository;

    private static DTOUtil dtoUtil;

    @PostConstruct
    public void init() {
        dtoUtil = this;
        dtoUtil.objectMapper = this.objectMapper;
        dtoUtil.equipmentRecordRepository = this.equipmentRecordRepository;
        dtoUtil.itemRecordRepository = this.itemRecordRepository;
        dtoUtil.mountRecordRepository = this.mountRecordRepository;
        dtoUtil.structureRecordRepository = this.structureRecordRepository;
    }


    public static AccountDTO toAccountDTO(Account account, List<ArchiveDTO> archiveDTOS) {
        return new AccountDTO(account.getId(), account.getUsername(), archiveDTOS);
    }

    public static ArchiveDTO toArchiveDTO(Archive archive) throws JsonProcessingException {
        return new ArchiveDTO(archive.getId(), archive.getGame().getId(), toPlayerDTO(archive.getGame().getPlayer1()));
    }

    public static List<ArchiveDTO> toArchiveDTOs(List<Archive> archives) throws JsonProcessingException {
        if (archives == null)
            return null;
        List<ArchiveDTO> archiveDTOS = new ArrayList<>(archives.size());
        for (Archive a : archives) {
            archiveDTOS.add(DTOUtil.toArchiveDTO(a));
        }
        return archiveDTOS;
    }

    public static CharacterDTO toCharacterDTO(CharacterRecord character) {
        return new CharacterDTO(character.getId(), character.getName(), character.getCharacterClass(),
                character.getActionRange(), character.getAttack(), character.getDefense(), character.getHp(),
                character.getLevel(), character.getX(), character.getY(),
                toEquipmentDTO(character.getEquipmentRecord()), toMountDTO(character.getMountRecord()));
    }

    public static List<CharacterDTO> toCharacterDTOs(List<CharacterRecord> characterRecords) {
        if (characterRecords == null)
            return null;
        List<CharacterDTO> characterDTOS = new ArrayList<>(characterRecords.size());
        characterDTOS.addAll(characterRecords.stream().map(DTOUtil::toCharacterDTO).toList());
        return characterDTOS;
    }

    public static EquipmentDTO toEquipmentDTO(EquipmentRecord equipment) {
        Equipment e = equipment.getEquipment();
        return new EquipmentDTO(equipment.getId(), e.getName(), e.getEquipmentClass(),
                e.getAttack(), e.getDefense(), e.getAttackRange(), e.getDescription());
    }

    public static List<EquipmentDTO> toEquipmentDTOs(List<EquipmentRecord> equipmentRecords) {
        if (equipmentRecords == null)
            return null;
        List<EquipmentDTO> equipmentDTOS = new ArrayList<>(equipmentRecords.size());
        equipmentDTOS.addAll(equipmentRecords.stream().map(DTOUtil::toEquipmentDTO).toList());
        return equipmentDTOS;
    }

    public static ItemDTO toItemDTO(ItemRecord item) {
        Item i = item.getItem();
        return new ItemDTO(item.getId(), i.getName(), i.getAttack(), i.getDefense(), i.getHp(), i.getDescription());
    }

    public static List<ItemDTO> toItemDTOs(List<ItemRecord> itemRecords) {
        if (itemRecords == null)
            return null;
        List<ItemDTO> itemDTOS = new ArrayList<>(itemRecords.size());
        itemDTOS.addAll(itemRecords.stream().map(DTOUtil::toItemDTO).toList());
        return itemDTOS;
    }

    public static MountDTO toMountDTO(MountRecord mount) {
        Mount m = mount.getMount();
        return new MountDTO(mount.getId(), m.getName(), m.getAttack(), m.getDefense(), m.getActionRange(), m.getDescription());
    }

    public static List<MountDTO> toMountDTOs(List<MountRecord> mountRecords) {
        if (mountRecords == null)
            return null;
        List<MountDTO> mountDTOS = new ArrayList<>(mountRecords.size());
        mountDTOS.addAll(mountRecords.stream().map(DTOUtil::toMountDTO).toList());
        return mountDTOS;
    }

    public static StructureDTO toStructureDTO(StructureRecord structure) throws JsonProcessingException {
        List<CharacterDTO> characterDTOS;
        if (structure.getCharacter() == null) {
            characterDTOS = null;
        } else {
            characterDTOS = dtoUtil.objectMapper.readValue(structure.getCharacter(),
                    dtoUtil.objectMapper.getTypeFactory().constructParametricType(List.class, CharacterDTO.class));
        }
        return new StructureDTO(structure.getId(), structure.getStructureClass(), structure.getLevel(), structure.getHp(),
                structure.getRemainingRound(), structure.getValue(), structure.getX(), structure.getY(), characterDTOS);
    }

    public static List<StructureDTO> toStructureDTOs(List<StructureRecord> structureRecords) throws JsonProcessingException {
        if (structureRecords == null)
            return null;
        List<StructureDTO> structureDTOS = new ArrayList<>(structureRecords.size());
        for (StructureRecord s : structureRecords) {
            structureDTOS.add(DTOUtil.toStructureDTO(s));
        }
        return structureDTOS;
    }

    public static ShopDTO toShopDTO(List<ShopRecord> shopRecords) {
        if (shopRecords == null)
            return null;
        List<EquipmentDTO> equipmentDTOS = new ArrayList<>();
        List<ItemDTO> itemDTOS = new ArrayList<>();
        List<MountDTO> mountDTOS = new ArrayList<>();
        for (ShopRecord shoprecord : shopRecords) {
            if (shoprecord.getShopClass() == ShopClass.EQUIPMENT)
                equipmentDTOS.add(DTOUtil.toEquipmentDTO(dtoUtil.equipmentRecordRepository.findEquipmentRecordById(shoprecord.getPropid())));
            else if (shoprecord.getShopClass() == ShopClass.ITEM)
                itemDTOS.add(DTOUtil.toItemDTO(dtoUtil.itemRecordRepository.findItemRecordById(shoprecord.getPropid())));
            else
                mountDTOS.add(DTOUtil.toMountDTO(dtoUtil.mountRecordRepository.findMountRecordById(shoprecord.getPropid())));
        }
        int[][] index = new int[3][equipmentDTOS.size()];
        int e = 0, i = 0, m = 0;
        for (ShopRecord shoprecord : shopRecords) {
            if (shoprecord.getShopClass() == ShopClass.EQUIPMENT)
                index[0][e++] = shoprecord.getId();
            else if (shoprecord.getShopClass() == ShopClass.ITEM)
                index[1][i++] = shoprecord.getId();
            else
                index[2][m++] = shoprecord.getId();
        }
        return new ShopDTO(equipmentDTOS, itemDTOS, mountDTOS, index);
    }

    public static PlayerDTO toPlayerDTO(Player player) throws JsonProcessingException {
        return new PlayerDTO(player.getId(), player.getStars(), player.getProsperityDegree(), player.getPeaceDegree(),
                player.getTech(), null, toShopDTO(player.getShopRecords()), toCharacterDTOs(player.getCharacterRecords()), toEquipmentDTOs(player.getEquipmentRecords()),
                toMountDTOs(player.getMountRecords()), toItemDTOs(player.getItemRecords()), toStructureDTOs(player.getStructureRecords()));
    }

    public static GameDTO toGameDTO(Game game, ShopDTO shopDTO, int round, boolean currentPlayer) throws JsonProcessingException {
        List<StructureRecord> neutralStructure = dtoUtil.structureRecordRepository.findStructureRecordsByGameAndPlayer(game, null);
        return new GameDTO(game.getId(), toPlayerDTO(game.getPlayer1()), toPlayerDTO(game.getPlayer2()), shopDTO, round,
                currentPlayer, DTOUtil.toStructureDTOs(neutralStructure), JSON.parseObject(game.getMap().getData(), int[][].class));
    }
}
