package org.loda.lalfred;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TestTika {
	public String fileToTxt(File f) {
		// 1、创建一个parser
		Parser parser = new AutoDetectParser();
		try (InputStream is = new FileInputStream(f)) {
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
			ContentHandler handler = new BodyContentHandler();
			ParseContext context = new ParseContext();
			context.set(Parser.class, parser);
			// 2、执行parser的parse()方法。
			parser.parse(is, handler, metadata, context);
			for (String name : metadata.names()) {
				System.out.println(name + ":" + metadata.get(name));
			}
			return handler.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void testPdf() {
		String text = tikaFascade(new File("/Users/loda/Documents/book/android/AndroidTrainingCHS.pdf"));
		System.out.println(text);
	}

	@Test
	public void testLoadPdf() {
		PDDocument document = null;
		for (int i = 0; i < 1000; i++) {
			try {
				document = PDDocument.load(new File("/Users/loda/Documents/book/android/AndroidTrainingCHS.pdf"));

			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
		System.out.println(document);
	}

	public String tikaFascade(File f) {
		try {
			Tika t = new Tika();
			return t.parseToString(f);
		} catch (IOException | TikaException e) {
			e.printStackTrace();
		}
		return null;
	}

}
