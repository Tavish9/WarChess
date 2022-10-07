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
@Table(name = "character_record")
@EntityListeners(AuditingEntityListener.class)
public class CharacterRecord {
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
    @JoinColumn(name = "character_id")
    private Character character;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @Builder.Default
    private Long exp = 0L;

    //    @Builder.Default
    @Column(name = "max_exp")
    private Long maxExp;

    @Builder.Default
    private Integer level = 1;

    //delete character record but not delete correspond equipment
//    @OneToMany(mappedBy = "characterRecord", cascade = CascadeType.PERSIST)
//    @ToString.Exclude
//    @Builder.Default
//    private List<EquipmentRecord> equipmentRecords = new ArrayList<>();
}
