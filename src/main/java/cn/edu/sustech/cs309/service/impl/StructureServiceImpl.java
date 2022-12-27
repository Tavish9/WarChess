package cn.edu.sustech.cs309.service.impl;

import cn.edu.sustech.cs309.domain.*;
import cn.edu.sustech.cs309.dto.CharacterDTO;
import cn.edu.sustech.cs309.dto.StructureDTO;
import cn.edu.sustech.cs309.repository.CharacterRecordRepository;
import cn.edu.sustech.cs309.repository.PlayerRepository;
import cn.edu.sustech.cs309.repository.StructureRecordRepository;
import cn.edu.sustech.cs309.service.StructureService;
import cn.edu.sustech.cs309.utils.DTOUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Slf4j
@Service
public class StructureServiceImpl implements StructureService {

    @Autowired
    private CharacterRecordRepository characterRecordRepository;

    @Autowired
    private StructureRecordRepository structureRecordRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public StructureDTO getStructure(Integer structureId) throws JsonProcessingException {
        StructureRecord structureRecord = structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord != null)
            return DTOUtil.toStructureDTO(structureRecord);
        else
            throw new RuntimeException("Structure does not exist");
    }

    @Override
    public StructureDTO getCharacters(Integer structureId) throws JsonProcessingException {
        StructureRecord structureRecord = structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord != null)
            return DTOUtil.toStructureDTO(structureRecord);
        else
            throw new RuntimeException("Structure does not exist");
    }

    @Override
    public StructureDTO buyCharacter(Integer structureId, Integer playerId, Integer id, Integer x, Integer y, Integer type) throws JsonProcessingException {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new RuntimeException("player does not exist");
        if (player.getStars() < 3)
            throw new RuntimeException("player does not have enough star");

        StructureRecord structureRecord = structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord == null)
            throw new RuntimeException("structure does not exist");
        if (structureRecord.getStructureClass() == StructureClass.RELIC)
            throw new RuntimeException("can't buy character in relic");
        StructureDTO structureDTO = DTOUtil.toStructureDTO(structureRecord);
        List<CharacterDTO> characterDTOS = structureDTO.characters();
        if (id < 0)
            throw new RuntimeException("character does not exist");
        if (type < 0 || type >= 3)
            throw new RuntimeException("type is wrong");

        CharacterDTO characterDTO_add = null;
        for (CharacterDTO c : characterDTOS) {
            if (c.id().equals(id)) {
                characterDTO_add = c;
                break;
            }
        }
        if (characterDTO_add == null) {
            throw new RuntimeException("character_add does not exist");
        }

        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(id);
        if (characterRecord == null)
            throw new RuntimeException("character does not exist");

        characterDTOS.remove(characterDTO_add);
        structureRecord.setCharacter(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(characterDTOS));
        structureRecordRepository.save(structureRecord);
        player.setStars(player.getStars() - 3);
        playerRepository.save(player);
        characterRecord.updateAttribute(type);
        characterRecord.setX(x);
        characterRecord.setY(y);
        characterRecord.setPlayer(player);
        log.debug(playerId + "buy a character");
        characterRecordRepository.save(characterRecord);
        return DTOUtil.toStructureDTO(structureRecord);
    }

    @Override
    public StructureDTO updateCharacter(Integer structureId, Integer characterId, Integer v) throws JsonProcessingException {
        StructureRecord structureRecord = structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord == null)
            throw new RuntimeException("structure does not exist");
        if (structureRecord.getStructureClass() != StructureClass.CAMP || structureRecord.getLevel() == 0)
            throw new RuntimeException("struct should be camp to train");
        if (structureRecord.getRemainingRound() > 0)
            throw new RuntimeException("structure is being used");
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord == null)
            throw new RuntimeException("character does not exist");
        if (characterRecord.getActionState() == 2)
            throw new RuntimeException("character already act");
        if (v < 0 || v >= 2)
            throw new RuntimeException("v is wrong");
        if (!Objects.equals(structureRecord.getX(), characterRecord.getX()) || !Objects.equals(structureRecord.getY(), characterRecord.getY()))
            throw new RuntimeException("character does not at structure");

        characterRecord.setActionState(2);
        characterRecordRepository.save(characterRecord);

        structureRecord.setValue(v);
        structureRecord.setRemainingRound(2);
        structureRecordRepository.save(structureRecord);
        return DTOUtil.toStructureDTO(structureRecord);
    }

    @Override
    public StructureDTO earnStars(Integer structureId, Integer characterId) throws JsonProcessingException {
        StructureRecord structureRecord = structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord == null)
            throw new RuntimeException("structure does not exist");
        if (structureRecord.getStructureClass() != StructureClass.MARKET || structureRecord.getLevel() == 0)
            throw new RuntimeException("struct should be market to earnStars");
        if (structureRecord.getRemainingRound() > 0)
            throw new RuntimeException("structure is being used");
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord == null)
            throw new RuntimeException("character does not exist");
        if (characterRecord.getActionState() == 2)
            throw new RuntimeException("character already act");
        if (!Objects.equals(structureRecord.getX(), characterRecord.getX()) || !Objects.equals(structureRecord.getY(), characterRecord.getY()))
            throw new RuntimeException("character does not at structure");

        characterRecord.setActionState(2);
        characterRecordRepository.save(characterRecord);

        structureRecord.setRemainingRound(1);
        structureRecordRepository.save(structureRecord);
        return DTOUtil.toStructureDTO(structureRecord);
    }

    @Override
    public StructureDTO updateTechnologies(Integer structureId, Integer characterId, Integer v) throws JsonProcessingException {
        StructureRecord structureRecord = structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord == null)
            throw new RuntimeException("structure does not exist");
        if (structureRecord.getStructureClass() != StructureClass.INSTITUTE || structureRecord.getLevel() == 0)
            throw new RuntimeException("structure should be institute to updateTechnologies");
        if (structureRecord.getRemainingRound() > 0)
            throw new RuntimeException("structure is being used");
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord == null)
            throw new RuntimeException("character does not exist");
        if (characterRecord.getActionState() == 2)
            throw new RuntimeException("character already act");
        if (characterRecord.getCharacterClass() != CharacterClass.SCHOLAR)
            throw new RuntimeException("character should be scholar to updateTechnologies");
        if (v < 0 || v > 10)
            throw new RuntimeException("Index is not valid");
        if (!Objects.equals(structureRecord.getX(), characterRecord.getX()) || !Objects.equals(structureRecord.getY(), characterRecord.getY()))
            throw new RuntimeException("character does not at structure");

        Player player = structureRecord.getPlayer();

        String techTreeFeasible = player.getTechtreeFeasible();
        String[] feasible = techTreeFeasible.split(", ");
        if (feasible[v].equals("0"))
            throw new RuntimeException("Tree node is not feasible");
        if (Player.map.get(Player.name[v])[1] > player.getStars())
            throw new RuntimeException("player does not have enough money");
        player.setStars(player.getStars() - Player.map.get(Player.name[v])[1]);
        playerRepository.save(player);

        characterRecord.setActionState(2);
        characterRecordRepository.save(characterRecord);

        structureRecord.setValue(v);
        structureRecord.setRemainingRound(Player.map.get(Player.name[v])[0]);
        structureRecordRepository.save(structureRecord);
        return DTOUtil.toStructureDTO(structureRecord);
    }

    @Override
    public StructureDTO updateStructure(Integer structureId, Integer v) throws JsonProcessingException {
        StructureRecord structureRecord = structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord == null)
            throw new RuntimeException("structure does not exist");
        if (structureRecord.getStructureClass()==StructureClass.BASE)
            throw new RuntimeException("Base can not be update");
        if (structureRecord.getStructureClass() == StructureClass.RELIC)
            throw new RuntimeException("Relic can not be update");
        if (structureRecord.getStructureClass() == StructureClass.INSTITUTE && structureRecord.getLevel() == 1)
            throw new RuntimeException("institute can not be update");
        if (structureRecord.getRemainingRound() > 0)
            throw new RuntimeException("structure can not be update when be using");

        Player player = structureRecord.getPlayer();
        if (player == null)
            throw new RuntimeException("player does not exist");
        int costMoney = structureRecord.getLevel() * 10;
        if (player.getStars() < costMoney)
            throw new RuntimeException("player does not have enough money");

        if (costMoney == 0) {
            if (v < 0 || v >= 3)
                throw new RuntimeException("v is wrong");
            StructureClass structureClass;
            if (v == 0) structureClass = StructureClass.CAMP;
            else {
                if (v == 1) structureClass = StructureClass.MARKET;
                else structureClass = StructureClass.INSTITUTE;
            }
            structureRecord.setStructureClass(structureClass);
        }

        player.setStars(player.getStars() - costMoney);
        playerRepository.save(player);
        structureRecord.setLevel(structureRecord.getLevel() + 1);
        structureRecordRepository.save(structureRecord);
        return DTOUtil.toStructureDTO(structureRecord);
    }

    @Override
    public StructureDTO healStructure(Integer structureId) throws JsonProcessingException {
        StructureRecord structureRecord = structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord == null)
            throw new RuntimeException("structure does not exist");

        if (structureRecord.getStructureClass() == StructureClass.RELIC)
            throw new RuntimeException("Relic can not be heal");

        Player player = structureRecord.getPlayer();
        if (player == null)
            throw new RuntimeException("player does not exist");

        int costMoney = 50-structureRecord.getHp() ;
        if (player.getStars() < costMoney)
            throw new RuntimeException("player does not have enough money");


        player.setStars(player.getStars() - costMoney);
        structureRecord.setHp(50);
        playerRepository.save(player);
        structureRecordRepository.save(structureRecord);
        return DTOUtil.toStructureDTO(structureRecord);
    }
}
