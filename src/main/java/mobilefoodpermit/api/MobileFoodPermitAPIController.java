package mobilefoodpermit.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mobilefoodpermit.models.MobileFoodPermit;
import mobilefoodpermit.models.UserError;
import mobilefoodpermit.storage.MobileFoodPermitStorageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@RestController
public class MobileFoodPermitAPIController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    private final MobileFoodPermitStorageHandler storageHandler;

    @Autowired
    public MobileFoodPermitAPIController(MobileFoodPermitStorageHandler storageHandler) {
        this.storageHandler = storageHandler;
    }

    private static String getErrorWithMessage() throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(new UserError("Unknown Error occurred"));
    }

    @RequestMapping("/")
    public String index() {
        return "You have reached the MobileFoodPermit API!";
    }

    /***
     *   retrieve all permits in storage. Please use pagedGetAllPermits() for scalability
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/getall")
    public String getAllPermits() throws JsonProcessingException {

        return OBJECT_MAPPER.writeValueAsString(storageHandler.getAllPermits());
    }

    /**
     * Scalable version of get all mobile permits that allows for pagination dependant on the clients requirements
     *
     * @param pageNumber
     * @param pagesSize
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/getallpaginated")
    public String pagedGetAllPermits(@RequestParam int pageNumber, @RequestParam int pagesSize) throws JsonProcessingException {
        List<MobileFoodPermit> page = storageHandler.getPage(pageNumber, pagesSize);
        if(page == null) {
           return  OBJECT_MAPPER.writeValueAsString(new UserError("Invalid Page"));
        }else {
            return OBJECT_MAPPER.writeValueAsString(storageHandler.getPage(pageNumber, pagesSize));
        }
    }

    /**
     * given a location ID return the Mobile Food Permit
     *
     * @param locationId
     * @return
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/getbylocationid")
    public String mobileFoodPermitByLocationId(@RequestParam String locationId) throws JsonProcessingException {
    try{
        return OBJECT_MAPPER.writeValueAsString(storageHandler.getMobileFoodPermitByLocationId(locationId));
    } catch (Exception e) {
        return OBJECT_MAPPER.writeValueAsString(new UserError("Location Id has no associated permit"));
    }
    }

    /***
     * Given an application name, retrieve mobile food permit
     * @param name
     * @return
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/getbyname")
    public String mobileFoodPermitByName(@RequestParam String name) throws JsonProcessingException {

        return OBJECT_MAPPER.writeValueAsString(storageHandler.getByName(name));
    }

    /**
     * Function is used for autocomplete, It will retrieve
     *
     * @param name
     * @return
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/getbynamestartswith")
    public String mobileFoodPermitByNameStartsWith(@RequestParam String name) throws JsonProcessingException {

        return OBJECT_MAPPER.writeValueAsString(storageHandler.getByNameStartingWith(name));
    }

    /***
     * permits within radius function
     * @param X x coordinate
     * @param Y y coordinate
     * @param radius  in meters
     * @return back all permits that are within the bounding circle.
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/getbyradius")
    public String mobileFoodPermitByRadius(@RequestParam double X, @RequestParam double Y,
                                           @RequestParam double radius) throws JsonProcessingException {

        try {
            return OBJECT_MAPPER.writeValueAsString(Optional.of(storageHandler.getByRadius(X, Y, radius)));
        } catch (Exception e) {
            return getErrorWithMessage();
        }
    }

    /***
     * function creates permits
     * @param mobileFoodPermit
     * @return HTTP status
     */
    @PostMapping("/create")
    public ResponseEntity create(@Valid @RequestBody MobileFoodPermit mobileFoodPermit) {
        // make sure to check whether the new person does not already exist
        return ResponseEntity.ok(storageHandler.addMobileFoodPermit(mobileFoodPermit));
    }


}
