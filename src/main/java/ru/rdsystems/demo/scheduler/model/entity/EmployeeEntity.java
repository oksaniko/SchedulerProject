package ru.rdsystems.demo.scheduler.model.entity;

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
    private List<PeriodEntity> periodsForAdmin;

    @OneToMany(mappedBy = "executor")
    private List<PeriodEntity> periodsForExecute;

    public enum EmployeeStatus {
        WORKING, TRIAL, TIME_OFF, DISMISSED
    }

    public enum EmployeePosition{
        MANAGER, EMPLOYEE, UNDEFINED, TECH
    }

}
