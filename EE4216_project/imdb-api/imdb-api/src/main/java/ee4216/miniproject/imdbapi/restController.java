/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.miniproject.imdbapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author EdwardHe
 */

@RestController
@CrossOrigin
@RequestMapping(path="/api")
public class restController {
    //goblal variables for connection
    public static String url = "jdbc:mysql://localhost:3306/memo";
    public static String user = "root";
    public static String password = "199530";
    public static Connection con;
        
    //static method for connecting the MySQL server, only 1 connection in the whole process 
    static {
        try 
        {
            con = DriverManager.getConnection(restController.url, restController.user, restController.password);
        }   
        catch (SQLException ex) 
        {
            Logger.getLogger(restController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Logger for showing debugging messages
    Logger logger = Logger.getLogger("LoggingDemo");
    
    
    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
       response.setHeader("Access-Control-Allow-Origin", "*");
    }   
    
    //check the server status
    @CrossOrigin
    @GetMapping("/status")
    public String status(){
        return "working";
    }
    
    //return all the notes for testing
    @CrossOrigin
    @GetMapping(value = "/memo", produces = MediaType.APPLICATION_JSON_VALUE)
    public String create() throws SQLException, JsonProcessingException {
        List<memo> memo = this.getAll();
    
        //note object to json string
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(memo);
        
        return result;        
    }
    
    //return the notes from specific user with user id
    @CrossOrigin
    @GetMapping(value = "/memo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getuser(@PathVariable("id") String id) throws SQLException, JsonProcessingException {
        List<memo> memo = this.getUser(id);
        //note object to json string
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(memo);
        
        //logger.warning(result);
        
        return result;        
    }    
    
    //create a new note with specific user and contents
    @CrossOrigin
    @PostMapping("/memo")
    public String addNotes(@RequestBody String memoDetails) throws JsonProcessingException, SQLException 
    {
        logger.warning("Post request is: ");
        logger.warning(memoDetails);
        
        //JSON string to notes object
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> notes = mapper.readValue(memoDetails, new TypeReference<HashMap<String, Object>>() {}); 
        
        //Putting all informations to a single note object
        int index = (int)notes.get("memoID");
        String userID = (String)notes.get("userID");
        String content = (String)notes.get("content");
        String fontSize = (String)notes.get("fontSize");
        String color = (String)notes.get("color");
        String positionHeight = (String)notes.get("positionHeight");
        String positionWidth = (String)notes.get("positionWidth");
        String positionTop = (String)notes.get("positionTop");
        String positionLeft = (String)notes.get("positionLeft");          
        String zIndex = (String)notes.get("zIndex");
        int orderIndex = (int)notes.get("orderIndex");
        Boolean fix = (Boolean)notes.get("fix");
        
        memo memoList = new memo(index, userID, content, fontSize, color, positionHeight, positionWidth, positionTop, positionLeft, zIndex, orderIndex, fix);
        String result = createNote(memoList);
        
        logger.warning("Post result is: ");
        logger.warning(result);
        return result;
    }
    
    //update a specific note with memo ID (editing note function)
    @CrossOrigin
    @PutMapping("/memo/{id}")
    public String updateNotes(
        @PathVariable("id") String id, @RequestBody String memoDetails) throws JsonProcessingException, SQLException 
    {
        //JSON string to notes object
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> notes = mapper.readValue(memoDetails, new TypeReference<HashMap<String, Object>>() {});
        
        //Putting all informations to a single note object
        int index = parseInt(id);
        String userID = (String)notes.get("userID");
        String content = (String)notes.get("content");
        String fontSize = (String)notes.get("fontSize");
        String color = (String)notes.get("color");
        String positionHeight = (String)notes.get("positionHeight");
        String positionWidth = (String)notes.get("positionWidth");
        String positionTop = (String)notes.get("positionTop");
        String positionLeft = (String)notes.get("positionLeft");
        String zIndex = (String)notes.get("zIndex");
        int orderIndex = (int)notes.get("orderIndex");
        Boolean fix = (Boolean)notes.get("fix");
        
        memo memoList = new memo(index, userID, content, fontSize, color, positionHeight, positionWidth, positionTop, positionLeft, zIndex, orderIndex, fix);
        
        //Calling update function
        memo test = update(memoList);
        
        String result = mapper.writeValueAsString(test);
        return result;
    }

    //update all current notes in once (save all function)
    @CrossOrigin
    @PutMapping("/memo")
    public String updateNotes(
        @RequestBody String memo) throws JsonProcessingException, SQLException {
        logger.warning("Update request is: ");
        logger.warning(memo);
        
        ArrayList<String> object = new ArrayList<String>();
        object.add("");
        List<memo> memoList = new ArrayList<memo>();
        
        int j = 0;
        
        //Extracting the JSON array to a string array list
        memo = memo.replace("[", "");
        memo = memo.replace("]", "");
        int length = memo.length();
        for (int i = 0; i < length; i++)
        {
            char temp = memo.charAt(i);
            
            if (temp == ',' && i != length - 1 && memo.charAt(i+1) == '{'){
                j++;
                object.add("");
            } 
            else 
            {
                object.set(j, object.get(j) + temp);
            }
        }        
        
        //Converting the string array list to the notes object list
        ObjectMapper mapper = new ObjectMapper();
        for (int x = 0; x < object.size(); x++)
        {
            Map<String, Object> notes = mapper.readValue(object.get(x), new TypeReference<HashMap<String, Object>>() {});
            
            //Putting all informations to a single note object
            int index = (int)notes.get("memoID");
            String userID = (String)notes.get("userID");
            String content = (String)notes.get("content");
            String fontSize = (String)notes.get("fontSize");
            String color = (String)notes.get("color");
            String positionHeight = (String)notes.get("positionHeight");
            String positionWidth = (String)notes.get("positionWidth");
            String positionTop = (String)notes.get("positionTop");
            String positionLeft = (String)notes.get("positionLeft");        
            String zIndex = (String)notes.get("zIndex"); 
            int orderIndex = (int)notes.get("orderIndex");
            Boolean fix = (Boolean)notes.get("fix");
            memo temp2 = new memo(index, userID, content, fontSize, color, positionHeight, positionWidth, positionTop, positionLeft, zIndex, orderIndex, fix);
            
            memoList.add(temp2);
        }

        //update each notes in the object list
        for (int i = 0; i < memoList.size(); i++)
        {
            logger.warning(String.valueOf(i));
            update(memoList.get(i));
        }
        String result = memo;
        //logger.warning(result);
        return result;
    }
    
    //Delete a specific note with memo id
    @CrossOrigin
    @DeleteMapping("/memo/{id}")    
    public String deleteNotes(@PathVariable("id") String id) throws SQLException {
        //logger.warning(id);
        int index = parseInt(id);
        delete(index);
        return "deleted";
    }
    
    //get method for showing all notes for testing
    public List<memo> getAll() throws SQLException
    {
        Statement st = null;
        ResultSet rs = null;
        
        List<memo> memo = new ArrayList<memo>();
        
        st = con.createStatement();
        rs = st.executeQuery("SELECT * FROM memo");        
        
        while (rs.next())
        {
            int index = rs.getInt("memoID");
            String userID = rs.getString("userID");
            String content = rs.getString("content");
            String fontSize = rs.getString("fontSize");
            String color = rs.getString("color");
            String positionHeight = rs.getString("positionHeight");
            String positionWidth = rs.getString("positionWidth");
            String positionTop = rs.getString("positionTop");
            String positionLeft = rs.getString("positionLeft");
            String zIndex = (String)rs.getString("zIndex");
            int orderIndex = (int)rs.getInt("orderIndex");
            Boolean fix = (Boolean)rs.getBoolean("fix");
            
            memo memoList = new memo(index, userID, content, fontSize, color, positionHeight, positionWidth, positionTop, positionLeft, zIndex, orderIndex, fix); 
            memo.add(memoList);
        }
        return memo;
    }
    
    //get method for a specifice user (return all notes from the same user)
    public List<memo> getUser(String userID) throws SQLException
    {
        ResultSet rs = null;
        
        List<memo> memo = new ArrayList<memo>();
        
        //Finding the notes if it matches the user id
        PreparedStatement statement = con.prepareStatement("SELECT * FROM memo WHERE userID = ?");    
        statement.setString(1, userID);    
        rs = statement.executeQuery();        
        while (rs.next())
        {
            //add all information from the selected notes to memo list
            int index = rs.getInt("memoID");
            String content = rs.getString("content");
            String fontSize = rs.getString("fontSize");
            String color = rs.getString("color");
            String positionHeight = rs.getString("positionHeight");
            String positionWidth = rs.getString("positionWidth");
            String positionTop = rs.getString("positionTop");
            String positionLeft = rs.getString("positionLeft");
            String zIndex = (String)rs.getString("zIndex");
            int orderIndex = (int)rs.getInt("orderIndex");
            Boolean fix = (Boolean)rs.getBoolean("fix");
            
            memo memoList = new memo(index, userID, content, fontSize, color, positionHeight, positionWidth, positionTop, positionLeft, zIndex, orderIndex, fix); 
            memo.add(memoList);
        }
        return memo;
    }
    
    //create a new note
    public String createNote(memo newMemo) throws SQLException, JsonProcessingException
    {
        String message = "";
        Boolean checkFirst = true;
        Statement st = null;
        ResultSet rs = null;

        ObjectMapper mapper = new ObjectMapper();
        
        st = con.createStatement();
        int maxIndex = 0; 

        //insert a new note object to the database
        Statement st2 = con.createStatement();
        PreparedStatement statement2 = con.prepareStatement("INSERT INTO memo (memoID, userID, content, "
                + "fontSize, color, positionHeight, positionWidth, positionTop, positionLeft, zIndex, orderIndex, fix)" 
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
        statement2.setInt(1, maxIndex);
        statement2.setString(2, newMemo.getUserID());
        statement2.setString(3, newMemo.getContent());
        statement2.setString(4, newMemo.getFont());
        statement2.setString(5, newMemo.getColor());
        statement2.setString(6, newMemo.getHeight());
        statement2.setString(7, newMemo.getWidth());
        statement2.setString(8, newMemo.getTop());
        statement2.setString(9, newMemo.getLeft());
        statement2.setString(10, newMemo.getzIndex());
        statement2.setInt(11, newMemo.getOrderIndex());
        statement2.setBoolean(12, newMemo.getFix());
       
        statement2.executeUpdate();
        
        PreparedStatement statement3 = con.prepareStatement("SELECT * FROM memo WHERE userID = ? AND content = ? AND positionHeight = ? AND positionWidth = ? AND positionTop = ? AND positionLeft = ?");
        statement3.setString(1, newMemo.getUserID());
        statement3.setString(2, newMemo.getContent());
        statement3.setString(3, newMemo.getHeight());
        statement3.setString(4, newMemo.getWidth());
        statement3.setString(5, newMemo.getTop());
        statement3.setString(6, newMemo.getLeft());
        
        rs = statement3.executeQuery();
        
        if (rs.next())
        {
            
            int index = rs.getInt("memoID");
            String userID = rs.getString("userID");
            String content = rs.getString("content");
            String fontSize = rs.getString("fontSize");
            String color = rs.getString("color");
            String positionHeight = rs.getString("positionHeight");
            String positionWidth = rs.getString("positionWidth");
            String positionTop = rs.getString("positionTop");
            String positionLeft = rs.getString("positionLeft");
            String zIndex = rs.getString("zIndex");
            int orderIndex = rs.getInt("orderIndex");
            Boolean fix = rs.getBoolean("fix");
            
            memo test = new memo(index, userID, content, fontSize, color, positionHeight, positionWidth, positionTop, positionLeft, zIndex, orderIndex, fix); 

            message = mapper.writeValueAsString(test); 
            
            
        }
        else
        {
        logger.warning("Create Note cannot return memo id.");
        }
        
        
        return message;
    }
 
    //update a single note
    public memo update(memo memoList) throws SQLException
    {
        Statement st = null;
        ResultSet rs = null;
        memo test = null;
        

        
        //update a note with all informations
        st = con.createStatement();
        PreparedStatement statement = con.prepareStatement("UPDATE memo" +
" SET userID = ?, content = ?, fontSize = ?, color = ?, positionHeight = ?, positionWidth = ?, positionTop = ?, positionLeft = ?, zIndex = ?, orderIndex = ?, fix = ?" 
                + " WHERE memoID = ?"); 
        
        statement.setString(1, memoList.getUserID());
        statement.setString(2, memoList.getContent());
        statement.setString(3, memoList.getFont());
        statement.setString(4, memoList.getColor());
        statement.setString(5, memoList.getHeight());
        statement.setString(6, memoList.getWidth());        
        statement.setString(7, memoList.getTop());        
        statement.setString(8, memoList.getLeft());        
        statement.setString(9, memoList.getzIndex());        
        statement.setInt(10, memoList.getOrderIndex());      
        statement.setBoolean(11, memoList.getFix());
        statement.setInt(12, memoList.getMemoID());

        
        logger.warning("Update SQL is: ");
        logger.warning(statement.toString());
        
        statement.executeUpdate(); 
        
        
        //find the 
        Statement st2 = con.createStatement();
        PreparedStatement statement2 = con.prepareStatement("SELECT * FROM memo WHERE memoID = ?");
        statement2.setInt(1, memoList.getMemoID());
        rs = statement2.executeQuery(); 
        
        while (rs.next())
        {
            int index = rs.getInt("memoID");
            String userID = rs.getString("userID");
            String content = rs.getString("content");
            String fontSize = rs.getString("fontSize");
            String color = rs.getString("color");
            String positionHeight = rs.getString("positionHeight");
            String positionWidth = rs.getString("positionWidth");
            String positionTop = rs.getString("positionTop");
            String positionLeft = rs.getString("positionLeft");
            String zIndex = rs.getString("zIndex");
            int orderIndex = rs.getInt("orderIndex");
            Boolean fix = rs.getBoolean("fix");
            test = new memo(index, userID, content, fontSize, color, positionHeight, positionWidth, positionTop, positionLeft, zIndex, orderIndex, fix); 

        }
        return test;
    }
    
    public void delete(int index) throws SQLException
    {
        Statement st = null;
        ResultSet rs = null;
        memo test = null;

        st = con.createStatement();
        PreparedStatement statement = con.prepareStatement("DELETE FROM memo WHERE memoID = ?"); 
        statement.setInt(1, index);

        statement.executeUpdate(); 
    }
    
    
//--------------------------------------------------------------------------------------------------------------------------------------------    

    //login part
    
    //register function for user (post method)
    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody String userDetails) throws JsonProcessingException, SQLException {
        
        logger.warning("Register request: ");
        logger.warning(userDetails);
        ResponseEntity<String> message;
        
        ObjectMapper mapper = new ObjectMapper();
        //JSON string to Java Object
        Map<String, Object> notes = mapper.readValue(userDetails, new TypeReference<HashMap<String, Object>>() {}); 
        
        String username = (String)notes.get("username");
        String password = (String)notes.get("password");
        
        user newUser = new user(username, password);
        
        message = createUser(newUser);

        return message;
    }
    
    //login function for user (post method)
    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<String> loginCheck(@RequestBody String loginDetails) throws JsonProcessingException, SQLException {
        ResponseEntity<String> message;
        
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> notes = mapper.readValue(loginDetails, new TypeReference<HashMap<String, Object>>() {}); 
        
        String username = (String)notes.get("username");
        String password = (String)notes.get("password");  
        
        user login = new user(username, password);
        
        message = login(login);
        
        return message;
    }
    
    //create new user in database
    public ResponseEntity<String> createUser(user newUser) throws SQLException
    {
        String message = "";
        Statement st = null;
        ResultSet rs = null;
        st = con.createStatement();
        
        //check if the username is already exists
        PreparedStatement statement = con.prepareStatement("SELECT username AS duplicate FROM user WHERE username = ?");
        statement.setString(1, newUser.getUsername());
        rs = statement.executeQuery();
        
        if (rs.next())
        {
            message = "{\"message\": \"The username is already exists.\"}";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST); 
        }
        else 
        {
            //insert a new user record to database
            PreparedStatement statement3 = con.prepareStatement("INSERT INTO user (username, password)" +
" VALUES (?,?)");
        
            statement3.setString(1, newUser.getUsername());
            statement3.setString(2, newUser.getPassword());
            statement3.executeUpdate(); 
        
            message = "{\"message\": \"New account registered.\" }";
        
            PreparedStatement statement4 = con.prepareStatement("SELECT * FROM user WHERE username = ?");
            statement4.setString(1, newUser.getUsername());
            rs = statement4.executeQuery();
        
            if (rs.next())
            {
                message = "{\"userID\": \"" + rs.getString("userID") + "\", \"message\": \"Register successful.\" }";
                return new ResponseEntity<>(message, HttpStatus.OK); 
            }
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
    
    //login check with database
    @ResponseBody
    public ResponseEntity<String> login(user loginDetails) throws SQLException
    {
        String message = "";
        Statement st = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        st = con.createStatement();        
        
        //check the username and password is match with the database
        PreparedStatement statement = con.prepareStatement("SELECT username AS correctUsername FROM user WHERE username = ? AND password = ?");
        statement.setString(1, loginDetails.getUsername());
        statement.setString(2, loginDetails.getPassword());
        rs = statement.executeQuery();
        if (rs.next())
        {
            PreparedStatement statement2 = con.prepareStatement("SELECT * FROM user WHERE username = ?");
            statement2.setString(1, loginDetails.getUsername());
            rs2 = statement2.executeQuery();
        
            if (rs2.next())
            {
                message = "{\"userID\": \"" + rs2.getString("userID") + "\", \"message\": \"Login successful.\" }";
                return new ResponseEntity<>(message, HttpStatus.OK); 
            }
        }
        else
        {
            message = "{\"message\": \"Incorrect username or password.\"}";
           return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST); 
        }
        //return message
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST); 
    }
}
