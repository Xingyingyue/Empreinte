package example.com.empreinte.Dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import example.com.empreinte.Bean.ShareContent;


/**
 * Created by huanghaojian on 17/7/9.
 */

public class ShareContentDao {
    private List<ShareContent>shareContentList=new ArrayList<>();

    public void saveShareContent(ShareContent shareContent, Context context){
        DBOperation dbOperation=DBOperation.getInstance(context);
        dbOperation.saveShareContentToDB(shareContent);
    }

    public List<ShareContent> queryShareContentList(Context context){
        DBOperation dbOperation=DBOperation.getInstance(context);
        List<ShareContent> shareContentList=dbOperation.queryAllShareContent();
        return shareContentList;
    }

    public List<ShareContent> queryShareContentListById(Context context,int id){
        DBOperation dbOperation=DBOperation.getInstance(context);
        List<ShareContent> shareContentList=dbOperation.queryShareContentById(id);
        return shareContentList;
    }
    public List<ShareContent> queryShareContentListByTitle(Context context,String title){
        DBOperation dbOperation=DBOperation.getInstance(context);
        List<ShareContent> shareContentList=dbOperation.queryShareContentByTitle(title);
        return shareContentList;
    }
    public void delete(Context context,int id){
        DBOperation dbOperation=DBOperation.getInstance(context);
        dbOperation.deleteShareContent(id);
    }

    //将图片转换为字节
    public List<byte[]> imgChangeTobyte(List<Bitmap>bitmapList){
        List<byte[]>bytesList=new ArrayList<>();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        for (int i=0;i<bitmapList.size();i++) {
            Bitmap bitmap=bitmapList.get(i);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            bytesList.add(byteArrayOutputStream.toByteArray());
        }
        return bytesList;
    }

    //将数据库中图片数据转换为bitmap
    public Bitmap byteChangeToImg(byte[]bytes){
        Bitmap output= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        return output;
    }

    public List<Marker> showMarker(AMap aMap, Context context) {
        shareContentList = queryShareContentList(context);
        List<Marker> markerList = new ArrayList<>();
        if (shareContentList.size() > 0) {
            for (int i = 0; i < shareContentList.size(); i++) {
                ShareContent shareContent = shareContentList.get(i);
                LatLng latLng = new LatLng(shareContent.getLatitude(), shareContent.getLongitude());
                Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(Integer.toString(shareContent.getId())).snippet(shareContent.getUserName()));
                markerList.add(marker);
            }
            return markerList;
        }
        return markerList;
    }
}
