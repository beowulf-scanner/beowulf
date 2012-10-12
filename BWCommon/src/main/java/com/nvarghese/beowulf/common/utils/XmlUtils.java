package com.nvarghese.beowulf.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author manish
 * 
 */
/**
 * @author love
 * 
 */
public class XmlUtils {

	private static final Logger logger = LoggerFactory.getLogger(XmlUtils.class);

	/**
	 * To not allow it to be created from public.
	 */
	private XmlUtils() {

	}

	/**
	 * Transform the input xml stream using the provided xsl stream and save the
	 * result xml at the given file location.
	 * 
	 * 
	 * @param xslStream
	 * @param xmlStream
	 * @param fileLocation
	 * @throws TransformerException
	 */
	public static void transformXMLUsingXSL(String xslFile, InputStream xmlStream, String fileLocation) throws TransformerException {

		transformXMLUsingXSL(xslFile, xmlStream, new File(fileLocation));
	}

	/**
	 * 
	 * Transform the input xml stream using the provided xsl stream and save the
	 * result xml at the given file location.
	 * 
	 * @param xslFile
	 * @param xmlStream
	 * @param outputFile
	 * @throws TransformerException
	 */
	public static void transformXMLUsingXSL(String xslFile, InputStream xmlStream, File outputFile) throws TransformerException {

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Source sourceXSL = new StreamSource(XmlUtils.class.getClassLoader().getResourceAsStream(xslFile));
		Templates templates = transformerFactory.newTemplates(sourceXSL);
		Transformer transformer = templates.newTransformer();
		Source sourceXML = new StreamSource(xmlStream);
		Result result = new StreamResult(outputFile);

		transformer.transform(sourceXML, result);
	}

	public static void transformXMLUsingXSL(String xslFile, InputStream xmlStream, File outputFile, Map<String, Object> parameters)
			throws TransformerException {

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Source sourceXSL = new StreamSource(XmlUtils.class.getClassLoader().getResourceAsStream(xslFile));
		Templates templates = transformerFactory.newTemplates(sourceXSL);
		Transformer transformer = templates.newTransformer();
		Source sourceXML = new StreamSource(xmlStream);
		Result result = new StreamResult(outputFile);
		Set<String> keys = parameters.keySet();
		for (String key : keys) {
			transformer.setParameter(key, parameters.get(key));
		}
		transformer.transform(sourceXML, result);
	}

	/**
	 * Transform the input xml stream using the provided xsl stream and save the
	 * result xml at the given file location.
	 * 
	 * @param xslStream
	 * @param xmlStream
	 * @param fileLocation
	 * @param parameters
	 * @throws TransformerException
	 */
	public static void transformXMLUsingXSL(String xslFile, InputStream xmlStream, String fileLocation, Map<String, Object> parameters)
			throws TransformerException {

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Source sourceXSL = new StreamSource(XmlUtils.class.getClassLoader().getResourceAsStream(xslFile));
		Templates templates = transformerFactory.newTemplates(sourceXSL);
		Transformer transformer = templates.newTransformer();
		Source sourceXML = new StreamSource(xmlStream);
		Result result = new StreamResult(new File(fileLocation));
		Set<String> keys = parameters.keySet();
		for (String key : keys) {
			transformer.setParameter(key, parameters.get(key));
		}
		transformer.transform(sourceXML, result);
	}

	/**
	 * @return Method is responsible for transforming the XMl to another formats
	 *         using a XSL.
	 * @throws TransformerException
	 */
	public static OutputStream transformXMLUsingXSL(InputStream xslStream, InputStream xmlStream, Map<String, Object> parameters)
			throws TransformerException {

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Source sourceXSL = new StreamSource(xslStream);
		Templates templates = transformerFactory.newTemplates(sourceXSL);
		Transformer transformer = templates.newTransformer();
		Source sourceXML = new StreamSource(xmlStream);
		OutputStream outputStream = new ByteArrayOutputStream();
		Result result = new StreamResult(outputStream);
		Set<String> keys = parameters.keySet();
		for (String key : keys) {
			transformer.setParameter(key, parameters.get(key));
		}
		transformer.transform(sourceXML, result);
		return outputStream;
	}

	/**
	 * Method is responsible for transforming the XMl to another formats using a
	 * XSL.
	 * 
	 * @param xslStream
	 * @param xmlStream
	 * @return
	 * @throws TransformerException
	 */
	public static OutputStream transformXMLUsingXSL(InputStream xslStream, InputStream xmlStream) throws TransformerException {

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Source sourceXSL = new StreamSource(xslStream);
		Templates templates = transformerFactory.newTemplates(sourceXSL);
		Transformer transformer = templates.newTransformer();
		Source sourceXML = new StreamSource(xmlStream);
		OutputStream outputStream = new ByteArrayOutputStream();
		Result result = new StreamResult(outputStream);
		transformer.transform(sourceXML, result);
		return outputStream;
	}

	/**
	 * Method is responsible for transforming the XMl to another formats using a
	 * XSL and a parameter map.
	 * 
	 * @return
	 * @throws TransformerException
	 */
	public static OutputStream transformXMLUsingXSL(String xslLocation, InputStream sourceXmlStream, Map<String, Object> parameters)
			throws TransformerException {

		logger.debug("Transforming the given xml stream using xsl `{}` with a substitution map.", xslLocation);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Source sourceXSL = new StreamSource(XmlUtils.class.getClassLoader().getResourceAsStream(xslLocation));
		Templates templates = transformerFactory.newTemplates(sourceXSL);
		Transformer transformer = templates.newTransformer();
		Source sourceXML = new StreamSource(sourceXmlStream);

		OutputStream outputStream = new ByteArrayOutputStream();
		Result result = new StreamResult(outputStream);
		Set<String> keys = parameters.keySet();
		for (String key : keys) {

			transformer.setParameter(key, parameters.get(key));
		}
		transformer.transform(sourceXML, result);
		return outputStream;
	}

	/**
	 * Method is responsible for transforming the XMl to another formats using a
	 * XSL and a parameter map.
	 * 
	 * @return
	 * @throws TransformerException
	 */
	public static OutputStream transformXMLUsingXSL(String xslLocation, InputStream sourceXmlStream, OutputStream outputStream)
			throws TransformerException {

		logger.debug("Transforming the given xml stream using xsl `{}` with a substitution map.", xslLocation);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Source sourceXSL = new StreamSource(XmlUtils.class.getClassLoader().getResourceAsStream(xslLocation));
		Templates templates = transformerFactory.newTemplates(sourceXSL);
		Transformer transformer = templates.newTransformer();
		Source sourceXML = new StreamSource(sourceXmlStream);

		Result result = new StreamResult(outputStream);
		transformer.transform(sourceXML, result);
		return outputStream;
	}

	/**
	 * Method is responsible for transforming the XMl to another formats using a
	 * XSL and a parameter map.
	 * 
	 * @return
	 * @throws TransformerException
	 */
	public static String transformXMLUsingXSL(String xslLocation, String sourceXml, Map<String, Object> parameters) throws TransformerException {

		logger.debug("Transforming the given xml using xsl `{}` with a substitution map.", xslLocation);
		InputStream sourceXmlStream = new ByteArrayInputStream(sourceXml.getBytes());
		OutputStream transformedXmlStream = transformXMLUsingXSL(xslLocation, sourceXmlStream, parameters);
		return transformedXmlStream.toString();
	}

	/**
	 * this method is responsible for converting any pojo to respective xml form
	 * 
	 * @param object
	 * @param filePath
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static File pojoToXml(Object object, String filePath) throws JAXBException, IOException {

		JAXBContext context = JAXBContext.newInstance(object.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		OutputStream os = new FileOutputStream(filePath);
		marshaller.marshal(object, os);
		File file = new File(filePath);
		return file;
	}

	/**
	 * 
	 * @param object
	 * @return
	 * @throws JAXBException
	 */
	public static String pojoToXml(Object object) throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(object.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// marshaller.setProperty(Marshaller.JAXB_ENCODING, "U");
		StringWriter writer = new StringWriter();
		marshaller.marshal(object, writer);
		String xmlData = writer.toString();
		return xmlData;
	}

	/**
	 * 
	 * @param xmlData
	 * @return
	 * @return
	 * @return
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xmlStringToPojo(String xmlData, Class<T> targetClass) throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(targetClass);
		StringReader reader = new StringReader(xmlData);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T) unmarshaller.unmarshal(reader);
	}

}
