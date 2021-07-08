package com.marklogic.mlcp2;

import com.beust.jcommander.JCommander;
import com.marklogic.mlcp2.file.IngestFilesJobRunner;
import com.marklogic.mlcp2.jdbc.IngestRowsJobRunner;
import org.springframework.batch.core.JobExecutionException;

public class Main {

    public static void main(String[] args) {
        JCommander commander = JCommander
            .newBuilder()
            .addCommand("custom", new CustomJobRunner())
            .addCommand("ingestFiles", new IngestFilesJobRunner())
            .addCommand("ingestRows", new IngestRowsJobRunner())
            .build();
        commander.setProgramName("java -jar <name of jar>");

        commander.parse(args);
        String parsedCommand = commander.getParsedCommand();
        if (parsedCommand == null) {
            commander.usage();
        } else {
            try {
                ((JobRunner) commander.getCommands().get(parsedCommand).getObjects().get(0)).runJob();
            } catch (JobExecutionException e) {
                throw new RuntimeException("Job exception: " + e.getMessage(), e);
            }
        }
    }
}
