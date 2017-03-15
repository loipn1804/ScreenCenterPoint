package loi.centerpoint;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private ToggleButton toggleBtn;
    private RadioButton radioSmall;
    private RadioButton radioMedium;
    private RadioButton radioLarge;
    private RadioButton radioRed;
    private RadioButton radioBlue;
    private RadioButton radioCircle;
    private RadioButton radioPlus;

    private LinearLayout lnlAdView;
    private AdView adView;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

        SharedPreferences preferences = getSharedPreferences("admob", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int count = preferences.getInt("count", 1);
        if (count > 3) {
            initialAdmob();
        } else {
            count++;
            editor.putInt("count", count);
            editor.commit();
        }
    }

    private void initView() {
        toggleBtn = (ToggleButton) findViewById(R.id.toggleBtn);
        radioSmall = (RadioButton) findViewById(R.id.radioSmall);
        radioMedium = (RadioButton) findViewById(R.id.radioMedium);
        radioLarge = (RadioButton) findViewById(R.id.radioLarge);
        radioRed = (RadioButton) findViewById(R.id.radioRed);
        radioBlue = (RadioButton) findViewById(R.id.radioBlue);
        radioCircle = (RadioButton) findViewById(R.id.radioCircle);
        radioPlus = (RadioButton) findViewById(R.id.radioPlus);
    }

    private void initData() {
        SharedPreferences preferences = getSharedPreferences("screen", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        boolean is_show = preferences.getBoolean("is_show", false);
        toggleBtn.setChecked(is_show);

        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getSharedPreferences("screen", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("is_show", isChecked);
                editor.commit();
            }
        });

        int size = preferences.getInt("size", 1);
        switch (size) {
            case 1:
                radioSmall.setChecked(true);
                break;
            case 2:
                radioMedium.setChecked(true);
                break;
            case 3:
                radioLarge.setChecked(true);
                break;
        }

        radioSmall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences preferences = getSharedPreferences("screen", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("size", 1);
                    editor.putBoolean("is_change", true);
                    editor.commit();
                }
            }
        });

        radioMedium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences preferences = getSharedPreferences("screen", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("size", 2);
                    editor.putBoolean("is_change", true);
                    editor.commit();
                }
            }
        });

        radioLarge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences preferences = getSharedPreferences("screen", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("size", 3);
                    editor.putBoolean("is_change", true);
                    editor.commit();
                }
            }
        });

        int color = preferences.getInt("color", 1);
        switch (color) {
            case 1:
                radioRed.setChecked(true);
                break;
            case 2:
                radioBlue.setChecked(true);
                break;
        }

        radioRed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences preferences = getSharedPreferences("screen", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("color", 1);
                    editor.putBoolean("is_change", true);
                    editor.commit();
                }
            }
        });

        radioBlue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences preferences = getSharedPreferences("screen", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("color", 2);
                    editor.putBoolean("is_change", true);
                    editor.commit();
                }
            }
        });

        int shape = preferences.getInt("shape", 1);
        switch (shape) {
            case 1:
                radioCircle.setChecked(true);
                break;
            case 2:
                radioPlus.setChecked(true);
                break;
        }

        radioCircle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences preferences = getSharedPreferences("screen", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("shape", 1);
                    editor.putBoolean("is_change", true);
                    editor.commit();
                }
            }
        });

        radioPlus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences preferences = getSharedPreferences("screen", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("shape", 2);
                    editor.putBoolean("is_change", true);
                    editor.commit();
                }
            }
        });

        setUpService();
    }

    private void setUpService() {
        Log.e("LIO", "isForegroundServiceRunning " + isForegroundServiceRunning(MyService.class));
        if (!isForegroundServiceRunning(MyService.class)) {
            Intent intent = new Intent(this, MyService.class);
            startService(intent);
        }
    }

    private boolean isForegroundServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void touchSample() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(100, 100, 2007, 8, -3);
        Button bb = new Button(this);
        bb.setText("Button");
        bb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        bb.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "Touched", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        params.gravity = Gravity.RIGHT | Gravity.TOP;
        params.setTitle("Load Average");
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(bb, params);
    }

    public void initialAdmob() {
        lnlAdView = (LinearLayout) findViewById(R.id.lnlAdView);
        adView = new AdView(this);
        adRequest = new AdRequest.Builder().build();
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-6956931160448072/4724617542");

        lnlAdView.addView(adView);
        adView.loadAd(adRequest);
    }
}
