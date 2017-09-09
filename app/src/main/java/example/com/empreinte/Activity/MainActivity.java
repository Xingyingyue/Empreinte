package example.com.empreinte.Activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import example.com.empreinte.Bean.Pass;
import example.com.empreinte.Bean.User;
import example.com.empreinte.Dao.PassDao;
import example.com.empreinte.Dao.ShareContentDao;
import example.com.empreinte.Dao.UserDao;
import example.com.empreinte.R;
import example.com.empreinte.Util.OkHttpUtil;
import example.com.empreinte.Util.SensorEventHelper;
import example.com.empreinte.View.MyToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by huanghaojian on 17/6/15.
 */

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,LocationSource,AMapLocationListener,AMap.OnMarkerClickListener,View.OnClickListener{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FloatingActionButton refresh;
    private NavigationView navigationView;

    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private TextView mLocationErrText;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private boolean mFirstFix = false;
    private Marker mLocMarker;
    private SensorEventHelper mSensorHelper;
    private Circle mCircle;
    public static final String LOCATION_MARKER_FLAG = "mylocation";

    private ShareContentDao shareContentDao=new ShareContentDao();
    private List<Marker> markerList=new ArrayList<>();
    private String userAccount;
    private String password;

    public static final String LOGOUT_BROADCAST_ACTION="xingyingyue.logout.BROADCAST";
    private IntentFilter intentFilter2;
    private LogoutBroadcastReceiver logoutBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        setContentView(R.layout.layout_main);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        //获取从登录界面传来的账号密码
        Intent intent=getIntent();
        if (intent.getExtras().getString("from_receiver")==null) {
            userAccount = intent.getStringExtra("userAccount");
            password = intent.getStringExtra("password");
        }
        initMap(savedInstanceState);
        initView();

        intentFilter2=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter2.addAction(LOGOUT_BROADCAST_ACTION);
        logoutBroadcastReceiver=new LogoutBroadcastReceiver();
        registerReceiver(logoutBroadcastReceiver,intentFilter2);

    }
    private void initMap(Bundle savedInstanceState){
        mapView=(MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);//创建地图
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        aMap.setOnMarkerClickListener(this);

        markerList=shareContentDao.showMarker(aMap,MainActivity.this);
    }
    private void initView(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.main_drawerLayout);
        refresh=(FloatingActionButton)findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
        navigationView=(NavigationView)findViewById(R.id.main_menu_left);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_personal_info_setting);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            //logout(userAccount);
            finish();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_personal_info_setting:
                ModifyAccountInfoActivity.actionStart(MainActivity.this,userAccount,password);
                break;
            case R.id.nav_role_setting:
                ModifyRoleInfoActivity.actionStart(MainActivity.this,userAccount,password);
                break;
            case R.id.nav_publish_record:
                MyShareContentListActivity.actionStart(MainActivity.this);
                break;
            case R.id.nav_about:
                AboutUsActivity.actionStart(MainActivity.this);
                break;
            default:
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //marker的点击事件
    @Override
    public boolean onMarkerClick(Marker marker){
        String id=marker.getTitle();
        PublishContentActivity.actionStart(MainActivity.this,Integer.parseInt(id));
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.edit_content:
                EditContentActivity.actionStart(MainActivity.this,userAccount,password);
                break;
            default:
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (mLocMarker != null) {
            mLocMarker.destroy();
        }
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        unregisterReceiver(logoutBroadcastReceiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
        deactivate();
        mFirstFix = false;

    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {

                mLocationErrText.setVisibility(View.GONE);
                LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());

                if (!mFirstFix) {
                    mFirstFix = true;
                    addCircle(location, amapLocation.getAccuracy());//添加定位精度圆
                    addMarker(location);//添加定位图标
                    mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,18));
                } else {
                    mCircle.setCenter(location);
                    mCircle.setRadius(amapLocation.getAccuracy());
                    mLocMarker.setPosition(location);
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(location));
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                mLocationErrText.setVisibility(View.VISIBLE);
                mLocationErrText.setText(errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    private void addCircle(LatLng latlng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        mCircle = aMap.addCircle(options);
    }

    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.navi_map_gps_locked)));
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
    }

    //接收登出广播，销毁主界面活动
    private class LogoutBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context,Intent intent){
            if (intent.getAction().equals(LOGOUT_BROADCAST_ACTION)) {
                finish();
            }
        }
    }

    private void logout(String userAccount){
        String logoutUrl="http://110.64.90.22:8080/login/logout?userAccount="+userAccount;

        OkHttpUtil.sendOkHttpRequest(logoutUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"登出失败，请检查网络",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String response1=response.body().string();
                Log.e("test",response1);
                if(response1.trim().equals("0")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "登出失败，请重试", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if(response1.trim().equals("1")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "登出成功", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.refresh:
                break;
            default:
                break;
        }
    }

    public static void actionStart(Context context,String userAccount,String password){
        Intent intent=new Intent(context,MainActivity.class);
        intent.putExtra("userAccount",userAccount);
        intent.putExtra("password",password);
        context.startActivity(intent);
    }
}

