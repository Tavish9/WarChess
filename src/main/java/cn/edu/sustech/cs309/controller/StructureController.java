package cn.edu.sustech.cs309.controller;

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

    @ApiOperation(value="获取建筑内角色列表")
    @GetMapping("/structure/char")
    public ResponseResult<?> getCharacters(@RequestParam("playerid")Integer playerId){
        
    }
}
