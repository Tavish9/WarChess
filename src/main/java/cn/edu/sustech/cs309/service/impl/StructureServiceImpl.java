package cn.edu.sustech.cs309.service.impl;

import cn.edu.sustech.cs309.domain.CharacterRecord;
import cn.edu.sustech.cs309.domain.Player;
import cn.edu.sustech.cs309.domain.StructureClass;
import cn.edu.sustech.cs309.domain.StructureRecord;
import cn.edu.sustech.cs309.dto.CharacterDTO;
import cn.edu.sustech.cs309.dto.StructureDTO;
import cn.edu.sustech.cs309.repository.CharacterRecordRepository;
import cn.edu.sustech.cs309.repository.PlayerRepository;
import cn.edu.sustech.cs309.repository.StructureRecordRepository;
import cn.edu.sustech.cs309.service.StructureService;
import cn.edu.sustech.cs309.utils.DTOUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Override
    public StructureDTO getStructure(Integer structureId) throws JsonProcessingException {
        StructureRecord structureRecord=structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord != null)
            return DTOUtil.toStructureDTO(structureRecord);
        else
            throw new RuntimeException("Structure does not exist");
    }
    @Override
    public StructureDTO getCharacters(Integer structureId) throws JsonProcessingException {
        StructureRecord structureRecord=structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord != null)
            return DTOUtil.toStructureDTO(structureRecord);
        else
            throw new RuntimeException("Structure does not exist");
    }
    @Override
    public StructureDTO buyCharacter(Integer structureId,Integer playerId,Integer id,Integer x,Integer y) throws JsonProcessingException {
        Player player=playerRepository.findPlayerById(playerId);
        if (player==null)
            throw new RuntimeException("player does not exist");
        if (player.getStars()<3)
            throw new RuntimeException("player does not have enough star");

        StructureRecord structureRecord=structureRecordRepository.findStructureRecordById(structureId);
        if (structureRecord == null)
            throw new RuntimeException("structure does not exist");
        if (structureRecord.getStructureClass()== StructureClass.RELIC)
            throw new RuntimeException("can't buy character in relic");
        StructureDTO structureDTO=DTOUtil.toStructureDTO(structureRecord);
        List<CharacterDTO>  characterDTOS= structureDTO.characterDTOS();
        if (id<0 || characterDTOS.size()<=id)
            throw new RuntimeException("character does not exist");
        //TODO:


        return DTOUtil.toStructureDTO(structureRecord);
    }

    @Override
    public StructureDTO updateCharacter(Integer structureId, Integer characterId, Integer v) {
        return null;
    }

    @Override
    public StructureDTO earnStars(Integer structureId, Integer characterId) {
        return null;
    }

    @Override
    public StructureDTO updateTechnologies(Integer structureId, Integer characterId, Integer v) {
        return null;
    }

    @Override
    public StructureDTO updateStructure(Integer structureId, Integer v) {
        return null;
    }


}
