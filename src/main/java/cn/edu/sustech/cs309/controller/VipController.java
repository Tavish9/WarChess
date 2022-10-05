package cn.edu.sustech.cs309.controller;

import cn.edu.sustech.cs309.service.VipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class VipController {
    @Autowired
    private VipService vipService;
}
