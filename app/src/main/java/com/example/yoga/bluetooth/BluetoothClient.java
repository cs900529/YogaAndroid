package com.example.yoga.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.UUID;
public class BluetoothClient {
    final String UUIDString = "00001101-0000-1000-8000-00805F9B34FB";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mSocket;
    private InputStream in;
    private PrintWriter out;
    private Object lock = new Object();
    public String filePath;
    public Boolean flag;
    byte[] bytes;

    public BluetoothClient(String remoteAddress) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice remoteDevice = mBluetoothAdapter.getRemoteDevice(remoteAddress);
        try {
            mSocket = remoteDevice.createRfcommSocketToServiceRecord(UUID.fromString(UUIDString));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void StringToArray(String str) {
        // 調用 python function "get_heatmap"
        Python python = Python.getInstance();
        PyObject pyObject = python.getModule("heatmap");
        bytes = pyObject.callAttr("get_heatmap", str).toJava(byte[].class);

        // 取得 yogamat 的data
        //int[][] test = pyObject.callAttr("get_rects").toJava(int[][].class);

        savePNG();

        // 回傳給 raspberrypi "done"
        send_msg("done");
    }

    // 儲存 heatmap PNG 供Kotlin使用
    public void savePNG() {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(bytes);
            fos.close();
            System.out.println("save file done!" + filePath);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void connect(){
        new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (lock) {
                        if (mSocket.isConnected()) {
                            return;
                        }
                        mSocket.connect(); //需要在子thread中執行，以免堵塞
                        in = mSocket.getInputStream();
                        out = new PrintWriter(mSocket.getOutputStream());
                        flag = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void begin_listen(String Path) {
        filePath = Path;

        new Thread() {
            @Override
            public void run() {
                while (!mSocket.isConnected()) {
                    connect();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("bluetooth connecting");
                    }
                }

                try {
                    while (mSocket.isConnected()) {
                        byte[] bt = new byte[1024];
                        in.read(bt);
                        String content = new String (bt, "UTF-8" );
                        if (content!=null && !content.equals("")) {
                            String[] x = content.split("!");
                            StringToArray(x[0]);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("bluetooth connection loss");
                }
            }
        }.start();
    }

    public void send_msg(final String content) {
        new Thread() {
            @Override
            public void run() {
                out.print(content);
                out.flush ();
            }
        }.start();
    }
}
