package com.fantasy.tracker2.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link OperacionSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class OperacionSearchRepositoryMockConfiguration {

    @MockBean
    private OperacionSearchRepository mockOperacionSearchRepository;

}
