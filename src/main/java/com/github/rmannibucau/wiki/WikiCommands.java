package com.github.rmannibucau.wiki;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.tomitribe.crest.api.Command;
import org.tomitribe.crest.api.Option;
import org.xwiki.component.embed.EmbeddableComponentManager;
import org.xwiki.component.manager.ComponentLifecycleException;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.rendering.converter.Converter;
import org.xwiki.rendering.internal.parser.asciidoc.AsciiDocParser;
import org.xwiki.rendering.renderer.printer.DefaultWikiPrinter;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.syntax.SyntaxType;

public class WikiCommands {
	@Command
	public void convert(final @Option("source-format") String inFormat,
						final @Option("target-format") String outFormat,
						final @Option("source") String in,
						final @Option("target") String out) {
		final EmbeddableComponentManager container = new EmbeddableComponentManager();
		container.initialize(Thread.currentThread().getContextClassLoader());
		Converter converter = null;
		try {
			converter = container.getInstance(Converter.class);

			final DefaultWikiPrinter printer = new DefaultWikiPrinter();
			converter.convert(toReader(in), findSyntax(inFormat), findSyntax(outFormat), printer);

			final Writer writer = toWriter(out);
			try {
				writer.write(printer.toString());
			} finally {
				if (out.startsWith("std")) {
					// ensure EOL is correct, worse case one too much line which is better than not having a new line at the end
					writer.write("\n");
					writer.flush();
				} else {
					writer.close();
				}
			}
		} catch (final ComponentLookupException e) {
			throw new IllegalArgumentException("Classpath is messed up", e);
		} catch (final Exception e) {
			throw new IllegalArgumentException("Can't convert " + inFormat + " to " + outFormat + " for " + in, e);
		} finally {
			if (converter != null) {
				try {
					container.release(converter);
				} catch (final ComponentLifecycleException e) {
					// no-op
				}
			}
		}
	}

	private Syntax findSyntax(final String format) {
		// hack for adoc while not part of main syntaxes
		if ("asciidoctor".equalsIgnoreCase(format)) {
			return AsciiDocParser.SYNTAX;
		}

		try {
			for (final Field f : Syntax.class.getFields()) {
				final int modifiers = f.getModifiers();
				if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && Syntax.class == f.getType()) {
					final Syntax syntax = Syntax.class.cast(f.get(null));
					final SyntaxType type = syntax.getType();
					if (type.getName().equalsIgnoreCase(format) || type.getId().equalsIgnoreCase(format)) {
						return syntax;
					}
				}
			}
		} catch (final Exception e) {
			// no-op
		}
		throw new IllegalArgumentException("Can't find format: " + format + ". Availables: " + SyntaxType.getSyntaxTypes().keySet());
	}

	private Reader toReader(final String path) throws FileNotFoundException {
		if ("stdin".equalsIgnoreCase(path)) { // not that recommanded since it will block most of the time
			return new InputStreamReader(System.in);
		}
		if (path.startsWith("classpath:")) {
			return new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(path.substring("classpath:".length())));
		}
		return new FileReader(path);
	}

	private Writer toWriter(final String path) throws IOException {
		if ("stdout".equalsIgnoreCase(path)) {
			return new OutputStreamWriter(System.out);
		}
		if ("stderr".equalsIgnoreCase(path)) {
			return new OutputStreamWriter(System.err);
		}
		return new FileWriter(path);
	}
}
