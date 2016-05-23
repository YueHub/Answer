package cn.lcy.answer.seg.service;

import cn.lcy.answer.sem.model.WordSegmentResult;

/**
 * @author NarutoKu
 *
 */
public interface WordSegmentationServiceI {
	
	/**
	 * 分词处理以及命名实体识别
	 * @param question
	 * @return
	 */
	public WordSegmentResult wordSegmentation(String question);
	
}
