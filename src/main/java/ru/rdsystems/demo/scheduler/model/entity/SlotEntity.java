package ru.rdsystems.demo.scheduler.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "slots")
public class SlotEntity {

    @Id
    @Column(length = 32, nullable = false)
    private String id;

    @Column(name = "begin_time", nullable = false)
    private LocalTime beginTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "schedule_template_id", referencedColumnName = "id")
    private TemplateEntity template;
}
