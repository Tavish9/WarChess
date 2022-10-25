package cn.edu.sustech.cs309.controller;

import cn.edu.sustech.cs309.domain.ResponseResult;
import cn.edu.sustech.cs309.service.CharacterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "人物操作")
@RestController
@Slf4j
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @ApiOperation(value = "获取角色信息")
    @GetMapping("/character/{characterid}")
    public ResponseResult<?> getCharacter(@PathVariable("characterid") Integer characterId) {
        return ResponseResult.success(characterService.getCharacter(characterId));
    }

    @ApiOperation(value = "解雇角色")
    @PutMapping("/characterid/{characterid}/dismiss")
    public ResponseResult<?> dismissCharacter(@PathVariable("characterid") Integer characterId) {
        return ResponseResult.success(characterService.dismissCharacter(characterId));
    }


    @ApiOperation(value = "移动角色")
    @PutMapping("/character/{characterid}/move")
    public ResponseResult<?> moveCharacter(@PathVariable("characterid") Integer characterId,
                                           @RequestParam("x") Integer x,
                                           @RequestParam("y") Integer y) {
        return ResponseResult.success(characterService.moveCharacter(characterId, x, y));
    }

    @ApiOperation(value = "角色攻击人")
    @PutMapping("/character/{characterid}/char")
    public ResponseResult<?> attackCharacter(@PathVariable("characterid") Integer characterId,
                                             @RequestParam("attackid") Integer attackId) {
        return ResponseResult.success(characterService.attackCharacter(characterId, attackId));
    }


    @ApiOperation(value = "角色攻击建筑")
    @PutMapping("/character/{characterid}/structure")
    public ResponseResult<?> attackStructure(@PathVariable("characterid") Integer characterId,
                                             @RequestParam("attackid") Integer attackId) {
        return ResponseResult.success(characterService.attackStructure(characterId, attackId));
    }
}
