package com.xinshiqi;

/**
 * Created by sjj on 2017/7/10.
 */

public class SerialConnectionParameter  {
    private final int baudRate;

    public SerialConnectionParameter(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getBaudRate() {
        return baudRate;
    }
}
