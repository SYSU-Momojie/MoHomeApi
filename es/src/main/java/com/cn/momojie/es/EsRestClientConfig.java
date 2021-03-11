package com.cn.momojie.es;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = {"com.cn.momojie.es.repo"})
public class EsRestClientConfig {
}
