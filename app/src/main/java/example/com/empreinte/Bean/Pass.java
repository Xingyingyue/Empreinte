package example.com.empreinte.Bean;

/**
 * Created by huanghaojian on 17/6/30.
 */

public class Pass {
    private String passId;
    private String userAccount;
    private double longitude;
    private double latitude;
    private String content;
    private String address;
    private int experience;
    private int state;

    public void setPassId(String passId){
        this.passId=passId;
    }
    public String getPassId(){
        return  passId;
    }
    public void setUserAccount(String userAccount){
        this.userAccount=userAccount;
    }
    public String getUserAccount(){
        return  userAccount;
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
        return latitude;
    }
    public void  setContent(String content){
        this.content=content;
    }
    public String getContent(){
        return content;
    }
    public void setAddress(String address){
        this.address=address;
    }
    public String getAddress(){
        return address;
    }
    public void setExperience(int experience){
        this.experience=experience;
    }
    public int getExperience(){
        return experience;
    }
    public void setState(int  state){
        this.state=state;
    }
    public int getState(){
        return  state;
    }
}
