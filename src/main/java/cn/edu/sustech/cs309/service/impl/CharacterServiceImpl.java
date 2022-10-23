package cn.edu.sustech.cs309.service.impl;

import cn.edu.sustech.cs309.domain.Account;
import cn.edu.sustech.cs309.domain.StructureRecord;
import cn.edu.sustech.cs309.repository.StructureRecordRepository;
import cn.edu.sustech.cs309.utils.DTOUtil;
import cn.edu.sustech.cs309.dto.CharacterDTO;
import cn.edu.sustech.cs309.repository.CharacterRecordRepository;
import cn.edu.sustech.cs309.service.CharacterService;
import cn.edu.sustech.cs309.domain.CharacterRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CharacterServiceImpl implements CharacterService {

    @Autowired
    private CharacterRecordRepository characterRecordRepository;

    @Autowired
    private StructureRecordRepository structureRecordRepository;

    @Override
    public CharacterDTO getCharacter(Integer characterId) {
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord != null)
            return DTOUtil.toCharacterDTO(characterRecord);
        else
            throw new RuntimeException("character does not exist");
    }

    @Override
    public CharacterDTO dismissCharacter(Integer characterId){
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord != null) {
            //TODO:check
            characterRecord = null;
            characterRecord = characterRecordRepository.save(characterRecord);
            return DTOUtil.toCharacterDTO(characterRecord);
        }
        else
            throw new RuntimeException("character does not exist");
    }

    @Override
    public CharacterDTO moveCharacter(Integer characterId, Integer x, Integer y) {
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord == null)
            throw new RuntimeException("character does not exist");
        if (characterRecord.getHp() <= 0)
            throw new RuntimeException("character is dead");
        if (characterRecord.getActionState() >= 1)
            throw new RuntimeException("character can't move in this round");
        characterRecord.setX(x);
        characterRecord.setY(y);
        characterRecord.setActionState(1);
        log.debug("move " + characterId + " to (" + x + "," + y + ")");
        characterRecord = characterRecordRepository.save(characterRecord);
        return DTOUtil.toCharacterDTO(characterRecord);
    }

    @Override
    public CharacterDTO attackCharacter(Integer characterId, Integer attackId) {
        CharacterRecord characterRecord_attack = characterRecordRepository.findCharacterRecordById(characterId);
        CharacterRecord characterRecord_attacked = characterRecordRepository.findCharacterRecordById(attackId);
        if (characterRecord_attack == null)
            throw new RuntimeException("attacker does not exist");
        if (characterRecord_attack.getHp() <= 0)
            throw new RuntimeException("attacker is dead");
        if (characterRecord_attacked == null)
            throw new RuntimeException("character attacked does not exist");
        if (characterRecord_attacked.getHp() <= 0)
            throw new RuntimeException("character attacked is dead");
        if (characterRecord_attack.getActionState() == 2)
            throw new RuntimeException("character can't attack in this round");
        characterRecord_attack.setActionState(2);
        characterRecord_attack = characterRecordRepository.save(characterRecord_attack);
        if (characterRecord_attack.getAttack() > characterRecord_attacked.getDefense()) {
            Integer newHp = characterRecord_attacked.getHp() - characterRecord_attack.getAttack() + characterRecord_attacked.getDefense();
            characterRecord_attacked.setHp(newHp);
            characterRecordRepository.save(characterRecord_attacked);
        }
        characterRecord_attack = characterRecordRepository.save(characterRecord_attack);
        return DTOUtil.toCharacterDTO(characterRecord_attack);
    }

    @Override
    public CharacterDTO attackStructure(Integer characterId, Integer attackId) {
        CharacterRecord character = characterRecordRepository.findCharacterRecordById(characterId);
        StructureRecord structure = structureRecordRepository.findStructureRecordById(attackId);
        if (character == null)
            throw new RuntimeException("attacker does not exist");
        if (character.getHp() <= 0)
            throw new RuntimeException("attacker is dead");
        if (structure == null)
            throw new RuntimeException("character attacked does not exist");
        if (structure.getPlayer()==character.getPlayer())
            throw new RuntimeException("can not attack structure in same camp");
        if (character.getActionState() == 2)
            throw new RuntimeException("character can't attack in this round");
        character.setActionState(2);
        character=characterRecordRepository.save(character);

        Integer newHp = structure.getHp()-character.getAttack();
        if (newHp>0)
            structure.setHp(newHp);
        else {
            structure.setHp(1);
            structure.setPlayer(character.getPlayer());
            //TODO:set palyer?
        }
        structureRecordRepository.save(structure);
        return DTOUtil.toCharacterDTO(character);
    }

}
