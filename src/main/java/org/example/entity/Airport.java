package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "airport", schema = "public")
public class Airport{
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
}
