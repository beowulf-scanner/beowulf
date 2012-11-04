package integration.beowulf.smf.scan.resources;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NewScanResourceTest {

	public DefaultHttpClient getHttpClient() {

		HttpParams params = new BasicHttpParams();
		params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, Boolean.FALSE);
		DefaultHttpClient httpClient = new DefaultHttpClient(params);

		return httpClient;

	}

	@Test(groups = "Smf_integration_test")
	public void testPostScanRequest() throws ClientProtocolException, IOException {

		DefaultHttpClient httpClient = getHttpClient();

		httpClient.getCookieStore().clear();
		HttpPost httppost = new HttpPost("http://localhost:13000/api/scan/new");
		FileEntity entity = new FileEntity(new File("src/integration/resources/scan_profile_for_integration.xml"));
		entity.setContentType(ContentType.APPLICATION_XML.getMimeType());
		httppost.setEntity(entity);

		HttpResponse responseForPost = httpClient.execute(httppost);
		Assert.assertEquals(responseForPost.getStatusLine().getStatusCode(), Status.CREATED.getStatusCode());

	}

}
