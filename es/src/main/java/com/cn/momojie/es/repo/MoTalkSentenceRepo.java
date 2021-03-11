package com.cn.momojie.es.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.cn.momojie.es.po.MoTalkSentence;

public interface MoTalkSentenceRepo extends ElasticsearchRepository<MoTalkSentence, String> {

	@Query("{\"function_score\":{\"functions\":[{\"random_score\":{\"seed\":\"?0\"}}]}}")
	Page<MoTalkSentence> findRandomly(String seedLong, Pageable pageable);

	@Query("{\"function_score\":{\"query\":{\"term\":{\"labels\":\"?0\"}},\"functions\":[{\"random_score\":{\"seed\":\"?1\"}}]}}")
	Page<MoTalkSentence> findByLabelsRandomly(String label, String seedLong, Pageable pageable);

	@Query("{\"function_score\":{\"query\":{\"match\":{\"content\":\"?0\"}},\"field_value_factor\":{\"field\":\"like\",\"modifier\":\"log1p\"}}}")
	Page<MoTalkSentence> findByContent(String content, Pageable pageable);

	@Query("{\"function_score\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"labels\":\"?1\"}},{\"match\":{\"content\":\"?0\"}}]}},\"field_value_factor\":{\"field\":\"like\",\"modifier\":\"log1p\"}}}")
	Page<MoTalkSentence> findByContentAndLabels(String content, String label, Pageable pageable);
}
