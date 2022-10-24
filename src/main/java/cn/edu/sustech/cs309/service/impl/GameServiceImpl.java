package cn.edu.sustech.cs309.service.impl;

import cn.edu.sustech.cs309.domain.*;
import cn.edu.sustech.cs309.dto.*;
import cn.edu.sustech.cs309.repository.*;
import cn.edu.sustech.cs309.service.GameService;
import cn.edu.sustech.cs309.utils.DTOUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ArchiveRepository archiveRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameRecordRepository gameRecordRepository;

    @Autowired
    private StructureRecordRepository structureRecordRepository;

    @Autowired
    private CharacterRecordRepository characterRecordRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<ArchiveDTO> getArchives(String username) throws JsonProcessingException {
        Account account = accountRepository.findAccountByUsername(username);
        if (account == null)
            throw new RuntimeException("Account does not exist");
        return DTOUtil.toArchiveDTOs(account.getArchives());
    }

    @Override
    public GameDTO loadArchive(Integer archiveId) throws JsonProcessingException {
        Archive archive = archiveRepository.findArchiveById(archiveId);
        if (archive == null) {
            throw new RuntimeException("Archive does not exist");
        }
        Game game = archive.getGame();
        Player human = game.getPlayer1();
        Player ai = game.getPlayer2();
        GameRecord last = gameRecordRepository.findFirstByGameOrderByIdDesc(game);
        if (last == null) {
            return DTOUtil.toGameDTO(human, ai, 1, game.getPlayerFirst());
        }
        boolean playerFirst;
        if (game.getPlayerFirst())
            playerFirst = last.getRound() % 2 == 0;
        else
            playerFirst = last.getRound() % 2 == 1;
        return DTOUtil.toGameDTO(human, ai, last.getRound() / 2, playerFirst);
    }

    @Override
    public GameDTO ini(String username1, String username2) throws JsonProcessingException {
        Account account2 = null;
        if (StringUtils.hasText(username2)) {
            account2 = accountRepository.findAccountByUsername(username2);
            if (account2 == null)
                throw new RuntimeException("Account does not exist");
        }
        Account account1 = accountRepository.findAccountByUsername(username1);
        if (account1 == null)
            throw new RuntimeException("Account does not exist");
        Random random = new Random();
        // TODO: specify the map
        Game game = Game.builder().playerFirst(random.nextBoolean()).build();
        gameRepository.save(game);
        Player player1 = Player.builder().account(account1).game(game).build();
        Player player2 = Player.builder().account(Objects.requireNonNullElse(account2, account1)).game(game).build();
        playerRepository.save(player1);
        playerRepository.save(player2);
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        gameRepository.save(game);
        return DTOUtil.toGameDTO(player1, player2, 1, game.getPlayerFirst());
    }

    @Override
    public GameDTO update(Integer playerId) throws JsonProcessingException {
        Player player1 = playerRepository.findPlayerById(playerId);
        if (player1 == null) {
            throw new RuntimeException("Player does not exist");
        }
        player1.setProsperityDegree(prosperityDegree(player1));
        player1.setPeaceDegree(peaceDegree(player1));
        String techTreeRemainRound = player1.getTechtreeRemainRound();
        String[] remainCnt = techTreeRemainRound.split(", ");
        String techTreeLight = player1.getTechtreeLight();
        String[] light = techTreeLight.split(", ");
        StringBuilder newTechTreeRemainRound = new StringBuilder();
        StringBuilder newTechTreeLight = new StringBuilder();
        for (int i = 0; i < remainCnt.length; i++) {
            int c = Integer.parseInt(remainCnt[i]);
            if (c == 0)
                newTechTreeLight.append(light[i]).append(", ");
            else {
                c--;
                if (c == 0)
                    newTechTreeLight.append("1").append(", ");
                else
                    newTechTreeLight.append("0").append(", ");
            }
            newTechTreeRemainRound.append(c).append(", ");
        }
        player1.setTechtreeRemainRound(newTechTreeRemainRound.substring(0, newTechTreeRemainRound.length() - 2));
        player1.setTechtreeLight(newTechTreeLight.substring(0, newTechTreeLight.length() - 2));
        playerRepository.save(player1);
        List<StructureRecord> player1Structures = structureRecordRepository.findStructureRecordByPlayerAndHpGreaterThan(player1, 0);
        for (StructureRecord s : player1Structures) {
            if (s.getRemainingRound() > 0)
                s.setRemainingRound(s.getRemainingRound() - 1);
        }
        Game game = player1.getGame();
        GameRecord last = gameRecordRepository.findFirstByGameOrderByIdDesc(game);
        int round;
        if (last == null) {
            round = 1;
        } else {
            round = last.getRound() + 1;
        }
        boolean currentPlayer;
        Player player2;
        GameRecord gameRecord = GameRecord.builder().game(game).round(round).build();
        if (player1.getId().equals(game.getPlayer1().getId())) {
            currentPlayer = true;
            player2 = game.getPlayer2();
            player2.setProsperityDegree(prosperityDegree(player2));
            player2.setProsperityDegree(prosperityDegree(player2));
            gameRecord.setPlayer1(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(player1));
            gameRecord.setPlayer2(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(player2));
        } else {
            currentPlayer = false;
            player2 = game.getPlayer1();
            player2.setProsperityDegree(prosperityDegree(player2));
            player2.setProsperityDegree(prosperityDegree(player2));
            gameRecord.setPlayer1(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(player2));
            gameRecord.setPlayer2(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(player1));
        }
        playerRepository.save(player2);
        gameRecordRepository.save(gameRecord);
        return DTOUtil.toGameDTO(game.getPlayer1(), game.getPlayer2(), round, currentPlayer);
    }

    private int prosperityDegree(Player player) {
        int structureCount = Objects.requireNonNullElse(structureRecordRepository.countByPlayerAndHpGreaterThan(player, 0), 0);
        int structureLevel = Objects.requireNonNullElse(structureRecordRepository.getSumLevel(player.getId()), 0);
        return 3 * structureCount + 2 * structureLevel;
    }

    private int peaceDegree(Player player) {
        int allCharacter = Objects.requireNonNullElse(characterRecordRepository.countByPlayer(player), 0);
        int deadCharacter = Objects.requireNonNullElse(characterRecordRepository.countByPlayerAndHpEquals(player, 0), 0);
        return allCharacter - deadCharacter;
    }
}
