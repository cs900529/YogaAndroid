package com.example.yoga.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yoga.CalibrationStage;
import com.example.yoga.R;

public class ChatActivity extends AppCompatActivity {
    private BluetoothClient client;
    private EditText et_msg;
    private TextView tv_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
    }

    private void init() {
        et_msg = (EditText) findViewById(R.id.et_msg);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        Intent intent = getIntent();
        String remoteAddress = intent.getStringExtra("remoteAddress");
        client = new BluetoothClient(mHandler, remoteAddress);
        client.begin_listen();

        // intent 回瑜珈主程式
        Intent intent_main = new Intent(this, CalibrationStage.class);
        startActivity(intent_main);
    }

    public void onClick(View v) { //點擊發送訊息
        String content = et_msg.getText().toString().trim();
        if (content!=null && !content.equals("")) {
            client.send_msg(content);
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String content = (String) msg.obj;
            tv_msg.setText(content);
        }
    };
}
