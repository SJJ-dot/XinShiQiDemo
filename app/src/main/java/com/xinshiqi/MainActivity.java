package com.xinshiqi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import sjj.alog.Log;

public class MainActivity extends AppCompatActivity {

    SerialConnectionParameter parameter = new SerialConnectionParameter(57600);
    SerialConnection serialConnection = new SerialConnection(parameter);
    private Thread read;
    private EditText editText;
    private Thread send;
    private Thread connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });
        findViewById(R.id.disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    serialConnection.closeConnection();
                } catch (IOException e) {
                    Log.e("closeConnection", e);
                }
            }
        });
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSend();
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (send != null)
                    send.interrupt();
            }
        });
    }

    private void connect() {
        if (connect != null && connect.isAlive()) {
            connect.interrupt();
        }
        connect = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serialConnection.openConnection();
                    startRead();
                } catch (Exception e) {
                    Log.e("",e);
                }
            }
        });
        connect.start();
    }

    private void startSend() {
        if (send != null && send.isAlive()) {
            send.interrupt();
        }
        send = new Thread(new Runnable() {
            @Override
            public void run() {
                String string = editText.getText().toString();
                byte[] bytes = string.getBytes();
                try {
                    while (true) {

                        serialConnection.sendBuffer(bytes);
                        Log.e("send size:" + bytes.length + " data:" + toHex(bytes, bytes.length));
                        Thread.sleep(500);

                    }
                } catch (Exception e) {
                    Log.e("send", e);
                }
            }
        });
        send.start();
    }

    private void startRead() {
        if (read != null && read.isAlive()) {
            read.interrupt();
        }
        read = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buff = new byte[1024];
                try {
                    while (true) {
                        int i = serialConnection.readDataBlock(buff);
                        Log.e("read size:" + i + " data:" + toHex(buff, i));
                    }
                } catch (Exception e) {
                    Log.e("read", e);
                }

            }
        });
        read.start();
    }

    private String toHex(byte[] bytes, int length) {
        StringBuilder builder = new StringBuilder(length * 2);
        for (int i = 0; i < length; i++) {
            builder.append(String.format("%02X ", bytes[i]));
        }
        return builder.toString();
    }
}
