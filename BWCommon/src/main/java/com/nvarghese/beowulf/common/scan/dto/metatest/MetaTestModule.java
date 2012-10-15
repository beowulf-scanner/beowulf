package com.nvarghese.beowulf.common.scan.dto.metatest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.nvarghese.beowulf.common.webtest.WebTestCategory;
import com.nvarghese.beowulf.common.webtest.WebTestType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "moduleNumber", "moduleName", "enabled", "moduleDescription", "options", "webTestCategory", "webTestType",
		"testAttributes" })
@XmlRootElement(name = "meta_test_module")
public class MetaTestModule {

	@XmlElement(name = "module_number", required = true)
	protected BigInteger moduleNumber;

	@XmlElement(name = "module_name", required = true)
	protected String moduleName;

	@XmlElement(name = "module_enabled", required = true)
	protected boolean enabled;

	@XmlElement(name = "module_description")
	protected String moduleDescription;

	@XmlElement(name = "options")
	protected List<Options> options;

	@XmlAttribute(name = "test_category", required = true)
	protected WebTestCategory webTestCategory;

	@XmlAttribute(name = "test_type", required = true)
	protected WebTestType webTestType;

	@XmlElement(name = "test_attributes")
	protected TestAttributes testAttributes;

	/**
	 * Gets the value of the moduleNumber property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getModuleNumber() {

		return moduleNumber;
	}

	/**
	 * Sets the value of the moduleNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setModuleNumber(BigInteger value) {

		this.moduleNumber = value;
	}

	/**
	 * Gets the value of the moduleName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getModuleName() {

		return moduleName;
	}

	/**
	 * Sets the value of the moduleName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setModuleName(String value) {

		this.moduleName = value;
	}

	/**
	 * Gets the value of the enabled property.
	 * 
	 */
	public boolean isEnabled() {

		return enabled;
	}

	/**
	 * Sets the value of the enabled property.
	 * 
	 */
	public void setEnabled(boolean value) {

		this.enabled = value;
	}

	/**
	 * Gets the value of the options property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the options property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getOptions().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Options }
	 * 
	 * 
	 */
	public List<Options> getOptions() {

		if (options == null) {
			options = new ArrayList<Options>();
		}
		return this.options;
	}

	public String getModuleDescription() {

		return moduleDescription;
	}

	public void setModuleDescription(String moduleDescription) {

		this.moduleDescription = moduleDescription;
	}

	public WebTestCategory getWebTestCategory() {

		return webTestCategory;
	}

	public void setWebTestCategory(WebTestCategory webTestCategory) {

		this.webTestCategory = webTestCategory;
	}

	public WebTestType getWebTestType() {

		return webTestType;
	}

	public void setWebTestType(WebTestType webTestType) {

		this.webTestType = webTestType;
	}

	public TestAttributes getTestAttributes() {

		return testAttributes;
	}

	public void setTestAttributes(TestAttributes testAttributes) {

		this.testAttributes = testAttributes;
	}

}
