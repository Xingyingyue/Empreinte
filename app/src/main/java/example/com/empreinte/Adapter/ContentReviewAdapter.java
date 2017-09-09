package example.com.empreinte.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import example.com.empreinte.Bean.Review;
import example.com.empreinte.R;

/**
 * Created by huanghaojian on 17/5/2.
 */

public class ContentReviewAdapter extends RecyclerView.Adapter<ContentReviewAdapter.ViewHolder> {
    private List<Review> reviewList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView head;
        TextView userName;
        TextView publishTime;
        TextView reviewContent;
        View reviewView;
        public ViewHolder(View view){
            super(view);
            reviewView=view;
            head=(ImageView)view.findViewById(R.id.review_head);
            userName=(TextView)view.findViewById(R.id.review_name);
            publishTime=(TextView)view.findViewById(R.id.review_time);
            reviewContent=(TextView)view.findViewById(R.id.review_content);
        }
    }
    public ContentReviewAdapter(List<Review>reviewList){
        this.reviewList=reviewList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        return  viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int position){
        Review review=reviewList.get(position);
        viewHolder.head.setImageResource(review.getImageId());
        viewHolder.userName.setText(review.getUserName());
        viewHolder.publishTime.setText(review.getPublishTime());
        viewHolder.reviewContent.setText(review.getContent());
    }
    @Override
    public int getItemCount(){
        return reviewList.size();
    }
}
