package org.kithsiri.quarkus.grpc;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import org.kithsiri.quarkus.iot.*;
import org.kithsiri.quarkus.service.SmartModelIxService;
import javax.inject.Inject;

@GrpcService
public class IotGrpcService implements SmartModelService {

    @Inject
    SmartModelIxService smartModelIxService;

    @Override
    public Uni<SmartModelResponse> saveSmartModel(SmartModel request) {
        return smartModelIxService.persist(request);
    }

    @Override
    public Uni<RetrieveSmartModelResponse> retrieveSmartModelById(RetrieveSmartModelById request) {
        return smartModelIxService.getModelById(request);
    }

    @Override
    public Uni<RetrieveSmartModelListResponse> retrieveSmartModelsByName(RetrieveSmartModelByName request) {
        return smartModelIxService.getModelByName(request);
    }

    @Override
    public Uni<RetrieveSmartModelListResponse> retrieveSmartModelsByType(RetrieveSmartModelByType request) {
        return smartModelIxService.getModelByType(request);
    }

    @Override
    public Uni<RetrieveSmartModelListResponse> retrieveSmartModelByCategory(RetrieveSmartModelByCategory request) {
        return smartModelIxService.getModelByCategory(request);
    }

}