package com.test.electronic.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "authorities")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    private String name;
}
