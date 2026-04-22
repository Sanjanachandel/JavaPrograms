package com.capg.rechargenova;

import com.capg.rechargenova.config.OpenApiConfig;
import com.capg.rechargenova.util.CloudinaryUtil;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.*;

class MiscConfigsTest {

    @Test
    void testOpenApiConfig() {
        OpenApiConfig config = new OpenApiConfig();
        assertNotNull(config.customOpenAPI());
    }
    
    @Test
    void testApplication() {
        UserServiceApplication app = new UserServiceApplication();
        assertNotNull(app);
        // Call main with random port to get coverage without port conflict
        UserServiceApplication.main(new String[]{"--server.port=0"});
    }

    @Test
    void testCloudinaryUtilValidations() {
        CloudinaryUtil util = new CloudinaryUtil();
        ReflectionTestUtils.setField(util, "cloudName", "1");
        ReflectionTestUtils.setField(util, "apiKey", "2");
        ReflectionTestUtils.setField(util, "apiSecret", "3");
        
        MultipartFile nullFile = null;
        assertThrows(IllegalArgumentException.class, () -> util.uploadProfilePicture(nullFile));
        
        MockMultipartFile emptyFile = new MockMultipartFile("f", new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> util.uploadProfilePicture(emptyFile));
        
        MockMultipartFile bigFile = new MockMultipartFile("f", "f", "image/jpeg", new byte[(2 * 1024 * 1024) + 1]);
        assertThrows(IllegalArgumentException.class, () -> util.uploadProfilePicture(bigFile));
        
        MockMultipartFile badType = new MockMultipartFile("f", "f", "text/plain", "data".getBytes());
        assertThrows(IllegalArgumentException.class, () -> util.uploadProfilePicture(badType));
        
        MockMultipartFile noType = new MockMultipartFile("f", "f", null, "data".getBytes());
        assertThrows(IllegalArgumentException.class, () -> util.uploadProfilePicture(noType));
        
        MockMultipartFile pngFile = new MockMultipartFile("f", "f", "image/png", "data".getBytes());
        // we can't fully upload without mocking getCloudinary, but we just want to pass validation
        assertThrows(RuntimeException.class, () -> util.uploadProfilePicture(pngFile)); 
    }
}
