package com.qdom.xml.dom.analysis;

import com.qdom.xml.dom.Node;
import com.qdom.xml.dom.analysis.impl.IteratorNodeImpl;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-3-7
 * 迭代器工厂
 *
 */
public class IteratorFactory {

	public static Iterator<Node> getIterator(Node root){
		return new IteratorNodeImpl(root);
	}
	
}
