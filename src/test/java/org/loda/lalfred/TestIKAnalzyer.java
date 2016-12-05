package org.loda.lalfred;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class TestIKAnalzyer {
//dsp|投放|引擎|设计|1.0-|完整版|docx|
	@Test
	public void testIk() {
		IKSegmenter seg = new IKSegmenter(new StringReader("DSP投放引擎设计1.0-完整版.docx"), true);

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
