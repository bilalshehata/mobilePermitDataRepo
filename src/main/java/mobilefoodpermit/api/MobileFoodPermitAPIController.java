package mobilefoodpermit.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mobilefoodpermit.MobileFoodPermitStorage;
import mobilefoodpermit.MobileFoodPermitStorageController;
import mobilefoodpermit.models.MobileFoodPermit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
public class MobileFoodPermitAPIController {

    public final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    ;


    @RequestMapping("/")
    public String index() {
        return "You have reached the MobileFoodPermit API!";
    }

    /***
     *   retrieve all permits in storage. Please use pagedGetAllPermits() for scalability
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/get-all")
    public String getAllPermits() throws JsonProcessingException {

        return OBJECT_MAPPER.writeValueAsString(MobileFoodPermitStorage.getAllPermits());
    }

    /**
     * Scalable version of get all mobile permits that allows for pagination dependant on the clients requirements
     * @param pageNumber
     * @param pagesSize
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/get-all-paginated")
    public String pagedGetAllPermits(@RequestParam int pageNumber, @RequestParam int pagesSize) throws JsonProcessingException {

        return OBJECT_MAPPER.writeValueAsString(MobileFoodPermitStorageController.getAllPaged(pageNumber,pagesSize));
    }

    /**
     * given a location ID return the Mobile Food Permit
     * @param locationId
     * @return
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/getbylocationid")
    public String mobileFoodPermitByLocationId(@RequestParam String locationId) throws JsonProcessingException, UnsupportedEncodingException {

        return OBJECT_MAPPER.writeValueAsString(MobileFoodPermitStorage.getByLocationId(locationId));
    }


    /***
     * Given an application name, retrieve mobile food permit
     * @param name
     * @return
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/getbyname")
    public String mobileFoodPermitByName(@RequestParam String name) throws JsonProcessingException, UnsupportedEncodingException {

        return OBJECT_MAPPER.writeValueAsString(MobileFoodPermitStorageController.getByName(name));
    }


    /**
     * Function is used for autocomplete, It will retrieve
     * @param name
     * @return
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/getbynamestartswith")
    public String mobileFoodPermitByNameStartsWith(@RequestParam String name) throws JsonProcessingException, UnsupportedEncodingException {

        return OBJECT_MAPPER.writeValueAsString(MobileFoodPermitStorageController.getByNameStartingWith(name));
    }

    @RequestMapping("/getbyradius")
    public String mobileFoodPermitByRadius(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double radius) throws JsonProcessingException, UnsupportedEncodingException {

        return OBJECT_MAPPER.writeValueAsString(MobileFoodPermitStorageController.getByRadius(latitude, longitude, radius));
    }


    @PostMapping("/create")
    public ResponseEntity create(@Valid @RequestBody MobileFoodPermit mobileFoodPermit) {
        // make sure to check whether the new person does not already exist
        return ResponseEntity.ok(MobileFoodPermitStorageController.addMobileFoodPermit(mobileFoodPermit));
    }


}
