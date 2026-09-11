package com.maelrltt.norna.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Space {
    @Id
    @UUID
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "space")
    private List<SpaceMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "space")
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "space")
    private List<Board> boards = new ArrayList<>();
}
