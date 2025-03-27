package com.krittawat.groomingapi.datasource.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @ManyToOne
    @JoinColumn(name = "role", referencedColumnName = "id", nullable = true,
            foreignKey = @ForeignKey(name = "fk_user_role", foreignKeyDefinition = "FOREIGN KEY (role) REFERENCES role(id) ON DELETE SET NULL"))
    private ERole role;
    private String firstname;
    private String lastname;
    private String nickname;
    private String email;
    private String phone1;
    private String phone2;
    @CreationTimestamp
    private LocalDateTime createdDate;
}
