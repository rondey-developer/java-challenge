package jp.co.axa.apidemo.entities;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="LoginInformation")
public class LoginInformation {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name="Id")
    private Long id;

    @Getter
    @Setter
    @Column(name="Username")
    private String username;

    @Getter
    @Setter
    @Column(name="Password")
    private String password;
}
