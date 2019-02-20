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

/**
 * Base.xml파일을 불러와서 Row들의 FILE_ID를 가져오고 F_{FILE_ID}_TB.xml의 조건을 분석해서 <br/>
 * P_{FILE_ID}_TB.xml 에서 동일한 P_ID를 가지고 있는 Row의 LICENSE_ID를 가져와 COMMENT의 TextContent로 설정
 * 
 * @author JuHan
 */
public class XMLAnalysis {

	DocumentBuilder builder = null;
	Transformer transformer = null;
	
	/**
	 * Xml파일을 Document로 파싱하기위해 필요한 Documentbuilder와 생성<br/>
	 * Document를 Xml파일로 변환하기위한 Transformer 생성
	 * 
	 */
	private void init()
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
	
	/**
	 * T_BASEFILE_TB.xml파일의 Row들이 가지고있는 File_ID를 가져와서 <br/>
	 * F_{FILE_ID}_TB.xml 과 P_{FILE_ID}_TB.xml을 가져온 다음 분석
	 * 
	 */
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
				Map<String, String> pRowsMap = ConvertToMap(pRows);
				
				if(pRowsMap.size() <= 0)
					continue;
				
				Node fRows = nodeMethod.getNodeByTagName(fXml.getDocumentElement(), "ROWS");
				List<Node> fRowList = nodeMethod.getNodesByTagName(fRows, "ROW");
				
				changeCommentCount = setCommentContent(fRowList, pRowsMap);
				
				System.out.println("T_" + fileId + "_TB.xml 변경 갯수 : " + changeCommentCount);
				
				if(changeCommentCount > 0)
					fXml.exportXML(resultPath + "/T_" + fileId + "_TB.xml", this.transformer);
				
			} // end if(!("".compareTo(fileId) == 0))
		} // end for(Element baseRow : baseRowList)

	}
	
	/**
	 * P_TB.xml의 Row들을 Map으로 변환<br/>
	 * Key : P_ID<br/>
	 * Value : LICENSE_ID
	 * 
	 * @param pRows Row들의 상위 노드
	 * @return Row들이 저장되어있는 Map
	 */
	private Map<String, String> ConvertToMap(Node pRows)
	{
		Map<String, String> map = new HashMap<String, String>();
		
		NodeList rowList = pRows.getChildNodes();
		
		for(int i = 0; i < rowList.getLength(); i++)
		{
			Node row = rowList.item(i);
			
			if(row != null)
			{
				findPIdAndLicenseId(row, map);
			}
		} // end loop rowList
		
		return map;
	}
	
	/**
	 * Row의 하위노드 중에서 P_ID와 LICENSE_ID를 찾아서 Map에 저장
	 * 
	 * @param row P_ID와 LICENSE_ID를 찾을 Row 노드
	 * @param map 찾은 값을 저장할 Map
	 */
	private void findPIdAndLicenseId(Node row, Map<String, String> map)
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
				if(find != 1 && "P_ID".compareTo(nodeName) == 0
				     && !("".compareTo(node.getTextContent()) == 0))
				{
					key = node.getTextContent();
					find += 1;
				}
				else if(find != 2 && "LICENSE_ID".compareTo(nodeName) == 0
				          && !("".compareTo(node.getTextContent()) == 0))
				{
					value = node.getTextContent();
					find += 2;
				}
				
				if(find == 3)
				{
					map.put(key, value);
					break;
				}
			}
		}
	}
	
	
	
	/**
	 * RowList 에 있는 Row들의 Comment를 변경
	 * 
	 * @param rowList Row 노드 리스트
	 * @param compareRowsMap Row와 비교할 P_TB의 Map 
	 * @return 변경된 Comment의 갯수
	 */
	private int setCommentContent(List<Node> rowList, Map<String, String> compareRowsMap)
	{
		int changeCommentCount = 0;
	
		XMLNodeMethod nodeMethod = new XMLNodeMethod();
		
		for(Node fRow : rowList)
		{
			Map<String, String> fRowMap = nodeMethod.getElementsContentToMap(fRow);
			
			String LicenseId = getLicenseId(fRowMap, compareRowsMap);
			
			if(LicenseId != null)
			{
				Node comment = nodeMethod.getNodesByTagName(fRow, "COMMENT").get(0);
				
				if(comment != null)
				{
					comment.setTextContent(LicenseId);
					changeCommentCount++;
				}
			}
		}
		
		return changeCommentCount;
	}
	
	/**
	 * Row의 SIMILAR_RATE / 100 이 15보다 클 경우 Row가 가지고있는 PID를<br/>
	 * P_TB의 P_ID와 비교하여 동일한 값이 있으면 LICENSE_ID를 반환 
	 * 
	 * @param targerRowMap Row를 변환시킨 Map
	 * @param compareRowsMap 비교할 P_ID와 가져올 LICENSE_ID를 가진 Map
	 * @return LICENSE_ID
	 */
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
