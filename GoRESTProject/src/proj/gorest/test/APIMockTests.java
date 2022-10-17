package proj.gorest.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import com.google.gson.Gson;

import proj.gorest.models.Data;
import proj.gorest.models.Model;
import proj.gorest.models.Node;
import proj.gorest.models.PageInfo;
import proj.gorest.models.Users;
import proj.gorest.services.GetHttpResponse;

public class APIMockTests {
    String url = "https://gorest.co.in/public/v2/graphql";
    int page = 1;
    // Initial query to get first page of users
    String queryStr = "{\"query\":\"query{users";
    String queryAfterPage = ""; // will be used later on to specify exact page to fetch
    String queryData = "{pageInfo {endCursor startCursor hasNextPage hasPreviousPage} totalCount nodes {id name email gender status}}}\"}";
    String query = queryStr + queryAfterPage + queryData;

    @SuppressWarnings("deprecation")
    @Test
    public void TestAPIConnection() throws ClientProtocolException, IOException, Exception {

        GetHttpResponse res = new GetHttpResponse();
        HttpResponse response = res.callGraphQLService(url, query);

        // Assert that Connected to the API and Performed Query successfully
        assertThat(response.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void RetrievedPage3() throws ClientProtocolException, IOException, Exception {

        GetHttpResponse res = new GetHttpResponse();
        HttpResponse response = res.callGraphQLService(url, query);
        String responseBody = EntityUtils.toString(response.getEntity()); // Fetches body from response

        Gson gson = new Gson();
        Model model = gson.fromJson(responseBody.toString(), Model.class);

        Data data = model.getData();
        Users users = data.getUsers();
        PageInfo pageInfo = users.getPageInfo();

        boolean hasNextPage = pageInfo.getHasNextPage(); // true if more pages available

        // we continue past 2 pages to get third
        while (page <= 2 & hasNextPage == true) {

            String endCursor = pageInfo.getEndCursor(); // end cursor of this page.

            queryAfterPage = "(after:\\\"" + endCursor + "\\\")";
            query = queryStr + queryAfterPage + queryData;
            

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

        List<Node> nodes = users.getNodes();
        Node firstUser = nodes.get(0);

        // Assert that first user's name is not empty
        Assert.assertNotEquals("", firstUser.getName());

    }

    @Test
    public void TotalNumberOfUsersInPage3() throws ClientProtocolException, IOException, Exception {

        GetHttpResponse res = new GetHttpResponse();
        HttpResponse response = res.callGraphQLService(url, query);
        String responseBody = EntityUtils.toString(response.getEntity()); // Fetches body from response

        Gson gson = new Gson();
        Model model = gson.fromJson(responseBody.toString(), Model.class);

        Data data = model.getData();
        Users users = data.getUsers();
        PageInfo pageInfo = users.getPageInfo();

        boolean hasNextPage = pageInfo.getHasNextPage(); // true if more pages available

        // we continue past 2 pages to get third
        while (page <= 2 & hasNextPage == true) {

            String endCursor = pageInfo.getEndCursor(); // end cursor of this page.

            queryAfterPage = "(after:\\\"" + endCursor + "\\\")";
            query = queryStr + queryAfterPage + queryData;

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

        List<Node> nodes = users.getNodes();

        // Assert that there are 10 users on page three
        Assert.assertEquals(nodes.size(), 10);

    }

    @Test
    public void ValidateLastUserOnPage3() throws ClientProtocolException, IOException, Exception {

        GetHttpResponse res = new GetHttpResponse();
        HttpResponse response = res.callGraphQLService(url, query);
        String responseBody = EntityUtils.toString(response.getEntity()); // Fetches body from response

        Gson gson = new Gson();
        Model model = gson.fromJson(responseBody.toString(), Model.class);

        Data data = model.getData();
        Users users = data.getUsers();
        PageInfo pageInfo = users.getPageInfo();

        boolean hasNextPage = pageInfo.getHasNextPage(); // true if more pages available

        // we continue past 2 pages to get third
        while (page <= 2 & hasNextPage == true) {

            String endCursor = pageInfo.getEndCursor(); // end cursor of this page.

            queryAfterPage = "(after:\\\"" + endCursor + "\\\")";
            query = queryStr + queryAfterPage + queryData;

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

        List<Node> nodes = users.getNodes();

        Collections.sort(nodes);

        Node lastUser = nodes.get(nodes.size() - 1);

        // After sorting Assert that last user's name is not empty
        Assert.assertNotEquals("", lastUser.getName());

    }

    @SuppressWarnings({ "deprecation" })
    @Test
    public void UpdateUserName() throws ClientProtocolException, IOException, Exception {

        GetHttpResponse res = new GetHttpResponse();
        HttpResponse response = res.callGraphQLService(url, query);
        String responseBody = EntityUtils.toString(response.getEntity()); // Fetches body from response

        Gson gson = new Gson();
        Model model = gson.fromJson(responseBody.toString(), Model.class);

        Data data = model.getData();
        Users users = data.getUsers();
        PageInfo pageInfo = users.getPageInfo();

        boolean hasNextPage = pageInfo.getHasNextPage(); // true if more pages available

        // we continue past 2 pages to get third
        while (page <= 2 & hasNextPage == true) {

            String endCursor = pageInfo.getEndCursor(); // end cursor of this page.

            queryAfterPage = "(after:\\\"" + endCursor + "\\\")";
            query = queryStr + queryAfterPage + queryData;

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

        List<Node> nodes = users.getNodes();

        Collections.sort(nodes);

        Node lastUser = nodes.get(nodes.size() - 1);

        Integer lastUserID = lastUser.getId();
        String newName = "Harry Potter";

        // Update last user's name to a new value & log.
        query = "{\"query\":\"mutation{updateUser(input: {id: " + lastUserID.toString() + " name: \\\"" + newName
                + "\\\"}) {user{id name}}}\"}";
        response = res.callGraphQLService(url, query);
        responseBody = EntityUtils.toString(response.getEntity());
        // LOGGER.log(Level.INFO, responseBody.toString());

        // Assert that response contains update user message
        assertThat(responseBody, containsString("updateUser"));

    }

    @SuppressWarnings({ "deprecation" })
    @Test
    public void DeleteUpdatedUser() throws ClientProtocolException, IOException, Exception {

        GetHttpResponse res = new GetHttpResponse();
        HttpResponse response = res.callGraphQLService(url, query);
        String responseBody = EntityUtils.toString(response.getEntity()); // Fetches body from response

        Gson gson = new Gson();
        Model model = gson.fromJson(responseBody.toString(), Model.class);

        Data data = model.getData();
        Users users = data.getUsers();
        PageInfo pageInfo = users.getPageInfo();

        boolean hasNextPage = pageInfo.getHasNextPage(); // true if more pages available

        // we continue past 2 pages to get third
        while (page <= 2 & hasNextPage == true) {

            String endCursor = pageInfo.getEndCursor(); // end cursor of this page.

            queryAfterPage = "(after:\\\"" + endCursor + "\\\")";
            query = queryStr + queryAfterPage + queryData;

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

        List<Node> nodes = users.getNodes();

        Collections.sort(nodes);

        Node lastUser = nodes.get(nodes.size() - 1);

        Integer lastUserID = lastUser.getId();
        String newName = "Harry Potter";

        // Update last user's name to a new value & log.
        query = "{\"query\":\"mutation{updateUser(input: {id: " + lastUserID.toString() + " name: \\\"" + newName
                + "\\\"}) {user{id name}}}\"}";
        response = res.callGraphQLService(url, query);
        responseBody = EntityUtils.toString(response.getEntity());

        // Delete last user & log
        query = "{\"query\":\"mutation{deleteUser(input: {id: " + lastUserID.toString()
                + "}){user {id name email gender status}}}\"}";
        response = res.callGraphQLService(url, query);
        responseBody = EntityUtils.toString(response.getEntity());

        // Assert that response contains update user message
        assertThat(responseBody, containsString("deleteUser"));

    }

    @SuppressWarnings({ "deprecation" })
    @Test
    public void RetrieveNonExistentUser() throws ClientProtocolException, IOException, Exception {

        GetHttpResponse res = new GetHttpResponse();

        // Get user with invalid id & log.
        Integer invalidUserID = 5000;
        query = "{\"query\":\"query{user(id: " + invalidUserID.toString() + ") { id name email gender status }}\"}";
        HttpResponse response = res.callGraphQLService(url, query);
        String responseBody = EntityUtils.toString(response.getEntity());

        // Assert that response contains NOT_FOUND for status code
        assertThat(responseBody, containsString("NOT_FOUND"));

    }

}
