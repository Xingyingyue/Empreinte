package example.com.empreinte.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import example.com.empreinte.Adapter.PictureAdapter;
import example.com.empreinte.Bean.ShareContent;
import example.com.empreinte.Bean.User;
import example.com.empreinte.Dao.ShareContentDao;
import example.com.empreinte.Dao.UserDao;
import example.com.empreinte.R;
import example.com.empreinte.Util.BitmapUtils;
import example.com.empreinte.View.MyToast;


/**
 * Created by huanghaojian on 17/6/19.
 */

public class EditContentActivity extends BaseActivity{
    private List<Bitmap> data = new ArrayList<Bitmap>();
    private GridView mGridView;
    private EditText title;
    private EditText content;
    private String photoPath;
    private PictureAdapter adapter;
    private Button back;
    private Button send;
    private String userAccount;
    private String password;
    private LocationManager locationManager;
    private String locationProvider;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        setContentView(R.layout.edit_content);
        if(ContextCompat.checkSelfPermission(EditContentActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditContentActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        Intent intent=getIntent();
        userAccount=intent.getStringExtra("userAccount");
        password=intent.getStringExtra("password");
        initView();

        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        List<String>providers=locationManager.getProviders(true);
        if(providers.contains(LocationManager.GPS_PROVIDER)){
            //如果是GPS
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
            //如果是Network
            locationProvider = LocationManager.GPS_PROVIDER;
        }else{
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return ;
        }
        //获取Location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if(location!=null){
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            Log.e("yes", Double.toString(longitude));
       }else{
            //因为使用gps定位时，需要一段时间才会获得，所以这时候得到的经纬度是空的。这里用广州塔的经纬度暂为代替
            Log.e("yes","no");
            latitude=23.106680;
            longitude=113.3245904;
        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
    }

    private void initView(){
        title=(EditText)findViewById(R.id.edit_content_title);
        content=(EditText)findViewById(R.id.content_et);
        back=(Button)findViewById(R.id.edit_content_title_back);
        back.setOnClickListener(this);
        send=(Button)findViewById(R.id.edit_content_send);
        send.setOnClickListener(this);

        Bitmap bp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_addpic);
        data.add(bp);

        //View view=this.getLayoutInflater().inflate(R.layout.edit_content,null);
        mGridView = (GridView) findViewById(R.id.gridView1);

        adapter = new PictureAdapter(EditContentActivity.this, data, mGridView);
        mGridView.setAdapter(adapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (data.size() == 10) {
                    Toast.makeText(EditContentActivity.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                } else {
                    if (position == data.size() - 1) {
                        Toast.makeText(EditContentActivity.this, "正在加载图片", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 0x1);
                    } else {
                        Toast.makeText(EditContentActivity.this, "当前图片为" + (position + 1) + " 张图片", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialog(position);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.edit_content_title_back:
                finish();
                break;
            case R.id.edit_content_send:
                if (title.getText().toString().trim().equals("")||content.getText().toString().trim().equals("")) {
                    MyToast.makeText(EditContentActivity.this,"图片可以不选，但内容要填写完整哦！～",Toast.LENGTH_LONG).show();
                }else{
                    saveShareContent();
                    MainActivity.actionStart(EditContentActivity.this,null,null);
                }
                break;
        }
    }

    //将编写的内容发布
    private void saveShareContent(){
        ShareContent shareContent=new ShareContent();
        UserDao userDao=new UserDao();
        ShareContentDao shareContentDao=new ShareContentDao();
        //将图片转换为字节
        List<byte[]>bytesList=shareContentDao.imgChangeTobyte(data);
        List<User>userList=userDao.queryRoleInfoByUserAccount(EditContentActivity.this,userAccount);
        if(userList.size()>0) {
            User user = userList.get(0);
            //获取系统日期
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(System.currentTimeMillis());
            String time = simpleDateFormat.format(curDate);

            shareContent.setUserAccount(userAccount);
            shareContent.setUserName(user.getUserName());
            shareContent.setTitle(title.getText().toString().trim());
            shareContent.setLatitude(latitude);
            shareContent.setLongitude(longitude);
            shareContent.setContent(content.getText().toString().trim());
            shareContent.setPublishTime(time);
            if (bytesList.size() > 1) {
                if (bytesList.size() == 2) {
                    shareContent.setImg1(bytesList.get(0));
                }
                if (bytesList.size() == 3) {
                    shareContent.setImg1(bytesList.get(0));
                    shareContent.setImg2(bytesList.get(1));
                }
                if (bytesList.size() == 4) {
                    shareContent.setImg1(bytesList.get(0));
                    shareContent.setImg2(bytesList.get(1));
                    shareContent.setImg3(bytesList.get(2));
                }
                if (bytesList.size() == 5) {
                    shareContent.setImg1(bytesList.get(0));
                    shareContent.setImg2(bytesList.get(1));
                    shareContent.setImg3(bytesList.get(2));
                    shareContent.setImg4(bytesList.get(3));
                }
                if (bytesList.size() == 6) {
                    shareContent.setImg1(bytesList.get(0));
                    shareContent.setImg2(bytesList.get(1));
                    shareContent.setImg3(bytesList.get(2));
                    shareContent.setImg4(bytesList.get(3));
                    shareContent.setImg5(bytesList.get(4));
                }
                if (bytesList.size() == 7) {
                    shareContent.setImg1(bytesList.get(0));
                    shareContent.setImg2(bytesList.get(1));
                    shareContent.setImg3(bytesList.get(2));
                    shareContent.setImg4(bytesList.get(3));
                    shareContent.setImg5(bytesList.get(4));
                    shareContent.setImg6(bytesList.get(5));
                }
                if (bytesList.size() == 8) {
                    shareContent.setImg1(bytesList.get(0));
                    shareContent.setImg2(bytesList.get(1));
                    shareContent.setImg3(bytesList.get(2));
                    shareContent.setImg4(bytesList.get(3));
                    shareContent.setImg5(bytesList.get(4));
                    shareContent.setImg6(bytesList.get(5));
                    shareContent.setImg7(bytesList.get(6));
                }
                if (bytesList.size() == 9) {
                    shareContent.setImg1(bytesList.get(0));
                    shareContent.setImg2(bytesList.get(1));
                    shareContent.setImg3(bytesList.get(2));
                    shareContent.setImg4(bytesList.get(3));
                    shareContent.setImg5(bytesList.get(4));
                    shareContent.setImg6(bytesList.get(5));
                    shareContent.setImg7(bytesList.get(6));
                    shareContent.setImg8(bytesList.get(7));
                }
                if (bytesList.size() == 10) {
                    shareContent.setImg1(bytesList.get(0));
                    shareContent.setImg2(bytesList.get(1));
                    shareContent.setImg3(bytesList.get(2));
                    shareContent.setImg4(bytesList.get(3));
                    shareContent.setImg5(bytesList.get(4));
                    shareContent.setImg6(bytesList.get(5));
                    shareContent.setImg7(bytesList.get(6));
                    shareContent.setImg8(bytesList.get(7));
                    shareContent.setImg9(bytesList.get(8));
                }
            }
            shareContentDao.saveShareContent(shareContent, EditContentActivity.this);
        }

    }
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditContentActivity.this);
        builder.setMessage("要删除这张图片吗");
        builder.setTitle("提示ʾ");
        builder.setPositiveButton("仍要删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                data.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("保留", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            if (data != null) {

                ContentResolver resolver = getContentResolver();
                try {
                    Uri uri = data.getData();

                    String[] proj = { MediaStore.Images.Media.DATA };
                    Cursor cursor = managedQuery(uri, proj, null, null, null);

                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();

                    photoPath = cursor.getString(column_index);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(photoPath)) {
            Bitmap newBp = BitmapUtils.decodeSampledBitmapFromFd(photoPath, 300, 300);
            data.remove(data.size() - 1);
            Bitmap bp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_addpic);
            data.add(newBp);
            data.add(bp);
            photoPath = null;
            adapter.notifyDataSetChanged();
        }
    }

    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationManager!=null){
            //移除监听器
            locationManager.removeUpdates(locationListener);
        }
    }

    public static void actionStart(Context context,String userAccount,String password){
        Intent intent=new Intent(context,EditContentActivity.class);
        intent.putExtra("userAccount",userAccount);
        intent.putExtra("password",password);
        context.startActivity(intent);
    }
}


