package cn.edu.sustech.cs309.controller;

import cn.edu.sustech.cs309.domain.ResponseResult;
import cn.edu.sustech.cs309.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(tags = "游戏操作")
@RestController
@Slf4j
public class GameController {
    @Autowired
    private GameService gameService;

    @ApiOperation(value = "获取存档")
    @GetMapping("/game/archive")
    public ResponseResult<?> getArchives(@RequestParam(value = "username") String username) throws JsonProcessingException {
        if (!StringUtils.hasText(username))
            throw new RuntimeException("Invalid account username");
        return ResponseResult.success(gameService.getArchives(username));
    }

    @ApiOperation(value = "读取存档")
    @GetMapping("/game/archive/{archiveid}")
    public ResponseResult<?> loadArchives(@PathVariable(value = "archiveid") Integer archiveId) throws JsonProcessingException {
        if (archiveId <= 0)
            throw new RuntimeException("Invalid archive");
        return ResponseResult.success(gameService.loadArchive(archiveId));
    }

    @ApiOperation(value = "开始新游戏")
    @PostMapping("/game/play")
    public ResponseResult<?> play(@RequestParam(value = "username1") String username1,
                                  @RequestParam(value = "username2", required = false) String username2) throws JsonProcessingException {
        return ResponseResult.success(gameService.ini(username1, username2));
    }

    @ApiOperation(value = "下一回合")
    @PutMapping("/game/play")
    public ResponseResult<?> update(@RequestParam(value = "playerid") Integer playerId) throws JsonProcessingException {
        if (playerId <= 0)
            throw new RuntimeException("Invalid player");
        return ResponseResult.success(gameService.update(playerId));
    }
}
