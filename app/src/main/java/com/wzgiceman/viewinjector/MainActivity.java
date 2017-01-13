package com.wzgiceman.viewinjector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.BindView;
import com.example.HelloWordAtion;
import com.example.WzgJector;

@HelloWordAtion("hello")
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.tv_other)
    TextView tvOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WzgJector.bind(this);
        if(tvMsg!=null){
            tvMsg.setText("我已经成功初始化了");
        }

        if(tvOther!=null){
            tvOther.setText("我就来看看而已");
        }
    }
}
