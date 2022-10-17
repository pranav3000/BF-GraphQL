package proj.gorest;

import java.util.Collections;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;

import proj.gorest.models.Data;
import proj.gorest.models.Model;
import proj.gorest.models.Node;
import proj.gorest.models.PageInfo;
import proj.gorest.models.Users;
import proj.gorest.services.GetHttpResponse;
import proj.gorest.services.TotalPagesFromUserCount;



public class Program {
    private static final Logger LOGGER = Logger.getLogger(Program.class.getName());
    private static String logFilePath = "/Users/Pveldurthy/desktop/GRAPHQL.log"; //ADD LOG FILE PATH HERE
    
    public static void main(String[] args) throws Exception {
        FileHandler fh;   
        fh = new FileHandler(logFilePath);   
        LOGGER.addHandler(fh); 
        
        // base URL of GraphQL API on gorest.co.in
        String url = "https://gorest.co.in/public/v2/graphql";
        
        //int totalPages  = ParseTotalPages.getPageCount();
        int totalPages  = TotalPagesFromUserCount.getPageCount();
        
        //1. Retrieve page 3 of the list of all users
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
        
        // List of Node contains Users on this page.
        // Each node represents one user
        List<Node> nodes = users.getNodes();
        LOGGER.log(Level.INFO, "Total nodes on page 3 = {0}", nodes.size());
        
        
        // List all users in log
        int userNo = 1;
        for(Node u:nodes) {
            String logStr = "User no " + userNo + " on page " + page + " is: " + u.toString();
            LOGGER.log(Level.INFO, logStr);
            userNo++;
        }
        
        //-----------------------------------//
        
        //2. Using a logger, log the total number of pages from the previous request
        LOGGER.log(Level.INFO, "log the total number of pages = {0}", totalPages);
        
        //-----------------------------------//
               
        
        //Implemented the comparator in Node class
        //so it can compare by names and sort.
        
        //3. Sort the retrieved user list by name. - add users names
        LOGGER.log(Level.INFO, "Sort the retrieved user list by name");
        Collections.sort(nodes);
        
       //-----------------------------------//
        
       //4. After sorting, log the name of the last user 
       //List sorted user names in log
        userNo = 1;
        for(Node u:nodes) {
            String logStr = "After sorting, User no " + userNo + " on page " + page + " is: " + u.getName();
            LOGGER.log(Level.INFO, logStr);
            userNo++;
        }

        Node lastUser = nodes.get(nodes.size() - 1);
        LOGGER.log(Level.INFO, "Last user: {0}", lastUser.getName());

        //-----------------------------------//
        
        //5. Update that user's name to a new value and use the correct HTTP method to save it - mutation/ update user
        LOGGER.log(Level.INFO, "Updating user name to new value");
        Integer lastUserID = lastUser.getId();
        String newName = "Harry Potter";
        
        query = "{\"query\":\"mutation{updateUser(input: {id: "+lastUserID.toString()+" name: \\\""+newName+"\\\"}) {user{id name}}}\"}";
        response = res.callGraphQLService(url, query);
        responseBody = EntityUtils.toString(response.getEntity());
        LOGGER.log(Level.INFO, responseBody.toString());
        
        //-----------------------------------//
        
        //6. Delete that user
        LOGGER.log(Level.INFO, "Deleting User");
        query = "{\"query\":\"mutation{deleteUser(input: {id: "+lastUserID.toString()+"}){user {id name email gender status}}}\"}";
        response = res.callGraphQLService(url, query);
        responseBody = EntityUtils.toString(response.getEntity());
        LOGGER.log(Level.INFO, responseBody.toString());
        
        
        //-----------------------------------//
        
        //7. Attempt to retrieve a nonexistent user with ID 5555. Log the resulting HTTP response code. - will return NOT_FOUND
        LOGGER.log(Level.INFO, "Attempting to retrieve 5555");
        Integer invalidUserID = 55555;
        query = "{\"query\":\"query{user(id: "+invalidUserID.toString()+") { id name email gender status }}\"}";
        response = res.callGraphQLService(url, query);
        responseBody = EntityUtils.toString(response.getEntity());
        LOGGER.log(Level.INFO, responseBody.toString());        
    }

}
