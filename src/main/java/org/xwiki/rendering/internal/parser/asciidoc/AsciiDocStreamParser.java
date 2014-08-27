package org.xwiki.rendering.internal.parser.asciidoc;

import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import javax.inject.Named;
import javax.inject.Singleton;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.extension.ExtensionRegistry;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.phase.Initializable;
import org.xwiki.component.phase.InitializationException;
import org.xwiki.rendering.listener.Listener;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.parser.StreamParser;
import org.xwiki.rendering.syntax.Syntax;

import static org.asciidoctor.Asciidoctor.Factory.create;

/**
 * Stream Parser for AsciiDoc syntax.
 *
 * @version $Id$
 * @since 5.4M1
 */
@Component
@Named("asciidoc/1.0")
@Singleton
public class AsciiDocStreamParser implements StreamParser, Initializable
{
	private Asciidoctor asciidoctor;

	@Override
	public Syntax getSyntax()
	{
		return null;
	}

	@Override
	public void initialize() throws InitializationException
	{
		this.asciidoctor = create();
		ExtensionRegistry extensionRegistry = this.asciidoctor.extensionRegistry();
		extensionRegistry.treeprocessor(XWikiTreeProcessor.class);
	}

	@Override
	public void parse(Reader source, Listener listener) throws ParseException
	{
		StringWriter writer = new StringWriter();
//        try {
		this.asciidoctor.render("this is *bold*", new HashMap<String, Object>());
//            this.asciidoctor.render(source, writer, new HashMap<String, Object>());
//        } catch (IOException e) {
//            throw new ParseException("Failed to parse AsciiDoc content", e);
//        }
	}
}
