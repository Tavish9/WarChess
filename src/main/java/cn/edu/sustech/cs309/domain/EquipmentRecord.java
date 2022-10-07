package cn.edu.sustech.cs309.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "equipment_record")
@EntityListeners(AuditingEntityListener.class)
public class EquipmentRecord {

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
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @OneToOne
    @JoinColumn(name = "character_record_id")
    private CharacterRecord characterRecord;
}
