package cn.edu.sustech.cs309.service.impl;

import cn.edu.sustech.cs309.domain.Vip;
import cn.edu.sustech.cs309.domain.ResponseResult;
import cn.edu.sustech.cs309.repository.VipRepository;
import cn.edu.sustech.cs309.service.VipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VipServiceImpl implements VipService {
    @Autowired
    private VipRepository vipRepository;

    @Override
    public ResponseResult<Vip> saveVip(Vip vip) {
        Vip vip1=vipRepository.findVipById(vip.getId());
        if (vip1 != null) {
            throw new RuntimeException("Vip already exists");
        } else {
            log.debug("create successfully");
            return ResponseResult.success(HttpStatus.OK.value(), vipRepository.save(vip));
        }
    }
}
