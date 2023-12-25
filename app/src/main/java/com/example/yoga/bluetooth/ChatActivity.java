package com.example.yoga.bluetooth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yoga.CalibrationStage;
import com.example.yoga.GlobalVariable;
import com.example.yoga.R;

public class ChatActivity extends AppCompatActivity {
    private BluetoothClient client;
    private GlobalVariable global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bluetooth);
        init();
    }

    public void init() {
        Intent intent = getIntent();
        String remoteAddress = intent.getStringExtra("remoteAddress");
        String filePath = getFilesDir().getPath() + "/yourFile.txt";
        client = new BluetoothClient(remoteAddress); //mHandler,
        client.begin_listen(filePath);

        global = (GlobalVariable) getApplication();
        global.setCurrentMS(intent.getIntExtra("currentMS",0));

        // intent 回瑜珈主程式
        Intent intent_main = new Intent(this, CalibrationStage.class);
        startActivity(intent_main);

        finish();
    }
}
