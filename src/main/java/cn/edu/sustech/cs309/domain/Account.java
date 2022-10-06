package cn.edu.sustech.cs309.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "account")
@EntityListeners(AuditingEntityListener.class)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Column(name = "username", nullable = false, length = 32, unique = true)
    private String username;

    private String password;

    @OneToOne(targetEntity = Vip.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "vip_id")
    private Vip vip;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
