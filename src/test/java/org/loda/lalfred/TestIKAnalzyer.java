package org.loda.lalfred;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class TestIKAnalzyer {
	// dsp|投放|引擎|设计|1.0-|完整版|docx|
	@Test
	public void testIk() {
		IKSegmenter seg = new IKSegmenter(new StringReader("4.4.3　并行连接可能让人“感觉”更快一些 ......................................................................95"), true);

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
