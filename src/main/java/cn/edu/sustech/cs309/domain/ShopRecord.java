package cn.edu.sustech.cs309.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "shop_record")
@EntityListeners(AuditingEntityListener.class)
public class ShopRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    private Integer round;

    private String name;

    private String description;

    @Column(name = "shop_class", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShopClass shopClass;

    private Integer cost;

    private Integer propid;

    @Builder.Default
    private Boolean purchased = false;

    @JsonBackReference(value = "player-shop")
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
}
