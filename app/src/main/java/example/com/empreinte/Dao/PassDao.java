package example.com.empreinte.Dao;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import example.com.empreinte.Bean.Pass;
import example.com.empreinte.R;

/**
 * Created by huanghaojian on 17/6/30.
 */

public class PassDao {
    private List<Pass> passList = new ArrayList<>();
    //public static final String state0="未点亮";
    //public static final String state1="已点亮";

    //将关节点显示在地图上
    public List<Marker> showMarker(AMap aMap, Context context) {
        passList = queryPassList(context);
        List<Marker> markerList = new ArrayList<>();
        if (passList.size() > 0) {
            for (int i = 0; i < passList.size(); i++) {
                Pass pass = passList.get(i);
                LatLng latLng = new LatLng(pass.getLatitude(), pass.getLongitude());
                if (pass.getState() == 0) {
                    Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(pass.getAddress()).snippet("经验值：" + pass.getExperience()));
                    markerList.add(marker);
                } else {
                    Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(pass.getAddress()).snippet("经验值：" + pass.getExperience()));
                    markerList.add(marker);
                }
            }
            return markerList;
        } else {
            loadPass(context);
            showMarker(aMap, context);
        }
        return markerList;
    }
    //更新数据库中的关节点信息
    public void updatePassInfo(Context context,String passId){
        DBOperation dbOperation = DBOperation.getInstance(context);
        dbOperation.updatePassInfo(passId);
        Log.e("updatesuccess","success");
    }

    //从数据库取出关节点信息
    public List<Pass> queryPassList(Context context) {
        DBOperation dbOperation = DBOperation.getInstance(context);
        List<Pass> passes = dbOperation.readPassFromDB();
        return passes;
    }

    //根据经纬度从数据库取出某个关节点的信息
    public List<Pass> queryPassByplace(Context context, double latitude, double longitude) {
        DBOperation dbOperation = DBOperation.getInstance(context);
        List<Pass> passes = dbOperation.readPassByPlaceFromDB(latitude, longitude);
        return passes;
    }

    //根据id从数据库取出某个关节点的信息
    public List<Pass> queryPassById(Context context, String passId) {
        DBOperation dbOperation = DBOperation.getInstance(context);
        List<Pass> passes = dbOperation.readPassByIdFromDB(passId);
        return passes;
    }


    //从服务器端加载关节点信息到数据库
    public void loadPass(Context context) {
        DBOperation dbOperation = DBOperation.getInstance(context);
        List<Pass> passes = new ArrayList<>();
        Pass pass = new Pass();
        pass.setUserAccount("18826075488");
        pass.setPassId("华工b7教学楼");
        pass.setAddress("华工b7教学楼");
        pass.setContent("华工b7教学楼");
        pass.setLatitude(23.0464700000);
        pass.setLongitude(113.4074800000);
        pass.setState(0);
        pass.setExperience(100);
        passes.add(pass);

        Pass pass1 = new Pass();
        pass1.setUserAccount("18826075488");
        pass1.setPassId("华工学生公寓");
        pass1.setAddress("华工学生公寓");
        pass1.setContent("华工学生公寓");
        pass1.setLatitude(23.0488800000);
        pass1.setLongitude(113.4015400000);
        pass1.setState(0);
        pass1.setExperience(100);
        passes.add(pass1);

        Pass pass2 = new Pass();
        pass2.setUserAccount("18826075488");
        pass2.setPassId("华工学术大讲堂");
        pass2.setAddress("华工学术大讲堂");
        pass2.setContent("华工学术大讲堂");
        pass2.setLatitude(23.0470500000);
        pass2.setLongitude(113.4068000000);
        pass2.setState(0);
        pass2.setExperience(100);
        passes.add(pass2);

        Pass pass3 = new Pass();
        pass3.setUserAccount("18826075488");
        pass3.setPassId("华工行政办公楼");
        pass3.setAddress("华工行政办公楼");
        pass3.setContent("华工行政办公楼");
        pass3.setLatitude(23.0435800000);
        pass3.setLongitude(113.4085600000);
        pass3.setState(0);
        pass3.setExperience(100);
        passes.add(pass3);

        Pass pass4 = new Pass();
        pass4.setUserAccount("18826075488");
        pass4.setPassId("华工第二饭堂");
        pass4.setAddress("华工第二饭堂");
        pass4.setContent("华工第二饭堂");
        pass4.setLatitude(23.0515500000);
        pass4.setLongitude(113.4031400000);
        pass4.setState(0);
        pass4.setExperience(100);
        passes.add(pass4);

        Pass pass5 = new Pass();
        pass5.setUserAccount("18826075488");
        pass5.setPassId("中大公共教学楼");
        pass5.setAddress("中大公共教学楼");
        pass5.setContent("中大公共教学楼");
        pass5.setLatitude(23.0684200000);
        pass5.setLongitude(113.3929700000);
        pass5.setState(0);
        pass5.setExperience(100);
        passes.add(pass5);

        Pass pass6 = new Pass();
        pass6.setUserAccount("18826075488");
        pass6.setPassId("中大第二食堂");
        pass6.setAddress("中大第二食堂");
        pass6.setContent("中大第二食堂");
        pass6.setLatitude(23.0602700000);
        pass6.setLongitude(113.3902900000);
        pass6.setState(0);
        pass6.setExperience(100);
        passes.add(pass6);

        Pass pass7 = new Pass();
        pass7.setUserAccount("18826075488");
        pass7.setPassId("岭南印象园公交站");
        pass7.setAddress("岭南印象园公交站");
        pass7.setContent("岭南印象园公交站");
        pass7.setLatitude(23.0366090000);
        pass7.setLongitude(113.4041740000);
        pass7.setState(1);
        pass7.setExperience(100);
        passes.add(pass7);

        Pass pass8 = new Pass();
        pass8.setUserAccount("18826075488");
        pass8.setPassId("岭南印象园小吃");
        pass8.setAddress("岭南印象园小吃");
        pass8.setContent("岭南印象园小吃");
        pass8.setLatitude(23.0362000000);
        pass8.setLongitude(113.4055000000);
        pass8.setState(1);
        pass8.setExperience(100);
        passes.add(pass8);

        Pass pass9 = new Pass();
        pass9.setUserAccount("18826075488");
        pass9.setPassId("岭南印象园包公庙");
        pass9.setAddress("岭南印象园包公庙");
        pass9.setContent("岭南印象园包公庙");
        pass9.setLatitude(23.0344900000);
        pass9.setLongitude(113.4060000000);
        pass9.setState(1);
        pass9.setExperience(100);
        passes.add(pass9);

        for (int i = 0; i < passes.size(); i++) {
            Pass tempPass = passes.get(i);
            dbOperation.savePassToDB(tempPass);
        }
    }

    //随机生成5个关节点作为随机百宝箱在关节点周围
    public List<Marker> generatePass(AMap aMap,Context context) {
        passList = queryPassList(context);
        List<Marker> markerList = new ArrayList<>();
        BitmapDescriptor descriptor= BitmapDescriptorFactory.fromResource(R.drawable.marker_icon);
        for (int j = 0; j < passList.size(); j++) {
            for (int i = 0; i < 5; i++) {
                Pass pass=passList.get(j);
                double lat = Math.random() * 0.001 + pass.getLatitude();
                double lon = Math.random() * 0.001 + pass.getLongitude();
                Log.e("lat", Double.toString(lat));
                LatLng latLng = new LatLng(lat, lon, false);
                Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("随机百宝箱").snippet("经验值：" + 100).icon(descriptor));
                markerList.add(marker);
            }
        }
        return markerList;
    }
}


