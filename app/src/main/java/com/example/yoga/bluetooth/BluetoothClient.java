package com.example.yoga.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.UUID;
public class BluetoothClient {
    final String UUIDString = "00001101-0000-1000-8000-00805F9B34FB";
    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mSocket;
    private InputStream in;
    private PrintWriter out;
    private Object lock = new Object();
    public String filePath;
    byte[] bytes;

    public BluetoothClient(Handler handler, String remoteAddress) {
        mHandler = handler;
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
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void begin_listen(String Path) {
        filePath = Path;
        while (!mSocket.isConnected()) {
            connect();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    while (mSocket.isConnected()) {
                        byte[] bt = new byte[1024];
                        in.read(bt);
                        String content = new String (bt, "UTF-8" );
                        if (content!=null && !content.equals("")) {
                            String[] x = content.split("!");
                            StringToArray(x[0]);
                            Message msg = new Message();
                            msg.obj = content;
                            mHandler.sendMessage(msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Message msg = new Message();
                    msg.obj = "失去連線";
                    mHandler.sendMessage(msg);
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
