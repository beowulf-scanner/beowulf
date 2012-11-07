package com.nvarghese.beowulf.smf.scan.services;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.exception.ServiceException;
import com.nvarghese.beowulf.common.utils.XmlUtils;
import com.nvarghese.beowulf.smf.SmfManager;
import com.nvarghese.beowulf.smf.scan.dto.reasons.Reasons;

public class ReasonsDispatcherService {

	static Logger logger = LoggerFactory.getLogger(ReasonsDispatcherService.class);

	public Reasons getAbortScanReasons() throws ServiceException {

		String abortFileName = SmfManager.getInstance().getSettings().getAbortReasonsFileName();

		try {
			Reasons reasons = XmlUtils.xmlStringToPojo(readContents(abortFileName), Reasons.class);
			return reasons;
		} catch (JAXBException e) {
			logger.error("Failed to unmarshall abort reasons. Reasons: {}", e.getMessage(), e);
			throw new ServiceException("Failed to retrieve abort reasons");
		}

	}

	/**
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public Reasons getReportGenReasons() throws ServiceException {

		String reportGenFileName = SmfManager.getInstance().getSettings().getReportGenReasonsFileName();

		try {
			Reasons reasons = XmlUtils.xmlStringToPojo(readContents(reportGenFileName), Reasons.class);
			return reasons;
		} catch (JAXBException e) {
			logger.error("Failed to unmarshall reportgen reasons. Reasons: {}", e.getMessage(), e);
			throw new ServiceException("Failed to retrieve report generate reasons");
		}

	}

	private String readContents(String fileName) {

		URL url = ReasonsDispatcherService.class.getClassLoader().getResource(fileName);
		String content = "";

		try {

			//
			if (url != null && !url.toString().startsWith("jar")) {
				logger.info("The resolved URL: {}", url.toString());
				content = FileUtils.readFileToString(new File(url.toURI()));
			} else {
				content = FileUtils.readFileToString(new File(SmfManager.getInstance().getSettings().getDefaultConfDir(), fileName));
			}

		} catch (IOException e) {
			logger.error("Failed to load test modules. Reason: {}", e.getMessage(), e);
		} catch (URISyntaxException e) {
			logger.error("Failed to load test modules. Reason: {}", e.getMessage(), e);
		}

		return content;

	}

}
