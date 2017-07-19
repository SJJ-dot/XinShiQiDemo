package com.xinshiqi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

public class SerialConnection {
    private final SerialConnectionParameter parameter;
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private final String sPort = "/dev/ttyHSL0";
    private boolean _isOpen = false;
    private byte[] _bLoopData = new byte[]{0x30};
    private int iDelay = 500;
    // 数据位
    private int databits = 8;
    // 停止位
    private int stopbits = 1;
    // 校验位
    private int jiaoyanbits = 'N';

    // ----------------------------------------------------
    public SerialConnection(SerialConnectionParameter parameter) {
        this.parameter = parameter;
    }
    public void openConnection() throws IOException {
        mSerialPort = new SerialPort(new File(sPort), parameter.getBaudRate(), 0, databits, stopbits, jiaoyanbits);
        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();
        _isOpen = true;
    }

    public int readDataBlock(byte[] buffer) throws IOException {
        return mInputStream.read(buffer);
    }

    public void sendBuffer(byte[] buffer) throws IOException {
        mOutputStream.write(buffer);
    }

    public void closeConnection() throws IOException {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        mInputStream = null;
        mOutputStream = null;
        _isOpen = false;
    }

}