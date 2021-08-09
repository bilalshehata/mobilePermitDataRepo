package mobileFoodPermit.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mobileFoodPermit.MobileFoodPermitStorage;
import mobileFoodPermit.models.MobileFoodPermit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
public class MobileFoodPermitAPIController {

    public final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);;



    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/get_all")
    public String firstObject() throws JsonProcessingException {

        return OBJECT_MAPPER.writeValueAsString(MobileFoodPermitStorage.getStorage().values());
    }

    @RequestMapping("/getbylocationid")
    public String mobileFoodPermitByLocationId(@RequestParam String locationId) throws JsonProcessingException, UnsupportedEncodingException {

        return OBJECT_MAPPER.writeValueAsString(MobileFoodPermitStorage.getByLocationId(locationId));
    }

    @RequestMapping("/getbyname")
    public String mobileFoodPermitByName(@RequestParam String name) throws JsonProcessingException, UnsupportedEncodingException {

        return OBJECT_MAPPER.writeValueAsString(MobileFoodPermitStorage.getByName(name));
    }
    @RequestMapping("/getbynamestartswith")
    public String mobileFoodPermitByNameStartsWith(@RequestParam String name) throws JsonProcessingException, UnsupportedEncodingException {

        return OBJECT_MAPPER.writeValueAsString(MobileFoodPermitStorage.getByNameStartingWith(name));
    }
    @RequestMapping("/getbyradius")
    public String mobileFoodPermitByRadius(@RequestParam double latitude, @RequestParam double longitude,@RequestParam double radius) throws JsonProcessingException, UnsupportedEncodingException {

        return OBJECT_MAPPER.writeValueAsString(MobileFoodPermitStorage.getByRadius(latitude,longitude,radius));
    }



    @PostMapping("/create")
    public ResponseEntity create(@Valid @RequestBody MobileFoodPermit mobileFoodPermit) {
        // make sure to check whether the new person does not already exist
        return ResponseEntity.ok(MobileFoodPermitStorage.addMobileFoodPermit(mobileFoodPermit));
    }



}
