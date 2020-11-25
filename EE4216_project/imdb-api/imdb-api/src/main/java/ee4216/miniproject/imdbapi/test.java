/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.miniproject.imdbapi;

import static ee4216.miniproject.imdbapi.restController.con;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 *
 * @author EdwardHe
 */
public class test {
    /*
    public static void main(String[] args) throws SQLException {
        System.out.println("Hello World");
	ArrayList<String> object = new ArrayList<String>();
        object.add("");
        String memo = "[{\"memoID\":1,\"userID\":\"ccc\",\"content\":\"test2\",\"color\":\"test2\",\"top\":0,\"left\":0,\"font\":\"test2\",\"height\":1,\"width\":0},{\"memoID\":1,\"userID\":\"ccc\",\"content\":\"test2\",\"color\":\"test2\",\"top\":0,\"left\":0,\"font\":\"test2\",\"height\":1,\"width\":0}]";
        int j = 0;
        
        memo = memo.replace("[", "");
        memo = memo.replace("]", "");
        int length = memo.length();
        for (int i = 0; i < length; i++)
        {
            char temp = memo.charAt(i);
            System.out.println(temp);
            if (temp == ',' && i != length - 1 && memo.charAt(i+1) == '{'){
                j++;
                object.add("");
            } else {
                
                object.set(j, object.get(j) + temp);
            }
        }
        
        for (int k = 0; k < object.size(); k++)
        {
            System.out.println(object.get(k));
        }
        
        Logger logger = Logger.getLogger("LoggingDemo");
        
        PreparedStatement statement = prepareStatement("SELECT * FROM memo WHERE userID = ?"); 
        
        System.out.println(statement.toString());
        
        //logger.warning((Supplier<String>) statement);
        
        //statement.executeUpdate(); 
    }
    */
}
