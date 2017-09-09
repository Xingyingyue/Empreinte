package example.com.empreinte.Bean;

/**
 * Created by huanghaojian on 17/5/2.
 */

public class Review {
    private int id;
    private String userName;
    private int imageId;
    private String publishTime;
    private String content;

    public Review(int id,String userName,int imageId,String publishTime){
        this.id=id;
        this.userName=userName;
        this.imageId=imageId;
        this.publishTime=publishTime;
    }
    public Review(int id,String userName,int imageId,String publishTime,String content){
        this.id=id;
        this.userName=userName;
        this.imageId=imageId;
        this.publishTime=publishTime;
        this.content=content;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName=userName;
    }
    public int getImageId(){
        return imageId;
    }
    public void setImageId(int imageId){
        this.imageId=imageId;
    }
    public String getPublishTime(){
        return publishTime;
    }
    public void setPublishTime(String publishTime){
        this.publishTime=publishTime;
    }
    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content=content;
    }
}

