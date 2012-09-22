package com.nvarghese.beowulf.common.cobra.html.domimpl;

import com.nvarghese.beowulf.common.cobra.html.BrowserFrame;

/**
 * Tag interface for frame nodes.
 */
public interface FrameNode {

	public BrowserFrame getBrowserFrame();

	public void setBrowserFrame(BrowserFrame frame);
}
