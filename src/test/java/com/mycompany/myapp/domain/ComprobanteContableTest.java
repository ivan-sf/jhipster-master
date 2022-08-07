package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComprobanteContableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComprobanteContable.class);
        ComprobanteContable comprobanteContable1 = new ComprobanteContable();
        comprobanteContable1.setId(1L);
        ComprobanteContable comprobanteContable2 = new ComprobanteContable();
        comprobanteContable2.setId(comprobanteContable1.getId());
        assertThat(comprobanteContable1).isEqualTo(comprobanteContable2);
        comprobanteContable2.setId(2L);
        assertThat(comprobanteContable1).isNotEqualTo(comprobanteContable2);
        comprobanteContable1.setId(null);
        assertThat(comprobanteContable1).isNotEqualTo(comprobanteContable2);
    }
}
