package com.company.dema.base64;

import org.apache.commons.cli.*;

import java.io.*;

public class ApplicationBase64 {
    private static String process = "";
    private static HelpFormatter formatter;
    private static String inputFileName;
    private static String outputFileName;

    private static Options commandLineOptions() {
        Options posixOptions = new Options();
        Option code = new Option("c", "code", false, "Code");
        Option decode = new Option("d", "decode", false, "Decode");
        Option help = new Option("h", "help", false, "Help");
        Option inputFile = new Option("i", "input file", true, "inputFile");
        inputFile.setArgs(1);
        Option outputFile = new Option("o", "output file", true, "outputFile");
        outputFile.setArgs(1);
        posixOptions.addOption(code);
        posixOptions.addOption(decode);
        posixOptions.addOption(help);
        posixOptions.addOption(inputFile);
        posixOptions.addOption(outputFile);
        formatter = new HelpFormatter();
        return posixOptions;
    }

    private static void parse(String[] args, Options options) throws ParseException {
        CommandLine commandLine = new PosixParser().parse(options, args);
        if (commandLine.getArgs().length > 2)
            throw new ParseException("wrong parameters\n for help use -h");

        if (commandLine.hasOption("code")) {
            process = "code";
        } else if (commandLine.hasOption("decode")) {
            process = "decode";
        } else if (commandLine.hasOption("help")) {
            process = "help";
            if (commandLine.getArgs().length > 0)
                throw new ParseException("wrong parameters\n for help use -h");
        } else
            throw new ParseException("wrong parameters\n for help use -h");

        if (commandLine.hasOption("i")) {
            inputFileName = commandLine.getOptionValue("i");
        }
        if (commandLine.hasOption("o")) {
            outputFileName = commandLine.getOptionValue("o");
        }
    }

    /*
        public void helpShow() {
            formatter.printHelp("help", posixOptions);
            System.out.println(HELP_TEXT);
        }
        */

    static private InputStream chooseInputStream(String fileName) throws FileNotFoundException {
        return (fileName == null ? System.in : new FileInputStream(fileName));
    }

    static private OutputStream chooseOutputStream(String fileName) throws FileNotFoundException {
        return (fileName == null ? System.out : new FileOutputStream(fileName));
    }

    static public void recordFromInToOut(InputStream in, OutputStream out) throws IOException {
        int i;
        for (i = in.read(); i != -1; i = in.read())
            out.write(i);
        in.close();
        out.close();
    }


    public static void main(String[] args) {
        try {
            Options options = commandLineOptions();
            parse(args, options);
            switch (process) {
                case "code":
                    recordFromInToOut(chooseInputStream(inputFileName), new Base64OutputStream(chooseOutputStream(outputFileName)));
                    break;
                case "decode":
                    recordFromInToOut(new Base64InputStream(chooseInputStream(inputFileName)), chooseOutputStream(outputFileName));
                    break;
                case "help":
                    formatter.printHelp("Base64", options, true);
                    break;
            }

        } catch (Exception exp) {
            System.err.println("\n" + "Error: " + exp.getMessage());
        }
        /*byte u = -80,i,p,o;
        o = (byte)(u >> 2);
        //i = (byte)((u >> 2) | ((u >> 2) ^ -128) );
        i = (byte) ((u >> 2) -96);
        p =  (byte)(o ^ (-128 >> 1));
        System.out.println((byte)(1 << 7));*/
    }
}
