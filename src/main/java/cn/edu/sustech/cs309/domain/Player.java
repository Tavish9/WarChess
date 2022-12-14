package cn.edu.sustech.cs309.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;
import java.util.Map;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player")
@EntityListeners(AuditingEntityListener.class)
public class Player {
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
    @JoinColumn(name = "account_id")
    private Account account;

    @Builder.Default
    private Long stars = 10L;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private Game game;

    @Builder.Default
    @Column(name = "prosperity_degree")
    private Integer prosperityDegree = 3;

    @Builder.Default
    @Column(name = "peace_degree")
    private Integer peaceDegree = 2;

    @Builder.Default
    @Column(name = "techtree_feasible")
    private String techtreeFeasible = "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0";

    @Builder.Default
    @Column(name = "techtree_remain_round")
    private String techtreeRemainRound = "1, 2, 2, 2, 3, 3, 3, 3, 3, 3, 5";

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String vision;

    public static final Map<String, int[]> map = new HashMap<>();

    public static final String[] name = {"Life", "Horse", "Fish", "Sword", "Elephant", "Fox", "Beer", "Potion", "Arrow", "Shield", "Cannon"};

    static {
        int[] round = {1, 2, 2, 2, 3, 3, 3, 3, 3, 3, 5};
        int[] stars = {0, 4, 4, 4, 10, 10, 10, 10, 10, 10, 20};
        for (int i = 0; i < name.length; i++) {
            map.put(name[i], new int[]{round[i], stars[i]});
        }
    }

    @JsonManagedReference(value = "player-character")
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<CharacterRecord> characterRecords = new ArrayList<>();

    @JsonManagedReference(value = "player-equipment")
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<EquipmentRecord> equipmentRecords = new ArrayList<>();

    @JsonManagedReference(value = "player-item")
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<ItemRecord> itemRecords = new ArrayList<>();

    @JsonManagedReference(value = "player-structure")
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<StructureRecord> structureRecords = new ArrayList<>();

    @JsonManagedReference(value = "player-mount")
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<MountRecord> mountRecords = new ArrayList<>();

    @JsonManagedReference(value = "player-shop")
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<ShopRecord> shopRecords = new ArrayList<>();

    public int[][] getTech() {
        int[][] technologyTree = new int[2][];
        String techTreeRemainRound = techtreeRemainRound;
        String techTreeFeasible = techtreeFeasible;
        String[] remain = techTreeRemainRound.split(", ");
        String[] feasible = techTreeFeasible.split(", ");
        int[] r = Arrays.stream(remain).mapToInt(Integer::parseInt).toArray();
        int[] f = Arrays.stream(feasible).mapToInt(Integer::parseInt).toArray();
        technologyTree[0] = f;
        technologyTree[1] = r;
        return technologyTree;
    }
}