/*
 * Copyright (c) 2000 World Wide Web Consortium, (Massachusetts Institute of
 * Technology, Institut National de Recherche en Informatique et en Automatique,
 * Keio University). All Rights Reserved. This program is distributed under the
 * W3C's Software Intellectual Property License. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * W3C License http://www.w3.org/Consortium/Legal/ for more details.
 */

package org.w3c.dom.css_bad;

import org.w3c.dom.DOMException;

/**
 * The <code>CSS2Properties</code> interface represents a convenience mechanism
 * for retrieving and setting properties within a
 * <code>CSSStyleDeclaration</code>. The attributes of this interface correspond
 * to all the properties specified in CSS2. Getting an attribute of this
 * interface is equivalent to calling the <code>getPropertyValue</code> method
 * of the <code>CSSStyleDeclaration</code> interface. Setting an attribute of
 * this interface is equivalent to calling the <code>setProperty</code> method
 * of the <code>CSSStyleDeclaration</code> interface.
 * <p>
 * A conformant implementation of the CSS module is not required to implement
 * the <code>CSS2Properties</code> interface. If an implementation does
 * implement this interface, the expectation is that language-specific methods
 * can be used to cast from an instance of the <code>CSSStyleDeclaration</code>
 * interface to the <code>CSS2Properties</code> interface.
 * <p>
 * If an implementation does implement this interface, it is expected to
 * understand the specific syntax of the shorthand properties, and apply their
 * semantics; when the <code>margin</code> property is set, for example, the
 * <code>marginTop</code>, <code>marginRight</code>, <code>marginBottom</code>
 * and <code>marginLeft</code> properties are actually being set by the
 * underlying implementation.
 * <p>
 * When dealing with CSS "shorthand" properties, the shorthand properties should
 * be decomposed into their component longhand properties as appropriate, and
 * when querying for their value, the form returned should be the shortest form
 * exactly equivalent to the declarations made in the ruleset. However, if there
 * is no shorthand declaration that could be added to the ruleset without
 * changing in any way the rules already declared in the ruleset (i.e., by
 * adding longhand rules that were previously not declared in the ruleset), then
 * the empty string should be returned for the shorthand property.
 * <p>
 * For example, querying for the <code>font</code> property should not return
 * "normal normal normal 14pt/normal Arial, sans-serif", when "14pt Arial,
 * sans-serif" suffices. (The normals are initial values, and are implied by use
 * of the longhand property.)
 * <p>
 * If the values for all the longhand properties that compose a particular
 * string are the initial values, then a string consisting of all the initial
 * values should be returned (e.g. a <code>border-width</code> value of "medium"
 * should be returned as such, not as "").
 * <p>
 * For some shorthand properties that take missing values from other sides, such
 * as the <code>margin</code>, <code>padding</code>, and
 * <code>border-[width|style|color]</code> properties, the minimum number of
 * sides possible should be used; i.e., "0px 10px" will be returned instead of
 * "0px 10px 0px 10px".
 * <p>
 * If the value of a shorthand property can not be decomposed into its component
 * longhand properties, as is the case for the <code>font</code> property with a
 * value of "menu", querying for the values of the component longhand properties
 * should return the empty string.
 * <p>
 * See also the <a
 * href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document
 * Object Model (DOM) Level 2 Style Specification</a>.
 * 
 * @since DOM Level 2
 */
public interface CSS2Properties {

	/**
	 * See the azimuth property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getAzimuth();

	/**
	 * See the background property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBackground();

	/**
	 * See the background-attachment property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBackgroundAttachment();

	/**
	 * See the background-color property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBackgroundColor();

	/**
	 * See the background-image property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBackgroundImage();

	/**
	 * See the background-position property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBackgroundPosition();

	/**
	 * See the background-repeat property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBackgroundRepeat();

	/**
	 * See the border property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorder();

	/**
	 * See the border-bottom property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderBottom();

	/**
	 * See the border-bottom-color property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderBottomColor();

	/**
	 * See the border-bottom-style property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderBottomStyle();

	/**
	 * See the border-bottom-width property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderBottomWidth();

	/**
	 * See the border-collapse property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderCollapse();

	/**
	 * See the border-color property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderColor();

	/**
	 * See the border-left property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderLeft();

	/**
	 * See the border-left-color property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderLeftColor();

	/**
	 * See the border-left-style property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderLeftStyle();

	/**
	 * See the border-left-width property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderLeftWidth();

	/**
	 * See the border-right property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderRight();

	/**
	 * See the border-right-color property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderRightColor();

	/**
	 * See the border-right-style property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderRightStyle();

	/**
	 * See the border-right-width property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderRightWidth();

	/**
	 * See the border-spacing property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderSpacing();

	/**
	 * See the border-style property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderStyle();

	/**
	 * See the border-top property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderTop();

	/**
	 * See the border-top-color property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderTopColor();

	/**
	 * See the border-top-style property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderTopStyle();

	/**
	 * See the border-top-width property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderTopWidth();

	/**
	 * See the border-width property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBorderWidth();

	/**
	 * See the bottom property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getBottom();

	/**
	 * See the caption-side property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getCaptionSide();

	/**
	 * See the clear property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getClear();

	/**
	 * See the clip property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getClip();

	/**
	 * See the color property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getColor();

	/**
	 * See the content property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getContent();

	/**
	 * See the counter-increment property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getCounterIncrement();

	/**
	 * See the counter-reset property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getCounterReset();

	/**
	 * See the float property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getCssFloat();

	/**
	 * See the cue property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getCue();

	/**
	 * See the cue-after property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getCueAfter();

	/**
	 * See the cue-before property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getCueBefore();

	/**
	 * See the cursor property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getCursor();

	/**
	 * See the direction property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getDirection();

	/**
	 * See the display property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getDisplay();

	/**
	 * See the elevation property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getElevation();

	/**
	 * See the empty-cells property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getEmptyCells();

	/**
	 * See the font property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getFont();

	/**
	 * See the font-family property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getFontFamily();

	/**
	 * See the font-size property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getFontSize();

	/**
	 * See the font-size-adjust property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getFontSizeAdjust();

	/**
	 * See the font-stretch property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getFontStretch();

	/**
	 * See the font-style property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getFontStyle();

	/**
	 * See the font-variant property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getFontVariant();

	/**
	 * See the font-weight property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getFontWeight();

	/**
	 * See the height property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getHeight();

	/**
	 * See the left property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getLeft();

	/**
	 * See the letter-spacing property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getLetterSpacing();

	/**
	 * See the line-height property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getLineHeight();

	/**
	 * See the list-style property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getListStyle();

	/**
	 * See the list-style-image property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getListStyleImage();

	/**
	 * See the list-style-position property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getListStylePosition();

	/**
	 * See the list-style-type property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getListStyleType();

	/**
	 * See the margin property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getMargin();

	/**
	 * See the margin-bottom property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getMarginBottom();

	/**
	 * See the margin-left property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getMarginLeft();

	/**
	 * See the margin-right property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getMarginRight();

	/**
	 * See the margin-top property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getMarginTop();

	/**
	 * See the marker-offset property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getMarkerOffset();

	/**
	 * See the marks property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getMarks();

	/**
	 * See the max-height property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getMaxHeight();

	/**
	 * See the max-width property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getMaxWidth();

	/**
	 * See the min-height property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getMinHeight();

	/**
	 * See the min-width property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getMinWidth();

	/**
	 * See the orphans property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getOrphans();

	/**
	 * See the outline property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getOutline();

	/**
	 * See the outline-color property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getOutlineColor();

	/**
	 * See the outline-style property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getOutlineStyle();

	/**
	 * See the outline-width property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getOutlineWidth();

	/**
	 * See the overflow property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getOverflow();

	/**
	 * See the padding property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPadding();

	/**
	 * See the padding-bottom property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPaddingBottom();

	/**
	 * See the padding-left property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPaddingLeft();

	/**
	 * See the padding-right property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPaddingRight();

	/**
	 * See the padding-top property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPaddingTop();

	/**
	 * See the page property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPage();

	/**
	 * See the page-break-after property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPageBreakAfter();

	/**
	 * See the page-break-before property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPageBreakBefore();

	/**
	 * See the page-break-inside property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPageBreakInside();

	/**
	 * See the pause property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPause();

	/**
	 * See the pause-after property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPauseAfter();

	/**
	 * See the pause-before property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPauseBefore();

	/**
	 * See the pitch property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPitch();

	/**
	 * See the pitch-range property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPitchRange();

	/**
	 * See the play-during property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPlayDuring();

	/**
	 * See the position property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getPosition();

	/**
	 * See the quotes property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getQuotes();

	/**
	 * See the richness property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getRichness();

	/**
	 * See the right property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getRight();

	/**
	 * See the size property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getSize();

	/**
	 * See the speak property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getSpeak();

	/**
	 * See the speak-header property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getSpeakHeader();

	/**
	 * See the speak-numeral property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getSpeakNumeral();

	/**
	 * See the speak-punctuation property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getSpeakPunctuation();

	/**
	 * See the speech-rate property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getSpeechRate();

	/**
	 * See the stress property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getStress();

	/**
	 * See the table-layout property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getTableLayout();

	/**
	 * See the text-align property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getTextAlign();

	/**
	 * See the text-decoration property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getTextDecoration();

	/**
	 * See the text-indent property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getTextIndent();

	/**
	 * See the text-shadow property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getTextShadow();

	/**
	 * See the text-transform property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getTextTransform();

	/**
	 * See the top property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getTop();

	/**
	 * See the unicode-bidi property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getUnicodeBidi();

	/**
	 * See the vertical-align property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getVerticalAlign();

	/**
	 * See the visibility property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getVisibility();

	/**
	 * See the voice-family property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getVoiceFamily();

	/**
	 * See the volume property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getVolume();

	/**
	 * See the white-space property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getWhiteSpace();

	/**
	 * See the widows property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getWidows();

	/**
	 * See the width property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getWidth();

	/**
	 * See the word-spacing property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getWordSpacing();

	/**
	 * See the z-index property definition in CSS2.
	 * 
	 * @exception DOMException
	 *                SYNTAX_ERR: Raised if the new value has a syntax error and
	 *                is unparsable. <br>
	 *                NO_MODIFICATION_ALLOWED_ERR: Raised if this property is
	 *                readonly.
	 */
	public String getZIndex();

	public void setAzimuth(String azimuth) throws DOMException;

	public void setBackground(String background) throws DOMException;

	public void setBackgroundAttachment(String backgroundAttachment) throws DOMException;

	public void setBackgroundColor(String backgroundColor) throws DOMException;

	public void setBackgroundImage(String backgroundImage) throws DOMException;

	public void setBackgroundPosition(String backgroundPosition) throws DOMException;

	public void setBackgroundRepeat(String backgroundRepeat) throws DOMException;

	public void setBorder(String border) throws DOMException;

	public void setBorderBottom(String borderBottom) throws DOMException;

	public void setBorderBottomColor(String borderBottomColor) throws DOMException;

	public void setBorderBottomStyle(String borderBottomStyle) throws DOMException;

	public void setBorderBottomWidth(String borderBottomWidth) throws DOMException;

	public void setBorderCollapse(String borderCollapse) throws DOMException;

	public void setBorderColor(String borderColor) throws DOMException;

	public void setBorderLeft(String borderLeft) throws DOMException;

	public void setBorderLeftColor(String borderLeftColor) throws DOMException;

	public void setBorderLeftStyle(String borderLeftStyle) throws DOMException;

	public void setBorderLeftWidth(String borderLeftWidth) throws DOMException;

	public void setBorderRight(String borderRight) throws DOMException;

	public void setBorderRightColor(String borderRightColor) throws DOMException;

	public void setBorderRightStyle(String borderRightStyle) throws DOMException;

	public void setBorderRightWidth(String borderRightWidth) throws DOMException;

	public void setBorderSpacing(String borderSpacing) throws DOMException;

	public void setBorderStyle(String borderStyle) throws DOMException;

	public void setBorderTop(String borderTop) throws DOMException;

	public void setBorderTopColor(String borderTopColor) throws DOMException;

	public void setBorderTopStyle(String borderTopStyle) throws DOMException;

	public void setBorderTopWidth(String borderTopWidth) throws DOMException;

	public void setBorderWidth(String borderWidth) throws DOMException;

	public void setBottom(String bottom) throws DOMException;

	public void setCaptionSide(String captionSide) throws DOMException;

	public void setClear(String clear) throws DOMException;

	public void setClip(String clip) throws DOMException;

	public void setColor(String color) throws DOMException;

	public void setContent(String content) throws DOMException;

	public void setCounterIncrement(String counterIncrement) throws DOMException;

	public void setCounterReset(String counterReset) throws DOMException;

	public void setCssFloat(String cssFloat) throws DOMException;

	public void setCue(String cue) throws DOMException;

	public void setCueAfter(String cueAfter) throws DOMException;

	public void setCueBefore(String cueBefore) throws DOMException;

	public void setCursor(String cursor) throws DOMException;

	public void setDirection(String direction) throws DOMException;

	public void setDisplay(String display) throws DOMException;

	public void setElevation(String elevation) throws DOMException;

	public void setEmptyCells(String emptyCells) throws DOMException;

	public void setFont(String font) throws DOMException;

	public void setFontFamily(String fontFamily) throws DOMException;

	public void setFontSize(String fontSize) throws DOMException;

	public void setFontSizeAdjust(String fontSizeAdjust) throws DOMException;

	public void setFontStretch(String fontStretch) throws DOMException;

	public void setFontStyle(String fontStyle) throws DOMException;

	public void setFontVariant(String fontVariant) throws DOMException;

	public void setFontWeight(String fontWeight) throws DOMException;

	public void setHeight(String height) throws DOMException;

	public void setLeft(String left) throws DOMException;

	public void setLetterSpacing(String letterSpacing) throws DOMException;

	public void setLineHeight(String lineHeight) throws DOMException;

	public void setListStyle(String listStyle) throws DOMException;

	public void setListStyleImage(String listStyleImage) throws DOMException;

	public void setListStylePosition(String listStylePosition) throws DOMException;

	public void setListStyleType(String listStyleType) throws DOMException;

	public void setMargin(String margin) throws DOMException;

	public void setMarginBottom(String marginBottom) throws DOMException;

	public void setMarginLeft(String marginLeft) throws DOMException;

	public void setMarginRight(String marginRight) throws DOMException;

	public void setMarginTop(String marginTop) throws DOMException;

	public void setMarkerOffset(String markerOffset) throws DOMException;

	public void setMarks(String marks) throws DOMException;

	public void setMaxHeight(String maxHeight) throws DOMException;

	public void setMaxWidth(String maxWidth) throws DOMException;

	public void setMinHeight(String minHeight) throws DOMException;

	public void setMinWidth(String minWidth) throws DOMException;

	public void setOrphans(String orphans) throws DOMException;

	public void setOutline(String outline) throws DOMException;

	public void setOutlineColor(String outlineColor) throws DOMException;

	public void setOutlineStyle(String outlineStyle) throws DOMException;

	public void setOutlineWidth(String outlineWidth) throws DOMException;

	public void setOverflow(String overflow) throws DOMException;

	public void setPadding(String padding) throws DOMException;

	public void setPaddingBottom(String paddingBottom) throws DOMException;

	public void setPaddingLeft(String paddingLeft) throws DOMException;

	public void setPaddingRight(String paddingRight) throws DOMException;

	public void setPaddingTop(String paddingTop) throws DOMException;

	public void setPage(String page) throws DOMException;

	public void setPageBreakAfter(String pageBreakAfter) throws DOMException;

	public void setPageBreakBefore(String pageBreakBefore) throws DOMException;

	public void setPageBreakInside(String pageBreakInside) throws DOMException;

	public void setPause(String pause) throws DOMException;

	public void setPauseAfter(String pauseAfter) throws DOMException;

	public void setPauseBefore(String pauseBefore) throws DOMException;

	public void setPitch(String pitch) throws DOMException;

	public void setPitchRange(String pitchRange) throws DOMException;

	public void setPlayDuring(String playDuring) throws DOMException;

	public void setPosition(String position) throws DOMException;

	public void setQuotes(String quotes) throws DOMException;

	public void setRichness(String richness) throws DOMException;

	public void setRight(String right) throws DOMException;

	public void setSize(String size) throws DOMException;

	public void setSpeak(String speak) throws DOMException;

	public void setSpeakHeader(String speakHeader) throws DOMException;

	public void setSpeakNumeral(String speakNumeral) throws DOMException;

	public void setSpeakPunctuation(String speakPunctuation) throws DOMException;

	public void setSpeechRate(String speechRate) throws DOMException;

	public void setStress(String stress) throws DOMException;

	public void setTableLayout(String tableLayout) throws DOMException;

	public void setTextAlign(String textAlign) throws DOMException;

	public void setTextDecoration(String textDecoration) throws DOMException;

	public void setTextIndent(String textIndent) throws DOMException;

	public void setTextShadow(String textShadow) throws DOMException;

	public void setTextTransform(String textTransform) throws DOMException;

	public void setTop(String top) throws DOMException;

	public void setUnicodeBidi(String unicodeBidi) throws DOMException;

	public void setVerticalAlign(String verticalAlign) throws DOMException;

	public void setVisibility(String visibility) throws DOMException;

	public void setVoiceFamily(String voiceFamily) throws DOMException;

	public void setVolume(String volume) throws DOMException;

	public void setWhiteSpace(String whiteSpace) throws DOMException;

	public void setWidows(String widows) throws DOMException;

	public void setWidth(String width) throws DOMException;

	public void setWordSpacing(String wordSpacing) throws DOMException;

	public void setZIndex(String zIndex) throws DOMException;

}
