package org.loda.lalfred;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class Doc {

	private static final int PDF = 1;

	private static final int TEXT = 2;

	private static final int WORD = 3;

	private final int type;

	private final File file;

	private final Extractor extractor;

	public Doc(File file) {
		this.file = file;
		type = TEXT;
		if (isPdf()) {
			extractor = new PdfExtractor();
		} else if (isText()) {
			extractor = new TextExtractor();
		} else if (isWord()) {
			extractor = new WordExtractor();
		} else {
			extractor = new TextExtractor();
		}
	}

	public boolean isPdf() {
		return type == PDF;
	}

	public boolean isText() {
		return type == TEXT;
	}

	public boolean isWord() {
		return type == WORD;
	}

	private String extractContent() {
		return extractor.extract(file);
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
