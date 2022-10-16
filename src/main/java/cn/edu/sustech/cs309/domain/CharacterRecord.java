package cn.edu.sustech.cs309.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.nio.MappedByteBuffer;
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
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private Character character;


    @Column(name = "character_class", nullable = false)
    @Enumerated(EnumType.STRING)
    private CharacterClass characterClass;

    @Builder.Default
    private Integer level = 1;

    private double attack;

    private double defense;

    private double hp;

    @Column(name="action_range")
    private Integer actionRange;

    private Integer x;

    private Integer y;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;


    @ManyToOne
    @JoinColumn(name = "mount_id")
    private Mount mount;

//    @OneToMany(mappedBy = "characterRecord", cascade = CascadeType.ALL, orphanRemoval = true)
//    @ToString.Exclude
//    @Builder.Default
//    private List<EquipmentRecord> equipmentRecords = new ArrayList<>();

}
