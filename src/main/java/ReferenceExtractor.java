import pl.edu.icm.cermine.ContentExtractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReferenceExtractor {

    private static List<File> getFiles(File file) {

        List<File> files = new ArrayList<File>();

        if(file.isFile() && file.getName().toLowerCase().endsWith(".pdf")) {
            files.add(file);
        } else if (file.isDirectory()) {
            File[] currentFolderFiles = file.listFiles();
            if(currentFolderFiles != null && currentFolderFiles.length > 0) {
                for (File folderFile : currentFolderFiles) {
                    files.addAll(getFiles(folderFile));
                }
            }
        }

        return files;
    }

    private static List<File> getFiles(String folder_path) {
        return getFiles(new File(folder_path));
    }

    private static String promptForString(String prompt, Scanner reader) {
        System.out.println(prompt);
        return reader.nextLine();
    }

    public static void main(String[] args) {

        try {

            //            String file = "/home/nick/PycharmProjects/reference_extracter/test_article.pdf";

            Scanner reader = new Scanner(System.in);
            String inputPath = promptForString("Enter path to PDF file (or folder of PDF files) to extract references: ", reader);
            String outputPath = promptForString("Enter path to .txt file to output results.", reader);
            reader.close();

            File outputFile = new File(outputPath);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }

            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile));
            outputWriter.write("TITLE\tYEAR PUBLISHED\tAUTHORS\n");

            ContentExtractor extractor = new ContentExtractor();

            List<File> files = getFiles(inputPath);

            for(File file : files) {

                System.out.printf("Extracting References From => %s\n\n", file.getPath());

                Article article = new Article(file.getPath(), extractor);

                System.out.println(article.fancyPrint());
                outputWriter.write(article.toString());
                for(Article reference : article.getReferences()) {
                    System.out.println(reference.fancyPrint());
                    outputWriter.write(reference.toString());
                }
            }

            outputWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
