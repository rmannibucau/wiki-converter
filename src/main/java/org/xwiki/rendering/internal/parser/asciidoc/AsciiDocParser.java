package org.xwiki.rendering.internal.parser.asciidoc;

import java.io.Reader;
import java.util.Collections;
import javax.inject.Named;
import javax.inject.Singleton;
import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.syntax.SyntaxType;

@Component
@Named("asciidoc/1.0")
@Singleton
public class AsciiDocParser implements Parser {
	public static final Syntax SYNTAX = new Syntax(new SyntaxType("asciidoctor", "AsciiDoc"), "1.0");
	static { // cause we don't modify SyntaxType so we fake a register
		SyntaxType.getSyntaxTypes().put(SYNTAX.getType().getId(), SYNTAX.getType());
	}

	@Override
	public Syntax getSyntax() {
		return SYNTAX;
	}

	@Override
	public XDOM parse(Reader source) throws ParseException {
		return new XDOM(Collections.<Block>emptyList());
	}
}
