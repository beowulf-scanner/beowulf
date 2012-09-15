package com.nvarghese.beowulf.sfe;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ProgramOptions {

	private Options options;
	private Option configFile;
	private Option help;

	public static final char HELP_OPTION = 'h';
	public static final char CONFIG_FILE_OPTION = 'c';

	public ProgramOptions() {

		options = new Options();

		configFile = new Option("c", "config-file", true, "Path to the ratify server settings file");
		options.addOption(configFile);

		help = new Option("h", "help", false, "This page");
		options.addOption(help);
	}

	public CommandLine parseArguments(String[] args) throws ParseException {

		CommandLineParser parser = new GnuParser();
		return parser.parse(options, args);
	}

	public void printHelp() {

		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(" ", options);
	}

}
