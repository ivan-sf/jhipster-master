package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoComprobanteContableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoComprobanteContable.class);
        TipoComprobanteContable tipoComprobanteContable1 = new TipoComprobanteContable();
        tipoComprobanteContable1.setId(1L);
        TipoComprobanteContable tipoComprobanteContable2 = new TipoComprobanteContable();
        tipoComprobanteContable2.setId(tipoComprobanteContable1.getId());
        assertThat(tipoComprobanteContable1).isEqualTo(tipoComprobanteContable2);
        tipoComprobanteContable2.setId(2L);
        assertThat(tipoComprobanteContable1).isNotEqualTo(tipoComprobanteContable2);
        tipoComprobanteContable1.setId(null);
        assertThat(tipoComprobanteContable1).isNotEqualTo(tipoComprobanteContable2);
    }
}
