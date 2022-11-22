package cn.edu.sustech.cs309.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "equipment")
@EntityListeners(AuditingEntityListener.class)
public class Equipment {
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

    @Column(name = "equipment_class")
    @Enumerated(EnumType.STRING)
    private EquipmentClass equipmentClass;

    @Builder.Default
    private Integer attack = 0;

    @Builder.Default
    private Integer defense = 0;

    @Column(name="attack_range")
    private Integer attackRange;

    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @Builder.Default
    private List<EquipmentRecord> equipmentRecords = new ArrayList<>();
}
