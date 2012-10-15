package com.nvarghese.beowulf.common.scan.dto.metatest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "metaTestModule" })
@XmlRootElement(name = "meta_test_modules")
public class MetaTestModules {

	@XmlElement(name = "meta_test_module", required = true)
	protected List<MetaTestModule> metaTestModule;

	public List<MetaTestModule> getMetaTestModule() {

		if (metaTestModule == null) {
			metaTestModule = new ArrayList<MetaTestModule>();
		}
		return this.metaTestModule;
	}

}
