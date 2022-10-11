package org.kithsiri.quarkus.grpc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.kithsiri.quarkus.iot.*;
import java.time.Duration;
import java.util.List;

@QuarkusTest
public class IotGrpcServiceTest {

    @GrpcClient
    SmartModelService iotGrpcService;


    @Test
    public void testSaveSmartModel() {
        SmartModelResponse expected = SmartModelResponse.newBuilder()
                .setMessage("1")
                .build();

        SmartModelResponse response =
                iotGrpcService.saveSmartModel(testGrpcSmartModel())
                        .await()
                        .atMost(Duration.ofSeconds(4));

        assertEquals(expected, response);
    }

    @Test
    public void testSaveSmartModelWithNulls() {
        assertThrows(NullPointerException.class, () -> iotGrpcService.saveSmartModel(testGrpcSmartModelWithNulls())
                .await()
                .atMost(Duration.ofSeconds(4)));
    }

    @Test
    public void testRetrieveSmartModelById(){
        SmartModel expected = testGrpcSmartModel();

        RetrieveSmartModelResponse response =
                iotGrpcService.retrieveSmartModelById(RetrieveSmartModelById.newBuilder().setId("1").build())
                        .await()
                        .atMost(Duration.ofSeconds(4));
        SmartModel responseModel = response.getModel();

        assertEquals(expected.getName(), responseModel.getName());
        assertEquals(expected.getCategory(), responseModel.getCategory());
        assertEquals(expected.getIpAddress(), responseModel.getIpAddress());
        assertEquals(expected.getStatus(), responseModel.getStatus());
        assertEquals(expected.getTimeZone(), responseModel.getTimeZone());
        assertEquals(expected.getType(), responseModel.getType());
        assertEquals(expected.getMiscellaneous(), responseModel.getMiscellaneous());
        assertEquals(expected.getSmartFeatureList().get(0).getName(), responseModel.getSmartFeatureList().get(0).getName());
        assertEquals(expected.getSmartFeatureList().get(0).getDescription(), responseModel.getSmartFeatureList().get(0).getDescription());

    }

    @Test
    public void testRetrieveSmartModelByNullId(){
        SmartModel expected = SmartModel.getDefaultInstance();

        RetrieveSmartModelResponse response =
                iotGrpcService.retrieveSmartModelById(null)
                        .await()
                        .atMost(Duration.ofSeconds(4));
        SmartModel responseModel = response.getModel();

        assertEquals(expected, responseModel);
    }

    @Test
    public void testRetrieveSmartModelByEmptyId(){
        SmartModel expected = SmartModel.getDefaultInstance();

        RetrieveSmartModelResponse response =
                iotGrpcService.retrieveSmartModelById(RetrieveSmartModelById.newBuilder().setId("").build())
                        .await()
                        .atMost(Duration.ofSeconds(4));
        SmartModel responseModel = response.getModel();

        assertEquals(expected, responseModel);
    }

    @Test
    public void testRetrieveSmartModelByWrongId(){
        SmartModel expected = SmartModel.getDefaultInstance();

        RetrieveSmartModelResponse response =
                iotGrpcService.retrieveSmartModelById(RetrieveSmartModelById.newBuilder().setId("4").build())
                        .await()
                        .atMost(Duration.ofSeconds(4));
        SmartModel responseModel = response.getModel();

        assertEquals(expected, responseModel);
    }

    @Test
    public void testRetrieveSmartModelByName(){
        SmartModel expected = testGrpcSmartModel();

        RetrieveSmartModelListResponse response =
                iotGrpcService.retrieveSmartModelsByName(RetrieveSmartModelByName.newBuilder().setName("Sony Xpro 343").build())
                        .await()
                        .atMost(Duration.ofSeconds(4));
        List<SmartModel> responseModels = response.getModelsList();

        for(SmartModel responseModel: responseModels) {
            assertEquals(expected.getName(), responseModel.getName());
            assertEquals(expected.getCategory(), responseModel.getCategory());
            assertEquals(expected.getIpAddress(), responseModel.getIpAddress());
            assertEquals(expected.getStatus(), responseModel.getStatus());
            assertEquals(expected.getTimeZone(), responseModel.getTimeZone());
            assertEquals(expected.getType(), responseModel.getType());
            assertEquals(expected.getMiscellaneous(), responseModel.getMiscellaneous());
            assertEquals(expected.getSmartFeatureList().get(0).getName(), responseModel.getSmartFeatureList().get(0).getName());
            assertEquals(expected.getSmartFeatureList().get(0).getDescription(), responseModel.getSmartFeatureList().get(0).getDescription());
        }
    }

    @Test
    public void testRetrieveSmartModelByEmptyName(){
        RetrieveSmartModelListResponse response =
                iotGrpcService.retrieveSmartModelsByName(RetrieveSmartModelByName.newBuilder().setName(" ").build())
                        .await()
                        .atMost(Duration.ofSeconds(4));
        List<SmartModel> responseModels = response.getModelsList();
        assertEquals(responseModels.size(), 0);
    }

    private static SmartModel testGrpcSmartModel(){
        SmartFeature feature = SmartFeature.newBuilder()
                .setName("360 Degree Rotation")
                .setDescription("Pan and Tilt")
                .build();

        return SmartModel.newBuilder()
                .setName("Sony Xpro 343")
                .setCategory("Camera")
                .setIpAddress("10.234.32.34")
                .setStatus("online")
                .setTimeZone("Singapore")
                .setType("CCTV")
                .setMiscellaneous("Front Gate Camera")
                .addSmartFeature(feature)
                .build();
    }

    private static SmartModel testGrpcSmartModelWithNulls(){
        SmartFeature feature = SmartFeature.newBuilder()
                .setName(null)
                .setDescription("Pan and Tilt")
                .build();

        return SmartModel.newBuilder()
                .setName(" ")
                .setCategory("")
                .setIpAddress("10.234.32.34")
                .setStatus("online")
                .setTimeZone("Singapore")
                .setType("CCTV")
                .setMiscellaneous("Front Gate Camera")
                .addSmartFeature(feature)
                .build();
    }
}
