package com.fantasy.tracker2.repository.search;

import com.fantasy.tracker2.domain.Liga;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Liga} entity.
 */
public interface LigaSearchRepository extends ElasticsearchRepository<Liga, Long> {
}
