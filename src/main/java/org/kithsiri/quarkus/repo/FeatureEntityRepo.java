package org.kithsiri.quarkus.repo;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import org.kithsiri.quarkus.entity.FeatureEntity;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FeatureEntityRepo implements PanacheRepository<FeatureEntity> {
}
