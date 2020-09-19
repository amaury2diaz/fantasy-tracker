package com.fantasy.tracker2.repository.search;

import com.fantasy.tracker2.domain.Operacion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Operacion} entity.
 */
public interface OperacionSearchRepository extends ElasticsearchRepository<Operacion, Long> {
}
