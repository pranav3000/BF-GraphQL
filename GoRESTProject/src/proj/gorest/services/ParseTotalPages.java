package proj.gorest.services;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import proj.gorest.models.Data;
import proj.gorest.models.Model;
import proj.gorest.models.PageInfo;
import proj.gorest.models.Users;

public class ParseTotalPages {
    public static int getPageCount() throws Exception{
        
        // base URL of GraphQL API on gorest.co.in
        String url = "https://gorest.co.in/public/v2/graphql";
        
       
        int page = 1;
        // Initial query to get first page of users
        String queryStr = "{\"query\":\"query{users";
        String queryAfterPage = ""; // will be used later on to specify exact page to fetch
        String queryData = "{pageInfo {endCursor startCursor hasNextPage hasPreviousPage} totalCount nodes {id name email gender status}}}\"}";
        String query = queryStr + queryAfterPage + queryData;

        GetHttpResponse res = new GetHttpResponse();
        HttpResponse response = res.callGraphQLService(url, query);
        String responseBody = EntityUtils.toString(response.getEntity()); // Fetches body from response

        Gson gson = new Gson();
        Model model = gson.fromJson(responseBody.toString(), Model.class);

        Data data = model.getData();
        Users users = data.getUsers();
        PageInfo pageInfo = users.getPageInfo();

        boolean hasNextPage = pageInfo.getHasNextPage(); // true if more pages available

        
        // we continue parsing pages until current page has no next page
        while (hasNextPage == true) {

            String endCursor = pageInfo.getEndCursor(); // end cursor of this page.

            queryAfterPage = "(after:\\\"" + endCursor + "\\\")";
            query = queryStr + queryAfterPage + queryData;
            System.out.println(query);

            response = res.callGraphQLService(url, query);
            responseBody = EntityUtils.toString(response.getEntity()); // Fetches body from response

            // Update data with next page users's data
            model = null;
            model = gson.fromJson(responseBody.toString(), Model.class);
            data = model.getData();
            users = data.getUsers();
            pageInfo = users.getPageInfo();
            hasNextPage = pageInfo.getHasNextPage();
            page++;
        }
 
        return page;
        
    }

}
