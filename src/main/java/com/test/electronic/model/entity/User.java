package com.test.electronic.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "User name cannot be empty")
    private String name;

    private String surname;

    @NotEmpty(message = "User email cannot be empty")
    private String email;

    @NotEmpty(message = "User username cannot be empty")
    private String username;

    @NotEmpty(message = "User password cannot be empty")
    private String password;
    private String verificationCode;
    private Boolean isVerified;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authorities",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<Authority> authorities = new HashSet<>();


    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
}
