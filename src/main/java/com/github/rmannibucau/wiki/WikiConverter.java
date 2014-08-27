package com.github.rmannibucau.wiki;

import org.tomitribe.crest.Main;
import org.tomitribe.crest.SystemEnvironment;

public class WikiConverter {
	public static void main(final String[] args) throws Exception {
		preMain();
		final Main main = new Main(WikiCommands.class);
		main.main(new SystemEnvironment(), args);
	}

	private static void preMain() {
		// preregister asciidoctor while not built in
		try {
			Class.forName("org.xwiki.rendering.internal.parser.asciidoc.AsciiDocParser", true, WikiConverter.class.getClassLoader());
		} catch (final ClassNotFoundException e) {
			// no-op
		}
	}

	private WikiConverter() {
		// no-op
	}
}
