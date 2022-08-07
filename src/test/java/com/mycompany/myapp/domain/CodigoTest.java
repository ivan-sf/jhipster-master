package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodigoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Codigo.class);
        Codigo codigo1 = new Codigo();
        codigo1.setId(1L);
        Codigo codigo2 = new Codigo();
        codigo2.setId(codigo1.getId());
        assertThat(codigo1).isEqualTo(codigo2);
        codigo2.setId(2L);
        assertThat(codigo1).isNotEqualTo(codigo2);
        codigo1.setId(null);
        assertThat(codigo1).isNotEqualTo(codigo2);
    }
}
