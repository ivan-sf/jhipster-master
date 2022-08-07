package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OficinaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Oficina.class);
        Oficina oficina1 = new Oficina();
        oficina1.setId(1L);
        Oficina oficina2 = new Oficina();
        oficina2.setId(oficina1.getId());
        assertThat(oficina1).isEqualTo(oficina2);
        oficina2.setId(2L);
        assertThat(oficina1).isNotEqualTo(oficina2);
        oficina1.setId(null);
        assertThat(oficina1).isNotEqualTo(oficina2);
    }
}
