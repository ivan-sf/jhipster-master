package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoUsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoUsuario.class);
        TipoUsuario tipoUsuario1 = new TipoUsuario();
        tipoUsuario1.setId(1L);
        TipoUsuario tipoUsuario2 = new TipoUsuario();
        tipoUsuario2.setId(tipoUsuario1.getId());
        assertThat(tipoUsuario1).isEqualTo(tipoUsuario2);
        tipoUsuario2.setId(2L);
        assertThat(tipoUsuario1).isNotEqualTo(tipoUsuario2);
        tipoUsuario1.setId(null);
        assertThat(tipoUsuario1).isNotEqualTo(tipoUsuario2);
    }
}
