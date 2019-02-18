package XML;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLAnalysis {

	DocumentBuilder builder = null;
	Transformer transformer = null;
	
	public void init()
	{
		try
		{
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			builder = documentFactory.newDocumentBuilder();
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();			
		} catch(Exception e)
		{
			
		}
	}
	
	public void analysis()
	{
		String defaultPath = "D:/assignment/1.XML 파일 분석";
		String resultPath = "D:/assignment/1.XML 파일 분석/result";
		
		XMLNodeMethod nodeMethod = new XMLNodeMethod();
		
		init();
		
		XMLDocument baseXml = new XMLDocument(defaultPath + "/T_BASEFILE_TB.xml", this.builder);
		
		Node baseRows = nodeMethod.getNodeByTagName(baseXml.getDocumentElement(), "ROWS");
		List<Node> baseRowList = nodeMethod.getNodesByTagName(baseRows, "ROW");
		
		for(Node baseRow : baseRowList)
		{
			Node fileIdNode = nodeMethod.getNodeByTagName(baseRow, "FILE_ID");
			String fileId = null;
			
			if(fileIdNode != null)
				fileId = fileIdNode.getTextContent();
			
			if(fileId != null && !("".compareTo(fileId) == 0))
			{
				int changeCommentCount = 0;
				
				XMLDocument fXml = new XMLDocument(defaultPath + "/F_" + fileId + "_TB.xml", this.builder);
				XMLDocument pXml = new XMLDocument(defaultPath + "/P_" + fileId + "_TB.xml", this.builder);

				Node pRows = nodeMethod.getNodeByTagName(pXml.getDocumentElement(), "ROWS");
				Map<String, String> pRowsMap = pRowsConvertToMap(pRows);
				//Map<String, Map<String, String>> pRowsMap = nodeMethod.nodeListConvertToMap(pRows,"P_ID");
				
				if(pRowsMap.size() <= 0)
					continue;
				
				Node fRows = nodeMethod.getNodeByTagName(fXml.getDocumentElement(), "ROWS");
				List<Node> fRowList = nodeMethod.getNodesByTagName(fRows, "ROW");
				
				
				for(Node fRow : fRowList)
				{
					Map<String, String> fRowMap = nodeMethod.getElementsContentToMap(fRow);
					
					String LicenseId = getLicenseId(fRowMap, pRowsMap);
					
					if(LicenseId != null)
					{
						Node comment = nodeMethod.getNodesByTagName(fRow, "COMMENT").get(0);
						
						if(comment != null)
						{
							comment.setTextContent(LicenseId);
							changeCommentCount++;
						}
					}
					
				} // end for(Node fRow : fRowList)
				
				System.out.println("T_" + fileId + "_TB.xml 변경 갯수 : " + changeCommentCount);
				
				if(changeCommentCount > 0)
					fXml.exportXML(resultPath + "/T_" + fileId + "_TB.xml", this.transformer);
				
			} // end if(!("".compareTo(fileId) == 0))
		} // end for(Element baseRow : baseRowList)

	}
	
	private Map<String, String> pRowsConvertToMap(Node pRows)
	{
		Map<String, String> map = new HashMap<String, String>();
		
		NodeList rowList = pRows.getChildNodes();
		
		for(int i = 0; i < rowList.getLength(); i++)
		{
			Node row = rowList.item(i);
			
			if(row != null)
			{
				NodeList nodeList = row.getChildNodes();
				String key = "", value = "";
				int find = 0;
				
				for(int j = 0; j < nodeList.getLength(); j++)
				{
					Node node = nodeList.item(j);
					
					if(node != null)
					{
						String nodeName = node.getNodeName();
						if(find != 1 && "P_ID".compareTo(nodeName) == 0)
						{
							if(!("".compareTo(node.getTextContent()) == 0))
							{
								key = node.getTextContent();
								find += 1;
							}
							else
								break;
						}
						else if(find != 2 && "LICENSE_ID".compareTo(nodeName) == 0)
						{
							if(!("".compareTo(node.getTextContent()) == 0))
							{
								value = node.getTextContent();
								find += 2;
							}
							else
								break;
						}
						
						if(find == 3)
						{
							map.put(key, value);
							break;
						}
							
					}
				} // end loop nodeList
			}
		} // end loop rowList
		
		return map;
	}
	
	private String getLicenseId(Map<String, String> targerRowMap, Map<String, String> compareRowsMap)
	{
		if(targerRowMap.containsKey("SIMILAR_RATE"))
		{
			int similarRate = Integer.parseInt(targerRowMap.get("SIMILAR_RATE"));
			
			if(similarRate / 100 > 15)
			{
				if(targerRowMap.containsKey("P_ID") && compareRowsMap.containsKey(targerRowMap.get("P_ID")))
					return compareRowsMap.get(targerRowMap.get("P_ID"));
				
			} // end if(similarRate / 100 > 50)						
		} // end if(fRowMap.containsKey("SIMILAR_RATE"))
		
		return null;
	}
	
}
