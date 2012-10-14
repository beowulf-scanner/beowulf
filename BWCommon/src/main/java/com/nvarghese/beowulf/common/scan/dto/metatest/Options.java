package com.nvarghese.beowulf.common.scan.dto.metatest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "optionName", "optionValue" })
@XmlRootElement(name = "options")
public class Options {

	@XmlElement(name = "option_name", required = true)
	protected String optionName;
	@XmlElement(name = "option_value", required = true)
	protected String optionValue;
	@XmlAttribute
	protected String group;
	@XmlAttribute
	protected String type;

	/**
	 * Gets the value of the optionName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOptionName() {

		return optionName;
	}

	/**
	 * Sets the value of the optionName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOptionName(String value) {

		this.optionName = value;
	}

	/**
	 * Gets the value of the optionValue property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOptionValue() {

		return optionValue;
	}

	/**
	 * Sets the value of the optionValue property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOptionValue(String value) {

		this.optionValue = value;
	}

	/**
	 * Gets the value of the group property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGroup() {

		return group;
	}

	/**
	 * Sets the value of the group property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGroup(String value) {

		this.group = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getType() {

		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setType(String value) {

		this.type = value;
	}

}
