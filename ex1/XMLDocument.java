package XML;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLDocument {

	private Document document = null;
	private String filePath = "";
	
	XMLDocument(String filePath, DocumentBuilder builder)
	{
		this.filePath = filePath;
		init(builder);
	}
	
	public void setFilePath(String filePath, DocumentBuilder builder) {
		this.filePath = filePath;
		init(builder);
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public Document getDocument() {	
		return this.document;
	}

	private boolean init(DocumentBuilder builder)
	{
		boolean result = false;
		
		try
		{
			if(builder == null)
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				builder = factory.newDocumentBuilder();
			}
			this.document = builder.parse(new FileInputStream(this.filePath));
			
			result = true;
			
		} catch(Exception e)
		{
			result = false;
			System.out.println("파일불러오기 실패");
		}
		
		return result;
	}
	
	public Element getDocumentElement()
	{
		if(document != null)
			return document.getDocumentElement();
		return null;
	}
	
	public boolean exportXML(String filePath, Transformer transformer)
	{
		boolean result = false;
		
		//System.out.println(filePath + " 에 내보는 중...");
		
		DOMSource xmlDOM = new DOMSource(this.document);

		try
		{
			StreamResult xmlFile = new StreamResult(new FileOutputStream(filePath));
			
			if(transformer == null)
			{
				TransformerFactory factory = TransformerFactory.newInstance();
				transformer = factory.newTransformer();				
			}
			
			transformer.transform(xmlDOM, xmlFile);
			
			result = true;
			
			//System.out.println(filePath + " 에 내보내기 성공");
			
		} catch(Exception e)
		{
			result = false;
			System.out.println("파일내보내기 실패");
		}
		
		return result;

	}
	
}
