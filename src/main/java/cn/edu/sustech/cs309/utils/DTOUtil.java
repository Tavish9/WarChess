package cn.edu.sustech.cs309.utils;

import cn.edu.sustech.cs309.domain.*;
import cn.edu.sustech.cs309.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DTOUtil {
    @Autowired
    private ObjectMapper objectMapper;

    private static DTOUtil dtoUtil;

    @PostConstruct
    public void init() {
        dtoUtil = this;
        dtoUtil.objectMapper = this.objectMapper;
    }

    public static String toCharacterString(List<CharacterDTO> characterDTOS) throws JsonProcessingException {
        return dtoUtil.objectMapper.writeValueAsString(characterDTOS);
    }

    public static AccountDTO toAccountDTO(Account account, List<ArchiveDTO> archiveDTOS) {
        return new AccountDTO(account.getId(), account.getUsername(), archiveDTOS);
    }

    public static ArchiveDTO toArchiveDTO(Archive archive) throws JsonProcessingException {
        return new ArchiveDTO(archive.getId(), archive.getGame().getId(), toPlayerDTO(archive.getGame().getPlayer1()));
    }

    public static List<ArchiveDTO> toArchiveDTOs(List<Archive> archices) throws JsonProcessingException {
        List<ArchiveDTO> archiveDTOS = new ArrayList<>(archices.size());
        for (Archive a : archices) {
            archiveDTOS.add(DTOUtil.toArchiveDTO(a));
        }
        return archiveDTOS;
    }

    public static CharacterDTO toCharacterDTO(CharacterRecord character) {
        return new CharacterDTO(character.getId(), character.getName(), character.getCharacterClass(),
                character.getAttack(), character.getDefense(), character.getHp(), character.getLevel(),
                toEquipmentDTO(character.getEquipmentRecord()), toMountDTO(character.getMountRecord()));
    }

    public static List<CharacterDTO> toCharacterDTOs(List<CharacterRecord> characterRecords) {
        List<CharacterDTO> characterDTOS = new ArrayList<>(characterRecords.size());
        characterDTOS.addAll(characterRecords.stream().map(DTOUtil::toCharacterDTO).collect(Collectors.toList()));
        return characterDTOS;
    }

    public static EquipmentDTO toEquipmentDTO(EquipmentRecord equipment) {
        Equipment e = equipment.getEquipment();
        return new EquipmentDTO(equipment.getId(), e.getName(), e.getEquipmentClass(),
                e.getAttack(), e.getDefense(), e.getAttackRange(), e.getDescription());
    }

    public static List<EquipmentDTO> toEquipmentDTOs(List<EquipmentRecord> equipmentRecords) {
        List<EquipmentDTO> equipmentDTOS = new ArrayList<>(equipmentRecords.size());
        equipmentDTOS.addAll(equipmentRecords.stream().map(DTOUtil::toEquipmentDTO).collect(Collectors.toList()));
        return equipmentDTOS;
    }

    public static ItemDTO toItemDTO(ItemRecord item) {
        Item i = item.getItem();
        return new ItemDTO(item.getId(), i.getName(), i.getAttack(), i.getDefense(), i.getHp(), i.getDescription());
    }

    public static List<ItemDTO> toItemDTOs(List<ItemRecord> itemRecords) {
        List<ItemDTO> itemDTOS = new ArrayList<>(itemRecords.size());
        itemDTOS.addAll(itemRecords.stream().map(DTOUtil::toItemDTO).collect(Collectors.toList()));
        return itemDTOS;
    }

    public static MountDTO toMountDTO(MountRecord mount) {
        Mount m = mount.getMount();
        return new MountDTO(mount.getId(), m.getName(), m.getAttack(), m.getDefense(), m.getActionRange(), m.getDescription());
    }

    public static List<MountDTO> toMountDTOs(List<MountRecord> mountRecords) {
        List<MountDTO> mountDTOS = new ArrayList<>(mountRecords.size());
        mountDTOS.addAll(mountRecords.stream().map(DTOUtil::toMountDTO).collect(Collectors.toList()));
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
        List<StructureDTO> structureDTOS = new ArrayList<>(structureRecords.size());
        for (StructureRecord s : structureRecords) {
            structureDTOS.add(DTOUtil.toStructureDTO(s));
        }
        return structureDTOS;
    }

    public static PlayerDTO toPlayerDTO(Player player) throws JsonProcessingException {
        return new PlayerDTO(player.getId(), player.getStars(), player.getProsperityDegree(), player.getPeaceDegree(),
                toCharacterDTOs(player.getCharacterRecords()), toEquipmentDTOs(player.getEquipmentRecords()),
                toMountDTOs(player.getMountRecords()), toItemDTOs(player.getItemRecords()), toStructureDTOs(player.getStructureRecords()));
    }

    public static GameDTO toGameDTO(Player player1, Player player2, Integer round, Boolean currentPlayer) throws JsonProcessingException {
        return new GameDTO(toPlayerDTO(player1), toPlayerDTO(player2), round, currentPlayer);
    }
}
