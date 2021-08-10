package mobilefoodpermit.config;

import mobilefoodpermit.storage.MobileFoodPermitStorage;
import mobilefoodpermit.storage.MobileFoodPermitStorageHandler;
import mobilefoodpermit.storage.StorageFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class Config {

    /**
     *
     * Using bean annotation to always return same one.
     * @return mobile food permit storage.
     * @throws IOException
     */
    @Bean
    public MobileFoodPermitStorage getMobileFoodPermitStorage(@Value("${csv_location}") String csvLocation) throws IOException {
        //this can be configured to point to application.properties later.
        return StorageFactory.fromCSV(csvLocation);
    }

    /**
     * handler to handle requests from the API controller.
     * Using bean annotation to always return same one.
     * @return
     * @throws IOException
     */
    @Bean
    public MobileFoodPermitStorageHandler getMobileFoodPermitStorageHandler(MobileFoodPermitStorage storage,
                                                                            @Value("${max_autofill}") String maxAutofill) throws IOException {
        return new MobileFoodPermitStorageHandler(storage, Integer.valueOf(maxAutofill));
    }

}
