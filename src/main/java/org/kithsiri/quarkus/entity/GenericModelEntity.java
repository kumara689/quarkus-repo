package org.kithsiri.quarkus.entity;

import lombok.Data;
import org.kithsiri.quarkus.iot.SmartFeature;
import org.kithsiri.quarkus.iot.SmartModel;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "SMART_MODEL")
public class GenericModelEntity extends BaseEntity {

    @Column(name="NAME")
    private String name;

    @Column(name="CATEGORY")
    private String category;

    @Column(name="IP_ADDRESS")
    private String ipAddress;

    @Column(name="STATUS")
    private String status;

    @Column(name="TIMEZONE")
    private String timeZone;

    @Column(name="TYPE")
    private String type;

    @Column(name="MISCELLANEOUS")
    private String miscellaneous;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "MODEL_FEATURE", joinColumns = @JoinColumn(name = "model_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id"))
    private Set<FeatureEntity> smartFeature;


    public SmartModel toGrpc(){
        SmartModel.Builder smartModelBuilder = SmartModel.newBuilder()
                .setId(this.id)
                .setName(this.name)
                .setCategory(this.category)
                .setIpAddress(this.ipAddress)
                .setStatus(this.status)
                .setTimeZone(this.timeZone)
                .setType(this.type)
                .setMiscellaneous(this.miscellaneous);

        for(FeatureEntity featureEntity: this.smartFeature){
            SmartFeature feature = SmartFeature.newBuilder()
                    .setId(featureEntity.id)
                    .setName(featureEntity.getName())
                    .setDescription(featureEntity.getDescription())
                    .build();
            smartModelBuilder.addSmartFeature(feature);
        }
        return smartModelBuilder.build();
    }

}
