package cn.edu.sustech.cs309.service.impl;

import cn.edu.sustech.cs309.domain.*;
import cn.edu.sustech.cs309.dto.ArchiveDTO;
import cn.edu.sustech.cs309.dto.CharacterDTO;
import cn.edu.sustech.cs309.dto.GameDTO;
import cn.edu.sustech.cs309.dto.ShopDTO;
import cn.edu.sustech.cs309.repository.*;
import cn.edu.sustech.cs309.service.GameService;
import cn.edu.sustech.cs309.utils.DTOUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

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
    private EquipmentRecordRepository equipmentRecordRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ItemRecordRepository itemRecordRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MountRepository mountRepository;

    @Autowired
    private MountRecordRepository mountRecordRepository;

    @Autowired
    private ShopRecordRepository shopRecordRepository;

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
        GameRecord last = gameRecordRepository.findFirstByGameOrderByIdDesc(game);
        if (last == null) {
            return DTOUtil.toGameDTO(game, null, 1, game.getPlayerFirst());
        }
        Game oldGame = last.getGame();
        Game newGame = Game.builder().playerFirst(oldGame.getPlayerFirst()).map(oldGame.getMap()).build();
        newGame = gameRepository.save(newGame);
        Player oldPlayer1 = oldGame.getPlayer1();
        Player oldPlayer2 = oldGame.getPlayer2();
        Player newPlayer1 = Player.builder()
                .account(oldPlayer1.getAccount())
                .stars(oldPlayer1.getStars())
                .game(newGame)
                .peaceDegree(oldPlayer1.getPeaceDegree())
                .prosperityDegree(oldPlayer1.getProsperityDegree())
                .techtreeFeasible(oldPlayer1.getTechtreeFeasible())
                .techtreeRemainRound(oldPlayer1.getTechtreeRemainRound())
                .vision(oldPlayer1.getVision()).build();
        // TODO: copy data
        Player newPlayer2 = Player.builder()
                .account(oldPlayer2.getAccount())
                .stars(oldPlayer2.getStars())
                .game(newGame)
                .peaceDegree(oldPlayer2.getPeaceDegree())
                .prosperityDegree(oldPlayer2.getProsperityDegree())
                .techtreeFeasible(oldPlayer2.getTechtreeFeasible())
                .techtreeRemainRound(oldPlayer2.getTechtreeRemainRound())
                .vision(oldPlayer2.getVision()).build();
        newPlayer1 = playerRepository.save(newPlayer1);
        newPlayer2 = playerRepository.save(newPlayer2);
        newGame.setPlayer1(newPlayer1);
        newGame.setPlayer2(newPlayer2);
        newGame = gameRepository.save(newGame);
        // TODO: copy data
        boolean currentPlayer;
        if (game.getPlayerFirst())
            currentPlayer = last.getRound() % 2 == 0;
        else
            currentPlayer = last.getRound() % 2 == 1;
        return DTOUtil.toGameDTO(newGame, null, last.getRound() / 2, currentPlayer);
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
        StringBuilder stringBuilder = new StringBuilder();
        for (String name : Player.name) {
            stringBuilder.append(Player.map.get(name)[0]).append(", ");
        }
        player1.setTechtreeRemainRound(stringBuilder.substring(0, stringBuilder.length() - 2));
        player2.setTechtreeRemainRound(stringBuilder.substring(0, stringBuilder.length() - 2));
        playerRepository.save(player1);
        playerRepository.save(player2);
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game = gameRepository.save(game);

        // TODO: character position
        CharacterRecord character1 = randomCharacter();
        int type1 = (character1.getAttack() + character1.getDefense() + character1.getHp()) % 3;
        character1.updateAttribute(type1);
//        character1.setX();
//        character1.setY();
        character1.setPlayer(player1);
        characterRecordRepository.save(character1);

        CharacterRecord character2 = randomCharacter();
        int type2 = (character1.getAttack() + character1.getDefense() + character1.getHp()) % 3;
        character2.updateAttribute(type2);
//        character2.setX();
//        character2.setY();
        character2.setPlayer(player2);
        characterRecordRepository.save(character2);

        // TODO: relic
        StructureRecord relic1 = StructureRecord.builder().game(game).structureClass(StructureClass.RELIC).build();
        relic1 = structureRecordRepository.save(relic1);

        // TODO: village
        StructureRecord village1 = StructureRecord.builder().game(game).structureClass(StructureClass.VILLAGE).build();
        List<CharacterDTO> characterDTOS = new ArrayList<>(3);
        for (int j = 0; j < 3; j++) {
            characterDTOS.add(DTOUtil.toCharacterDTO(randomCharacter()));
        }
        village1.setCharacter(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(characterDTOS));
        village1 = structureRecordRepository.save(village1);

        return DTOUtil.toGameDTO(game, null, 1, game.getPlayerFirst());
    }

    @Override
    public GameDTO update(Integer playerId) throws JsonProcessingException {
        Player player1 = playerRepository.findPlayerById(playerId);
        if (player1 == null) {
            throw new RuntimeException("Player does not exist");
        }
        player1.setProsperityDegree(prosperityDegree(player1));
        player1.setPeaceDegree(peaceDegree(player1));

        /*every round get some star*/
        player1.setStars(player1.getStars() + 3);

        List<StructureRecord> player1Structures = structureRecordRepository.findStructureRecordsByPlayer(player1);
        List<CharacterRecord> characterRecords = characterRecordRepository.findCharacterRecordsByPlayer(player1);
        for (CharacterRecord c : characterRecords) {
            if (c.getHp() > 0) {
                c.setActionState(0);
                characterRecordRepository.save(c);
            }
        }
        String techTreeRemainRound = player1.getTechtreeRemainRound();
        String[] remainCnt = techTreeRemainRound.split(", ");
        int[] r = Arrays.stream(remainCnt).mapToInt(Integer::parseInt).toArray();

        for (StructureRecord s : player1Structures) {
            if (s.getRemainingRound() > 0) {
                if (s.getStructureClass() == StructureClass.INSTITUTE) {
                    if (r[s.getValue()] > 0) {
                        r[s.getValue()]--;
                    }
                }
                if (s.getRemainingRound() == 1) {
                    if (s.getStructureClass() == StructureClass.CAMP) {
                        if (s.getValue() == 0) {
                            //update hp
                            for (CharacterRecord c : characterRecords) {
                                if (c.getHp() > 0 && c.getX().equals(s.getX()) && c.getY().equals(s.getY())) {
                                    c.setHp(Math.min(50, c.getHp() + s.getLevel() * 2));
                                    c.setLevel(c.getLevel() + 1);
                                    characterRecordRepository.save(c);
                                }
                            }
                        } else {
                            //update attack
                            for (CharacterRecord c : characterRecords) {
                                if (c.getHp() > 0 && c.getX().equals(s.getX()) && c.getY().equals(s.getY())) {
                                    c.setAttack(Math.min(50, c.getAttack() + s.getLevel() * 2));
                                    c.setLevel(c.getLevel() + 1);
                                    characterRecordRepository.save(c);
                                }
                            }
                        }
                    } else {
                        if (s.getStructureClass() == StructureClass.MARKET) {
                            player1.setStars(player1.getStars() + 5L * s.getLevel());
                        }
                    }
                } else {
                    for (CharacterRecord c : characterRecords) {
                        if (c.getHp() > 0 && c.getX().equals(s.getX()) && c.getY().equals(s.getY())) {
                            c.setActionState(2);
                            characterRecordRepository.save(c);
                        }
                    }
                }
                s.setRemainingRound(s.getRemainingRound() - 1);
            }
        }
        techTreeRemainRound = Arrays.toString(r);
        player1.setTechtreeRemainRound(techTreeRemainRound.substring(1, techTreeRemainRound.length() - 1));
        playerRepository.save(player1);

        Game game = player1.getGame();
        GameRecord last = gameRecordRepository.findFirstByGameOrderByIdDesc(game);
        int round;
        if (last == null) {
            round = 1;
        } else {
            round = last.getRound() + 1;
        }

        int marketCnt = structureRecordRepository.countByPlayerAndStructureClass(player1.getId(), StructureClass.MARKET.name());
        List<EquipmentRecord> e = new ArrayList<>(2 * marketCnt);
        List<ItemRecord> it = new ArrayList<>(2 * marketCnt);
        List<MountRecord> m = new ArrayList<>(2 * marketCnt);
        int[][] index = new int[3][2 * marketCnt];
        ShopRecord shopRecord;
        for (int i = 0; i < 2 * marketCnt; i++) {
            EquipmentRecord equipmentRecord = randomEquipmentRecord(player1);
            Equipment equipment = equipmentRecord.getEquipment();
            shopRecord = ShopRecord.builder()
                    .round(round + 2)
                    .name(equipment.getName())
                    .description(equipment.getDescription())
                    .shopClass(ShopClass.EQUIPMENT)
                    .cost(7).propid(equipmentRecord.getId()).build();
            shopRecord = shopRecordRepository.save(shopRecord);
            index[0][i] = shopRecord.getId();
            e.add(equipmentRecord);

            ItemRecord itemRecord = randomItemRecord(player1);
            Item item = itemRecord.getItem();
            shopRecord = ShopRecord.builder()
                    .round(round + 2)
                    .name(item.getName())
                    .description(item.getDescription())
                    .shopClass(ShopClass.ITEM)
                    .cost(7).propid(itemRecord.getId()).build();
            shopRecord = shopRecordRepository.save(shopRecord);
            index[1][i] = shopRecord.getId();
            it.add(itemRecord);

            MountRecord mountRecord = randomMountRecord(player1);
            Mount mount = mountRecord.getMount();
            shopRecord = ShopRecord.builder()
                    .round(round + 2)
                    .name(mount.getName())
                    .description(mount.getDescription())
                    .shopClass(ShopClass.MOUNT)
                    .cost(7).propid(mountRecord.getId()).build();
            shopRecord = shopRecordRepository.save(shopRecord);
            index[2][i] = shopRecord.getId();
            m.add(mountRecord);
        }
        ShopDTO shopDTO = new ShopDTO(DTOUtil.toEquipmentDTOs(e), DTOUtil.toItemDTOs(it), DTOUtil.toMountDTOs(m), index);

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

        // update current shop info
        player1.getShopRecords().clear();
        List<ShopRecord> shopRecords1 = shopRecordRepository.findShopRecordsByPlayerAndRound(player1, round);
        if (shopRecords1 != null)
            player1.getShopRecords().addAll(shopRecords1);
        player2.getShopRecords().clear();
        List<ShopRecord> shopRecords2 = shopRecordRepository.findShopRecordsByPlayerAndRound(player2, round - 1);
        if (shopRecords2 != null)
            player2.getShopRecords().addAll(shopRecords2);
        return DTOUtil.toGameDTO(game, shopDTO, round, currentPlayer);
    }

    private int prosperityDegree(Player player) {
        int structureCount = Objects.requireNonNullElse(structureRecordRepository.countByPlayer(player), 0);
        int structureLevel = Objects.requireNonNullElse(structureRecordRepository.getSumLevel(player.getId()), 0);
        return 3 * structureCount + 2 * structureLevel;
    }

    private int peaceDegree(Player player) {
        int allCharacter = Objects.requireNonNullElse(characterRecordRepository.countByPlayer(player), 0);
        int deadCharacter = Objects.requireNonNullElse(characterRecordRepository.countByPlayerAndHpEquals(player, 0), 0);
        return 2 * allCharacter - 3 * deadCharacter;
    }

    private CharacterRecord randomCharacter() {
        Random r = new Random();
        int tmp1 = r.nextInt(-2, 2);
        int tmp2 = r.nextInt(-1, 1);
        int hp = 10 + 2 * tmp1;
        int attack = 5 + tmp2;
        int defense = 3 - tmp1 - tmp2;
        int actionRange = 1;
        int tmp3 = r.nextInt(1000) % 100;
        CharacterRecord characterRecord = CharacterRecord.builder()
                .name(CharacterRecord.characterName[tmp3]).actionRange(actionRange)
                .hp(hp).attack(attack).defense(defense).build();
        return characterRecordRepository.save(characterRecord);
    }

    private EquipmentRecord randomEquipmentRecord(Player player) {
        Random r = new Random();
        int tmp = 1;
        String techTreeRemainRound = player.getTechtreeRemainRound();
        String[] remainCnt = techTreeRemainRound.split(", ");
        if (Integer.parseInt(remainCnt[3]) == 0) {
            tmp++;
        }
        if (Integer.parseInt(remainCnt[8]) == 0) {
            tmp++;
        }
        if (Integer.parseInt(remainCnt[9]) == 0) {
            tmp++;
        }
        if (Integer.parseInt(remainCnt[10]) == 0) {
            tmp++;
        }
        //1:basic  2:sword  3:shield  4:shield 5:cannon
        int id = r.nextInt(1, tmp + 1);
        Equipment equipment = equipmentRepository.findEquipmentById(id);
        EquipmentRecord equipmentRecord = EquipmentRecord.builder().equipment(equipment).build();
        equipmentRecord = equipmentRecordRepository.save(equipmentRecord);
        return equipmentRecord;
    }

    private MountRecord randomMountRecord(Player player) {
        Random r = new Random();
        int tmp = 1;
        String techTreeRemainRound = player.getTechtreeRemainRound();
        String[] remainCnt = techTreeRemainRound.split(", ");
        if (Integer.parseInt(remainCnt[1]) == 0) {
            tmp++;
        }
        if (Integer.parseInt(remainCnt[4]) == 0) {
            tmp++;
        }
        if (Integer.parseInt(remainCnt[5]) == 0) {
            tmp++;
        }
        //1:basic  2:horse  3:elephant  4:fox
        int id = r.nextInt(1, tmp + 1);
        Mount mount = mountRepository.findMountById(id);
        MountRecord mountRecord = MountRecord.builder().mount(mount).build();
        mountRecord = mountRecordRepository.save(mountRecord);
        return mountRecord;
    }


    private ItemRecord randomItemRecord(Player player) {
        Random r = new Random();
        int tmp = 1;
        String techTreeRemainRound = player.getTechtreeRemainRound();
        String[] remainCnt = techTreeRemainRound.split(", ");
        if (Integer.parseInt(remainCnt[2]) == 0) {
            tmp++;
        }
        if (Integer.parseInt(remainCnt[6]) == 0) {
            tmp++;
        }
        if (Integer.parseInt(remainCnt[7]) == 0) {
            tmp++;
        }
        //1:basic  2:fish  3:beer  4:potion
        int id = r.nextInt(1, tmp + 1);
        Item item = itemRepository.findItemById(id);
        ItemRecord itemRecord = ItemRecord.builder().item(item).build();
        itemRecord = itemRecordRepository.save(itemRecord);
        return itemRecord;
    }
}
