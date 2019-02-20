package XML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Document Node에서 정보를 가져오기한 함수 모음
 * 
 * @author JuHan
 */
public class XMLNodeMethod {
	
	/**
	 * 하위노드들 중에서 TagName과 이름이 동일한 노드를 1개 반환<br/>
	 * 여러개 일 경우 처음 찾은 노드 1개만 반환한다
	 * 
	 * @param parent 부모 노드
	 * @param tagName 찾을 태그 이름
	 * @return 찾은 노드
	 */
	public Node getNodeByTagName(Node parent, String tagName)
	{
		if(parent != null && parent.hasChildNodes())
		{
			NodeList nodeList = parent.getChildNodes();
			
			int len = nodeList.getLength();
			
			for(int i = 0; i < len; i++)
			{
				Node node = nodeList.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE && tagName.toUpperCase().compareTo(node.getNodeName().toUpperCase()) == 0)
					return node;
			}
		}
		
		return null;
	}
	
	/**
	 * 하위노드들 중에서 TagName과 이름이 동일한 모든 노드를 반환
	 * 
	 * @param parent 부모 노드
	 * @param tagName 찾을 태그 이름
	 * @return 찾은 노드들의 리스트
	 */
	public List<Node> getNodesByTagName(Node parent, String tagName)
	{
		List<Node> list = new ArrayList<Node>();
		
		getChildNodes(parent, (node)->{ 
			
			if(node.getNodeType() == Node.ELEMENT_NODE && tagName.toUpperCase().compareTo(node.getNodeName().toUpperCase()) == 0)
			{
				list.add(node);
			}
			
		});
		
		return list;
	}
	
	/**
	 * 하위노드들을 Map으로 변환한다<br/>
	 * Key : TagName<br/>
	 * Value : TextConent<br/>
	 * 
	 * @param parent 부모 노드
	 * @return 변환된 Map
	 */
	public Map<String, String> getElementsContentToMap(Node parent)
	{
		Map<String, String> map = new HashMap<String, String>();
		
		getChildNodes(parent, (node)->{ 
			
			if(node.getNodeType() == Node.ELEMENT_NODE)
			{
				map.put(node.getNodeName(), node.getTextContent());
			}
			
		});
		
		return map;
	}
	
	/**
	 * 노드 리스트에 있는 노드들의 하위노드들을 Map으로 변환
	 * 
	 * @param nodeList 노드 리스트
	 * @param keyTagName Map의 Key로 설정할 TagName
	 * @return 변환된 Map
	 */
	public Map<String, Map<String, String>> nodeListConvertToMap(List<Node> nodeList, String keyTagName)
	{
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		
		if(nodeList != null)
		{
			for(Node n : nodeList)
			{
				Map<String, String> elementsMap = new HashMap<String, String>();
				
				if(n.hasChildNodes())
				{
					getChildNodes(n, (node)->{
						
						if(node.getNodeType() == Node.ELEMENT_NODE)							
							elementsMap.put(node.getNodeName(), node.getTextContent());

					});					
				}
				
				map.put(elementsMap.get(keyTagName), elementsMap);
				
			} // end for(Node n : nodeList)
		} // end if(nodeList != null) 
		
		return map;
	}
	
	/**
	 * 하위노드들의 하위노드들을 Map으로 변환
	 * 
	 * @param parent 부모 노드
	 * @param keyTagName Map의 Key로 설정할 TagName
	 * @return 변환된 Map
	 */
	public Map<String, Map<String, String>> nodeListConvertToMap(Node parent, String keyTagName)
	{
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		
		getChildNodes(parent, (child)->{
			
			Map<String, String> elementsMap = new HashMap<String, String>();
			
			if(child.getNodeType() == Node.ELEMENT_NODE)
			{
				getChildNodes(child, (node)->{
					
					if(node.getNodeType() == Node.ELEMENT_NODE)							
						elementsMap.put(node.getNodeName(), node.getTextContent());

				});
			}
				
			map.put(elementsMap.get(keyTagName), elementsMap);

		});
		
		return map;
	}
	
	/**
	 * 하위노드들을 가져온다
	 * 
	 * @param parent 부모노드
	 * @param lambda 가져온 하위노드로 처리 할 함수
	 */
	private void getChildNodes(Node parent, NodeLambda lambda)
	{
		if(parent != null && parent.hasChildNodes())
		{
			NodeList nodeList = parent.getChildNodes();
			
			int len = nodeList.getLength();
			
			for(int i = 0; i < len; i++)
			{
				Node node = nodeList.item(i);
				lambda.run(node);
			}
		}
	}
}
