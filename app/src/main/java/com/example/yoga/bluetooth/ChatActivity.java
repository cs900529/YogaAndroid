package com.example.yoga.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yoga.CalibrationStage;
import com.example.yoga.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

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

    public void init() {
        et_msg = (EditText) findViewById(R.id.et_msg);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        Intent intent = getIntent();
        String remoteAddress = intent.getStringExtra("remoteAddress");
        client = new BluetoothClient(mHandler, remoteAddress);
        client.begin_listen();

        String filePath = getFilesDir().getPath() + "/yourFile.txt";
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(1000);
                        System.out.println(filePath);
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(client.getHeatmap());
                        fos.close();
                        System.out.println("OK");
                    } catch (InterruptedException | NullPointerException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }.start();


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
