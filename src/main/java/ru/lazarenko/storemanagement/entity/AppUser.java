package ru.lazarenko.storemanagement.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String login;

    private String password;

    private String email;

    @Column(name = "locked")
    private Boolean isAccountLocked = false;

    private String activationCode;

    @Column(name = "active")
    private Boolean isAccountActive = false;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Client client;

    public void addRole(Role role){
        roles.add(role);
    }

    public void removeRole(Role role){
        roles.remove(role);
    }

    public void setClient(Client client) {
        client.setUser(this);
        this.client = client;
    }
}
