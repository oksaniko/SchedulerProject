package ru.rdsystems.demo.scheduler.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "timetables")
public class TimetableEntity {

    @Id
    @Column(length = 32, nullable = false)
    private String id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "slot_id", referencedColumnName = "id")
    private SlotEntity slot;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private ScheduleEntity schedule;

    @Column(name = "slot_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private SlotType slotType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "administrator_id", referencedColumnName = "id", nullable = false)
    private EmployeeEntity administrator;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "executor_id", referencedColumnName = "id")
    private EmployeeEntity executor;

    public enum SlotType {
        LOCAL, FROM_HOME, UNDEFINED
    }
}
