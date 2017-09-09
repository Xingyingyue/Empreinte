package example.com.empreinte.Bean;

/**
 * Created by huanghaojian on 17/6/19.
 */

public class ShareContent {
    private int id;
    private String userAccount;
    private String userName;
    private int imageId;
    private String title;
    private String publishTime;
    private String content;
    private String place;
    private double longitude;
    private double latitude;
    private byte[]img1;
    private byte[]img2;
    private byte[]img3;
    private byte[]img4;
    private byte[]img5;
    private byte[]img6;
    private byte[]img7;
    private byte[]img8;
    private byte[]img9;

    public ShareContent(int id,String userName,int imageId,String title,String publishTime){
        this.id=id;
        this.userName=userName;
        this.imageId=imageId;
        this.title=title;
        this.publishTime=publishTime;
    }
    public ShareContent(int id,String userName,int imageId,String title,String publishTime,String content,String place){
        this.id=id;
        this.userName=userName;
        this.imageId=imageId;
        this.title=title;
        this.publishTime=publishTime;
        this.content=content;
        this.place=place;
    }
    public ShareContent(){}
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getUserAccount(){
        return userAccount;
    }
    public void setUserAccount(String userAccount){
        this.userAccount=userAccount;
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
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
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
    public void setPlace(String place){
        this.place=place;
    }
    public String getPlace(){
        return place;
    }
    public void setLongitude(double longitude){
        this.longitude=longitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public void setLatitude(double latitude){
        this.latitude=latitude;
    }
    public double getLatitude(){
        return  latitude;
    }

    public void setImg1(byte[]img){
        img1=img;
    }
    public byte[]getImg1(){
        return img1;
    }
    public void setImg2(byte[]img){
        img2=img;
    }
    public byte[]getImg2(){
        return img2;
    }
    public void setImg3(byte[]img){
        img3=img;
    }
    public byte[]getImg3(){
        return img3;
    }
    public void setImg4(byte[]img){
        img4=img;
    }
    public byte[]getImg4(){
        return img4;
    }
    public void setImg5(byte[]img){
        img5=img;
    }
    public byte[]getImg5(){
        return img5;
    }
    public void setImg6(byte[]img){
        img6=img;
    }
    public byte[]getImg6(){
        return img6;
    }
    public void setImg7(byte[]img){
        img7=img;
    }
    public byte[]getImg7(){
        return img7;
    }
    public void setImg8(byte[]img){
        img8=img;
    }
    public byte[]getImg8(){
        return img8;
    }
    public void setImg9(byte[]img){
        img9=img;
    }
    public byte[]getImg9(){
        return img9;
    }
}


