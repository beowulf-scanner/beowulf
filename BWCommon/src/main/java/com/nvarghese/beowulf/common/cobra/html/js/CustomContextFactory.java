package com.nvarghese.beowulf.common.cobra.html.js;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;

public class CustomContextFactory extends ContextFactory {

	// Custom Context to store execution time.
	private static class CustomContext extends Context {

		long startTime;
	}

	static {
		// Initialize GlobalFactory with custom factory
		ContextFactory.initGlobal(new CustomContextFactory());
	}

	// Override makeContext()
	protected Context makeContext() {

		CustomContext cx = new CustomContext();
		// Use pure interpreter mode to allow for
		// observeInstructionCount(Context, int) to work
		cx.setOptimizationLevel(-1);
		// Make Rhino runtime to call observeInstructionCount
		// each 10000 bytecode instructions
		cx.setInstructionObserverThreshold(10000);
		return cx;
	}

	// Override hasFeature(Context, int)
	public boolean hasFeature(Context cx, int featureIndex) {

		// Turn on maximum compatibility with MSIE scripts
		switch (featureIndex) {
			case Context.FEATURE_NON_ECMA_GET_YEAR:
				return true;

			case Context.FEATURE_MEMBER_EXPR_AS_FUNCTION_NAME:
				return true;

			case Context.FEATURE_RESERVED_KEYWORD_AS_IDENTIFIER:
				return true;

			case Context.FEATURE_PARENT_PROTO_PROPRTIES:
				return false;
		}
		return super.hasFeature(cx, featureIndex);
	}

	// Override observeInstructionCount(Context, int)
	protected void observeInstructionCount(Context cx, int instructionCount) {

		CustomContext mcx = (CustomContext) cx;
		long currentTime = System.currentTimeMillis();
		if (currentTime - mcx.startTime > 20 * 1000) {
			// More then 20 seconds from Context creation time:
			// it is time to stop the script.
			// Throw Error instance to ensure that script will never
			// get control back through catch or finally.
			throw new Error();
		}
	}

	// Override doTopCall(Callable, Context, Scriptable scope, Scriptable
	// thisObj, Object[] args)
	protected Object doTopCall(Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {

		CustomContext mcx = (CustomContext) cx;
		mcx.startTime = System.currentTimeMillis();

		return super.doTopCall(callable, cx, scope, thisObj, args);
	}

}
