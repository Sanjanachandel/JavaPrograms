package com.capg.rechargenova.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RazorpayConfigTest {

    @Test
    void testRazorpayClient_ValidKeys() throws RazorpayException {
        RazorpayConfig config = new RazorpayConfig();
        ReflectionTestUtils.setField(config, "keyId", "test_key_id");
        ReflectionTestUtils.setField(config, "keySecret", "test_key_secret");

        RazorpayClient client = config.razorpayClient();
        assertNotNull(client);
    }

    @Test
    void testRazorpayClient_NullKeyId() throws RazorpayException {
        RazorpayConfig config = new RazorpayConfig();
        ReflectionTestUtils.setField(config, "keyId", null);
        ReflectionTestUtils.setField(config, "keySecret", "test_key_secret");

        RazorpayClient client = config.razorpayClient();
        assertNotNull(client);
    }

    @Test
    void testRazorpayClient_EmptyKeyId() throws RazorpayException {
        RazorpayConfig config = new RazorpayConfig();
        ReflectionTestUtils.setField(config, "keyId", "");
        ReflectionTestUtils.setField(config, "keySecret", "test_key_secret");

        RazorpayClient client = config.razorpayClient();
        assertNotNull(client);
    }

    @Test
    void testRazorpayClient_NullKeySecret() throws RazorpayException {
        RazorpayConfig config = new RazorpayConfig();
        ReflectionTestUtils.setField(config, "keyId", "test_key_id");
        ReflectionTestUtils.setField(config, "keySecret", null);

        RazorpayClient client = config.razorpayClient();
        assertNotNull(client);
    }

    @Test
    void testRazorpayClient_EmptyKeySecret() throws RazorpayException {
        RazorpayConfig config = new RazorpayConfig();
        ReflectionTestUtils.setField(config, "keyId", "test_key_id");
        ReflectionTestUtils.setField(config, "keySecret", "");

        RazorpayClient client = config.razorpayClient();
        assertNotNull(client);
    }
}
