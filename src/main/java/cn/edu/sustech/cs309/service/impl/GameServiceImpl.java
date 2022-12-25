package cn.edu.sustech.cs309.service.impl;

import cn.edu.sustech.cs309.domain.Map;
import cn.edu.sustech.cs309.domain.*;
import cn.edu.sustech.cs309.dto.*;
import cn.edu.sustech.cs309.repository.*;
import cn.edu.sustech.cs309.service.GameService;
import cn.edu.sustech.cs309.utils.DTOUtil;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
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
    private MapRepository mapRepository;

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
        gameRepository.save(newGame);
        List<Player> players = playerRepository.findPlayersByGameOrderById(oldGame);
        Player oldPlayer1 = players.get(0);
        Player oldPlayer2 = players.get(1);
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
        playerRepository.save(newPlayer1);
        playerRepository.save(newPlayer2);
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
//        Map map1 = Map.builder().data("[[0,0,0,3,3,2,0,3,2,3,2,2,2,0,2,0,0],[2,0,0,0,2,0,0,3,1,0,3,2,0,0,0,0,2],[3,2,2,0,0,2,2,0,0,0,0,0,0,0,0,2,0],[3,0,0,0,2,2,2,0,0,0,3,3,1,0,1,2,2],[3,3,0,0,0,0,2,3,0,0,0,3,3,0,0,0,0],[2,2,2,3,3,0,3,3,3,0,3,3,0,0,0,0,2],[2,0,0,3,1,3,2,2,2,0,0,0,0,0,0,0,0],[2,0,0,0,0,0,2,0,0,0,3,3,0,0,2,2,2],[0,2,0,0,0,0,2,1,0,0,1,2,2,2,2,0,0],[0,0,0,0,0,0,0,0,0,2,2,2,2,3,0,0,2],[0,0,0,0,2,0,0,0,2,2,2,2,0,0,3,0,0],[3,0,2,2,1,0,0,0,2,0,3,2,0,0,0,0,2],[3,0,0,3,1,0,3,3,2,0,0,3,2,3,0,3,0],[0,0,1,0,0,0,0,3,2,0,0,2,3,3,0,0,2],[0,0,0,0,0,3,3,0,2,2,2,2,1,3,0,0,0],[0,0,0,0,2,3,0,0,0,2,1,1,0,0,0,0,2],[0,0,0,0,2,2,0,2,0,0,2,0,0,0,0,0,0]]").build();
//        mapRepository.save(map1);
//        for (int i = 0; i < 4; i++) {
//            Item item1 = Item.builder().build();
//            itemRepository.save(item1);
//            Mount mount = Mount.builder().build();
//            mountRepository.save(mount);
//            Equipment equipment = Equipment.builder().build();
//            equipmentRepository.save(equipment);
//        }
//        Equipment equipment = Equipment.builder().build();
//        equipmentRepository.save(equipment);
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
//        use old map
//        int totalMapSize = mapRepository.countAll();
//        Map map = mapRepository.findMapById(random.nextInt(totalMapSize) + 1);
        Map map=Map.builder().build();
        int mapSize=17;
        String mapData=randomMap(mapSize);
        map.setData(mapData);
        mapRepository.save(map);

        Game game = Game.builder().playerFirst(random.nextBoolean()).map(map).build();
        gameRepository.save(game);
        Player player1 = Player.builder().account(account1).game(game).build();
        Player player2 = Player.builder().account(Objects.requireNonNullElse(account2, account1)).game(game).build();
        playerRepository.save(player1);
        playerRepository.save(player2);

        CharacterRecord character1 = randomCharacter();
        character1.updateAttribute(1);
        if (!random.nextBoolean()) {
            character1.setX(0);
            character1.setY(16);
        } else {
            character1.setX(16);
            character1.setY(0);
        }
        character1.setPlayer(player1);
        characterRecordRepository.save(character1);
        StructureRecord structureRecord1 = StructureRecord.builder()
                .x(character1.getX()).y(character1.getY()).game(game).player(player1).build();
        structureRecordRepository.save(structureRecord1);
        player1.getStructureRecords().add(structureRecord1);
        player1.getCharacterRecords().add(character1);

        CharacterRecord character2 = randomCharacter();
        character2.updateAttribute(1);
        if (character1.getX() == 0) {
            character2.setX(16);
            character2.setY(0);
        } else {
            character2.setX(0);
            character2.setY(16);
        }
        character2.setPlayer(player2);
        characterRecordRepository.save(character2);
        StructureRecord structureRecord2 = StructureRecord.builder()
                .x(character2.getX()).y(character2.getY()).game(game).player(player2).build();
        structureRecordRepository.save(structureRecord2);
        player2.getStructureRecords().add(structureRecord2);
        player2.getCharacterRecords().add(character2);

        ArrayList<Pair<Integer, Integer>> position = new ArrayList<>();

        int[][] mapInt = JSON.parseObject(map.getData(), int[][].class);
        int[][] mark = new int[17][17];
        for (int i = 0; i < mapInt.length; i++) {
            for (int j = 0; j < mapInt[i].length; j++) {
                mark[i][j] = 1;
                if (mapInt[i][j] != 2) {
                    if (i == 0 && j == 16) continue;
                    if (i == 16 && j == 0) continue;
                    position.add(Pair.of(i, j));
                }
            }
        }
        int villageCount = 20, relicCount = 4, hp = 15;
        Collections.shuffle(position);
        // 0空地 1山 2水 3树
        // village 可以在0   relic可以在0 1 2 3
        for (Pair<Integer, Integer> integerIntegerPair : position) {
            int x = integerIntegerPair.getLeft(), y = integerIntegerPair.getRight();
            if (mapInt[x][y] == 0) {
                if (villageCount > 0) {
                    boolean flag = true;
                    for (int i = -2; i < 3 && flag; i++)
                        if (0 <= x + i && x + i < 17) {
                            for (int j = -2; j < 3 && flag; j++)
                                if (0 <= y + j && y + j < 17) {
                                    if (mark[x + i][y + j] == 0) flag = false;
                                }
                        }
                    if (!flag) continue;
                    StructureRecord structureRecord = StructureRecord.builder()
                            .x(y).y(x).hp(hp + random.nextInt(-10, 10))
                            .game(game).structureClass(StructureClass.VILLAGE).build();
                    List<CharacterDTO> characterDTOS = new ArrayList<>(3);
                    for (int t = 0; t < 3; t++) {
                        characterDTOS.add(DTOUtil.toCharacterDTO(randomCharacter()));
                    }
                    structureRecord.setCharacter(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(characterDTOS));
                    structureRecordRepository.save(structureRecord);
                    villageCount -= 1;
                    mark[x][y] = 0;
                } else if (relicCount > 0) {
                    boolean flag = true;
                    for (int i = -6; i < 6 && flag; i++)
                        if (0 <= x + i && x + i < 17) {
                            for (int j = -6; j < 6 && flag; j++)
                                if (0 <= y + j && y + j < 17) {
                                    if (mark[x + i][y + j] == 2) flag = false;
                                }
                        }
                    if (!flag) continue;
                    if (x + y > 5 && x + y < 28) continue;
                    StructureRecord structureRecord = StructureRecord.builder()
                            .x(y).y(x).game(game).structureClass(StructureClass.RELIC).build();
                    structureRecordRepository.save(structureRecord);
                    relicCount -= 1;
                    mark[x][y] = 2;
                }
            } else {
                if (relicCount > 0) {
                    boolean flag = true;
                    for (int i = -6; i < 6 && flag; i++)
                        if (0 <= x + i && x + i < 17) {
                            for (int j = -6; j < 6 && flag; j++)
                                if (0 <= y + j && y + j < 17) {
                                    if (mark[x + i][y + j] == 2) flag = false;
                                }
                        }
                    if (!flag) continue;
                    if (x + y > 5 && x + y < 28) continue;
                    StructureRecord structureRecord = StructureRecord.builder()
                            .x(y).y(x).hp(hp + random.nextInt(-10, 10))
                            .game(game).structureClass(StructureClass.RELIC).build();
                    structureRecordRepository.save(structureRecord);
                    relicCount -= 1;
                    mark[x][y] = 2;
                }
            }
        }


        if (villageCount > 0 || relicCount > 0) {

            for (Pair<Integer, Integer> integerIntegerPair : position) {
                int x = integerIntegerPair.getLeft(), y = integerIntegerPair.getRight();
                if (mark[x][y] == 0 || mark[x][y] == 2) continue;
                if (mapInt[x][y] == 0) {
                    if (villageCount > 0) {
                        StructureRecord structureRecord = StructureRecord.builder()
                                .x(y).y(x).hp(hp + random.nextInt(-10, 10))
                                .game(game).structureClass(StructureClass.VILLAGE).build();
                        List<CharacterDTO> characterDTOS = new ArrayList<>(3);
                        for (int t = 0; t < 3; t++) {
                            characterDTOS.add(DTOUtil.toCharacterDTO(randomCharacter()));
                        }
                        structureRecord.setCharacter(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(characterDTOS));
                        structureRecordRepository.save(structureRecord);
                        villageCount -= 1;
                    } else if (relicCount > 0) {
                        StructureRecord structureRecord = StructureRecord.builder()
                                .x(y).y(x).game(game).structureClass(StructureClass.RELIC).build();
                        structureRecordRepository.save(structureRecord);
                        relicCount -= 1;
                    }
                } else {
                    if (relicCount > 0) {
                        StructureRecord structureRecord = StructureRecord.builder()
                                .x(y).y(x).hp(hp + random.nextInt(-10, 10))
                                .game(game).structureClass(StructureClass.RELIC).build();
                        structureRecordRepository.save(structureRecord);
                        relicCount -= 1;
                    }
                }
            }

        }

        GameRecord gameRecord = GameRecord.builder().game(game).build();
        gameRecord.setPlayer1(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(player1));
        gameRecord.setPlayer2(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(player2));
        List<StructureRecord> neutralStructure = structureRecordRepository.findStructureRecordsByGameAndPlayer(game, null);
        gameRecord.setStructure(Objects.requireNonNullElse(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(neutralStructure), null));
        gameRecordRepository.save(gameRecord);

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
        if (last == null)
            throw new RuntimeException("Restart your game!");
        int round = last.getRound() + 1;

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
                    .player(player1)
                    .name(equipment.getName())
                    .description(equipment.getDescription())
                    .shopClass(ShopClass.EQUIPMENT)
                    .cost(7).propid(equipmentRecord.getId()).build();
            shopRecordRepository.save(shopRecord);
            index[0][i] = shopRecord.getId();
            e.add(equipmentRecord);

            ItemRecord itemRecord = randomItemRecord(player1);
            Item item = itemRecord.getItem();
            shopRecord = ShopRecord.builder()
                    .round(round + 2)
                    .player(player1)
                    .name(item.getName())
                    .description(item.getDescription())
                    .shopClass(ShopClass.ITEM)
                    .cost(7).propid(itemRecord.getId()).build();
            shopRecordRepository.save(shopRecord);
            index[1][i] = shopRecord.getId();
            it.add(itemRecord);

            MountRecord mountRecord = randomMountRecord(player1);
            Mount mount = mountRecord.getMount();
            shopRecord = ShopRecord.builder()
                    .round(round + 2)
                    .player(player1)
                    .name(mount.getName())
                    .description(mount.getDescription())
                    .shopClass(ShopClass.MOUNT)
                    .cost(7).propid(mountRecord.getId()).build();
            shopRecordRepository.save(shopRecord);
            index[2][i] = shopRecord.getId();
            m.add(mountRecord);
        }
        ShopDTO shopDTO = new ShopDTO(DTOUtil.toEquipmentDTOs(e), DTOUtil.toItemDTOs(it), DTOUtil.toMountDTOs(m), index);

        boolean currentPlayer;
        Player player2;
        List<Player> players = playerRepository.findPlayersByGameOrderById(game);
        GameRecord gameRecord = GameRecord.builder().game(game).round(round).build();
        if (player1.getId().equals(players.get(0).getId())) {
            currentPlayer = true;
            player2 = players.get(1);
            player2.setProsperityDegree(prosperityDegree(player2));
            player2.setProsperityDegree(prosperityDegree(player2));
            gameRecord.setPlayer1(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(player1));
            gameRecord.setPlayer2(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(player2));
        } else {
            currentPlayer = false;
            player2 = players.get(0);
            player2.setProsperityDegree(prosperityDegree(player2));
            player2.setProsperityDegree(prosperityDegree(player2));
            gameRecord.setPlayer1(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(player2));
            gameRecord.setPlayer2(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(player1));
        }
        playerRepository.save(player2);

        List<StructureRecord> neutralStructure = structureRecordRepository.findStructureRecordsByGameAndPlayer(game, null);
        gameRecord.setStructure(Objects.requireNonNullElse(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(neutralStructure), null));
        gameRecordRepository.save(gameRecord);

        // update current shop info
        player1.getShopRecords().clear();
        List<ShopRecord> shopRecords1 = shopRecordRepository.findShopRecordsByPlayerAndRound(player1, round);
        if (!shopRecords1.isEmpty())
            player1.getShopRecords().addAll(shopRecords1);
        player2.getShopRecords().clear();
        List<ShopRecord> shopRecords2 = shopRecordRepository.findShopRecordsByPlayerAndRound(player2, round - 1);
        if (!shopRecords2.isEmpty())
            player2.getShopRecords().addAll(shopRecords2);
        return DTOUtil.toGameDTO(game, shopDTO, round, currentPlayer);
    }

    @Override
    public GameDTO stepBack(Integer playerId, boolean current) throws JsonProcessingException {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new RuntimeException("Player does not exist");
        }
        Game game = player.getGame();
        GameRecord last = gameRecordRepository.findFirstByGameOrderByIdDesc(game);
        if (last == null)
            throw new RuntimeException("Restart your game");

        if (!current) {
            last = gameRecordRepository.findGameRecordByGameAndRound(game, last.getRound() - 2);
            if (last == null)
                throw new RuntimeException("Unable to step back more");
            gameRecordRepository.deleteById(gameRecordRepository.findGameRecordByGameAndRound(game, last.getRound() + 1).getId());
            gameRecordRepository.deleteById(gameRecordRepository.findGameRecordByGameAndRound(game, last.getRound() + 2).getId());
        }

        Player player1 = objectMapper.readValue(last.getPlayer1(), Player.class);
        Player player2 = objectMapper.readValue(last.getPlayer2(), Player.class);

        for (Player p : List.of(player1, player2)) {
            List<CharacterRecord> characterRecords = characterRecordRepository.findCharacterRecordsByPlayer(p);
            List<CharacterRecord> extraCharacters = characterRecords.stream()
                    .filter(c -> !p.getCharacterRecords().stream().map(CharacterRecord::getId).toList().contains(c.getId()))
                    .toList();
            extraCharacters.forEach(c -> c.setPlayer(null));

            List<EquipmentRecord> equipmentRecords = equipmentRecordRepository.findEquipmentRecordsByPlayer(p);
            List<EquipmentRecord> extraEquipments = equipmentRecords.stream()
                    .filter(e -> !p.getEquipmentRecords().stream().map(EquipmentRecord::getId).toList().contains(e.getId())).toList();
            extraEquipments.forEach(e -> e.setPlayer(null));

            List<ItemRecord> itemRecords = itemRecordRepository.findItemRecordsByPlayer(p);
            List<ItemRecord> extraItems = itemRecords.stream()
                    .filter(i -> !p.getItemRecords().stream().map(ItemRecord::getId).toList().contains(i.getId())).toList();
            extraItems.forEach(i -> i.setPlayer(null));

            List<MountRecord> mountRecords = mountRecordRepository.findMountRecordsByPlayer(p);
            List<MountRecord> extraMounts = mountRecords.stream()
                    .filter(m -> !p.getMountRecords().stream().map(MountRecord::getId).toList().contains(m.getId())).toList();
            extraMounts.forEach(m -> m.setPlayer(null));
        }
        playerRepository.save(player1);
        playerRepository.save(player2);

        if (!current) {
            List<ShopRecord> shopRecords = new ArrayList<>();
            for (Player p : List.of(player1, player2)) {
                shopRecords.addAll(Objects.requireNonNullElse(shopRecordRepository.findShopRecordsByPlayerAndRound(p, last.getRound() + 3), new ArrayList<>()));
                shopRecords.addAll(Objects.requireNonNullElse(shopRecordRepository.findShopRecordsByPlayerAndRound(p, last.getRound() + 4), new ArrayList<>()));
            }
            shopRecordRepository.deleteAll(shopRecords);
        }

        List<StructureRecord> structureRecords = objectMapper.readValue(last.getStructure(),
                objectMapper.getTypeFactory().constructParametricType(List.class, StructureRecord.class));
        if (structureRecords != null)
            structureRecordRepository.saveAll(structureRecords);

        Player lastPlayer = player1.getId().equals(playerId) ? player2 : player1;
        List<ShopRecord> shopRecords = shopRecordRepository.findShopRecordsByPlayerAndRound(lastPlayer, last.getRound() + 2);
        ShopDTO shopDTO = DTOUtil.toShopDTO(shopRecords);
        boolean currentPlayer;
        if (game.getPlayerFirst())
            currentPlayer = last.getRound() % 2 == 0;
        else
            currentPlayer = last.getRound() % 2 == 1;
        return DTOUtil.toGameDTO(game, shopDTO, last.getRound(), currentPlayer);
    }

    public static String mapToString(int[][] map, int mapSize){
        StringBuilder mapData= new StringBuilder("[");
        for (int i=0;i<mapSize;i++){
            if (i!=0) mapData.append(',');
            mapData.append('[');
            for (int j=0;j<mapSize;j++){
                if (j!=0) mapData.append(',');
                mapData.append(map[i][j]);
            }
            mapData.append(']');
        }
        mapData.append(']');
        return String.valueOf(mapData);
    }

    private Player getPlayer(PlayerDTO playerDTO,Game game,GameDTO gameDTO) throws JsonProcessingException {
        Player player=playerRepository.findPlayerById(playerDTO.id());
        Account account=player.getAccount();

        List<CharacterRecord>characterRecords=new ArrayList<>();
        List<EquipmentRecord>equipmentRecords=new ArrayList<>();
        List<ItemRecord>itemRecords=new ArrayList<>();
        List<MountRecord>mountRecords=new ArrayList<>();
        List<StructureRecord>structureRecords=new ArrayList<>();

        for (EquipmentDTO e:playerDTO.equipments()){
            EquipmentRecord equipmentRecord=EquipmentRecord.builder()
                    .used(false).equipment(equipmentRepository.findEquipmentByName(e.name()))
                    .build();
            equipmentRecord=equipmentRecordRepository.save(equipmentRecord);
            equipmentRecords.add(equipmentRecord);
        }
        for (ItemDTO i:playerDTO.items()){
            ItemRecord itemRecord=ItemRecord.builder().item(itemRepository.findItemByName(i.name())).build();
            itemRecord=itemRecordRepository.save(itemRecord);
            itemRecords.add(itemRecord);
        }
        for (MountDTO m:playerDTO.mounts()){
            MountRecord mountRecord=MountRecord.builder()
                    .used(false).mount(mountRepository.findMountByName(m.name()))
                    .build();
            mountRecord=mountRecordRepository.save(mountRecord);
            mountRecords.add(mountRecord);
        }
        for (CharacterDTO c:playerDTO.characters()){
            CharacterRecord characterRecord=CharacterRecord.builder().x(c.x()).y(c.y())
                    .attack(c.attack()).defense(c.defense()).hp(c.hp()).level(c.level())
                    .actionRange(c.actionRange()).actionState(c.actionState()).characterClass(c.characterClass())
                    .name(c.name()).build();
            if (c.equipment()!=null){
                EquipmentRecord equipmentRecord=EquipmentRecord.builder()
                        .used(true).equipment(equipmentRepository.findEquipmentByName(c.equipment().name()))
                        .build();
                equipmentRecord=equipmentRecordRepository.save(equipmentRecord);
                equipmentRecords.add(equipmentRecord);
                characterRecord.setEquipmentRecord(equipmentRecord);
            }
            if (c.mount()!=null){
                MountRecord mountRecord=MountRecord.builder().
                        mount(mountRepository.findMountByName(c.mount().name())).used(true).build();
                mountRecord=mountRecordRepository.save(mountRecord);
                mountRecords.add(mountRecord);
                characterRecord.setMountRecord(mountRecord);
            }
            characterRecord=characterRecordRepository.save(characterRecord);
            characterRecords.add(characterRecord);
        }
        List<ShopRecord> shopRecords=new ArrayList<>();
        for (EquipmentDTO e:playerDTO.shopDTO().equipments()){
            ShopRecord shopRecord=ShopRecord.builder().shopClass(ShopClass.EQUIPMENT)
                    .round(gameDTO.round()).name(e.name()).cost(7)
                    .description(e.description()).propid(equipmentRepository.findEquipmentByName(e.name()).getId())
                    .build();
            shopRecord=shopRecordRepository.save(shopRecord);
            shopRecords.add(shopRecord);
        }
        for (MountDTO e:playerDTO.shopDTO().mounts()){
            ShopRecord shopRecord=ShopRecord.builder().shopClass(ShopClass.MOUNT)
                    .round(gameDTO.round()).name(e.name()).cost(7)
                    .description(e.description()).propid(mountRepository.findMountByName(e.name()).getId())
                    .build();
            shopRecord=shopRecordRepository.save(shopRecord);
            shopRecords.add(shopRecord);
        }
        for (ItemDTO e:playerDTO.shopDTO().items()){
            ShopRecord shopRecord=ShopRecord.builder().shopClass(ShopClass.ITEM)
                    .round(gameDTO.round()).name(e.name()).cost(7)
                    .description(e.description()).propid(itemRepository.findItemByName(e.name()).getId())
                    .build();
            shopRecord=shopRecordRepository.save(shopRecord);
            shopRecords.add(shopRecord);
        }
        for (StructureDTO s:playerDTO.structures()){
            String characterString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(s.characters());
            StructureRecord structureRecord=StructureRecord.builder()
                    .x(s.x()).y(s.y())
                    .hp(s.hp()).level(s.level()).game(game)
                    .structureClass(s.structureClass())
                    .remainingRound(s.remainingRound()).value(s.value())
                    .character(characterString)
                    .build();
            structureRecord=structureRecordRepository.save(structureRecord);
            structureRecords.add(structureRecord);
        }
        int[] r=playerDTO.technologyTree()[1];
        int[] f=playerDTO.technologyTree()[0];
        return Player.builder().game(game)
                .characterRecords(characterRecords).equipmentRecords(equipmentRecords)
                .itemRecords(itemRecords).mountRecords(mountRecords)
                .structureRecords(structureRecords)
                .account(account).vision(null)
                .peaceDegree(playerDTO.peaceDegree()).prosperityDegree(playerDTO.prosperityDegree())
                .stars(playerDTO.stars()).shopRecords(shopRecords)
                .techtreeRemainRound(Arrays.toString(r)).techtreeFeasible(Arrays.toString(f))
                .build();
    }

    @Override
    public GameDTO loadLocalArchive(String gameStr) throws JsonProcessingException {

        GameDTO gameDTO = objectMapper.readValue(gameStr,GameDTO.class);

        Map map=Map.builder().data(mapToString(gameDTO.map(),17)).build();
        map=mapRepository.save(map);

        int playerFirst=gameDTO.round();
        if (gameDTO.currentPlayer())playerFirst++;

        Game game=Game.builder().playerFirst(playerFirst%2==1).map(map).build();
        game=gameRepository.save(game);

        Player player1,player2;

        player1=getPlayer(gameDTO.player1(),game,gameDTO);
        player1=playerRepository.save(player1);
        player2=getPlayer(gameDTO.player2(),game,gameDTO);
        player2=playerRepository.save(player2);

        GameRecord gameRecord=GameRecord.builder().game(game).round(gameDTO.round()).build();
        gameRecord.setPlayer1(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(player1));
        gameRecord.setPlayer2(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(player2));

        List<StructureDTO> structureDTOS=gameDTO.structures();
        List<StructureRecord> neutralStructure=new ArrayList<>();
        //add structure into repository
        for (StructureDTO x:structureDTOS){
            String characterString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(x.characters());
            StructureRecord structureRecord=StructureRecord.builder().player(null).game(game).hp(x.hp()).structureClass(x.structureClass()).x(x.x()).y(x.y()).character(characterString).level(x.level()).remainingRound(x.remainingRound()).value(x.value()).build();
            structureRecordRepository.save(structureRecord);
            neutralStructure.add(structureRecord);
        }
        gameRecord.setStructure(Objects.requireNonNullElse(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(neutralStructure), null));
        gameRecordRepository.save(gameRecord);

        Player lastPlayer = gameDTO.currentPlayer()? player2 : player1;
        List<ShopRecord> shopRecords = shopRecordRepository.findShopRecordsByPlayerAndRound(lastPlayer, gameDTO.round());
        ShopDTO shopDTO = DTOUtil.toShopDTO(shopRecords);
        return DTOUtil.toGameDTO(game,shopDTO,gameDTO.round(),gameDTO.currentPlayer());
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
        //1:basic  2:sword  3:arrow  4:shield  5:cannon
        int id = r.nextInt(1, tmp + 1);
        Equipment equipment = equipmentRepository.findEquipmentById(id);
        EquipmentRecord equipmentRecord = EquipmentRecord.builder().equipment(equipment).build();
        return equipmentRecordRepository.save(equipmentRecord);
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
        return mountRecordRepository.save(mountRecord);
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
        return itemRecordRepository.save(itemRecord);
    }


    public static String randomMap(int mapSize){
        Random r = new Random();
        int[][] height = new int[mapSize][mapSize];
        int[][] newMap = new int[mapSize][mapSize];
        int[][] nextX = new int[mapSize][mapSize];
        int[][] nextY = new int[mapSize][mapSize];
        int [][] dir_x={{0,0},{0,0},{-1,-1},{-1,-1},{1,1},{1,1}};
        int [][] dir_y={{-1,-1},{1,1},{0,0},{-1,1},{0,0},{-1,1}};

//        generate new map
        for (int i=0;i<mapSize;i++){
            for (int j=0;j<mapSize;j++){
                newMap[i][j]=0;
                height[i][j]=r.nextInt(25)+15*(Math.min(i,mapSize-1-i)+Math.min(j,mapSize-1-j));
            }
        }
        for (int i=0;i<mapSize;i++){
            nextX[i][0]=nextX[0][i]=nextX[mapSize-1][i]=nextX[i][mapSize-1]=-1;
        }
        for (int i=1;i<mapSize-1;i++){
            for (int j=1;j<mapSize-1;j++){
                int cnt=0;
                for (int d=0;d<6;d++) {
                    if (height[i+dir_x[d][i&1]][j+dir_y[d][i&1]] < height[i][j]) {
                        cnt += height[i][j]-height[i+dir_x[d][i&1]][j+dir_y[d][i&1]];
                    }
                }
                if (cnt==0){
                    nextX[i][j]=-1;
                    continue;
                }
                int nextVal=r.nextInt(cnt);
                for (int d=0;d<6;d++) {
                    if (height[i+dir_x[d][i&1]][j+dir_y[d][i&1]] < height[i][j]) {
                        nextVal -= height[i][j]-height[i+dir_x[d][i&1]][j+dir_y[d][i&1]];
                        if (nextVal<0){
                            nextX[i][j]=i+dir_x[d][i&1];
                            nextY[i][j]=j+dir_y[d][i&1];
                            break;
                        }
                    }
                }
            }
        }
//        generate river
        for (int t=0;t<4;t++){
            int cnt=0,x = 0,y = 0;
            int lx,rx,ly,ry;
            if (t%2==0) {
                lx = 2;
                rx = mapSize / 2;
            }
            else {
                lx=mapSize/2;
                rx=mapSize-2;
            }
            if ((t/2)%2==0) {
                ly = 2;
                ry = mapSize / 2;
            }
            else {
                ly=mapSize/2;
                ry=mapSize-2;
            }
            for (int i=lx;i<rx;i++){
                for (int j=ly;j<ry;j++)if (newMap[i][j]==0){
                    cnt+=height[i][j];
                }
            }
            if (cnt==0)continue;
            int nextVal=r.nextInt(cnt);
            for (int i=lx;i<rx;i++){
                for (int j=ly;j<ry;j++)if (newMap[i][j]==0){
                    nextVal-=height[i][j];
                    if (nextVal<0){
                        x=i;
                        y=j;
                        break;
                    }
                }
                if (nextVal<0)break;
            }
            while(true){
                newMap[x][y]=2;
                int next_x=nextX[x][y],next_y=nextY[x][y];
                if (next_x==-1)break;
                x=next_x;
                y=next_y;
            }
        }
        for (int i=3;i<mapSize-3;i++){
            for (int j=3;j<mapSize-3;j++){
                if (height[i][j]<=3){
                    newMap[i][j]=2;
                }
            }
        }

        //generate pool
        for (int t=0;t<4;t++){
            int cnt=0,x = 0,y = 0;
            int lx,rx,ly,ry;
            int lim=r.nextInt(4)+3;
            if (t%2==0) {
                lx = 2;
                rx = mapSize / 2;
            }
            else {
                lx=mapSize/2;
                rx=mapSize-2;
            }
            if ((t/2)%2==0) {
                ly = 2;
                ry = mapSize / 2;
            }
            else {
                ly=mapSize/2;
                ry=mapSize-2;
            }
            for (int i=lx;i<rx;i++){
                for (int j=ly;j<ry;j++)if (newMap[i][j]==0){
                    cnt+=height[i][j];
                }
            }
            if (cnt==0)continue;
            int nextVal=r.nextInt(cnt);
            for (int i=lx;i<rx;i++){
                for (int j=ly;j<ry;j++)if (newMap[i][j]==0){
                    nextVal-=height[i][j];
                    if (nextVal<0){
                        x=i;
                        y=j;
                        break;
                    }
                }
                if (nextVal<0)break;
            }
            while(true){
                newMap[x][y]=2;
                lim-=1;
                if (lim==0)break;
                int next_x=nextX[x][y],next_y=nextY[x][y];
                if (next_x==-1)break;
                x=next_x;
                y=next_y;
            }
        }
        for (int i=3;i<mapSize-3;i++){
            for (int j=3;j<mapSize-3;j++){
                if (height[i][j]<=3){
                    newMap[i][j]=2;
                }
            }
        }
//        generate mountain

        List<Integer> h=new ArrayList<Integer>();

        for (int i=2;i<mapSize-2;i++){
            for (int j=2;j<mapSize-2;j++){
                if (newMap[i][j]!=2){
                    h.add(height[i][j]);
                }
            }
        }
        h.sort(Integer::compareTo);
        int lim= h.get(Math.max(0,h.size()-mapSize*10));
        for (int i=2;i<mapSize-2;i++){
            for (int j=2;j<mapSize-2;j++)if (newMap[i][j]!=2){
                if (height[i][j]>lim && r.nextInt(10)==0){
                    newMap[i][j]=1;
                }
            }
        }
//        generate forest
        for (int i=0;i<mapSize;i++){
            for (int j=0;j<mapSize;j++)if (newMap[i][j]==0){
                if (r.nextInt(19)==0){
                    newMap[i][j]=3;
                }
            }
        }
        newMap[0][mapSize-1]=0;
        newMap[mapSize-1][0]=0;
        for (int i=1;i<mapSize;i+=2){
            newMap[i][mapSize-1]=2;
        }
//      transform data to string
        return mapToString(newMap,mapSize);
    }
}
