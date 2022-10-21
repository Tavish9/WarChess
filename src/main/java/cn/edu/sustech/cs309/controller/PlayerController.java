package cn.edu.sustech.cs309.controller;

import cn.edu.sustech.cs309.domain.ResponseResult;
import cn.edu.sustech.cs309.service.PlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "玩家操作")
@RestController
@Slf4j
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @ApiOperation(value="获取信息")
    @GetMapping("/player/{playerid}")
    public ResponseResult<?> getAll(@PathVariable("playerid") Integer playerId){
        return ResponseResult.success(playerService.getAll(playerId));
    }

    @ApiOperation(value="使用道具")
    @PutMapping("/player/{playerid}/item")
    public ResponseResult<?> updateItem(@PathVariable("playerid") Integer playerId,
                                        @RequestParam("characterid")Integer characterId,
                                        @RequestParam("itemid")Integer itemId){
        return ResponseResult.success(playerService.updateItem(playerId,characterId,itemId));
    }

    @ApiOperation(value="操作已有装备")
    @PutMapping("/player/{playerid}/equip")
    public ResponseResult<?> updateEquipment(@PathVariable("playerid")Integer playerId,
                                             @RequestParam("characterid")Integer characterId,
                                             @RequestParam("equipmentid")Integer equipmentId,
                                             @RequestParam("off")Boolean off){
        if (off) {
            return ResponseResult.success(playerService.offEquipment(playerId,characterId,equipmentId));
        }
        else {
            return ResponseResult.success(playerService.wearEquipment(playerId,characterId,equipmentId));
        }
    }

    @ApiOperation(value="操作已有坐骑")
    @PutMapping("/player/{playerid}/mount")
    public ResponseResult<?> updateMount(@PathVariable("playerid")Integer playerId,
                                         @RequestParam("characterid")Integer characterId,
                                         @RequestParam("mountid")Integer mountId,
                                         @RequestParam("off")Boolean off){
        if (off) {
            return ResponseResult.success(playerService.offMount(playerId,characterId,equipmentId));
        }
        else {
            return ResponseResult.success(playerService.wearMount(playerId,characterId,equipmentId));
        }

    }

    @ApiOperation(value="显示科技树")
    @GetMapping("/player/{playerid}/tech")
    public ResponseResult<?> getTechnologies(@PathVariable("playerid")Integer playerId){
        return ResponseResult.success(playerService.getTechnologies(playerId));
    }
}
