package com.qdom.xml.dom.analysis;

import com.qdom.xml.dom.analysis.impl.AnalysisXmlImpl;

/**
 * 
 * @author 	������
 * @date	2018-3-6
 * xml��������
 *
 */
public class AnalysisFactory {

	public static AnalysisXml getAnalysisXml(){
		return new AnalysisXmlImpl();
	}
	
}
