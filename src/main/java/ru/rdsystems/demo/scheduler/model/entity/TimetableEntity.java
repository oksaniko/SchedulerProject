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

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "slot_id", referencedColumnName = "id")
    private SlotEntity slot;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private ScheduleEntity schedule;

    @Column(name = "slot_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private SlotType slotType;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "administrator_id", referencedColumnName = "id", nullable = false)
    private EmployeeEntity administrator;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "executor_id", referencedColumnName = "id")
    private EmployeeEntity executor;

    public enum SlotType {
        LOCAL, FROM_HOME, UNDEFINED
    }
}
