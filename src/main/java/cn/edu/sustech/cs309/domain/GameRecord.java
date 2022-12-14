package cn.edu.sustech.cs309.domain;

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
@Table(name = "game_record")
@EntityListeners(AuditingEntityListener.class)
public class GameRecord {
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

    @Builder.Default
    private Integer round = 0;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String player1;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String player2;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String structure;
}
