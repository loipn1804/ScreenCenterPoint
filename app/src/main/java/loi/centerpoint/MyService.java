package loi.centerpoint;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by user on 1/27/2016.
 */
public class MyService extends Service {

    private WindowManager manager;
    private CustomViewGroup view;
    private WindowManager.LayoutParams localLayoutParams;
    private Handler handler;
    private Handler handler_add;
    private Handler handler_remove;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("LIO", "currentThread " + Thread.currentThread().getId() + "-" + Thread.currentThread().getName());

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                String s = msg.getData().getString("message");
                Toast.makeText(MyService.this, s, Toast.LENGTH_SHORT).show();
            }
        };

        handler_add = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                manager.addView(view, localLayoutParams);
//                Log.e("service", "adview");
            }
        };

        handler_remove = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                manager.removeView(view);
                view = null;
//                Log.e("service", "removeview");
            }
        };

        startThread();

//        Log.e("service", "service onStartCommand");

        return Service.START_REDELIVER_INTENT;
    }

    private void sendMessage(String msg) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("message", msg);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void startThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        doIt();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void doIt() {
        Log.e("LIO", "doIt " + Thread.currentThread().getId() + "-" + Thread.currentThread().getName());
//        Log.e("service", "service doit");
        SharedPreferences preferences = getSharedPreferences("screen", MODE_PRIVATE);
        boolean is_show = preferences.getBoolean("is_show", false);
        boolean is_change = preferences.getBoolean("is_change", true);
        if (is_show) {
            if (view == null) {
                prepareView();

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("message", "abcd");
                message.setData(bundle);
                handler_add.sendMessage(message);
            } else {
                if (is_change) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", "abcd");
                    message.setData(bundle);
                    handler_remove.sendMessage(message);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("is_change", false);
                    editor.commit();
                }
            }
        } else {
            if (view != null) {
//                manager.removeView(view);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("message", "abcd");
                message.setData(bundle);
                handler_remove.sendMessage(message);

//                sendMessage("service REMOVE_VIEW");
            }
        }
    }

    private void prepareView() {
        SharedPreferences preferences = getSharedPreferences("screen", MODE_PRIVATE);

        manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        localLayoutParams = new WindowManager.LayoutParams(100, 100, 2007, 8, -2);
//        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
//        localLayoutParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        localLayoutParams.gravity = Gravity.CENTER;
        localLayoutParams.flags =
                // can touch or click
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                        // this is to enable the notification to recieve touch events
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |

                        // Draws over status bar
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;


//        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        localLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        int size = preferences.getInt("size", 1);
        int color = preferences.getInt("color", 1);
        int shape = preferences.getInt("shape", 1);
        int circle = 0;
        if (shape == 1) {
            circle = 2;
        }
        localLayoutParams.width = (int) ((size * 2 + 5 - circle) * getResources().getDisplayMetrics().scaledDensity);
        localLayoutParams.height = (int) ((size * 2 + 5 - circle) * getResources().getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;

        view = new CustomViewGroup(this);
        if (color == 1) {
            if (shape == 1) {
                view.setBackgroundResource(R.drawable.dr_circle_red);
            } else {
                view.setBackgroundResource(R.drawable.plus_red);
            }
        } else {
            if (shape == 1) {
                view.setBackgroundResource(R.drawable.dr_circle_blue);
            } else {
                view.setBackgroundResource(R.drawable.plus_blue);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("service", "service onDestroy");
    }
}
