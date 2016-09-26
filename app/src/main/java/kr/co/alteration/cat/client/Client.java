package kr.co.alteration.cat.client;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;

/**
 * Created by blogc on 2016-09-23.
 */
public class Client {
    private final String TAG = "Client";
    private Context context;
    private ReceiveThread receiveThread;
    private Socket socket;
    private BufferedInputStream bufferedInputStream;

    public Client(Context context) {
        if (context != null) {
            this.context = context;
        }
    }

    public JSONObject getResult() {
        receiveThread = new ReceiveThread();
        receiveThread.start();
        try {
            receiveThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject result = receiveThread.getResult();

        if (result != null) {
            return result;
        } else {
            return null;
        }
    }

    private class ReceiveThread extends Thread {
        private byte[] header = new byte[ClientConstants.HEADER_SIZE];
        private JSONObject result = null;

        @Override
        public void run() {
            try {
                SocketAddress socketAddress = new InetSocketAddress(ClientConstants.SERVER_IP, Integer.parseInt(ClientConstants.SERVER_PORT));
                socket = new Socket();
                socket.connect(socketAddress);
            } catch (IOException e) {
                Log.d(TAG, "Socket Open Failed");
                e.printStackTrace();
                return;
            }


            try {
                bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            } catch (IOException e) {
                Log.d(TAG, "InputStream Open Failed");
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return;
            }



            try {
                Log.d(TAG, String.valueOf(bufferedInputStream.read(header, 0, ClientConstants.HEADER_SIZE)));
            } catch (IOException e) {
                Log.d(TAG, "Failed to read Header");
                e.printStackTrace();
                try {
                    socket.shutdownInput();
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                return;
            }

            int dataSize = 0;

            try {
                dataSize = Integer.parseInt(new String(header, "UTF-8").trim().replace(" ", "").trim());
            } catch (UnsupportedEncodingException e) {
                Log.d(TAG, "Unsupported Character found");
                e.printStackTrace();

                try {
                    socket.shutdownInput();
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                return;
            } catch (NumberFormatException e) {
                Log.d(TAG, "NumberFormatException found");
                e.printStackTrace();

                    Log.d(TAG, Arrays.toString(header));
                try {
                    socket.shutdownInput();
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                return;
            }


            byte[] data = new byte[dataSize];

            try {
                bufferedInputStream.read(data);
            } catch (IOException e) {
                Log.d(TAG, "Failed to read data");
                e.printStackTrace();

                try {
                    socket.shutdownInput();
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                return;
            }

            try {
                JSONObject received = new JSONObject(new String(data, "UTF-8"));

                if (received != null) {
                    result = received;
                } else {
                    result = null;
                }

            } catch (UnsupportedEncodingException e) {
                Log.d(TAG, "Unsupported Character found");
                e.printStackTrace();
                try {
                    socket.shutdownInput();
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            } catch (JSONException e) {
                Log.d(TAG, "No json found");

                e.printStackTrace();
                try {
                    socket.shutdownInput();
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            try {
                Log.d(TAG, "Client Ends");
                socket.shutdownInput();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
            if (!MainActivity.getAlarmStatus()) {
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    if(!pm.isInteractive()) {
                        Intent intent = new Intent(context, AlarmActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {

                    }
                } else {
                    if (!pm.isScreenOn()) {
                        Intent intent = new Intent(context, AlarmActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {

                    }

                }
            } else {
                Log.d(TAG, "Alarm is disabled");
            }*/
        }

        public JSONObject getResult() {
            if (result != null) {
                return result;
            } else {
                return null;
            }
        }
    }
}
