package example.com.empreinte.Dao;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import example.com.empreinte.Bean.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by huanghaojian on 17/6/26.
 */

public class UserDao {
    //从服务器端加载角色信息到数据库
    public void loadRoleInfo(Context context,String userAccount){
        DBOperation dbOperation=DBOperation.getInstance(context);
        User user=new User();
        user.setUserAccount(userAccount);
        user.setUserName("xingyingyue");
        user.setAge(20);
        user.setSex("男");
        user.setExperience(1);
        user.setLevel(1);
        dbOperation.saveUserToDB(user);
    }
    //从数据库读取角色信息显示到界面
    public List<User> queryRoleInfoByUserAccount(Context context,String userAccount){
        DBOperation dbOperation=DBOperation.getInstance(context);
        List<User>userList=dbOperation.readUserByUserAccountFromDB(userAccount);
        return userList;
    }
    //更新数据库角色信息
    public void updateRoleInfo(Context context,String userAccount,User user){
        DBOperation dbOperation=DBOperation.getInstance(context);
        dbOperation.updateUser(userAccount,user);
    }
    //插入角色信息到数据库
    public void insertRoleInfo(Context context,User user){
        DBOperation dbOperation=DBOperation.getInstance(context);
        dbOperation.saveUserToDB(user);
    }
    //获得经验更新信息
    public void getExperience(Context context,String userAccount){
        DBOperation dbOperation=DBOperation.getInstance(context);
        dbOperation.getExperience(userAccount);

    }
}