package com.marklogic.mlcp2;

import com.beust.jcommander.JCommander;

public class Main {

    /**
     * I think every JCommander Command = a Spring Batch Job.
     * <p>
     * We then have a bunch of common options for e.g. authentication, and then each Job can have its own
     * Job-specific arguments.
     *
     * @param args
     */
    public static void main(String[] args) {
        args = new String[]{
                "runMyConfig"
        };
        
        JCommander commander = JCommander
                .newBuilder()
                .addCommand("runMyConfig", new RunMyConfigCommand())
                .build();
        commander.setProgramName("java -jar <name of jar>");

        commander.parse(args);
        String parsedCommand = commander.getParsedCommand();
        if (parsedCommand == null) {
            commander.usage();
        } else {
            ((Runnable) commander.getCommands().get(parsedCommand).getObjects().get(0)).run();
        }
    }
}
