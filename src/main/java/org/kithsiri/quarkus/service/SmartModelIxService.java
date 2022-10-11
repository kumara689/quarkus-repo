package org.kithsiri.quarkus.service;

import io.smallrye.mutiny.Uni;
import org.kithsiri.quarkus.iot.*;

public interface SmartModelIxService {
    Uni<SmartModelResponse> persist(SmartModel request);
    Uni<RetrieveSmartModelResponse> getModelById(RetrieveSmartModelById request);
    Uni<RetrieveSmartModelListResponse> getModelByName(RetrieveSmartModelByName request);
    Uni<RetrieveSmartModelListResponse> getModelByType(RetrieveSmartModelByType request);

    Uni<RetrieveSmartModelListResponse> getModelByCategory(RetrieveSmartModelByCategory request);
}
