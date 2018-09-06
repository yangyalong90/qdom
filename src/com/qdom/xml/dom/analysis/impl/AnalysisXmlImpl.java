package com.qdom.xml.dom.analysis.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.qdom.xml.dom.Attribute;
import com.qdom.xml.dom.Node;
import com.qdom.xml.dom.analysis.AnalysisXml;

/**
 * 
 * @author 	杨亚龙
 * @date	2018-3-6
 * 解析xml文件
 *
 */

public class AnalysisXmlImpl implements AnalysisXml{
	
	private Node root;
	private String xmlData;

	@Override
	public void read(String xmlName) throws FileNotFoundException {
		File file=new File(xmlName);
		read(file);
	}

	@Override
	public void read(File file) throws FileNotFoundException {
		InputStream inputStream=new FileInputStream(file);
		try {
			read(inputStream);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void read(InputStream inputStream) throws IOException {
		if(inputStream==null){
			return;
		}
		byte by[]=new byte[1024];
		StringBuffer xmlBuf=new StringBuffer();
		while(inputStream.read(by)!=-1){
			xmlBuf.append(new String(by));
		}
		xmlData=xmlBuf.toString();
		analysis();
	}

	@Override
	public Node getRoot() {
		return root;
	}

	@Override
	public List<Node> findNodes(String nodeName) {
		List<Node> nodes=new ArrayList<Node>();
		findNodes(nodeName,root,nodes);
		return nodes;
	}
	
	public void findNodes(String nodeName,Node root,List<Node> nodes){
		for(Node node:root.getChilds()){
			if(nodeName.equals(node.getName())){
				nodes.add(node);
			}else{
				findNodes(nodeName, node, nodes);
			}
		}
	}
	
	private void analysis(){
		Node parent=null;
		for(int i=0;i<xmlData.length();i++){
			char xml_ch=xmlData.charAt(i);
			if(xml_ch=='<' && xmlData.charAt(i+1)!='?' && xmlData.charAt(i+1)!='/'){
				Node node_=null;
				if(root==null){
					root=new Node();
					node_=root;
				}else{
					Node node=new Node();
					parent.pushChild(node);
					node.setParent(parent);
					node_=node;
				}
				i=readNode(node_, i);
				parent=node_;
			}else if((xml_ch=='<' && xmlData.charAt(i+1)=='/') 
					|| (xml_ch=='/' && xmlData.charAt(i+1)=='>')){
				parent=parent.getParent();
			}
		}
	}
	
	private int readNode(Node node,int startIndex){
		if(startIndex==xmlData.length() || xmlData.charAt(startIndex)=='>'){
			return startIndex;
		}
		int nameLen=0;
		while(xmlData.charAt(startIndex+1)!=' ' && xmlData.charAt(startIndex+1)!='>'){
			nameLen++;
			startIndex++;
		}
		String nodeName=xmlData.substring(startIndex+1-nameLen, startIndex+1);
		node.setName(nodeName);
		int index=startIndex+1;
		char sign=xmlData.charAt(index);
		while(sign!='>'){
			index=readNodeAttr(node,index);
			if(index<xmlData.length()){
				sign=xmlData.charAt(index);
			}else{
				break;
			}
		}
		if(sign=='>' && xmlData.charAt(index+1) != '<'){
			readNodeText(node, index+1);
		}
		return index;
	}
	

	private int readNodeText(Node node,int textStartIndex){
		char sign=xmlData.charAt(textStartIndex);
		int textLen=0;
		while(sign != '<'){
			textLen++;
			sign=xmlData.charAt(textStartIndex+textLen);
		}
		String text=xmlData.substring(textStartIndex, textStartIndex+textLen);
		
		if(!text.isEmpty() && !isEmpty(text)){
			text.trim();
			node.setText(text);
		}
		return textStartIndex+textLen;
	}
	
	private int readNodeAttr(Node node,int attrStartIndex){
		int endIndex=attrStartIndex;
		int eqIndex=0;
		char sign=xmlData.charAt(attrStartIndex);
		while(sign!='>' && attrStartIndex+1<xmlData.length()){
			if(sign=='='){
				eqIndex=attrStartIndex;
				break;
			}
			attrStartIndex++;
			sign=xmlData.charAt(attrStartIndex);
		}
		if(eqIndex==0){
			return endIndex+1;
		}
		int attrStart=eqIndex;
		sign=xmlData.charAt(attrStart);
		while(sign!=' ' && sign!='<' && sign!='>'){
			attrStart--;
			sign=xmlData.charAt(attrStart);
		}
		String attrName=xmlData.substring(attrStart+1, attrStartIndex);
		attrName=attrName.trim();
		if(!attrName.isEmpty() && node.getAttr(attrName)==null){
			Attribute attribute=new Attribute();
			node.pushAttr(attribute);
			attribute.setName(attrName);
			int valueLen=0;
			sign=xmlData.charAt(eqIndex+2);
			while(sign!='"' && eqIndex+3<xmlData.length()){
				valueLen++;
				sign=xmlData.charAt(++eqIndex+2);
			}
			String attrValue=xmlData.substring(eqIndex+2-valueLen,eqIndex+2);
			attribute.setValue(attrValue);
			endIndex=eqIndex+2;
		}
		return endIndex;
	}
	
	private boolean isEmpty(String str){
		boolean flag=true;
		for(char ch:str.toCharArray()){
			if(ch != '\n' && ch != '\t' && ch != '\r' && ch != ' '){
				flag=false;
				break;
			}
		}
		return flag;
	}

}
