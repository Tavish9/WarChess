package cn.edu.sustech.cs309.repository;

import cn.edu.sustech.cs309.domain.Account;
import cn.edu.sustech.cs309.domain.Vip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VipRepository extends JpaRepository<Vip, Integer> {

    Vip findVipById(Integer id);
}
