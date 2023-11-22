package com.example.yoga.discover;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.yoga.bluetooth.ChatActivity;
import com.example.yoga.R;

import java.util.ArrayList;

public class BluetoothActivity extends AppCompatActivity {
    private ListView listView;
    private DevicesAdapter adapter;
    private ArrayList<BluetoothDevice> list;
    private ScanDevices mScanDevices;

    public static final String[] permissions = {
            "android.permission.BLUETOOTH",
            "android.permission.BLUETOOTH_ADMIN",
            "android.permission.BLUETOOTH_PRIVILEGED",
            "android.permission.BLUETOOTH_CONNECT",
            "android.permission.BLUETOOTH_SCAN",
            "android.permission.BLUETOOTH_ADVERTISE",
            "android.permission.ACCESS_COARSE_LOCATION"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bluetooth);
        if(Build.VERSION.SDK_INT>=23){
            requestPermissions(permissions,1);
        }

        //初始化python環境
        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }
        Python python=Python.getInstance();

        // 調用hello_python.py裡面的Python_say_Hello函式
        int x = 8;
        PyObject pyObject=python.getModule("hello_python");
        x = pyObject.callAttr("Python_say_Hello", x).toJava(int.class);

        // 照理來說會 print 出 32，驗證正確性
        System.out.println(x);

        init();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.listview);
        list = new ArrayList<>();
        adapter = new DevicesAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(icl);
        mScanDevices = new ScanDevices();
        initReceiver();
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    public void onClick(View view){ //onClick 搜尋設備
        list.clear();
        list.addAll(mScanDevices.getBondedDevices()); //先添加已綁定過的設備
        adapter.notifyDataSetChanged();
        mScanDevices.startDiscovery(); //搜尋附近的設備
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothDevice.ACTION_FOUND: //接收附近的裝置
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String address = device.getAddress();
                    for (int i = 0; i < list.size(); i++) { //避免有重複的裝置出現
                        if (address == null || address.equals(list.get(i).getAddress())) {
                            return;
                        }
                    }
                    list.add(device);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    AdapterView.OnItemClickListener icl = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String address = list.get(position).getAddress();
            startChat(address);
        }
    };

    private void startChat(String remoteAddress) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("remoteAddress", remoteAddress);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
