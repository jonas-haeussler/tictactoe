package com.game.tictactoe;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import com.game.tictactoe.objects.MapGrid;
import com.game.tictactoe.screens.PlayableScreen;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BlueToothConnection {
    private TicTacToeGame game;
    private BluetoothAdapter bluetoothAdapter;
    private AndroidLauncher launcher;
    private int requestCode;
    private boolean BT;
    private ArrayList<BluetoothDevice> btdevices;
    private BluetoothSocket socket;
    private BluetoothServerSocket serverSocket;
    private final Object threadCommunicator1, threadCommunicator2;
    private final UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private final String APP_NAME = "tictactoe";
    private InputStream in;
    private OutputStream out;
    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    public BlueToothConnection(final TicTacToeGame game, final AndroidLauncher launcher, int requestCode, final Object threadCommunicator1, final Object threadCommunicator2){
        this.game = game;
        this.threadCommunicator1 = threadCommunicator1;
        this.threadCommunicator2 = threadCommunicator2;
        btdevices = new ArrayList<BluetoothDevice>();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        this.launcher = launcher;
        this.requestCode =requestCode;


            new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        synchronized (threadCommunicator1) {
                            threadCommunicator1.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (bluetoothAdapter != null) {
                        try {
                            if (serverSocket == null) {
                                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BT = true;
                        if (bluetoothAdapter.isEnabled()) {
                            launcher.setBT(true);
                        }
                    }
                    System.out.println("hirerere");
                    socket = null;
                    // Keep listening until exception occurs or a socket is returned.
                    while (true) {
                        if (serverSocket != null) {
                            try {
                                System.out.println("Waiting for connection " + MY_UUID);
                                socket = serverSocket.accept();
                                System.out.println("Connected");
                            } catch (IOException e) {
                                System.out.println("End the serversocket");
                                break;
                            }
                        }
                        if (socket != null) {
                            try {
                                in = socket.getInputStream();
                                out = socket.getOutputStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            playOnBluetooth();
                            break;
                        }

                    }

                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothDevice device = null;
                outerloop:
                while (true) {
                    try {
                        synchronized (threadCommunicator2) {
                            threadCommunicator2.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("thread starts");
                    if (socket == null) {
                        System.out.println("Adress: " + game.activeBTName);
                        device = getDevice(game.activeBTName);
                        try {
                            if (device != null) {
                                System.out.println("Found device");
                                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    while (socket != null) {
                        bluetoothAdapter.cancelDiscovery();
                        try {
                            System.out.println("Trying to connect with " + device);
                            socket.connect();
                            System.out.println("Connected");
                            if (serverSocket != null) {
                                game.isServer = true;
                            }
                            in = socket.getInputStream();
                            out = socket.getOutputStream();
                            System.out.println("Socket con established");
                        } catch (IOException e) {
                            e.printStackTrace();
                            if (socket != null) {
                                System.out.println("Connection failed " + socket);
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                if (device != null) {
//                                    try {
//                                        socket = device.createRfcommSocketToServiceRecord(MY_UUID);
//                                    } catch (IOException e1) {
//                                        e1.printStackTrace();
//                                    }
                                    continue;
                                }
                            }
                            continue outerloop;

                        }
                        receiveData();
                        playOnBluetooth();

                    }
                }
            }
        }).start();

    }
    private void playOnBluetooth(){
            do {
                if(game.player == 0){
                    game.player = 2;
                }

                game.btConnected = true;
                if(game.getScreen() instanceof PlayableScreen) {
                    synchronized ((PlayableScreen) game.getScreen()) {
                        try {
                            ((PlayableScreen) game.getScreen()).wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Sending");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < game.getFieldSize(); i++) {
                        for (int j = 0; j < game.getFieldSize(); j++) {
                            for (int k = 0; k < game.getFieldSize(); k++) {
                                for (int l = 0; l < game.getFieldSize(); l++) {
                                    try {
                                        MapGrid[][] mapGrids = game.getMapGrids();
                                        if (mapGrids != null) {
                                            System.out.print(mapGrids[i][j].getArray()[k][l]);
                                            out.write(mapGrids[i][j].getArray()[k][l]);
                                        }
                                    } catch (IOException e) {
                                        game.btConnected = false;
                                        System.out.println("Connection failed");
                                    }
                                }
                                System.out.println();
                            }

                        }
                    }
                    try {
                        out.flush();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Receiving");
                    MapGrid[][] originalMapGrids = game.getMapGrids();
                    for (int i = 0; i < game.getFieldSize(); i++) {
                        for (int j = 0; j < game.getFieldSize(); j++) {
                            for (int k = 0; k < game.getFieldSize(); k++) {
                                for (int l = 0; l < game.getFieldSize(); l++) {
                                    try {
                                        if (originalMapGrids != null) {
                                            int temp = in.read();
                                            if (temp != -1) {
//                                            System.out.print(temp);
                                                if ((byte) temp != originalMapGrids[i][j].getArray()[k][l]) {
                                                    game.addPlayerMove(i, j, k, l);
                                                }
                                            }
                                        }
                                        else {
                                            game.btConnected = false;
                                            return;
                                        }
                                    } catch (IOException e) {
                                        game.btConnected = false;
                                        System.out.println("Connection failed");
                                    }
                                }
                                System.out.println();
                            }

                        }
                    }
                }
            } while (game.btConnected);
    }
    private void receiveData(){
        game.player = 1;
        game.btConnected = true;
        System.out.println("Receiving");
        MapGrid[][] originalMapGrids = null;
        while(originalMapGrids == null){
            originalMapGrids = game.getMapGrids();
        }
        for (int i = 0; i < game.getFieldSize(); i++) {
            for (int j = 0; j < game.getFieldSize(); j++) {
                for (int k = 0; k < game.getFieldSize(); k++) {
                    for (int l = 0; l < game.getFieldSize(); l++) {
                        try {
                            if(originalMapGrids != null) {
                                int temp = in.read();
                                if(temp != -1) {
//                                System.out.print(temp);
                                    if ((byte) temp != originalMapGrids[i][j].getArray()[k][l]) {
                                        game.addPlayerMove(i, j, k, l);
                                    }
                                }
                                else {
                                    game.btConnected = false;
                                    return;
                                }
                            }
                        } catch (IOException e) {
                            System.out.println("Connection timed out");
                            game.btConnected = false;
                        }
                    }
                    System.out.println();
                }

            }
        }
    }
    public void onBluetoothMode(){
        if(bluetoothAdapter != null && !bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            launcher.startActivityForResult(enableBtIntent, requestCode);
        }
        else if(bluetoothAdapter != null && bluetoothAdapter.isEnabled()){
            game.btOn = true;
        }
    }
    public ArrayList<String>[] getDevices() {
        ArrayList<String>[] devices = new ArrayList[2];
        devices[0] = new ArrayList<String>();
        devices[1] = new ArrayList<String>();
        btdevices.clear();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        btdevices.add(device);
                        devices[0].add(device.getName());
                        devices[1].add(device.getAddress());
                    }
                }
            }
        }
        return devices;
    }

    public void cancel() {
        try {
            if(serverSocket != null){
                serverSocket.close();
                serverSocket = null;
            }
            if(socket != null){
                socket.close();
                System.out.println("Socket closed");
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BluetoothDevice getDevice(String name){
        for (BluetoothDevice device : btdevices) {
            if(device.getName().equals(name)){
                return device;
            }
        }
        return null;
    }

}
