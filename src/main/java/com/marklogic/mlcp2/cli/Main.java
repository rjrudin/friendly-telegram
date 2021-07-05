package com.marklogic.mlcp2.cli;

import com.beust.jcommander.JCommander;
import org.springframework.batch.core.JobExecutionException;

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
            "--username", "admin",
            "--password", "admin"
//            "ingestRows",
//            "--jdbc_url", "jdbc:h2:file:./data/h2/sample"
            , "ingestFiles"
            , "--input_file_path", "data/csv/**/*.csv"
            //, "--input_file_path", "data/csv/customers1.csv"
        };

        JCommander commander = JCommander
            .newBuilder()
            .addObject(CommonOptions.getInstance())
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
