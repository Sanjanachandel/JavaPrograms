package com.capg.rechargenova.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockedConstruction;
import java.util.Map;
import java.util.HashMap;

class CloudinaryUtilTest {

    @InjectMocks
    private CloudinaryUtil cloudinaryUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(cloudinaryUtil, "cloudName", "test");
        ReflectionTestUtils.setField(cloudinaryUtil, "apiKey", "test");
        ReflectionTestUtils.setField(cloudinaryUtil, "apiSecret", "test");
    }

    @Test
    void testValidateImageNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            ReflectionTestUtils.invokeMethod(cloudinaryUtil, "validateImage", (Object)null));
    }

    @Test
    void testValidateImageEmpty() {
        MultipartFile file = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> 
            ReflectionTestUtils.invokeMethod(cloudinaryUtil, "validateImage", file));
    }

    @Test
    void testValidateImageTooLarge() {
        byte[] largeData = new byte[3 * 1024 * 1024];
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", largeData);
        assertThrows(IllegalArgumentException.class, () -> 
            ReflectionTestUtils.invokeMethod(cloudinaryUtil, "validateImage", file));
    }

    @Test
    void testValidateImageInvalidType() {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", new byte[10]);
        assertThrows(IllegalArgumentException.class, () -> 
            ReflectionTestUtils.invokeMethod(cloudinaryUtil, "validateImage", file));
    }

    @Test
    void testValidateImageSuccess() {
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[10]);
        assertDoesNotThrow(() -> ReflectionTestUtils.invokeMethod(cloudinaryUtil, "validateImage", file));
    }

    @Test
    void testUploadProfilePicture() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        
        com.cloudinary.Uploader mockUploader = mock(com.cloudinary.Uploader.class);
        
        Map<String, Object> result = new HashMap<>();
        result.put("secure_url", "http://secure.url");
        
        try (MockedConstruction<com.cloudinary.Cloudinary> mocked = mockConstruction(com.cloudinary.Cloudinary.class, (mock, context) -> {
            when(mock.uploader()).thenReturn(mockUploader);
        })) {
            when(mockUploader.upload(any(byte[].class), anyMap())).thenReturn(result);
            
            String url = cloudinaryUtil.uploadProfilePicture(file);
            assertEquals("http://secure.url", url);
        }
    }
}
