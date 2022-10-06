package cn.edu.sustech.cs309.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player")
@EntityListeners(AuditingEntityListener.class)
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String name;

    @Builder.Default
    private Long exp = 0L;

//    @Builder.Default
    @Column(name = "max_exp")
    private Long maxExp;

    @Builder.Default
    private Integer level = 1;

    @Builder.Default
    private Long coins = 0L;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<CharacterRecord> characterRecords = new ArrayList<>();

}