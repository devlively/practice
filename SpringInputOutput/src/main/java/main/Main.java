package main;

import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import vo.InputOutput;

public class Main {
	
	public static void main(String[] args)
	{
		GenericApplicationContext gac = new GenericXmlApplicationContext("xmlpack/generic.xml");
		gac.registerShutdownHook();
	}
	
}
