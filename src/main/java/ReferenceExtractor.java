import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ReferenceExtractor {

    private static List<File> getPDFFiles(File file) {

        List<File> files = new ArrayList<File>();

        if(file.isFile() && file.getName().toLowerCase().endsWith(".pdf")) {
            files.add(file);
        } else if (file.isDirectory()) {
            File[] currentFolderFiles = file.listFiles();
            if(currentFolderFiles != null && currentFolderFiles.length > 0) {
                for (File folderFile : currentFolderFiles) {
                    files.addAll(getPDFFiles(folderFile));
                }
            }
        }

        return files;
    }

    public static void main(String[] args) {

        try {

            GUI gui = new GUI();

            gui.logln("Select .pdf file (or folder containing .pdf files) for reference extraction.");
            File inputPath = gui.choosePDF();
            if(inputPath == null) {
                gui.dispose();
                return;
            }
            gui.logln(String.format("Selected => %s\n", inputPath.getPath()));

            gui.logln("Select .txt file for output.");
            File outputPath = gui.chooseTxt();
            if(outputPath == null) {
                gui.dispose();
                return;
            }
            gui.logln(String.format("Selected => %s\n", outputPath.getPath()));

            if (!outputPath.exists()) {
                outputPath.createNewFile();
            }

            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputPath));
            outputWriter.write("TITLE\tYEAR PUBLISHED\tAUTHORS\n");

            List<File> files = getPDFFiles(inputPath);

            for(File file : files) {

                gui.logln(String.format("\nExtracting References From => %s\n", file.getPath()));

                Article article = new Article(file.getPath());

                gui.logln(article.fancyPrint());
                outputWriter.write(article.toString());
                for(Article reference : article.getReferences()) {
                    gui.logln(reference.fancyPrint());
                    outputWriter.write(reference.toString());
                }
            }

            outputWriter.close();

            gui.logln(String.format("\n***************************************************************\n" +
                    "Reference Extraction Complete!\n" +
                    "Results saved to %s", outputPath.getPath()));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
