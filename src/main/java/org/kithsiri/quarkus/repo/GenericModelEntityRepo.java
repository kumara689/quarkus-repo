package org.kithsiri.quarkus.repo;

import io.quarkus.arc.log.LoggerName;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import org.jboss.logging.Logger;
import org.kithsiri.quarkus.entity.GenericModelEntity;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GenericModelEntityRepo implements PanacheRepository<GenericModelEntity> {
}
