import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class GUI extends JFrame {

    private static int WIDTH = 800;
    private static int HEIGHT = 500;

    private JFileChooser fc;
    private JTextArea textArea = new JTextArea();

    public GUI() {
        super("Reference Extractor");

        setSize(WIDTH, HEIGHT);
        add(new JScrollPane(textArea));
        setVisible(true);
    }

    public void log(String data) {
        textArea.append(data);
        textArea.setCaretPosition(textArea.getDocument().getLength());
        this.validate();
    }

    public void logln(String data) {
        log(data + "\n");
    }

    public File choosePDF() {
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.addChoosableFileFilter(new PDFFilter());
        fc.setAcceptAllFileFilterUsed(false);
        fc.setApproveButtonText("Select");
        fc.setApproveButtonToolTipText("Select .pdf file (or folder of .pdf files)");
        fc.setDialogTitle("Select .pdf file (or folder of .pdf files)");

        fc.showDialog(this, null);

        return fc.getSelectedFile();

    }

    public File chooseTxt() {
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addChoosableFileFilter(new TXTFilter());
        fc.setAcceptAllFileFilterUsed(false);
        fc.setApproveButtonText("Select");
        fc.setApproveButtonToolTipText("Select a .txt file for output");
        fc.setDialogTitle("Select a .txt file for output");

        fc.showDialog(this, null);

        return fc.getSelectedFile();

    }

    private class PDFFilter extends FileFilter {

        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".pdf");
        }

        @Override
        public String getDescription() {
            return ".pdf";
        }

    }

    private class TXTFilter extends FileFilter {

        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");

        }

        @Override
        public String getDescription() {
            return ".txt";
        }

    }
}

