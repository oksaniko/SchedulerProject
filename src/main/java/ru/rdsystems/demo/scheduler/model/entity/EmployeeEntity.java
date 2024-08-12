package ru.rdsystems.demo.scheduler.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @Column(length = 32, nullable = false)
    private String id;

    @Column(name = "employee_name", nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeePosition position;

    @OneToMany(mappedBy = "administrator")
    private List<TimetableEntity> timetableForAdmin;

    @OneToMany(mappedBy = "executor")
    private List<TimetableEntity> timetablesForExecute;

    public enum EmployeeStatus {
        WORKING, TRIAL, TIME_OFF, DISMISSED
    }

    public enum EmployeePosition{
        MANAGER, EMPLOYEE, UNDEFINED, TECH
    }

}
