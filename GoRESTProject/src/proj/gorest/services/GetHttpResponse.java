package proj.gorest.services;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class GetHttpResponse {
    public HttpResponse callGraphQLService(String url, String query) 
            throws URISyntaxException, IOException {
              HttpClient client = HttpClientBuilder.create().build();
              HttpPost httpPost = new HttpPost(url);
              
              StringEntity input = new StringEntity(query);
              Authorization auth = new Authorization();
              
              httpPost.setEntity(input);

              httpPost.addHeader("Authorization","Bearer " + auth.getToken());
              httpPost.addHeader("Content-Type", "application/json");
              httpPost.addHeader("Accept", "application/json");
              httpPost.addHeader("Cache-Control", "no-cache");


              return client.execute(httpPost);
          }
}
