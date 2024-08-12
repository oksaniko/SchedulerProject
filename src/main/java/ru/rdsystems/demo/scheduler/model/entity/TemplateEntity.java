package ru.rdsystems.demo.scheduler.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "templates")
public class TemplateEntity {

    @Id
    @Column(length = 32, nullable = false)
    private String id;

    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private List<SlotEntity> slots;

}
