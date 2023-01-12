package com.trk.demo.html2pdf_;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

/**
 * POC convercion HTML to PDF
 *
 */
public class App {
	public static void main(String[] args) throws IOException, DocumentException {
		String sDirectorio = "../html2pdf_/htmlInput/";
		File f = new File(sDirectorio);
		File[] ficheros = f.listFiles();
		for (File fileinput : ficheros) {
			String filename = fileinput.getName();
			int dotExtension = filename.lastIndexOf('.');
			String extension = filename.substring(dotExtension + 1);
			if (extension.equals("html")) {
				htmltopdf(filename, sDirectorio);
				System.out.println(filename);
			}
		}

	}

	public static void htmltopdf(String filename, String path) throws IOException, DocumentException {

		// File htmlFile = new
		// File("../html2pdf_/htmlInput/DetalleOtrosServicios.html");
		File htmlFile = new File(path + filename);
		Document doc = Jsoup.parse(htmlFile, "UTF-8");
		doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
		try (OutputStream os = new FileOutputStream(
				"../html2pdf_/outputPdf/" + filename.substring(0, filename.lastIndexOf('.')) + ".pdf")) {
			ITextRenderer renderer = new ITextRenderer();
			SharedContext cntxt = renderer.getSharedContext();
			cntxt.setPrint(true);
			cntxt.setInteractive(false);
			String baseUrl = FileSystems.getDefault().getPath("../html2pdf_/htmlInput").toUri().toURL().toString();
			renderer.setDocumentFromString(doc.html(), baseUrl);
			renderer.layout();
			renderer.createPDF(os);
		}

	}
}