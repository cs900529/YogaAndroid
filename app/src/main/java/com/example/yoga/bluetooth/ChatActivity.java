package com.example.yoga.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.yoga.View.CalibrationStage;
import com.example.yoga.R;

import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private BluetoothClient client;

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

        if(!Objects.equals(remoteAddress, "0")){
            client = new BluetoothClient(remoteAddress); //mHandler,
            client.begin_listen(filePath);
        }

        // intent 回瑜珈主程式
        Intent intent_main = new Intent(this, CalibrationStage.class);
        startActivity(intent_main);

        finish();
    }
}
