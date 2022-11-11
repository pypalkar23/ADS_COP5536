import java.io.FileWriter;

class ResultWriter {
    //Output file writer which writes to a file named output_file.txt;
    private static final String OUTPUT_FILE_NAME = "output_file.txt";
    private FileWriter writer;

    public ResultWriter() {
        try {
            initializeFile();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void initializeFile() throws Exception {
        writer = new FileWriter(OUTPUT_FILE_NAME);
    }

    public void writeToFile(String txt) throws Exception {
        this.writer.write(String.format("%s\n",txt));
    }

    public void close() throws Exception {
        writer.close();
    }
}