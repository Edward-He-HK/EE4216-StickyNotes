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
public class memo {
    private int memoID;
    private String userID;
    private String content;
    private String fontSize;
    private String color;
    private String positionHeight;
    private String positionWidth;
    private String positionTop;
    private String positionLeft;
    private String zIndex;
    private int orderIndex;
    private Boolean fix;

    public memo(int currentIndex, String userid, String currentContent, String font, 
            String currentColor, String height, String width, String top, String left, String z, int order, Boolean fixIndex) {
        
        this.memoID = currentIndex;
        this.userID = userid;
        this.content = currentContent;
        this.fontSize = font;
        this.color = currentColor;
        this.positionHeight = height;
        this.positionWidth = width;
        this.positionTop = top;
        this.positionLeft = left;
        this.zIndex = z;
        this.orderIndex = order;
        this.fix = fixIndex;
    }
    
    public int getMemoID() {
        return memoID;
    }
    
    public String getUserID() {
        return userID;
    }
    
    public String getContent() {
        return content;
    }    
    
    public String getFont() {
        return fontSize;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getHeight() {
        return positionHeight;
    }    

    public String getWidth() {
        return positionWidth;
    } 
    
    public String getTop() {
        return positionTop;
    } 
    
    public String getLeft() {
        return positionLeft;
    } 
    
    public String getzIndex() {
        return zIndex;
    }
    
    public int getOrderIndex() {
        return orderIndex;
    }

    public Boolean getFix() {
        return fix;
    }        

    public void setMemoID(int memoID) {
        this.memoID = memoID;
    }    
    
    public void setUserID(String userID) {
        this.userID = userID;
    }     
    
    public void setContent(String content) {
        this.content = content;
    }   
 
    public void setFont(String fontSize) {
        this.fontSize = fontSize;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    public void setHeight(String positionHeight) {
        this.positionHeight = positionHeight;
    }    
    
    public void setWidth(String width) {
        this.positionWidth = width;
    }  
    
    public void setTop(String top) {
        this.positionTop = top;
    }      
    
    public void setLeft(String left) {
        this.positionLeft = left;
    }
    
    public void setzIndex(String zIndex) {
        this.zIndex = zIndex;
    }
 
    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }    
    
    public void setFix(Boolean fix) {
        this.fix = fix;
    }      
    //@Override 
    //        public String toString() {
                //String temp = "[{\"memoID\":" + this.memoID + ",\"userID\":\"" + this.userID + "\",\"content\":\"" + this.content 
                //        + "\",\"color\":\"test2\",\"top\":0,\"left\":0,\"font\":\"test2\",\"height\":1,\"width\":0},{\"memoID\":1,\"userID\":\"ccc\",\"content\":\"test2\",\"color\":\"test2\",\"top\":0,\"left\":0,\"font\":\"test2\",\"height\":1,\"width\":0}]";
                //String temp2 = this.color;
    //           return this.positionTop;
    //       }
}
