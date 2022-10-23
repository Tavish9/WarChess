package cn.edu.sustech.cs309.service;


import cn.edu.sustech.cs309.dto.CharacterDTO;

public interface CharacterService {

    CharacterDTO getCharacter(Integer characterId);
q
    CharacterDTO moveCharacter(Integer characterId,Integer x,Integer y);

    CharacterDTO attackCharacter(Integer characterId,Integer attackId);
    CharacterDTO attackStructure(Integer characterId,Integer attackId);
}
