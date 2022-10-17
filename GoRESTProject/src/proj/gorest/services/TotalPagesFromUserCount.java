package proj.gorest.services;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import proj.gorest.models.Data;
import proj.gorest.models.Model;
import proj.gorest.models.Users;

public class TotalPagesFromUserCount {
    public static int getPageCount() throws Exception{
        
        // base URL of GraphQL API on gorest.co.in
        String url = "https://gorest.co.in/public/v2/graphql";
        
       
        int page = 1;
        // Query to get count of users.
       
        String query = "{\"query\":\"query{users{totalCount}}\"}";

        GetHttpResponse res = new GetHttpResponse();
        HttpResponse response = res.callGraphQLService(url, query);
        String responseBody = EntityUtils.toString(response.getEntity()); // Fetches body from response

        Gson gson = new Gson();
        Model model = gson.fromJson(responseBody.toString(), Model.class);

        Data data = model.getData();
        Users users = data.getUsers();

        page = users.getTotalCount() / 10;
        
        return page;
        
    }

}
