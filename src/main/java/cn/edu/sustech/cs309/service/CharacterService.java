package cn.edu.sustech.cs309.service;


import cn.edu.sustech.cs309.dto.CharacterDTO;
import cn.edu.sustech.cs309.dto.StructureDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CharacterService {

    CharacterDTO getCharacter(Integer characterId);

    CharacterDTO dismissCharacter(Integer characterId);
    CharacterDTO moveCharacter(Integer characterId,Integer x,Integer y);

    CharacterDTO attackCharacter(Integer characterId,Integer attackId);
    StructureDTO attackStructure(Integer characterId, Integer attackId) throws JsonProcessingException;
}
