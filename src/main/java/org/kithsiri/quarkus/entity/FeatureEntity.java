package org.kithsiri.quarkus.entity;

import lombok.Data;
import org.kithsiri.quarkus.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "SMART_FEATURE")
public class FeatureEntity extends BaseEntity {
    @Column(name="NAME")
    private String name;

    @Column(name="DESCRIPTION")
    private String description;

}
