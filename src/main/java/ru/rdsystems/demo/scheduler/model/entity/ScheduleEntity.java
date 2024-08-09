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
@Table(name = "schedules")
public class ScheduleEntity {

    @Id
    @Column(length = 32, nullable = false)
    private String id;

    @Column(name = "schedule_name")
    private String name;

    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<PeriodEntity> periods;

}
