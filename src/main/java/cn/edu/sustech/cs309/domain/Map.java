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
@Table(name = "map")
@EntityListeners(AuditingEntityListener.class)
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    private String name;

    @OneToMany(mappedBy = "map", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<Game> games = new ArrayList<>();

    @Transient
    private List<List<Integer>> data;
}
