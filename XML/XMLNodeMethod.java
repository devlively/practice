package XML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLNodeMethod {
	
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
