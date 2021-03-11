package com.cn.momojie.es;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cn.momojie.basic.vo.OperationResp;
import com.cn.momojie.basic.vo.PageResult;
import com.cn.momojie.es.param.MoTalkSentenceParam;
import com.cn.momojie.es.po.MoTalkLabel;
import com.cn.momojie.es.po.MoTalkSentence;
import com.cn.momojie.es.repo.MoTalkLabelRepo;
import com.cn.momojie.es.repo.MoTalkSentenceRepo;
import com.cn.momojie.utils.DateTimeUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/motalk/sentence")
@Slf4j
public class MoTalkSentenceController {

	@Autowired
	private MoTalkSentenceRepo gsr;

	@Autowired
	private MoTalkLabelRepo lr;

	@RequestMapping(path = "/addSentence", method = RequestMethod.POST)
	public OperationResp addSentence(@RequestBody MoTalkSentence input) {
		try {
			input.setId(null);
			input.setLike(0);
			if (input.getLabels() == null) {
				input.setLabels(new ArrayList<>());
			}
			MoTalkSentence result = gsr.save(input);
			return OperationResp.ok(result.getId());
		} catch (Exception e) {
			log.error("保存句子失败", e);
			return OperationResp.fail(e.getMessage(), null);
		}
	}

	@RequestMapping(path = "/deleteSentence", method = RequestMethod.POST)
	public OperationResp deleteSentence(@RequestBody String id) {
		try {
			gsr.deleteById(id);
			return OperationResp.ok(null);
		} catch (Exception e) {
			log.error("删除句子失败", e);
			return OperationResp.fail(e.getMessage(), null);
		}
	}

	@RequestMapping(path = "/editSentence", method = RequestMethod.POST)
	public OperationResp editSentence(@RequestBody MoTalkSentence input) {
		try {
			MoTalkSentence result = gsr.save(input);
			return OperationResp.ok(result.getId());
		} catch (Exception e) {
			log.error("编辑句子失败", e);
			return OperationResp.fail(e.getMessage(), null);
		}
	}

	@RequestMapping(path = "/getByPage", method = RequestMethod.POST)
	public PageResult<MoTalkSentence> getByPage(@RequestBody MoTalkSentenceParam param) {
		if (param.getRandom()) {
			if (param.noLabel()) {
				return fromRepo(gsr.findRandomly(DateTimeUtils.getCurrentTimestampStr(), getPageParam(param)));
			} else {
				return fromRepo(gsr.findByLabelsRandomly(param.getLabel(), DateTimeUtils.getCurrentTimestampStr(), getPageParam(param)));
			}
		} else {
			if (param.noLabel()) {
				return fromRepo(gsr.findByContent(param.getContent(), getPageParam(param)));
			} else {
				return fromRepo(gsr.findByContentAndLabels(param.getContent(), param.getLabel(), getPageParam(param)));
			}
		}
	}

	@RequestMapping(path = "/likeSentence", method = RequestMethod.POST)
	public OperationResp likeSentence(@RequestBody String id) {
		MoTalkSentence result = gsr.findById(id).orElse(null);
		if (result == null) {
			return OperationResp.fail("无法找到对应记录", null);
		}
		result.setLike(result.getLike() + 1);
		return editSentence(result);
	}

	@RequestMapping(path = "/getAllLabels", method = RequestMethod.POST)
	public List<MoTalkLabel> getAllLabels() {
		List<MoTalkLabel> result = new LinkedList<>();
		lr.findAll().iterator().forEachRemaining(result::add);
		return result;
	}

	private Pageable getPageParam(MoTalkSentenceParam param) {
		if (param.getPageNum() != null && param.getPageSize() != null) {
			return PageRequest.of(param.getPageNum(), param.getPageSize());
		}
		return PageRequest.of(0, 20);
	}

	private <T> PageResult<T> fromRepo(Page<T> page) {
		PageResult<T> result = new PageResult<>();
		result.setTotal(page.getTotalElements());
		result.setList(page.getContent());
		return result;
	}
}
