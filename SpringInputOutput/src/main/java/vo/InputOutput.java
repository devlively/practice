package vo;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;

public class InputOutput
{
	String content = "";
	@Autowired
	private Input input;
	@Autowired
	private Output output;
	@Autowired
	private Count count;
	
	@PostConstruct
	public void init()
	{
		content = input.in();
	}
	
	@PreDestroy
	public void destroy()
	{
		output.out(String.valueOf(count.count(content)));
	}
	
	public void setInput(Input input) {
		this.input = input;
	}
	
	public void setOutput(Output output) {
		this.output = output;
	}

}
