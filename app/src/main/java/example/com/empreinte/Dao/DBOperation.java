package example.com.empreinte.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import example.com.empreinte.Bean.Pass;
import example.com.empreinte.Bean.ShareContent;
import example.com.empreinte.Bean.User;
import example.com.empreinte.Util.MyDatabaseHelper;

/**
 * Created by huanghaojian on 17/6/30.
 */

public class DBOperation {
    public static final String DB_NAME="goexplore";
    public static final int version=1;
    private SQLiteDatabase db;
    private static DBOperation dbOperation;

    private DBOperation(Context context) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context, DB_NAME, null, version);
        db=dbHelper.getWritableDatabase();
    }

    public  static synchronized DBOperation getInstance(Context context) {
        if (dbOperation == null) {
            dbOperation = new DBOperation(context);
        }
        return dbOperation;
    }

    //储存User实例到数据库
    public void saveUserToDB(User user){
        if(user!=null){
            ContentValues contentValues=new ContentValues();
            contentValues.put("user_account",user.getUserAccount());
            contentValues.put("user_name",user.getUserName());
            contentValues.put("age",user.getAge());
            contentValues.put("experience",user.getExperience());
            contentValues.put("sex",user.getSex());
            contentValues.put("level",user.getLevel());
            db.insert("User",null,contentValues);
        }
    }
    //从数据库读取特定的User信息
    public List<User> readUserByUserAccountFromDB(String userAccount){
        List<User>userList=new ArrayList<>();
        Cursor cursor=db.query("User",null,"user_account=?",new String[]{userAccount},null,null,null);
        if(cursor.moveToFirst()){
            do {
                User user=new User();
                user.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                user.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
                user.setAge(cursor.getInt(cursor.getColumnIndex("age")));
                user.setSex(cursor.getString(cursor.getColumnIndex("sex")));
                user.setExperience(cursor.getInt(cursor.getColumnIndex("experience")));
                user.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
                userList.add(user);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }
    //更新User信息
    public void updateUser(String userAccount,User user){
        ContentValues values=new ContentValues();
        values.put("user_name",user.getUserName());
        values.put("age",user.getAge());
        values.put("sex",user.getSex());
        db.update("User",values,"user_account=?",new String[]{userAccount});
    }

    //获得经验值时更新User信息
    public void getExperience(String userAccount){
        List<User>userList=readUserByUserAccountFromDB(userAccount);
        ContentValues values=new ContentValues();
        if (userList.size()>0) {
            User user=userList.get(0);
            int experience=user.getExperience();
            if (experience + 10 > 100) {
                user.setLevel(user.getLevel() + 1);
                user.setExperience(experience + 10 - 100);
                values.put("level", user.getLevel());
                values.put("experience", user.getExperience());
                db.update("User", values, "user_account=?", new String[]{userAccount});
                Log.e("getandup", "success");
            } else {
                user.setExperience(experience + 10);
                values.put("experience", user.getExperience());
                db.update("User", values, "user_account=?", new String[]{userAccount});
                Log.e("getexp", Integer.toString(user.getExperience()));
                Log.e("getexp1", Integer.toString(user.getLevel()));
            }
        }
    }

    //储存Pass实例到数据库
    public void savePassToDB(Pass pass){
        if(pass!=null){
            ContentValues contentValues=new ContentValues();
            contentValues.put("pass_id",pass.getPassId());
            contentValues.put("user_account",pass.getUserAccount());
            contentValues.put("longitude",pass.getLongitude());
            contentValues.put("latitude",pass.getLatitude());
            contentValues.put("content",pass.getContent());
            contentValues.put("address",pass.getAddress());
            contentValues.put("experience",pass.getExperience());
            contentValues.put("state",pass.getState());
            db.insert("Pass",null,contentValues);
        }
    }

    //从数据库读取所有Pass信息
    public List<Pass> readPassFromDB(){
        List<Pass>passList=new ArrayList<>();
        Cursor cursor=db.query("Pass",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Pass pass=new Pass();
                pass.setPassId(cursor.getString(cursor.getColumnIndex("pass_id")));
                pass.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                pass.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                pass.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                pass.setContent(cursor.getString(cursor.getColumnIndex("content")));
                pass.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                pass.setExperience(cursor.getInt(cursor.getColumnIndex("experience")));
                pass.setState(cursor.getInt(cursor.getColumnIndex("state")));
                passList.add(pass);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return passList;
    }

    //根据经纬度从数据库取出某个关节点的信息
    public List<Pass> readPassByPlaceFromDB(double latitude,double longitude){
        List<Pass>passList=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from Pass where latitude=? and longitude=?",new String[]{String.valueOf(latitude),String.valueOf(longitude)});
        if(cursor.moveToFirst()){
            do{
                Pass pass=new Pass();
                pass.setPassId(cursor.getString(cursor.getColumnIndex("pass_id")));
                pass.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                pass.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                pass.setContent(cursor.getString(cursor.getColumnIndex("content")));
                pass.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                pass.setExperience(cursor.getInt(cursor.getColumnIndex("experience")));
                pass.setState(cursor.getInt(cursor.getColumnIndex("state")));
                passList.add(pass);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return passList;
    }

    //根据经纬度从数据库取出某个关节点的信息
    public List<Pass> readPassByIdFromDB(String passId){
        List<Pass>passList=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from Pass where pass_id=?",new String[]{passId});
        if(cursor.moveToFirst()){
            do{
                Pass pass=new Pass();
                pass.setPassId(cursor.getString(cursor.getColumnIndex("pass_id")));
                pass.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                pass.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                pass.setContent(cursor.getString(cursor.getColumnIndex("content")));
                pass.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                pass.setExperience(cursor.getInt(cursor.getColumnIndex("experience")));
                pass.setState(cursor.getInt(cursor.getColumnIndex("state")));
                passList.add(pass);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return passList;
    }

    //根据passId更新pass信息
    public void updatePassInfo(String passId){
        ContentValues values=new ContentValues();
        values.put("state",1);
        db.update("Pass",values,"pass_id=?",new String[]{passId});
    }

    //清除数据库中所有的Pass信息
    public void deleteAllPass(){
        db.delete("Pass",null,null);
    }

    //储存分享内容到数据库
    public void saveShareContentToDB(ShareContent shareContent){
        ContentValues values=new ContentValues();
        values.put("user_account",shareContent.getUserAccount());
        values.put("user_name",shareContent.getUserName());
        values.put("title",shareContent.getTitle());
        values.put("publish_time",shareContent.getPublishTime());
        values.put("content",shareContent.getContent());
        values.put("place",shareContent.getPlace());
        values.put("latitude",shareContent.getLatitude());
        values.put("longitude",shareContent.getLongitude());
        values.put("image1",shareContent.getImg1());
        values.put("image2",shareContent.getImg2());
        values.put("image3",shareContent.getImg3());
        values.put("image4",shareContent.getImg4());
        values.put("image5",shareContent.getImg5());
        values.put("image6",shareContent.getImg6());
        values.put("image7",shareContent.getImg7());
        values.put("image8",shareContent.getImg8());
        values.put("image9",shareContent.getImg9());
        db.insert("ShareContent",null,values);
    }

    //删除分享内容
    public void deleteShareContent(int id){
        db.delete("ShareContent","id=?",new String[]{Integer.toString(id)});
    }

    //读取所有的分享内容
    public List<ShareContent> queryAllShareContent(){
        List<ShareContent>shareContentList=new ArrayList<>();
        Cursor cursor=db.query("ShareContent",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                ShareContent shareContent=new ShareContent();
                shareContent.setId(cursor.getInt(cursor.getColumnIndex("id")));
                shareContent.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                shareContent.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
                shareContent.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                shareContent.setPublishTime(cursor.getString(cursor.getColumnIndex("publish_time")));
                shareContent.setContent(cursor.getString(cursor.getColumnIndex("content")));
                shareContent.setPlace(cursor.getString(cursor.getColumnIndex("place")));
                shareContent.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                shareContent.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                shareContent.setImg1(cursor.getBlob(cursor.getColumnIndex("image1")));
                shareContent.setImg2(cursor.getBlob(cursor.getColumnIndex("image2")));
                shareContent.setImg3(cursor.getBlob(cursor.getColumnIndex("image3")));
                shareContent.setImg4(cursor.getBlob(cursor.getColumnIndex("image4")));
                shareContent.setImg5(cursor.getBlob(cursor.getColumnIndex("image5")));
                shareContent.setImg6(cursor.getBlob(cursor.getColumnIndex("image6")));
                shareContent.setImg7(cursor.getBlob(cursor.getColumnIndex("image7")));
                shareContent.setImg8(cursor.getBlob(cursor.getColumnIndex("image8")));
                shareContent.setImg9(cursor.getBlob(cursor.getColumnIndex("image9")));
                shareContentList.add(shareContent);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return shareContentList;
    }

    //根据标题读取分享内容
    public List<ShareContent> queryShareContentByTitle(String title){
        List<ShareContent>shareContentList=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from ShareContent where title=?",new String[]{title});
        if(cursor.moveToFirst()){
            do{
                ShareContent shareContent=new ShareContent();
                shareContent.setId(cursor.getInt(cursor.getColumnIndex("id")));
                shareContent.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                shareContent.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
                shareContent.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                shareContent.setPublishTime(cursor.getString(cursor.getColumnIndex("publish_time")));
                shareContent.setContent(cursor.getString(cursor.getColumnIndex("content")));
                shareContent.setPlace(cursor.getString(cursor.getColumnIndex("place")));
                shareContent.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                shareContent.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                shareContent.setImg1(cursor.getBlob(cursor.getColumnIndex("image1")));
                shareContent.setImg2(cursor.getBlob(cursor.getColumnIndex("image2")));
                shareContent.setImg3(cursor.getBlob(cursor.getColumnIndex("image3")));
                shareContent.setImg4(cursor.getBlob(cursor.getColumnIndex("image4")));
                shareContent.setImg5(cursor.getBlob(cursor.getColumnIndex("image5")));
                shareContent.setImg6(cursor.getBlob(cursor.getColumnIndex("image6")));
                shareContent.setImg7(cursor.getBlob(cursor.getColumnIndex("image7")));
                shareContent.setImg8(cursor.getBlob(cursor.getColumnIndex("image8")));
                shareContent.setImg9(cursor.getBlob(cursor.getColumnIndex("image9")));
                shareContentList.add(shareContent);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return shareContentList;
    }

    //根据id读取分享内容
    public List<ShareContent> queryShareContentById(int id){
        List<ShareContent>shareContentList=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from ShareContent where id=?",new String[]{Integer.toString(id)});
        if(cursor.moveToFirst()){
            do{
                ShareContent shareContent=new ShareContent();
                shareContent.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                shareContent.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
                shareContent.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                shareContent.setPublishTime(cursor.getString(cursor.getColumnIndex("publish_time")));
                shareContent.setContent(cursor.getString(cursor.getColumnIndex("content")));
                shareContent.setPlace(cursor.getString(cursor.getColumnIndex("place")));
                shareContent.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                shareContent.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                shareContent.setImg1(cursor.getBlob(cursor.getColumnIndex("image1")));
                shareContent.setImg2(cursor.getBlob(cursor.getColumnIndex("image2")));
                shareContent.setImg3(cursor.getBlob(cursor.getColumnIndex("image3")));
                shareContent.setImg4(cursor.getBlob(cursor.getColumnIndex("image4")));
                shareContent.setImg5(cursor.getBlob(cursor.getColumnIndex("image5")));
                shareContent.setImg6(cursor.getBlob(cursor.getColumnIndex("image6")));
                shareContent.setImg7(cursor.getBlob(cursor.getColumnIndex("image7")));
                shareContent.setImg8(cursor.getBlob(cursor.getColumnIndex("image8")));
                shareContent.setImg9(cursor.getBlob(cursor.getColumnIndex("image9")));
                shareContentList.add(shareContent);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return shareContentList;
    }
}

