package PDFDisplay;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PDFDisplay extends JComponent {
    private PDDocument document;
    private PDFRenderer pdfRenderer;

    public PDFDisplay(int id_transaksi) {
        try {
            // Load the PDF file from the given path
            File file = new File("D:\\Asa\\PBO\\kasir\\pdf\\transaksi-" + id_transaksi + ".pdf");
            document = PDDocument.load(file); // This method loads the PDF document
            pdfRenderer = new PDFRenderer(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            // Render page 0 (first page) of the PDF
            int pageIndex = 0; // First page (index starts from 0)
            Image image = pdfRenderer.renderImage(pageIndex); // Render page as image
            g.drawImage(image, 0, 0, null); // Draw the image on the component
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to release resources after use
    public void closeDocument() {
        try {
            if (document != null) {
                document.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
