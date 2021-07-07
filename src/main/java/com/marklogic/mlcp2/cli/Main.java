package com.marklogic.mlcp2.cli;

import com.beust.jcommander.JCommander;
import org.springframework.batch.core.JobExecutionException;

public class Main {

    public static void main(String[] args) {
        JCommander commander = JCommander
            .newBuilder()
            .addCommand("custom", new CustomCommand())
            .addCommand("ingestFiles", new IngestFilesCommand())
            .addCommand("ingestRows", new IngestRowsCommand())
            .build();
        commander.setProgramName("java -jar <name of jar>");

        commander.parse(args);
        String parsedCommand = commander.getParsedCommand();
        if (parsedCommand == null) {
            commander.usage();
        } else {
            try {
                ((JobCommand) commander.getCommands().get(parsedCommand).getObjects().get(0)).runJob();
            } catch (JobExecutionException e) {
                throw new RuntimeException("Job exception: " + e.getMessage(), e);
            }
        }
    }
}
