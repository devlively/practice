package XML;

import java.util.Date;

public class Main {

	public static void main(String[] args) throws Exception {

		XMLAnalysis analysis = new XMLAnalysis();
		
		Date start = new Date(), end = null;
		long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), endMemory = 0;
		System.out.println("분석 시작");
		
		analysis.analysis();
		
		end = new Date();
		endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		
		System.out.print("\n소요시간 : " + String.format("%,d", end.getTime() - start.getTime()) + " ms");
		System.out.println(", 메모리 사용량 : " + String.format("%,d", (endMemory - startMemory) / 1000) + " kbyte");
		System.out.println("분석 종료");
	}

}
