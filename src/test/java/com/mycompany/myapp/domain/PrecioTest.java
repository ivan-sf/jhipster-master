package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrecioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Precio.class);
        Precio precio1 = new Precio();
        precio1.setId(1L);
        Precio precio2 = new Precio();
        precio2.setId(precio1.getId());
        assertThat(precio1).isEqualTo(precio2);
        precio2.setId(2L);
        assertThat(precio1).isNotEqualTo(precio2);
        precio1.setId(null);
        assertThat(precio1).isNotEqualTo(precio2);
    }
}
