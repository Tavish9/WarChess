package cn.edu.sustech.cs309.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;
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
@Table(name = "game")
@EntityListeners(AuditingEntityListener.class)
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @JsonIgnore
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Player> players = new ArrayList<>();

    //false means player1 first,otherwise player2 first
    private Boolean playerFirst;

    @ManyToOne
    @JoinColumn(name = "map_id")
    private Map map;

    @JsonIgnore
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @Builder.Default
    private List<GameRecord> gameRecords = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @Builder.Default
    private List<StructureRecord> structureRecords = new ArrayList<>();
}