package com.pusatict.getvet.forum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pusatict.getvet.MainActivity;
import com.pusatict.getvet.R;

/**
 * Created by bronky on 07/10/2015.
 */
public class ForumSetting extends Activity {
    private ImageButton imgBack,imgThread,imgComment,imgLogout;
    private TextView tvThread,tvComment,tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_setting);
        imgBack = (ImageButton)findViewById(R.id.imgBack);

        tvThread=(TextView)findViewById(R.id.tvmenuthread);
        imgThread=(ImageButton)findViewById(R.id.imgmenuthread);

        tvComment=(TextView)findViewById(R.id.tvmenucoment);
        imgComment=(ImageButton)findViewById(R.id.imgmenucoment);

        tvLogout=(TextView)findViewById(R.id.tvmenuout);
        imgLogout=(ImageButton)findViewById(R.id.imgmenuout);

        isiBtnClick();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ForumSetting.this, forum_main.class);
        startActivity(intent);
//        overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
        finish();
        overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
    }
    public void isiBtnClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumSetting.this, ForumList.class);
                startActivity(intent);
//                finish();
                overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
            }
        });
        imgThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumSetting.this, ForumList.class);
                startActivity(intent);
//                finish();
                overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
            }
        });
        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumSetting.this, CommentList.class);
                startActivity(intent);
//                finish();
                overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
            }
        });
        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumSetting.this, CommentList.class);
                startActivity(intent);
//                finish();
                overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumSetting.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumSetting.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
            }
        });
    }
}
