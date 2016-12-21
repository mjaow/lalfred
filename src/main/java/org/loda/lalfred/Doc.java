package org.loda.lalfred;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class Doc {

	private final File file;

	private Tika tika = new Tika();

	public Doc(File file) {
		this.file = file;
	}

	private String extractContent() {
		try {
			System.out.println(file.getName());
			return tika.parseToString(file);
		} catch (IOException | TikaException e) {
			return null;
		}
	}

	public List<Token> getTokens(boolean fullText) {
		String text;
		if (fullText) {
			text = extractContent();
		} else {
			text = file.getName();
		}

		if (text == null) {
			return Collections.emptyList();
		}

		List<Token> list = new ArrayList<>();
		IKSegmenter seg = new IKSegmenter(new StringReader(text), true);
		try {
			Lexeme lexeme;
			while ((lexeme = seg.next()) != null) {
				list.add(new Token(lexeme.getLexemeText(), lexeme.getLexemeType()));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		return list;

	}

	public File getFile() {
		return file;
	}

	public String getFileName() {
		return file.getName();
	}
}
