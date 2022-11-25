package cn.edu.sustech.cs309.service;


import cn.edu.sustech.cs309.dto.StructureDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface StructureService {

    StructureDTO getStructure(Integer structureId) throws JsonProcessingException;

    StructureDTO getCharacters(Integer structureId) throws JsonProcessingException;

    StructureDTO buyCharacter(Integer structureId,Integer playerId,Integer id,Integer x,Integer y,Integer type) throws JsonProcessingException;

    StructureDTO updateCharacter(Integer structureId,Integer characterId,Integer v) throws JsonProcessingException;

    StructureDTO earnStars(Integer structureId,Integer characterId) throws JsonProcessingException;

    StructureDTO updateTechnologies(Integer structureId,Integer characterId,Integer v) throws JsonProcessingException;

    StructureDTO updateStructure(Integer structureId,Integer v) throws JsonProcessingException;

    StructureDTO healStructure(Integer structureId) throws  JsonProcessingException;
}
