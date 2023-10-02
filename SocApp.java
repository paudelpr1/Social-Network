/**
Date: 03/07/2022

Course: CSCI 3005 60360

Description: This program uses Breadth-first Search algorithm to find relationship between users in a social
             network graph. This program uses DiGraph.java class to model a directed graph to find the mutual 
             relationship in the social network. 

On my honor, I have neither given nor received unauthorized help while
completing this assignment.

Name: Prasansha Paudel
CWID: 30120811
*/

/*imports required java classes */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * SocApp class takes the x follows y relationship from the text file to find the relationship between users in a
 * social network graph. 
 * It uses Breadth-first search alsgorith and Digraph.java class to find the social connection between users. 
 */
public class SocApp {
    //creates an object graph to form adjacency list
    private DiGraph graph = new DiGraph();
    //declares and initializes arraylist to store the vertices of adjacency list. 
    private ArrayList<String> mutual = new ArrayList<>();

    /**global variables to store the shortest distance and path between vertices */
    int[] pathDistance;
    int[] pathName;
    
    /**
     * Reads the text file called filename and stores the values of filename in an arraylist wothour duplicates. 
     * creates adjacency list from the x follows y relationship from the text file and arraylist.
     * @param filename text file containing the mutual connection between vertices. 
     */
    public SocApp(String filename)
    {
        /**
         * try block reads filename and adds the vertices in arraylist.
         * it cretaes the adjacency list using graph object
         */
        try{
            Scanner in = new Scanner(new File(filename));
            String data = "";

            while(in.hasNextLine())
            { 
                data = in.nextLine();
                String[] input = data.split(" ");
                for(int i = 0; i < input.length; i++)
                {
                    //condition to check duplicates elements in arraylist.
                    if(!mutual.contains(input[i]))
                    {
                        mutual.add(input[i]); //adds the input vales inside arraylist.
                        graph.addVertex(mutual.indexOf(input[i])); //creates vertices of adjacency list 
                    }
                }
                graph.addEdge(mutual.indexOf(input[0]), mutual.indexOf(input[1])); //creates edges of the adjacency list
            } 
            in.close();
        }

        /*Catch block to prevent exception */
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }

    }

    /**
     * This method goes through the list of vertices to find the user followed by largest number of other users.
     * @return res user with the highest number of followers. 
     */
    public String mostPopular()
    {
        String res = "";
        //initializes hashmap to store the count for the followers. 
        HashMap<Integer, Integer> hmap = new HashMap<>();
        //iterate through each vertices 
        for(int i = 0; i < mutual.size(); i++)
        {
            //iterate through each edges
            for(int w: graph.getAdjacent(i))
            {
                //increses the count for the edges which is followed by more than 1 user.
                if(hmap.containsKey(w)) hmap.put(w,hmap.get(w)+1);

                //initializes the count for the edges followed by 1 user.
                else hmap.put(w,1);
            }
        }
        int max = 0;
        int index = -1;
        for(Map.Entry<Integer, Integer> it: hmap.entrySet())
        {
            //condition to check if the count of edges is gretaer than max.
            if(it.getValue() > max)
            {
                max = it.getValue(); //updates the max.
                index = it.getKey(); //gets the index with maximum count 
            }
        }
        //assigns the user from the recieved index with highest follower. 
        res = mutual.get(index);

        //returns string user with highest followers.
        return res;
    }

    /**
     * This method finds the user who follows largest number of other users
     * @return follower user who follows largest number of other users. 
     */
    public String topFollower(){
        //initializes maximum value.
        int max = -1;
        String follower = "";
        for(int i = 0; i < mutual.size(); i++)
        {
            //gets the number of edges of vertices
            int size = graph.getAdjacent(i).size();

            //compares the obtained size of edges and compares with maximum no. 
            if(size >= max) follower = mutual.get(i); //obatins the vertex with highest no. of edges
        }

        //returns string user who follows largest number of other users. 
        return follower;
    }

    /**
     * This method returns a boolena value for the user. 
     * This method considers a user an influencer if the user is followed by at least 40% of all users. 
     * @param user vertex to check if the user is an influencer.
     * @return true if the user is an influencer, false otherwise.
     */
    public boolean isInfluencer(String user)
    {
        //declares arraylist to store count of followers
        ArrayList<Integer> influencer = new ArrayList<>();
        for (String mut : mutual) {
            influencer.add(0); //assigns size and value to arraylist
        }
        int count = 0; 
        double calc = 0.0;

        //gets the index of user
        int index = mutual.indexOf(user);

        //loops through each vertices
        for(int i = 0; i < mutual.size(); i++)
        {
            //loops through each edges of the vertex
            for(int w: graph.getAdjacent(i))
            {
                // condition to check if the given obtain edge matches the given user
                if(w == index)
                {
                    //initialies the count of the edge at the given index
                    influencer.add(w, count++);
                }
            }
        }
   
        //iterates through the arraylist
        for(int i = 0; i < influencer.size(); i++)
        {
            //gets the count of followers and calculates the percentage
            calc = (influencer.get(i) * 100) / mutual.size();

            //returns true if the followers is at least 40%
            if(calc >= 40) return true;
        }
        //returns false if the user is not an influencer.
        return false;

    }

    /**
     * This method calcultes the number of symmetrical vertices in the adjacency list
     * @return reciprocal no. of symmetrical edges divided by total edges.
     */
    public double reciprocity()
    {
        double count = 0.0;
        double reciprocal = 0.0;

        //loops through each vertices
        for(int i = 0; i < mutual.size();i++)
        {
            //loops through each edges of the vertex
            for(int w: graph.getAdjacent(i))
            {
                //increases count if the obtained edges are symmetrical for both vertices.
                if(graph.getAdjacent(w).contains(i)) count++;

            }
        }
        //calculates the reciprocity. 
        reciprocal = (count / graph.edges());

        //returns reciprocity of the graph.
        return reciprocal;
    }

    /**
     * This method takes two users and find the number of edges and path between the vertices. 
     * this method uses breadth-first search to fing the shortest path and distance.
     * @param user1 fisrt vertex of the adjacency list
     * @param user2 second vertex of the adjacency list
     */
    public void BFS(String user1, String user2)
    {
        /*initializes queue data structure to store track the BFS */
        Queue<Integer> tracker = new LinkedList<>();

        //index of users
        int startIndex = mutual.indexOf(user1);
        int endIndex = mutual.indexOf(user2); 

        //declares the size of arrays that store distance and path to the size of global arraylist
        pathDistance  = new int[mutual.size()];
        pathName = new int[mutual.size()];

        //initializes the values of arrays to -1.
        Arrays.fill(pathDistance, -1);
        Arrays.fill(pathName, -1); 

        //initializes distance from the vertex to itself as 0.
        pathDistance[startIndex] = 0; 

        //adds the 1st vertex to queue
        tracker.add(startIndex);
        boolean found = false; 

        //loop until the condition is not matched
        while(!tracker.isEmpty() && !found)
        {
            //removes the element from the queue
            int u = tracker.remove();
            //goes to each edge of the vertex
            for(int w : graph.getAdjacent(u))
            {
                //if the egde is not added in the queue
                if(pathDistance[w] == -1)
                {
                    tracker.add(w); //adds the element to the queue
                    pathDistance[w] = pathDistance[u] + 1;  //increses the distance by 1
                    pathName[w] = u;  // stores the parent vertex of the given edge.

                    //condition to check if obtained vertex is the required vertex.
                    if(w == endIndex)
                        found = true;
                }
            }
        }

        
    }

    /**
     * This method uses the BFS method to calculate the smallest number of directed edged in the path.  
     * This method returns Integer.Max_VALUE if there is no edge between vertices
     * @param user1 The starting vertex
     * @param user2 The ending vertex
     * @return distance smallest number of directed edges in the path
     */
    public int distance(String user1, String user2)
    {
        int distance = -1;
        //gets the index of ending vertex.
        int index = mutual.indexOf(user2);
        //calls BFS method to calculate the distance between the vertices
        BFS(user1, user2);

        //if the value of array at the ending index is -1, 
        if(pathDistance[index] == -1)
        {
            // no path is found
            //return the maximum value(closet to infinity)
            distance = Integer.MAX_VALUE;
        }
        //if the condition do not match
        else {
            //returns the value of distance at the ending index. 
            distance = pathDistance[index];
        }

        //returns the smallest number of directed edges in the path from user1 to user2
        return distance;
    }

    /**
     * This method uses BFS method to return the string values of vertices in the shortest path from user1 to user2
     * This method return NONE if no path exists. 
     * @param user1 The starting vertex
     * @param user2 The ending vertex
     * @return path strings that forms the shortest path from user1 to user2
     */
    public String path(String user1, String user2)
    {
        String path = "";

        //Arraylist to store the index and string values of vertices 
        ArrayList<String> shortestPath = new ArrayList<>(); 
        ArrayList<Integer> pathArray = new ArrayList<>(); 

        ////gets the index of given vertices.
        int startIndex = mutual.indexOf(user1);
        int endIndex = mutual.indexOf(user2); 

        //calls the BFS method to calulate the shortest path. 
        BFS (user1, user2); 

        //adds the value of endIndex on the integer arraylist
        pathArray.add(endIndex);

        //iterate until the condition is met
        while(pathName[endIndex] != -1)
        {
            //adds the value of obtained index of verties from the pathName array
            pathArray.add(pathName[endIndex]);
            endIndex = pathName[endIndex];
        }

        //Condition to check if the last element in the pathArray arraylist is the startIndex of user1
        if(pathArray.get((pathArray.size())-1) == startIndex)
        {
            //iterates from the end of arraylist
            for(int i = pathArray.size()-1; i>= 0; i--)
            {
                //adds the vertex obtained to the String Arraylist
                shortestPath.add(mutual.get(pathArray.get(i)));
            }
        } 

        //Condition if there is no path between vertices
        else
        {
            shortestPath.add("NONE");
        }
        //formats and prints the value of arrylist in a string.
        path = shortestPath.toString().replace(", ", "|");

        //returns the String consisting of vertices in the shortest path from user1 to user2.
        return path;     
    }
    
    /**
     * This method prints the collections of users reachable from user.
     * @param user The starting user vertex.
     * @return output collection of users reachable from given vertex.
     */
    public Set<String> reachable(String user)
    {
        //Initializes hashset to store collection of users
        Set<String> output = new HashSet<>();
        
        //initializes queue to track the visited vertices.
        Queue<Integer> visited = new LinkedList<>();

        int startIndex;

        //adds the index of user inside the queue
        visited.add(mutual.indexOf(user));

        //iterates unitl the confition is matched
        while(!visited.isEmpty()){
            //removes the first elemnt from the queue.
            startIndex = visited.remove();
            //iterates through each edges at the given vertex
            for(int w : graph.getAdjacent(startIndex))
            {  
                 //removes duplicates 
                if(!output.contains(mutual.get(w)))
                {
                    //adds the string at the given index edge
                    output.add(mutual.get(w));
                    visited.add(w);
                }
            }
        }
        
        //removes the user itself from the collection.
        output.remove(user);

        //returns the collection of users reachable frim user. 
        return output;
    }    
 }
