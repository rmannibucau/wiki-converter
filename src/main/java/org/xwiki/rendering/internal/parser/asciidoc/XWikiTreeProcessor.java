package org.xwiki.rendering.internal.parser.asciidoc;

import java.util.List;
import org.asciidoctor.extension.Treeprocessor;
import org.asciidoctor.internal.Block;
import org.asciidoctor.internal.DocumentRuby;

public class XWikiTreeProcessor extends Treeprocessor {
	public XWikiTreeProcessor(DocumentRuby documentRuby) {
		super(documentRuby);
	}

	@Override
	public void process() {
		final List<Block> blocks = this.document.blocks();
		for (final Block currentBlock : blocks) {
			List<String> lines = currentBlock.lines();
			System.out.println(lines);
		}
	}
}
