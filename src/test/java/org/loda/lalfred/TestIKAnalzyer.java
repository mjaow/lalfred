package org.loda.lalfred;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class TestIKAnalzyer {

	@Test
	public void testIk() {
		IKSegmenter seg = new IKSegmenter(new StringReader("编程之美.pdf"), true);

		try {
			Lexeme lexeme;
			while ((lexeme = seg.next()) != null) {
				String text = lexeme.getLexemeText();
				System.out.print(text + "|");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
