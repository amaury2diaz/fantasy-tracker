package com.fantasy.tracker2.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link FutbolistaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FutbolistaSearchRepositoryMockConfiguration {

    @MockBean
    private FutbolistaSearchRepository mockFutbolistaSearchRepository;

}
