/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.miniproject.imdbapi;

/**
 *
 * @author EdwardHe
 */
public class user {
    private String username;
    private String password;

    public user(String name, String pw) {
        this.username = name;
        this.password = pw;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }     
    
    public void setUsername(String username) {
        this.username = username;
    }   
    
    public void setPassword(String password) {
        this.password = password;
    }     
       
}
