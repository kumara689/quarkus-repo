package org.kithsiri.quarkus.service;

import io.quarkus.arc.log.LoggerName;
import io.quarkus.runtime.util.StringUtil;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.logging.Logger;
import org.kithsiri.quarkus.entity.FeatureEntity;
import org.kithsiri.quarkus.entity.GenericModelEntity;
import org.kithsiri.quarkus.iot.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

@ApplicationScoped
public class SmartModelIxServiceImpl implements SmartModelIxService {

    @LoggerName("SmartModelIxServiceImpl")
    Logger logger;

    @ConfigProperty(name = "app.db.model.record.created.by")
    String createdBy;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    private Function<SmartModel, GenericModelEntity> smartModelToEntity =
            new Function<SmartModel, GenericModelEntity>() {
        public GenericModelEntity apply(SmartModel request) {
            GenericModelEntity genericModelEntity = new GenericModelEntity();
            if (request.getId() != 0) {
                genericModelEntity.id = request.getId();
            }
            genericModelEntity.setName(request.getName());
            genericModelEntity.setCategory(request.getCategory());
            genericModelEntity.setIpAddress(request.getIpAddress());
            genericModelEntity.setStatus(request.getStatus());
            genericModelEntity.setTimeZone(request.getTimeZone());
            genericModelEntity.setType(request.getType());
            genericModelEntity.setMiscellaneous(request.getMiscellaneous());
            genericModelEntity.setCreatedBy(createdBy);
            genericModelEntity.setCreatedTime(LocalDateTime.now());

            if (null != request.getSmartFeatureList()
                    && request.getSmartFeatureList().size() > 0) {
                Set<FeatureEntity> smartFeatures = new HashSet<>(0);
                for (SmartFeature feature : request.getSmartFeatureList()) {
                    FeatureEntity modelFeature = new FeatureEntity();
                    modelFeature.setName(feature.getName());
                    modelFeature.setDescription(feature.getDescription());
                    modelFeature.setCreatedBy(createdBy);
                    modelFeature.setCreatedTime(LocalDateTime.now());
                    smartFeatures.add(modelFeature);
                }
                genericModelEntity.setSmartFeature(smartFeatures);
            }
            return genericModelEntity;
        }
    };

    @Override
    public Uni<SmartModelResponse> persist(SmartModel request) {
        if(Objects.nonNull(request)){
            logger.infof("New Model is going to be created. %s", request.getName());
            GenericModelEntity genericModelEntity = smartModelToEntity.apply(request);
            return sessionFactory.withTransaction((s, t) -> s.persist(genericModelEntity))
                    .replaceWith(() -> SmartModelResponse.newBuilder()
                            .setMessage(Objects.nonNull(genericModelEntity.id) && genericModelEntity.id > 0
                                    ? Long.toString(genericModelEntity.id) : "ERROR")
                            .build());
        }
        return Uni.createFrom().item(SmartModelResponse.getDefaultInstance());
    }

    @Override
    public Uni<RetrieveSmartModelResponse> getModelById(RetrieveSmartModelById request) {
        if(Objects.nonNull(request) && !StringUtil.isNullOrEmpty(request.getId())){
            return sessionFactory.withTransaction((s, t) -> s.find(GenericModelEntity.class, Long.valueOf(request.getId())))
                    .map(s -> Objects.nonNull(s) ?
                            RetrieveSmartModelResponse.newBuilder()
                                    .setModel(SmartModel.newBuilder(s.toGrpc()).build())
                                    .build() : RetrieveSmartModelResponse.getDefaultInstance()
                    );
        }
        return Uni.createFrom().item(RetrieveSmartModelResponse.getDefaultInstance());
    }

    @Override
    public Uni<RetrieveSmartModelListResponse> getModelByName(RetrieveSmartModelByName request) {
        if(Objects.nonNull(request) && !StringUtil.isNullOrEmpty(request.getName())) {
            RetrieveSmartModelListResponse.Builder responseBuilder = RetrieveSmartModelListResponse.newBuilder();
            Uni<List<GenericModelEntity>> responseList = GenericModelEntity.list("name", request.getName());
            return responseList.onItem().transform(
                    x -> {
                        collector(responseBuilder, x);
                        return responseBuilder.build();
                    }
            );
        }
        return Uni.createFrom().item(RetrieveSmartModelListResponse.getDefaultInstance());
    }

    @Override
    public Uni<RetrieveSmartModelListResponse> getModelByType(RetrieveSmartModelByType request) {
        if(Objects.nonNull(request) && !StringUtil.isNullOrEmpty(request.getType())) {
            RetrieveSmartModelListResponse.Builder responseBuilder = RetrieveSmartModelListResponse.newBuilder();
            Uni<List<GenericModelEntity>> responseList = GenericModelEntity.list("type", request.getType());
            return responseList.onItem().transform(
                    x -> {
                        collector(responseBuilder, x);
                        return responseBuilder.build();
                    }
            );
        }
        return Uni.createFrom().item(RetrieveSmartModelListResponse.getDefaultInstance());
    }

    @Override
    public Uni<RetrieveSmartModelListResponse> getModelByCategory(RetrieveSmartModelByCategory request) {
        if(Objects.nonNull(request) && !StringUtil.isNullOrEmpty(request.getCategory())) {
            RetrieveSmartModelListResponse.Builder responseBuilder = RetrieveSmartModelListResponse.newBuilder();
            Uni<List<GenericModelEntity>> responseList = GenericModelEntity.list("category", request.getCategory());
            return responseList.onItem().transform(
                    x -> {
                        collector(responseBuilder, x);
                        return responseBuilder.build();
                    }
            );
        }
        return Uni.createFrom().item(RetrieveSmartModelListResponse.getDefaultInstance());
    }


    private void collector(RetrieveSmartModelListResponse.Builder builder, List<GenericModelEntity> entities){
        for(GenericModelEntity g: entities){
            builder.addModels(g.toGrpc());
        }
    }

}
