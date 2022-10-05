package cn.edu.sustech.cs309.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@MappedSuperclass
@Table(name = "vip")
@EntityListeners(AuditingEntityListener.class)
public abstract class Vip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Column(name = "duration", nullable = false, length = 32, unique = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date duration;


    public Vip() {

    }

    public Vip(Date duration) {
        this.duration = duration;
    }

}
