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
import cn.edu.sustech.cs309.service.CharacterService;
import cn.edu.sustech.cs309.utils.DTOUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class CharacterServiceImpl implements CharacterService {

    @Autowired
    private CharacterRecordRepository characterRecordRepository;

    @Autowired
    private StructureRecordRepository structureRecordRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public CharacterDTO getCharacter(Integer characterId) {
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord != null)
            return DTOUtil.toCharacterDTO(characterRecord);
        else
            throw new RuntimeException("character does not exist");
    }

    @Override
    public CharacterDTO dismissCharacter(Integer characterId) {
        CharacterRecord characterRecord = characterRecordRepository.findCharacterRecordById(characterId);
        if (characterRecord != null) {
            characterRecord.setPlayer(null);
            characterRecordRepository.save(characterRecord);
            return DTOUtil.toCharacterDTO(characterRecord);
        } else
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
        characterRecordRepository.save(characterRecord);
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
        if (characterRecord_attacked.getPlayer() == null)
            throw new RuntimeException("character attacked is dismissed");
        if (characterRecord_attacked.getHp() <= 0)
            throw new RuntimeException("character attacked is dead");
        if (characterRecord_attack.getActionState() == 2)
            throw new RuntimeException("character can't attack in this round");
        characterRecord_attack.setActionState(2);
        characterRecordRepository.save(characterRecord_attack);
        Integer attack=characterRecord_attack.getAttack();
        if (characterRecord_attack.getEquipmentRecord()!=null) {
            attack += characterRecord_attack.getEquipmentRecord().getEquipment().getAttack();
        }
        if (characterRecord_attacked.getMountRecord()!=null){
            attack += characterRecord_attack.getMountRecord().getMount().getAttack();
        }
        Integer defense=characterRecord_attacked.getDefense();
        if (characterRecord_attacked.getEquipmentRecord()!=null){
            defense+=characterRecord_attacked.getEquipmentRecord().getEquipment().getDefense();
        }
        if (characterRecord_attacked.getMountRecord()!=null){
            defense+=characterRecord_attacked.getMountRecord().getMount().getDefense();
        }
        Integer damage=attack-defense;
        if ( damage>0 ) {
            int newHp = characterRecord_attacked.getHp() - damage;
            characterRecord_attacked.setHp(Math.max(0, newHp));
            characterRecordRepository.save(characterRecord_attacked);
            if (newHp <= 0) {
                int x = characterRecord_attacked.getX(), y = characterRecord_attacked.getY();
                List<StructureRecord> structureRecords = structureRecordRepository.findStructureRecordsByPlayer(characterRecord_attacked.getPlayer());
                for (StructureRecord s : structureRecords) {
                    if (s.getX().equals(x) && s.getY().equals(y)) {
                        if (s.getStructureClass() == StructureClass.INSTITUTE && s.getRemainingRound() > 0) {
                            Player player = s.getPlayer();
                            String techTreeRemainRound = player.getTechtreeRemainRound();
                            String[] remain = techTreeRemainRound.split(", ");
                            int[] r = Arrays.stream(remain).mapToInt(Integer::parseInt).toArray();
                            r[s.getValue()] = Player.map.get(Player.name[s.getValue()])[0];
                            techTreeRemainRound = Arrays.toString(r);
                            String techTreeFeasible = player.getTechtreeFeasible();
                            String[] feasible = techTreeFeasible.split(", ");
                            int[] f= Arrays.stream(feasible).mapToInt(Integer::parseInt).toArray();
                            f[s.getValue()]=1;
                            String feasibleStr=Arrays.toString(f);
                            player.setTechtreeFeasible(feasibleStr.substring(1,feasibleStr.length()-1));
                            player.setTechtreeRemainRound(techTreeRemainRound.substring(1, techTreeRemainRound.length() - 1));
                            playerRepository.save(player);
                        }
                        s.setRemainingRound(0);
                        s.setValue(0);
                        structureRecordRepository.save(s);
                    }
                }
            }
        }
        return DTOUtil.toCharacterDTO(characterRecord_attacked);
    }

    @Override
    public StructureDTO attackStructure(Integer characterId, Integer attackId) throws JsonProcessingException {
        CharacterRecord character = characterRecordRepository.findCharacterRecordById(characterId);
        StructureRecord structure = structureRecordRepository.findStructureRecordById(attackId);
        if (character == null)
            throw new RuntimeException("attacker does not exist");
        if (character.getHp() <= 0)
            throw new RuntimeException("attacker is dead");
        if (structure == null)
            throw new RuntimeException("character attacked does not exist");
        if (structure.getPlayer() == character.getPlayer())
            throw new RuntimeException("can not attack structure in same camp");
        if (character.getActionState() == 2)
            throw new RuntimeException("character can't attack in this round");
        character.setActionState(2);
        characterRecordRepository.save(character);

        int newHp = structure.getHp() - character.getAttack();
        if (character.getMountRecord()!=null){
            newHp-=character.getMountRecord().getMount().getAttack();
        }
        if (character.getEquipmentRecord()!=null){
            newHp-=character.getEquipmentRecord().getEquipment().getAttack();
        }
        if (newHp > 0)
            structure.setHp(newHp);
        else {
            if (structure.getStructureClass() == StructureClass.BASE)
                structure.setHp(0);
            else {
                structure.setHp(1);
                if (structure.getStructureClass() == StructureClass.INSTITUTE && structure.getRemainingRound() > 0) {
                    Player player = structure.getPlayer();
                    String techTreeRemainRound = player.getTechtreeRemainRound();
                    String[] remain = techTreeRemainRound.split(", ");
                    int[] r = Arrays.stream(remain).mapToInt(Integer::parseInt).toArray();
                    r[structure.getValue()] = Player.map.get(Player.name[structure.getValue()])[0];
                    techTreeRemainRound = Arrays.toString(r);
                    player.setTechtreeRemainRound(techTreeRemainRound.substring(1, techTreeRemainRound.length() - 1));
                    String techTreeFeasible = player.getTechtreeFeasible();
                    String[] feasible = techTreeFeasible.split(", ");
                    int[] f= Arrays.stream(feasible).mapToInt(Integer::parseInt).toArray();
                    f[structure.getValue()]=1;
                    String feasibleStr=Arrays.toString(f);
                    player.setTechtreeFeasible(feasibleStr.substring(1,feasibleStr.length()-1));
                    playerRepository.save(player);
                }
                structure.setPlayer(character.getPlayer());
                structure.setValue(0);
                structure.setRemainingRound(0);
            }
            structure.setPlayer(character.getPlayer());
            structure.setValue(0);
            structure.setRemainingRound(0);
        }
        structureRecordRepository.save(structure);
        return DTOUtil.toStructureDTO(structure);
    }

}
