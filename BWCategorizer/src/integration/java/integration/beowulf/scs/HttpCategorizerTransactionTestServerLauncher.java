package integration.beowulf.scs;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class HttpCategorizerTransactionTestServerLauncher {

	private RequestListenerThread requestListener = null;
	private File tempDir = null;

	static Logger logger = LoggerFactory.getLogger(HttpCategorizerTransactionTestServerLauncher.class);

	@BeforeTest(groups = "Scs_integration_test")
	public void startServer() throws Exception {

		PropertyConfigurator.configure("log4j.properties");

		tempDir = new File("temp");
		tempDir.mkdir();
		generateFile(tempDir.getAbsolutePath(), "index.html");

		requestListener = setUpServer(9888, tempDir.getAbsolutePath());

	}

	@AfterTest(groups = "Scs_integration_test")
	public void stopServer() throws IOException {

		requestListener.shutDownWorkerThreads();
		FileUtils.deleteDirectory(tempDir);

	}

	private void generateFile(String docRoot, String filename) throws IOException {

		String htmlContent = null;

		htmlContent = "<html><body><h1>" + "Content Generated</h1>" + "<br>" + "<pre> sample text </pre></body></html>";

		FileUtils.writeStringToFile(new File(docRoot + File.separator + filename), htmlContent);

	}

	private RequestListenerThread setUpServer(int port, String docRoot) {

		RequestListenerThread requestListenerThread = null;
		try {
			requestListenerThread = new RequestListenerThread(port, docRoot);
			requestListenerThread.setDaemon(false);
			requestListenerThread.start();
			// return t;
		} catch (IOException e) {
			logger.error("I/O error initialising connection thread: " + e.getMessage());
		}
		return requestListenerThread;

	}

	private static class HttpFileHandler implements HttpRequestHandler {

		private final String docRoot;

		Logger logger = LoggerFactory.getLogger(HttpFileHandler.class);

		public HttpFileHandler(final String docRoot) {

			super();
			this.docRoot = docRoot;
		}

		public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {

			String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
			if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
				throw new MethodNotSupportedException(method + " method not supported");
			}
			String target = request.getRequestLine().getUri();

			if (request instanceof HttpEntityEnclosingRequest) {
				HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
				byte[] entityContent = EntityUtils.toByteArray(entity);
				logger.info("Incoming entity enclosed request content (bytes): {}", entityContent.length);
			}

			if (target.endsWith("/dir1/")) {
				handleDirTest(request, response);
			} else if (target.endsWith("/dir1/subdir/")) {
				handleDirTest(request, response);
			} else if (target.endsWith("/dir1/subdir/somefile.txt")) {
				handleDirTest(request, response);
			} else {

				final File file = new File(this.docRoot, URLDecoder.decode(target, "UTF-8"));
				if (!file.exists()) {

					response.setStatusCode(HttpStatus.SC_NOT_FOUND);
					EntityTemplate body = new EntityTemplate(new ContentProducer() {

						public void writeTo(final OutputStream outstream) throws IOException {

							OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8");
							writer.write("<html><body><h1>");
							writer.write("File ");
							writer.write(file.getPath());
							writer.write(" not found");
							writer.write("</h1></body></html>");
							writer.flush();
						}

					});
					body.setContentType("text/html; charset=UTF-8");
					response.setEntity(body);
					logger.debug("File " + file.getPath() + " not found");

				} else if (!file.canRead() || file.isDirectory()) {

					response.setStatusCode(HttpStatus.SC_FORBIDDEN);
					EntityTemplate body = new EntityTemplate(new ContentProducer() {

						public void writeTo(final OutputStream outstream) throws IOException {

							OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8");
							writer.write("<html><body><h1>");
							writer.write("Access denied");
							writer.write("</h1></body></html>");
							writer.flush();
						}

					});
					body.setContentType("text/html; charset=UTF-8");
					response.setEntity(body);
					logger.debug("Cannot read file " + file.getPath());

				} else {

					response.setStatusCode(HttpStatus.SC_OK);
					FileEntity body = new FileEntity(file, ContentType.TEXT_HTML);
					response.setEntity(body);
					logger.debug("Serving file " + file.getPath());

				}
			}
		}

		private void handleDirTest(HttpRequest request, HttpResponse response) {

			EntityTemplate body = new EntityTemplate(new ContentProducer() {

				public void writeTo(final OutputStream outstream) throws IOException {

					OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8");
					writer.write("<html><body><h1>");
					writer.write("Dir Test OK");
					writer.write("</h1></body></html>");
					writer.flush();
				}

			});
			body.setContentType("text/html; charset=UTF-8");
			response.setEntity(body);

		}

	}

	private static class RequestListenerThread implements Runnable {

		private final ServerSocket serversocket;
		private final HttpParams params;
		private final HttpService httpService;
		private Thread thread;
		private List<Thread> workerThreads;

		Logger logger = LoggerFactory.getLogger(RequestListenerThread.class);

		public RequestListenerThread(int port, final String docroot) throws IOException {

			workerThreads = new ArrayList<Thread>();
			this.serversocket = new ServerSocket(port);
			this.thread = new Thread(this, "HTTPServerThread-" + this.serversocket.getLocalPort());
			this.params = new BasicHttpParams();
			this.params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000).setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
					.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
					.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true).setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");

			// Set up the HTTP protocol processor
			BasicHttpProcessor httpproc = new BasicHttpProcessor();
			httpproc.addInterceptor(new ResponseDate());
			httpproc.addInterceptor(new ResponseServer());
			httpproc.addInterceptor(new ResponseContent());
			httpproc.addInterceptor(new ResponseConnControl());

			// Set up request handlers
			HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
			registry.register("*", new HttpFileHandler(docroot));

			// Set up the HTTP service
			this.httpService = new HttpService(httpproc, new DefaultConnectionReuseStrategy(), new DefaultHttpResponseFactory(), registry, params);
		}

		public void start() {

			thread.start();
		}

		public final String getName() {

			return thread.getName();
		}

		public void setDaemon(boolean on) {

			thread.setDaemon(on);
		}

		public int getLocalPort() {

			return this.serversocket.getLocalPort();
		}

		public void interupt() {

			try {
				logger.debug("Closing socket: " + this.serversocket.getLocalPort());
				this.serversocket.close();

			} catch (IOException e) {
				logger.error("IOException when HTTPServerThread was interrupted " + e.getMessage());
			}
			logger.debug("Interrupting " + thread.getName());
			thread.interrupt();
		}

		public void shutDownWorkerThreads() {

			for (Thread workerThread : workerThreads) {
				logger.debug("Shutting down workerThread: " + workerThread.getName());
				workerThread.interrupt();
			}

		}

		public void run() {

			logger.debug("Listening on port " + this.serversocket.getLocalPort());
			while (!Thread.interrupted()) {
				try {
					// Set up HTTP connection
					Socket socket = this.serversocket.accept();
					DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
					logger.debug("Incoming connection from " + socket.getInetAddress());
					conn.bind(socket, this.params);

					// Start worker thread
					Thread t = new WorkerThread(this.httpService, conn, "workerThread-" + socket.getInetAddress());
					workerThreads.add(t);
					t.setDaemon(true);
					t.start();
				} catch (InterruptedIOException ex) {
					break;
				} catch (IOException e) {
					logger.error("I/O error initialising connection thread: " + e.getMessage());
					break;
				}
			}
		}
	}

	private static class WorkerThread extends Thread {

		private final HttpService httpservice;
		private final HttpServerConnection conn;

		Logger logger = LoggerFactory.getLogger(WorkerThread.class);

		public WorkerThread(final HttpService httpservice, final HttpServerConnection conn, String threadName) {

			super(null, null, threadName);
			this.httpservice = httpservice;
			this.conn = conn;

		}

		public void run() {

			logger.debug("New connection thread");
			HttpContext context = new BasicHttpContext(null);
			try {
				while (!Thread.interrupted() && this.conn.isOpen()) {
					this.httpservice.handleRequest(this.conn, context);
				}
			} catch (ConnectionClosedException ex) {
				logger.error("Client closed connection");
			} catch (IOException ex) {
				logger.error("I/O error: " + ex.getMessage());
			} catch (HttpException ex) {
				logger.error("Unrecoverable HTTP protocol violation: " + ex.getMessage());
			} finally {
				try {
					this.conn.shutdown();
				} catch (IOException ignore) {
				}
			}
		}

	}

}
