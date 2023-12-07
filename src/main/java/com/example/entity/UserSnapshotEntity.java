package com.example.entity;
import com.example.users.ServerUserSnapshot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_snapshots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserSnapshotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "state_name")
    private String stateName;


    public UserSnapshotEntity(String name, ServerUserSnapshot snapshot) {
        this.name = name;
        this.username = snapshot.getName();
        this.password = snapshot.getHashedPassword();
        this.stateName = snapshot.getState().getName();
    }

}
