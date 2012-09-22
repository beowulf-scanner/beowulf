package com.nvarghese.beowulf.common.cobra.util.io;

import java.io.IOException;
import java.io.Reader;

public class EmptyReader extends Reader {

	public void close() throws IOException {

	}

	public int read(final char[] cbuf, final int off, final int len) throws IOException {

		return 0;
	}
}
