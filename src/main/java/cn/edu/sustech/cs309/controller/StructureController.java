package cn.edu.sustech.cs309.controller;

import antlr.debug.InputBufferListener;
import cn.edu.sustech.cs309.domain.ResponseResult;
import cn.edu.sustech.cs309.service.PlayerService;
import cn.edu.sustech.cs309.service.StructureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Parameter;


@Api(tags = "建筑操作")
@RestController
@Slf4j
public class StructureController {

    @Autowired
    private StructureService structureService;

    @ApiOperation(value="获取建筑信息")
    @GetMapping("/structure/{structureid}")
    public ResponseResult<?> getStructure(@PathVariable("structureid")Integer structureId){
        return ResponseResult.success(structureService.getStructure(structureId));
    }

    @ApiOperation(value="获取建筑内角色列表")
    @GetMapping("/structure/{structureid}/char")
    public ResponseResult<?> getCharacters(@PathVariable("structureid")Integer structureId){
        return ResponseResult.success(structureService.getCharacters(structureId));
    }

    @ApiOperation(value="购买建筑内的角色")
    @PostMapping("/structure/{structureid}/char")
    public ResponseResult<?> buyCharacter(@PathVariable("structureid")Integer structureId,
                                           @RequestParam("playerid")Integer playerid,
                                           @RequestParam("id")Integer id,
                                          @RequestParam("x")Integer x,
                                          @RequestParam("y")Integer y){
        return ResponseResult.success(structureService.buyCharacter(structureId,playerid,id,x,y));
    }

    @ApiOperation(value="兵营训练人物")
    @PutMapping("/structure/{structureid}/camp")
    public ResponseResult<?> updateCharacter(@PathVariable("structureid")Integer structureId,
                                             @RequestParam("characterid")Integer characterId,
                                             @RequestParam("option")Integer v){
        return ResponseResult.success(structureService.updateCharacter(structureId,characterId,v));
    }

    @ApiOperation(value="人物放在兵营挣钱")
    @PutMapping("structure/{structureid}/market")
    public  ResponseResult<?> earnStars(@PathVariable("structureiid")Integer structureId,
                                        @RequestParam("characterid")Integer characterId){
        return ResponseResult.success(structureService.earnStars(structureId,characterId));
    }

    @ApiOperation(value="升级科技树")
    @PutMapping("/structure/{structureid}/insititude")
    public ResponseResult<?> updateTechnologies(@PathVariable("structureid")Integer structureId,
                                                @RequestParam("characterid")Integer characterId,
                                                @RequestParam("option")Integer v){
        return ResponseResult.success(structureService.updateTechnologies(structureId,characterId,v));
    }

    @ApiOperation(value="升级建筑")
    @PutMapping("structure/{structure}/upd")
    public ResponseResult<?> updateStructure(@PathVariable("structureid")Integer structureId,
                                             @RequestParam("option")Integer v){
        return ResponseResult.success(structureService.updateStructure(structureId,v));
    }

}
