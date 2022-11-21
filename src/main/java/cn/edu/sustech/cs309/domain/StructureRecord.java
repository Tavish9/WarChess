package cn.edu.sustech.cs309.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "structure_class", nullable = false)
    @Enumerated(EnumType.STRING)
    private StructureClass structureClass;

    @Builder.Default
    private Integer level = 0;

    @Builder.Default
    private Integer hp = 30;

    @Builder.Default
    @Column(name="remaining_round")
    private Integer remainingRound = 0;

    @Builder.Default
    private Integer value = 0;

    private Integer x;

    private Integer y;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String character;
}
