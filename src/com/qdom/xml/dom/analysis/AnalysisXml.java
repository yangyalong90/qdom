package com.qdom.xml.dom.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.qdom.xml.dom.Node;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-3-6
 * 解析xml接口
 *
 */
public interface AnalysisXml {
	
	public void read(String xmlName) throws FileNotFoundException;

	public void read(File file) throws FileNotFoundException;
	
	public void read(InputStream inputStream) throws IOException;
	
	public Node getRoot();
	
	public List<Node> findNodes(String nodeName);
	
}
