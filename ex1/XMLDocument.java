package XML;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * XML파일을 Document로 파싱해서 가지고있는 클래스
 * 
 * @author JuHan
 *
 */
public class XMLDocument {

	private Document document = null;
	private String filePath = "";
	
	/**
	 * @param filePath xml파일 경로
	 * @param builder DocumentBuilder / null 일경우 자체 생성해서 파싱
	 */
	XMLDocument(String filePath, DocumentBuilder builder)
	{
		this.filePath = filePath;
		init(builder);
	}
	
	/**
	 * xml파일의 경로를 설정하고 가져와서 Document로 변환
	 * 
	 * @param filePath xml파일 경로
	 * @param builder DocumentBuilder / null 일경우 자체 생성해서 파싱
	 */
	public void setFilePath(String filePath, DocumentBuilder builder) {
		this.filePath = filePath;
		init(builder);
	}
	
	/**
	 * @return 현재 파일의 경로
	 */
	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * @return 현재 가지고있는 Document
	 */
	public Document getDocument() {	
		return this.document;
	}

	/**
	 * 
	 * xml파일을 불러와서 Document로 파싱
	 * 
	 * @param builder DocumentBuilder / null 일경우 자체 생성해서 파싱
	 * @return 파일 불러오기 성공,실패 여부
	 */
	private boolean init(DocumentBuilder builder)
	{
		boolean result = false;
		
		FileInputStream fis = null;
		
		try
		{
			if(builder == null)
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				builder = factory.newDocumentBuilder();
			}
			fis = new FileInputStream(this.filePath);
			this.document = builder.parse(fis);
			
			result = true;
			
		} catch(Exception e)
		{
			result = false;
			System.out.println("파일불러오기 실패");
		} finally
		{
			try
			{
				if(fis != null) fis.close();
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/**
	 * @return Document의 최상위 노드
	 */
	public Element getDocumentElement()
	{
		if(document != null)
			return document.getDocumentElement();
		return null;
	}
	
	/**
	 * 현재 가지고있는 document를 받아온 파일 경로로 내보내기
	 * 
	 * @param filePath 내보낼 파일 경로
	 * @param transformer<br/>
	 * 		  Document를 xml형식으로 변환하기위한 Transformer<br/>
	 *        null일 경우 자체 생성
	 * @return 파일 내보내기 성공,실패 여부
	 */
	public boolean exportXML(String filePath, Transformer transformer)
	{
		boolean result = false;
		
		//System.out.println(filePath + " 에 내보는 중...");
		
		if(document != null)
		{
			DOMSource xmlDOM = new DOMSource(this.document);

			FileOutputStream fos = null;
			try
			{
				fos = new FileOutputStream(filePath);
				StreamResult xmlFile = new StreamResult(fos);
				
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
			} finally
			{
				try
				{
					if(fos != null) fos.close();
				} catch(IOException e)
				{
					
				}
			}
		}
		
		return result;

	}
	
}
