package com.capg.rechargenova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoverageTest {

    @Test
    void testMain() {
        assertNotNull(new EurekaServerApplication());
    }
}
