package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import mobilefoodpermit.api.MobileFoodPermitAPIController;
import mobilefoodpermit.models.MobileFoodPermit;
import mobilefoodpermit.storage.MobileFoodPermitStorageHandler;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class MobileFoodPermitAPIControllerTest {

    @Test
    public void create_permitWithData_shouldCreate() {
        MobileFoodPermit permit= new MobileFoodPermit();
        MobileFoodPermitAPIController apiController =  new MobileFoodPermitAPIController(Mockito.mock(MobileFoodPermitStorageHandler.class));
        Assert.assertEquals(200,apiController.create(permit).getStatusCodeValue());

    }

    @Test
    public void mobileFoodPermitByLocationId_withoutResult_shouldFail() throws JsonProcessingException {
        MobileFoodPermitAPIController apiController =  new MobileFoodPermitAPIController(Mockito.mock(MobileFoodPermitStorageHandler.class));
        Assert.assertEquals("null",apiController.mobileFoodPermitByLocationId("UNAVAILABLE") );

    }


}