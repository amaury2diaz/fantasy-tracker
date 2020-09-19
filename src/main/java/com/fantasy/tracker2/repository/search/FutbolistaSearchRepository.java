package com.fantasy.tracker2.repository.search;

import com.fantasy.tracker2.domain.Futbolista;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Futbolista} entity.
 */
public interface FutbolistaSearchRepository extends ElasticsearchRepository<Futbolista, Long> {
}
