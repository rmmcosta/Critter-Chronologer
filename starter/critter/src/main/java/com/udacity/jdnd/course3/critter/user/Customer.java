package com.udacity.jdnd.course3.critter.user;

import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 128)
    @Nationalized
    private String name;
    @Column(length = 50)
    private String phoneNumber;
    @Column(length = 5000)
    @Nationalized
    private String notes;
}
