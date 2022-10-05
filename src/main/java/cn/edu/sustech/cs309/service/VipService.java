package cn.edu.sustech.cs309.service;

import cn.edu.sustech.cs309.domain.Vip;
import cn.edu.sustech.cs309.domain.ResponseResult;

public interface VipService {
    ResponseResult<Vip> saveVip(Vip vip);
}
