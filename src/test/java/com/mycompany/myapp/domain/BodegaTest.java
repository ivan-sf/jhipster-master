package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BodegaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bodega.class);
        Bodega bodega1 = new Bodega();
        bodega1.setId(1L);
        Bodega bodega2 = new Bodega();
        bodega2.setId(bodega1.getId());
        assertThat(bodega1).isEqualTo(bodega2);
        bodega2.setId(2L);
        assertThat(bodega1).isNotEqualTo(bodega2);
        bodega1.setId(null);
        assertThat(bodega1).isNotEqualTo(bodega2);
    }
}
