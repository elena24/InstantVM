package com.endava.cloudpractice.instantvm.cli;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.endava.cloudpractice.instantvm.datamodel.BuilderType;
import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.datamodel.VMStatus;
import com.endava.cloudpractice.instantvm.orchestrator.Orchestrator;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;


public class InstantVMCLI {

	private static final String OPT_HELP = "help";
	private static final String OPT_LAUNCHVM = "launchVM";
	private static final String OPT_TERMINATEVM = "terminateVM";
	private static final String OPT_LISTVMS = "listVMs";
	private static final String OPT_ADDVMDEF = "addVMDef";
	private static final String OPT_REMOVEVMDEF = "removeVMDef";
	private static final String OPT_LISTVMDEFS = "listVMDefs";


	private final Orchestrator orchestrator;


	private InstantVMCLI(Orchestrator orchestrator) {
		Preconditions.checkArgument(orchestrator != null);
		this.orchestrator = orchestrator;
	}


	private void run(String[] args) {
		CommandLine line = parse(args);
		if(line == null) {
			return;
		}
		if(line.hasOption(OPT_LAUNCHVM)) {
			launchVM(line);
		}
		if(line.hasOption(OPT_TERMINATEVM)) {
			terminateVM(line);
		}
		if(line.hasOption(OPT_LISTVMS)) {
			listVMs(line);
		}
		if(line.hasOption(OPT_ADDVMDEF)) {
			addVMDef(line);
		}
		if(line.hasOption(OPT_REMOVEVMDEF)) {
			removeVMDef(line);
		}
		if(line.hasOption(OPT_LISTVMDEFS)) {
			listVMDefs(line);
		}
	}


	private void launchVM(CommandLine line) {
		orchestrator.launchVM(line.getOptionValue(OPT_LAUNCHVM));
	}


	private void terminateVM(CommandLine line) {
		orchestrator.terminateVM(line.getOptionValue(OPT_TERMINATEVM));
	}


	private void listVMs(CommandLine line) {
		for(VMStatus vmStatus : orchestrator.listVMs()) {
			System.out.println(vmStatus);
		}
	}


	private void addVMDef(CommandLine line) {
		Map<String, String> params = getCmdParams(line, OPT_ADDVMDEF);
		VMDefinition vmDefinition = new VMDefinition();
		vmDefinition.setName(params.get("name"));
		vmDefinition.setDescription(params.get("description"));
		vmDefinition.setBuilder(BuilderType.fromString(params.get("builder")));
		vmDefinition.setRecipe(params.get("recipe"));
		orchestrator.addVMDefinition(vmDefinition);
	}


	private void removeVMDef(CommandLine line) {
		orchestrator.removeVMDefinition(line.getOptionValue(OPT_REMOVEVMDEF));
	}


	private void listVMDefs(CommandLine line) {
		for(VMDefinition vmDefinition : orchestrator.listVMDefinitions()) {
			System.out.println(vmDefinition);
		}
	}


	private CommandLine parse(String[] args) {
		CommandLineParser parser = new BasicParser();

		Options options = new Options();
		OptionGroup commands = new OptionGroup();

		OptionBuilder.withDescription("Print command usage.");
		OptionBuilder.hasArg(false);
		Option help = OptionBuilder.create(OPT_HELP);

		OptionBuilder.withDescription("Launch a new VM based on the specified VM definition.");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("name");
		Option launchVM = OptionBuilder.create(OPT_LAUNCHVM);

		OptionBuilder.withDescription("Terminate a VM.");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("id");
		Option terminateVM = OptionBuilder.create(OPT_TERMINATEVM);

		OptionBuilder.withDescription("Lists the currently running VMs.");
		OptionBuilder.hasArg(false);
		Option listVMs = OptionBuilder.create(OPT_LISTVMS);

		OptionBuilder.withDescription("Add a new VM definition.");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("key=value,...");
		OptionBuilder.withValueSeparator();
		Option addVMDef = OptionBuilder.create(OPT_ADDVMDEF);

		OptionBuilder.withDescription("Remove a VM definition.");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("name");
		Option removeVMDef = OptionBuilder.create(OPT_REMOVEVMDEF);

		OptionBuilder.withDescription("Lists the currently available VM definitions.");
		OptionBuilder.hasArg(false);
		Option listDefs = OptionBuilder.create(OPT_LISTVMDEFS);

		commands.addOption(help);
		commands.addOption(launchVM);
		commands.addOption(terminateVM);
		commands.addOption(listVMs);
		commands.addOption(addVMDef);
		commands.addOption(removeVMDef);
		commands.addOption(listDefs);
		options.addOptionGroup(commands);

		CommandLine line = null;
		try {
			line = parser.parse(options, args);
			if(line.getOptions().length == 0 || line.hasOption(OPT_HELP)) {
				printHelp(options);
			}
		} catch(ParseException e) {
			System.err.println(e.getMessage());
			printHelp(options);
		}
		return line;
	}


	Map<String, String> getCmdParams(CommandLine line, String command) {
		Builder<String, String> builder = ImmutableMap.builder();
		for(List<String> entry : Lists.partition(ImmutableList.copyOf(line.getOptionValues(OPT_ADDVMDEF)), 2)) {
			Iterator<String> iterator = entry.iterator();
			builder.put(iterator.next(), iterator.next());
		}
		return builder.build();
	}


	private void printHelp(Options options) {
		new HelpFormatter().printHelp("InstantVMCLI", options);
	}

	
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"/com/endava/cloudpractice/instantvm/cli/InstantVMCLI-context.xml");
		Orchestrator orchestrator = (Orchestrator) applicationContext.getBean("orchestrator");
		new InstantVMCLI(orchestrator).run(args);
	}

}
