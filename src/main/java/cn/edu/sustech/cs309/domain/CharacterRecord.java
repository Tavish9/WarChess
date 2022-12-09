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

    @JsonBackReference(value = "player-character")
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "character_class")
    @Enumerated(EnumType.STRING)
    private CharacterClass characterClass;

    @Builder.Default
    @Column(name = "action_state")
    private Integer actionState = 0;

    private String name;

    @Builder.Default
    private Integer level = 1;

    private Integer attack;

    private Integer defense;

    private Integer hp;

    @Column(name = "action_range")
    private Integer actionRange;

    private Integer x;

    private Integer y;

    @OneToOne
    @JoinColumn(name = "equipment_record_id")
    private EquipmentRecord equipmentRecord;

    @OneToOne
    @JoinColumn(name = "mount_record_id")
    private MountRecord mountRecord;

    public void updateAttribute(int type) {
        if (type == 0) {
            characterClass = CharacterClass.WARRIOR;
            attack = (attack * 15 + 14) / 10;
            defense = (defense * 15 + 14) / 10;
            hp = (hp * 15 + 14) / 10;
        } else {
            if (type == 1) {
                characterClass = CharacterClass.EXPLORER;
                actionRange = actionRange + 1;
            } else
                characterClass = CharacterClass.SCHOLAR;
        }
    }

    public static final String[] characterName = {"James", "Mary", "Robert", "Patricia", "John", "Jennifer", "Michael", "Linda", "David", "Elizabeth", "William", "Barbara", "Richard", "Susan", "Joseph", "Jessica", "Thomas", "Sarah", "Charles", "Karen", "Christopher", "Lisa", "Daniel", "Nancy", "Matthew", "Betty", "Anthony", "Margaret", "Mark", "Sandra", "Donald", "Ashley", "Steven", "Kimberly", "Paul", "Emily", "Andrew", "Donna", "Joshua", "Michelle", "Kenneth", "Carol", "Kevin", "Amanda", "Brian", "Dorothy", "George", "Melissa", "Timothy", "Deborah", "Ronald", "Stephanie", "Edward", "Rebecca", "Jason", "Sharon", "Jeffrey", "Laura", "Ryan", "Cynthia", "Jacob", "Kathleen", "Gary", "Amy", "Nicholas", "Angela", "Eric", "Shirley", "Jonathan", "Anna", "Stephen", "Brenda", "Larry", "Pamela", "Justin", "Emma", "Scott", "Nicole", "Brandon", "Helen", "Benjamin", "Samantha", "Samuel", "Katherine", "Gregory", "Christine", "Alexander", "Debra", "Frank", "Rachel", "Patrick", "Carolyn", "Raymond", "Janet", "Jack", "Catherine", "Dennis", "Maria", "Jerry", "Heather", "Tyler", "Diane", "Aaron", "Ruth", "Jose", "Julie", "Adam", "Olivia", "Nathan", "Joyce", "Henry", "Virginia", "Douglas", "Victoria", "Zachary", "Kelly", "Peter", "Lauren", "Kyle", "Christina", "Ethan", "Joan", "Walter", "Evelyn", "Noah", "Judith", "Jeremy", "Megan", "Christian", "Andrea", "Keith", "Cheryl", "Roger", "Hannah", "Terry", "Jacqueline", "Gerald", "Martha", "Harold", "Gloria", "Sean", "Teresa", "Austin", "Ann", "Carl", "Sara", "Arthur", "Madison", "Lawrence", "Frances", "Dylan", "Kathryn", "Jesse", "Janice", "Jordan", "Jean", "Bryan", "Abigail", "Billy", "Alice", "Joe", "Julia", "Bruce", "Judy", "Gabriel", "Sophia", "Logan", "Grace", "Albert", "Denise", "Willie", "Amber", "Alan", "Doris", "Juan", "Marilyn", "Wayne", "Danielle", "Elijah", "Beverly", "Randy", "Isabella", "Roy", "Theresa", "Vincent", "Diana", "Ralph", "Natalie", "Eugene", "Brittany", "Russell", "Charlotte", "Bobby", "Marie", "Mason", "Kayla", "Philip", "Alexis", "Louis", "Lori"};

}
