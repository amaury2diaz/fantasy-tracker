package com.fantasy.tracker2.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.fantasy.tracker2.web.rest.TestUtil;

public class FutbolistaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Futbolista.class);
        Futbolista futbolista1 = new Futbolista();
        futbolista1.setId(1L);
        Futbolista futbolista2 = new Futbolista();
        futbolista2.setId(futbolista1.getId());
        assertThat(futbolista1).isEqualTo(futbolista2);
        futbolista2.setId(2L);
        assertThat(futbolista1).isNotEqualTo(futbolista2);
        futbolista1.setId(null);
        assertThat(futbolista1).isNotEqualTo(futbolista2);
    }
}
