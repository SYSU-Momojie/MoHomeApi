package com.cn.momojie.talk;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cn.momojie.auth.SessionUtil;
import com.cn.momojie.basic.vo.OperationResp;
import com.cn.momojie.basic.vo.PageResult;
import com.cn.momojie.es.param.MoTalkSentenceParam;
import com.cn.momojie.es.po.MoTalkLabel;
import com.cn.momojie.es.po.MoTalkSentence;
import com.cn.momojie.es.repo.MoTalkLabelRepo;
import com.cn.momojie.es.repo.MoTalkSentenceRepo;
import com.cn.momojie.talk.constant.SentenceType;
import com.cn.momojie.talk.dao.UserLikeDao;
import com.cn.momojie.talk.dto.UserLike;
import com.cn.momojie.talk.vo.SentenceVo;
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

	@Autowired
	private UserLikeDao likeDao;

	@RequestMapping(path = "/addSentence", method = RequestMethod.POST)
	public OperationResp addSentence(@RequestBody MoTalkSentence input) {
		input.setId(null);
		input.setLike(0);
		input.setDislike(0);
		if (input.getLabels() == null) {
			input.setLabels(new ArrayList<>());
		}
		MoTalkSentence result = gsr.save(input);
		return OperationResp.ok(result.getId());
	}

	@RequestMapping(path = "/deleteSentence", method = RequestMethod.POST)
	public OperationResp deleteSentence(@RequestBody String id) {
		gsr.deleteById(id);
		return OperationResp.ok(null);
	}

	@RequestMapping(path = "/editSentence", method = RequestMethod.POST)
	public OperationResp editSentence(@RequestBody MoTalkSentence input) {
		MoTalkSentence result = gsr.save(input);
		return OperationResp.ok(result.getId());
	}

	@RequestMapping(path = "/getByPage", method = RequestMethod.POST)
	public PageResult<SentenceVo> getByPage(@RequestBody MoTalkSentenceParam param) {
		Page<MoTalkSentence> esResult = null;
		if (param.getRandom()) {
			if (param.noLabel()) {
				esResult = gsr.findRandomly(DateTimeUtils.getCurrentTimestampStr(), getPageParam(param));
			} else {
				esResult = gsr.findByLabelsRandomly(param.getLabel(), DateTimeUtils.getCurrentTimestampStr(), getPageParam(param));
			}
		} else {
			if (param.noLabel()) {
				esResult = gsr.findByContent(param.getContent(), getPageParam(param));
			} else {
				esResult = gsr.findByContentAndLabels(param.getContent(), param.getLabel(), getPageParam(param));
			}
		}

		List<MoTalkSentence> esList = esResult.getContent();

		PageResult<SentenceVo> ret = new PageResult<>();
		ret.setTotal(esResult.getTotalElements());

		List<UserLike> likeList = likeDao.getSpecify(SentenceType.SHORT, SessionUtil.getUserId(),
				esList.stream().map(MoTalkSentence::getId).collect(Collectors.toList()));

		Map<String, UserLike> sidMap = likeList.stream().collect(Collectors.toMap(UserLike::getSentenceId, i->i));

		List<SentenceVo> voList = new LinkedList<>();
		for (MoTalkSentence es: esList) {
			voList.add(SentenceVo.fromSentence(es, sidMap.get(es.getId())));
		}
		ret.setList(voList);
		return ret;
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

	@RequestMapping(path = "/getSentenceById", method = RequestMethod.POST)
	public SentenceVo getSentenceById(@RequestBody String id) {
		MoTalkSentence es = gsr.findById(id).orElse(null);
		if (es == null) {
			return new SentenceVo();
		}
		UserLike like = likeDao.getSpecify(SentenceType.SHORT, SessionUtil.getUserId(), Arrays.asList(es.getId())).stream().findFirst().orElse(null);
		return SentenceVo.fromSentence(es, like);
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
}
