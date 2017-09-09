package example.com.empreinte.Bean;

/**
 * Created by huanghaojian on 17/6/26.
 */

public class User {
    private String userAccount;
    private String userName;
    private String password;
    private Integer age;
    private Integer level;
    private String sex;
    private Integer experience;
    private Integer state;

    public void setUserAccount(String userAccount){
        this.userAccount=userAccount;
    }
    public String getUserAccount(){
        return userAccount;
    }
    public void setUserName(String userName){
        this.userName=userName;
    }
    public String getUserName(){
        return userName;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getPassword(){
        return password;
    }
    public void setAge(Integer age){
        this.age=age;
    }
    public Integer getAge(){
        return age;
    }
    public void setLevel(Integer level){
        this.level=level;
    }
    public Integer getLevel(){
        return level;
    }
    public void setSex(String sex){
        this.sex=sex;
    }
    public String getSex(){
        return sex;
    }
    public void setExperience(Integer experience){
        this.experience=experience;
    }
    public Integer getExperience(){
        return experience;
    }
    public void setState(Integer state){
        this.state=state;
    }
    public Integer getState(){
        return state;
    }
}


