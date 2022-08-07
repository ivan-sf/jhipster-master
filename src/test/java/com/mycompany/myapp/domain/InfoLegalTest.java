package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InfoLegalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfoLegal.class);
        InfoLegal infoLegal1 = new InfoLegal();
        infoLegal1.setId(1L);
        InfoLegal infoLegal2 = new InfoLegal();
        infoLegal2.setId(infoLegal1.getId());
        assertThat(infoLegal1).isEqualTo(infoLegal2);
        infoLegal2.setId(2L);
        assertThat(infoLegal1).isNotEqualTo(infoLegal2);
        infoLegal1.setId(null);
        assertThat(infoLegal1).isNotEqualTo(infoLegal2);
    }
}
