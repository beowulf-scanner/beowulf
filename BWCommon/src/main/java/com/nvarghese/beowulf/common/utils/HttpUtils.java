package com.nvarghese.beowulf.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.util.ByteArrayBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {

	static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * 
	 * @param entity
	 * @param maxSize
	 *            Set to zero for no limit
	 * @return
	 * @throws IOException
	 */
	public static byte[] entityToByteArray(final HttpEntity entity, int maxSize) throws IOException {

		if (maxSize <= 0) {
			maxSize = Integer.MAX_VALUE;
		}
		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}
		InputStream instream = entity.getContent();
		if (instream == null) {
			return new byte[] {};
		}
		int i = (int) entity.getContentLength();
		if (entity.getContentLength() > maxSize) {
			i = maxSize;
		} else if (i < 0) {
			i = 4096;
		}
		ByteArrayBuffer buffer = new ByteArrayBuffer(i);
		try {
			byte[] tmp = new byte[4096];
			int l;
			while ((l = instream.read(tmp)) != -1 && buffer.length() <= maxSize) {
				buffer.append(tmp, 0, l);
			}
		} finally {
			instream.close();
		}
		return buffer.toByteArray();
	}

	/**
	 * 
	 * @param entity
	 * @param maxSize
	 * @param encodingType
	 * @return
	 * @throws IOException
	 */
	public static byte[] compressedEntityToByteArray(final HttpEntity entity, int maxSize, String encodingType) throws IOException {

		if (maxSize <= 0) {
			maxSize = Integer.MAX_VALUE;
		}

		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}

		InputStream instream = entity.getContent();
		if (instream == null) {
			return new byte[] {};
		}
		int i = (int) entity.getContentLength();
		if (entity.getContentLength() > maxSize) {
			i = maxSize;
		} else if (i < 0) {
			i = 4096;
		}

		byte[] bytes = new byte[] {};
		if (encodingType.equalsIgnoreCase("gzip")) {
			GZIPInputStream gzipInputStream = new GZIPInputStream(instream, i);
			bytes = compressedStreamToByteArray(gzipInputStream, i);

		} else if (encodingType.equalsIgnoreCase("deflate")) {
			InflaterInputStream inflateInputStream = new InflaterInputStream(instream);
			bytes = compressedStreamToByteArray(inflateInputStream, i);

		} else if (encodingType.equalsIgnoreCase("compress")) {

		}

		instream.close();
		return bytes;

	}

	private static byte[] compressedStreamToByteArray(final InputStream instream, int bufferSize) throws IOException {

		ByteArrayBuffer buffer = new ByteArrayBuffer(bufferSize);
		try {
			byte[] tmp = new byte[4096];
			int l;
			while ((l = instream.read(tmp)) != -1 && buffer.length() <= bufferSize) {
				buffer.append(tmp, 0, l);
			}
		} finally {
			instream.close();
		}
		return buffer.toByteArray();

	}

	/**
	 * 
	 * @param httpStatusCode
	 * @return
	 */
	public static boolean fileExists(int httpStatusCode) {

		boolean exists = false;
		if (((httpStatusCode >= 100) && (httpStatusCode < 300)) || (httpStatusCode == 304) || ((httpStatusCode >= 401) && (httpStatusCode <= 403))) {
			exists = true;
		}
		return exists;
	}

}
