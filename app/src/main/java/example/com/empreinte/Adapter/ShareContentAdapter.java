package example.com.empreinte.Adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import example.com.empreinte.Activity.MyShareContentActivity;
import example.com.empreinte.Bean.ShareContent;
import example.com.empreinte.Dao.ShareContentDao;
import example.com.empreinte.R;


/**
 * Created by huanghaojian on 17/6/19.
 */

public class ShareContentAdapter extends RecyclerView.Adapter<ShareContentAdapter.ViewHolder>{
    private List<ShareContent> shareContentList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView head;
        TextView title;
        TextView publicPlace;
        TextView publishTime;
        View shareContentView;
        public ViewHolder(View view){
            super(view);
            shareContentView=view;
            head=(ImageView)view.findViewById(R.id.head);
            title=(TextView)view.findViewById(R.id.item_title);
            publicPlace=(TextView)view.findViewById(R.id.item_publish_place);
            publishTime=(TextView)view.findViewById(R.id.item_publish_time);
        }
    }
    public ShareContentAdapter(List<ShareContent> shareContentList){
        this.shareContentList=shareContentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.share_content_list_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.shareContentView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position=viewHolder.getAdapterPosition();
                ShareContent shareContent=shareContentList.get(position);
                MyShareContentActivity.actionStart(v.getContext(),shareContent.getId());
            }
        });
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int position){
        ShareContent shareContent=shareContentList.get(position);
        if (shareContent.getImg1()!=null){
            ShareContentDao shareContentDao=new ShareContentDao();
            Bitmap bitmap=shareContentDao.byteChangeToImg(shareContent.getImg1());
            Drawable drawable=new BitmapDrawable(bitmap);
            viewHolder.head.setImageDrawable(drawable);
        }else {
            viewHolder.head.setImageResource(R.mipmap.ic_launcher);
        }
        viewHolder.title.setText(shareContent.getTitle());
        viewHolder.publicPlace.setText(shareContent.getPlace());
        viewHolder.publishTime.setText(shareContent.getPublishTime());
    }
    @Override
    public int getItemCount(){
        return shareContentList.size();
    }
}


