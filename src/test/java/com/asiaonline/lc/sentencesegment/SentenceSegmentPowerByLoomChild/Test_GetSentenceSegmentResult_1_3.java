package com.asiaonline.lc.sentencesegment.SentenceSegmentPowerByLoomChild;

import java.io.IOException;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;

import com.omniscien.lc.sentencesegment.model.SentenceSegment;
import com.omniscien.lc.sentencesegment.service.SentenceServiceImpl;

public class Test_GetSentenceSegmentResult_1_3 {

	public static void main(String[] args) throws IOException, ParseException {
		String input = "用于骨植。入材料的磷酸钙盐涂层沉积溶液配方及涂层方法在检讨中, 我们会考虑有关由银行作出财务担保的建议. 对于符合资格的申请人, 创建一个个人帐户大约需要 10 个工作日。用于提供及时的市场交易质量指示的方法确认具有未授权用户物理特征记录的信用卡的使用的系统金属沉积物表面上出现晶须风险的评测方法有关预订的押金的事宜, 酒店会向您发送一封包括酒店银行资料的电子邮件。根据建立的无卡信用帐户信息对所述信用消费认。证请求进行认证;在控制信息保。留成本的同时, 将风险降至最低程度并改进数据保护。x 86 平台税源监控系统及其采集商业销售数据的方法目标系统上至少有一。个用户帐户未按照策略的要求被禁用。此项协议是通过 brand sense 销售公司达成的, 这是一家位于洛杉矶的销售和许可公司.激光唱盘(cd)包装。盒铰链式容纳装置和销售方法另外,";
		String languageid = "zh";
		String encryptline = "\r\n ::: MARKER ::: \r\n";
		String delimiter = ",";
		
		SentenceServiceImpl segment = new SentenceServiceImpl();
		
		String result = segment.getSentenceSegmentResult(input, languageid, encryptline, delimiter);
		
		System.out.println("Input: \r\n"+input);
		System.out.println("\r\n");
		System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////");
		System.out.println("Output: \r\n"+result);

	}
}
