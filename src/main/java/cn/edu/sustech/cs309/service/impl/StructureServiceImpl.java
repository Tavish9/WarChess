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
    public StructureDTO buyCharacter(Integer structureId, Integer playerId, Integer id, Integer x, Integer y,Integer type) throws JsonProcessingException {
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
        if (id < 0 || characterDTOS.size() <= id)
            throw new RuntimeException("character does not exist");
        if (type<0 || type>=3)
            throw new RuntimeException("type is wrong");

        player.setStars(player.getStars()-3);
        player=playerRepository.save(player);
        CharacterDTO characterDTO_add= characterDTOS.get(id);
        characterDTOS.remove(id);
        structureRecord.setCharacter(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(characterDTOS));
        structureRecord=structureRecordRepository.save(structureRecord);
        //todo:set action range
        int actionRange=1;
        int attack=characterDTO_add.attack();
        int defense=characterDTO_add.defense();
        int hp=characterDTO_add.hp();

        CharacterClass characterClass;
        if (type==0) {
            characterClass = CharacterClass.WARRIOR;
            attack=(attack*15+14)/10;
            defense=(defense*15+14)/10;
            hp=(hp*15+14)/10;
        }
        else {
            if (type == 1) {
                characterClass = CharacterClass.EXPLORER;
                actionRange = 2;
            }
            else
                characterClass = CharacterClass.SCHOLAR;
        }
        CharacterRecord characterRecord = CharacterRecord.builder().characterClass(characterClass)
                .player(player).actionState(2).name(characterDTO_add.name()).level(1).attack(attack)
                .defense(defense).hp(hp).actionRange(actionRange)
                .build();
        log.debug(playerId+"buy a character");

        characterRecordRepository.save(characterRecord);

        return DTOUtil.toStructureDTO(structureRecord);
    }

    @Override
    public StructureDTO updateCharacter(Integer structureId, Integer characterId, Integer v) throws JsonProcessingException {
        StructureRecord structureRecord = structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord == null)
            throw new RuntimeException("structure does not exist");
        if (structureRecord.getStructureClass()!=StructureClass.CAMP||structureRecord.getLevel()==0)
            throw new RuntimeException("struct should be camp to train");
        if (structureRecord.getRemainingRound()>0)
            throw new RuntimeException("structure is being used");
        CharacterRecord characterRecord=characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord == null)
            throw new RuntimeException("character does not exist");
        if (characterRecord .getActionState()==2)
            throw new RuntimeException("character already act");
        if (v<0 || v>=2)
            throw new RuntimeException("v is wrong");

        characterRecord.setActionState(2);
        characterRecordRepository.save(characterRecord);

        structureRecord.setValue(v);
        structureRecord.setRemainingRound(2);
        structureRecord=structureRecordRepository.save(structureRecord);
        return DTOUtil.toStructureDTO(structureRecord);
    }

    @Override
    public StructureDTO earnStars(Integer structureId, Integer characterId) throws JsonProcessingException {
        StructureRecord structureRecord = structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord == null)
            throw new RuntimeException("structure does not exist");
        if (structureRecord.getStructureClass()!=StructureClass.MARKET||structureRecord.getLevel()==0)
            throw new RuntimeException("struct should be market to earnStars");
        if (structureRecord.getRemainingRound() > 0)
            throw new RuntimeException("structure is being used");
        CharacterRecord characterRecord=characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord == null)
            throw new RuntimeException("character does not exist");
        if (characterRecord .getActionState()==2)
            throw new RuntimeException("character already act");

        characterRecord.setActionState(2);
        characterRecordRepository.save(characterRecord);

        structureRecord.setRemainingRound(1);
        structureRecord=structureRecordRepository.save(structureRecord);
        return DTOUtil.toStructureDTO(structureRecord);
    }

    @Override
    public StructureDTO updateTechnologies(Integer structureId, Integer characterId, Integer v ,Integer round) throws JsonProcessingException {
        StructureRecord structureRecord = structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord == null)
            throw new RuntimeException("structure does not exist");
        if (structureRecord.getStructureClass()!=StructureClass.INSTITUTE ||structureRecord.getLevel()==0)
            throw new RuntimeException("structure should be institute to updateTechnologies");
        if (structureRecord.getRemainingRound() > 0)
            throw new RuntimeException("structure is being used");
        CharacterRecord characterRecord=characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord == null)
            throw new RuntimeException("character does not exist");
        if (characterRecord.getActionState()==2)
            throw new RuntimeException("character already act");
        if (characterRecord.getCharacterClass()!=CharacterClass.SCHOLAR)
            throw new RuntimeException("character should be scholar to updateTechnologies");

        /*update tech tree*/
        Player player=structureRecord.getPlayer();
        String techTreeRemainRound = player.getTechtreeRemainRound();
        String[] remainCnt = techTreeRemainRound.split(", ");
        StringBuilder newTechTreeRemainRound = new StringBuilder();
        for (int i = 0; i < remainCnt.length; i++) {
            int c = Integer.parseInt(remainCnt[i]);
            if (i==v)
                newTechTreeRemainRound.append(1).append(", ");
            else
                newTechTreeRemainRound.append(c).append(", ");
        }
        player.setTechtreeRemainRound(newTechTreeRemainRound.substring(0, newTechTreeRemainRound.length() - 2));
        playerRepository.save(player);

        characterRecord.setActionState(2);
        characterRecordRepository.save(characterRecord);

        structureRecord.setValue(v);
        structureRecord.setRemainingRound(round);
        structureRecord=structureRecordRepository.save(structureRecord);
        return DTOUtil.toStructureDTO(structureRecord);
    }

    @Override
    public StructureDTO updateStructure(Integer structureId, Integer v) throws JsonProcessingException {
        StructureRecord structureRecord = structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord == null)
            throw new RuntimeException("structure does not exist");

        if (structureRecord.getStructureClass()==StructureClass.RELIC)
            throw  new RuntimeException("Relic can not be update");
        if (structureRecord.getStructureClass()==StructureClass.INSTITUTE)
            throw  new RuntimeException("institute can not be update");
        if (structureRecord.getRemainingRound()>0)
            throw new RuntimeException("structure can not be update when be using");

        Player player= structureRecord.getPlayer();
        if (player==null)
            throw new RuntimeException("structure does not exist");
        int costMoney=structureRecord.getLevel()*10;
        if (player.getStars()<costMoney)
            throw new RuntimeException("player does not have enough money");

        if (costMoney==0){
            if (v<0 || v>=3 )
                throw new RuntimeException("v is wrong");
            StructureClass structureClass;
            if (v==0)structureClass=StructureClass.CAMP;
            else {
                if (v==1)structureClass=StructureClass.MARKET;
                else structureClass=StructureClass.INSTITUTE;
            }
            structureRecord.setStructureClass(structureClass);
        }

        player.setStars(player.getStars()-costMoney);
        playerRepository.save(player);
        structureRecord.setLevel(structureRecord.getLevel()+1);

        return DTOUtil.toStructureDTO(structureRecord);
    }
}
