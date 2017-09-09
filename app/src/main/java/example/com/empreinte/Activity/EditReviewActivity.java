package example.com.empreinte.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import example.com.empreinte.R;

/**
 * Created by huanghaojian on 17/8/29.
 */

public class EditReviewActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstancedState){
        super.onCreate(savedInstancedState);
        setContentView(R.layout.edit_review);
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context,EditReviewActivity.class);
        context.startActivity(intent);
    }
}
