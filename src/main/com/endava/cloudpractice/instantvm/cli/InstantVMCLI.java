package com.endava.cloudpractice.instantvm.cli;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.endava.cloudpractice.instantvm.orchestrator.Orchestrator;
import com.google.common.base.Preconditions;


public class InstantVMCLI {

	private final Orchestrator orchestrator;


	private InstantVMCLI(Orchestrator orchestrator) {
		Preconditions.checkArgument(orchestrator != null);
		this.orchestrator = orchestrator;
	}


	private void run(String[] args) {
	}


	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"/com/endava/cloudpractice/instantvm/cli/InstantVMCLI-context.xml");
		Orchestrator orchestrator = (Orchestrator) applicationContext.getBean("orchestrator");
		new InstantVMCLI(orchestrator).run(args);
	}

}
