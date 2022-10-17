package cn.edu.sustech.cs309.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "character_class", nullable = false)
    @Enumerated(EnumType.STRING)
    private CharacterClass characterClass;

    @Builder.Default
    private Integer level = 1;

    private Integer attack;

    private Integer defense;

    private Integer hp;

    @Column(name="action_range")
    private Integer actionRange;

    private Integer x;

    private Integer y;

    @OneToOne
    @JoinColumn(name = "equipment_record_id")
    private EquipmentRecord equipmentRecord;

    @OneToOne
    @JoinColumn(name = "mount_record_id")
    private MountRecord mountRecord;
}
