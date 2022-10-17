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
@Table(name = "structure_record")
@EntityListeners(AuditingEntityListener.class)
public class StructureRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Column(name = "structure_class", nullable = false)
    @Enumerated(EnumType.STRING)
    private StructureClass structureClass;

    private Integer level;

    @Column(name="ramining_round")
    private Integer remainingRound;

    private Integer value;

    private Integer x;

    private Integer y;
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private String character;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
}
