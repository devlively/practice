package XML;

import org.w3c.dom.Node;

/**
 * 람다식을 사용하기 위한  인터페이스
 * 
 * @author JuHan
 *
 */
@FunctionalInterface
public interface NodeLambda {

	public void run(Node node);
	
}
