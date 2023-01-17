package ru.job4j.cars.model;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auto_user")
public class User {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private String password;

    @Override
    public String toString() {
        return "User{id='" + id
                + "', login='" + login
                + "', password='" + password + "'}";
    }
}
