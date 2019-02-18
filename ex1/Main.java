package XML;

import java.util.Date;

public class Main {

	public static void main(String[] args) throws Exception {

		XMLAnalysis analysis = new XMLAnalysis();
		
		Date start = new Date(), end = null;
		System.out.println("분석 시작");
		
		analysis.analysis();		
		
		end = new Date();
		
		System.out.println("소요시간 : " + ((double)(end.getTime() - start.getTime()) / 1000) + "초");
		
		System.out.println("분석 종료");
	}

}
