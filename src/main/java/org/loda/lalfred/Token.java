package org.loda.lalfred;

import org.wltea.analyzer.core.Lexeme;

public class Token {

	private String text;

	private boolean chinese;

	public Token(String text, int type) {
		super();
		this.text = text;

		switch (type) {
		case Lexeme.TYPE_CNCHAR:
		case Lexeme.TYPE_CNUM:
		case Lexeme.TYPE_CNWORD:
		case Lexeme.TYPE_COUNT:
		case Lexeme.TYPE_CQUAN:
			chinese = true;
			break;
		default:
			chinese = false;
			break;
		}
	}

	public String getText() {
		return text;
	}

	public boolean isChinese() {
		return chinese;
	}
}
