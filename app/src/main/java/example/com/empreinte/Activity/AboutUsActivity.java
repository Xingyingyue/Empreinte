package example.com.empreinte.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import example.com.empreinte.R;

/**
 * Created by yaozeming on 2017/9/1.
 */

public class AboutUsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }
    public static void actionStart(Context context){
        Intent intent=new Intent(context,AboutUsActivity.class);
        context.startActivity(intent);
    }
}