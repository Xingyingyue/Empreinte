package example.com.empreinte.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.com.empreinte.Adapter.ContentReviewAdapter;
import example.com.empreinte.Adapter.PictureAdapter;
import example.com.empreinte.Bean.Review;
import example.com.empreinte.Bean.ShareContent;
import example.com.empreinte.Dao.ShareContentDao;
import example.com.empreinte.R;


/**
 * Created by huanghaojian on 17/6/19.
 */

public class MyShareContentActivity extends BaseActivity{
    private Button back;
    private ImageView head;
    private TextView author;
    private TextView content;
    private TextView publishTime;
    private TextView deleteButton;
    private GridView gridView;
    //private List<Map<String,Object>> imageList;
    private List<Bitmap>imageList=new ArrayList<>();
    private PictureAdapter adapter;
    private int id;
    private List<Review>reviewList;
    private RecyclerView reviewView;
    private ContentReviewAdapter myShareContentReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_content);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        Intent intent=getIntent();
        id=intent.getIntExtra("item_id",1);
        reviewList=new ArrayList<>();
        initReviewList();
        initView();
    }
    private void initView(){
        back=(Button)findViewById(R.id.title_back);
        back.setOnClickListener(this);

        head=(ImageView)findViewById(R.id.myshare_content_head);
        author=(TextView)findViewById(R.id.myshare_content_name);
        content=(TextView)findViewById(R.id.myshare_content_text);
        publishTime=(TextView)findViewById(R.id.myshare_content_publish_time);
        deleteButton=(TextView)findViewById(R.id.myshare_content_delete);
        deleteButton.setOnClickListener(this);

        reviewView=(RecyclerView)findViewById(R.id.myshare_content_review);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        reviewView.setLayoutManager(layoutManager);
        reviewView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        myShareContentReviewAdapter=new ContentReviewAdapter(reviewList);
        reviewView.setAdapter(myShareContentReviewAdapter);

        loadData();

        gridView=(GridView)findViewById(R.id.myshare_content_image_layout);
        adapter=new PictureAdapter(MyShareContentActivity.this,imageList,gridView);
        gridView.setAdapter(adapter);

    }
    private void loadData(){
        ShareContentDao shareContentDao=new ShareContentDao();
        List<ShareContent>shareContentList=shareContentDao.queryShareContentListById(MyShareContentActivity.this,id);
        if (shareContentList.size()>0){
            ShareContent shareContent=shareContentList.get(0);
            head.setImageResource(R.mipmap.ic_launcher);
            author.setText(shareContent.getUserName());
            content.setText(shareContent.getContent());
            publishTime.setText(shareContent.getPublishTime());

            if (shareContent.getImg1()!=null){
                Bitmap bitmap=shareContentDao.byteChangeToImg(shareContent.getImg1());
                imageList.add(bitmap);
                if (shareContent.getImg2()!=null){
                    Bitmap bitmap1=shareContentDao.byteChangeToImg(shareContent.getImg2());
                    imageList.add(bitmap1);
                    if(shareContent.getImg3()!=null){
                        Bitmap bitmap2=shareContentDao.byteChangeToImg(shareContent.getImg3());
                        imageList.add(bitmap2);
                        if (shareContent.getImg4()!=null){
                            Bitmap bitmap3=shareContentDao.byteChangeToImg(shareContent.getImg4());
                            imageList.add(bitmap3);
                            if (shareContent.getImg5()!=null){
                                Bitmap bitmap4=shareContentDao.byteChangeToImg(shareContent.getImg5());
                                imageList.add(bitmap4);
                                if (shareContent.getImg6()!=null){
                                    Bitmap bitmap5=shareContentDao.byteChangeToImg(shareContent.getImg6());
                                    imageList.add(bitmap5);
                                    if (shareContent.getImg7()!=null){
                                        Bitmap bitmap6=shareContentDao.byteChangeToImg(shareContent.getImg7());
                                        imageList.add(bitmap6);
                                        if (shareContent.getImg8()!=null){
                                            Bitmap bitmap7=shareContentDao.byteChangeToImg(shareContent.getImg8());
                                            imageList.add(bitmap7);
                                            if (shareContent.getImg9()!=null){
                                                Bitmap bitmap8=shareContentDao.byteChangeToImg(shareContent.getImg9());
                                                imageList.add(bitmap8);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.myshare_content_delete:
                delete();
                Intent intent=new Intent("xingyingyue.contentDelete.BROADCAST");
                sendBroadcast(intent);
                MyShareContentListActivity.actionStart(MyShareContentActivity.this);
                finish();
                break;
            default:
                break;
        }
    }
    private void delete(){
        ShareContentDao shareContentDao=new ShareContentDao();
        shareContentDao.delete(MyShareContentActivity.this,id);
    }

    private void initReviewList(){
        Review review=new Review(0,"xingyingyue",R.mipmap.ic_launcher,"2017-4-29","对啊");
        reviewList.add(review);
    }

    public static void actionStart(Context context, int id){
        Intent intent=new Intent(context,MyShareContentActivity.class);
        intent.putExtra("item_id",id);
        context.startActivity(intent);
    }

}

