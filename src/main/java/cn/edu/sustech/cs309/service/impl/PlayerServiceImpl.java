package cn.edu.sustech.cs309.service.impl;

import cn.edu.sustech.cs309.domain.Account;
import cn.edu.sustech.cs309.domain.MyUserDetails;
import cn.edu.sustech.cs309.dto.AccountDTO;
import cn.edu.sustech.cs309.repository.AccountRepository;
import cn.edu.sustech.cs309.security.MyPasswordEncoder;
import cn.edu.sustech.cs309.service.AccountService;
import cn.edu.sustech.cs309.service.PlayerService;
import cn.edu.sustech.cs309.utils.DTOUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {
}
