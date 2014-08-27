package com.github.rmannibucau.wiki;

import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.internal.PrintStreamLog;

public class WikiCommandsTest {
	@Rule
	public final PrintStreamLog log = new StandardOutputStreamLog();

	@Test
	public void run() {
		new WikiCommands().convert("confluence", "tex", "classpath:test.confluence", "stdout");
		assertTrue(log.getLog().contains("\\chapter{Title}"));
		assertTrue(log.getLog().contains("\\begin{tabular}{|p{3cm}|p{3cm}|p{3cm}|p{3cm}|}\\hline"));
	}
}
