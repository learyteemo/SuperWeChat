package com.hyphenate.chatuidemo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.platform.comapi.map.L;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.db.UserDao;
import com.hyphenate.chatuidemo.ui.LoginActivity;
import com.hyphenate.chatuidemo.ui.MainActivity;

public class SplshMainActivity extends AppCompatActivity {
    private static final String TAG = SplshMainActivity.class.getSimpleName();
    static final long sleepTime = 2000;
    SplshMainActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splsh_main);
        mContext = this;
        super.onCreate(savedInstanceState);

        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        TextView versionText = (TextView) findViewById(R.id.tv_version);

        versionText.setText(getVersion());
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        rootLayout.startAnimation(animation);
    }
    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            public void run() {
                if (DemoHelper.getInstance().isLoggedIn()) {
                    // auto login mode, make sure all group and conversation is loaed before enter the main screen
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    long costTime = System.currentTimeMillis() - start;
                    //wait
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //enter main screen
                    startActivity(new Intent(SplshMainActivity.this, MainActivity.class));
                    finish();
                }else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    startActivity(new Intent(SplshMainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }).start();

    }
    /**
     * get sdk version
     */
    private String getVersion() {
        return EMClient.getInstance().getChatConfig().getVersion();
    }
}
