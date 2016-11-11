package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.data.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.ResultUtils;

public class FriendProfileActivity extends BaseActivity {


    @Bind(R.id.ivBack)
    ImageView mivBack;
    @Bind(R.id.txt_title)
    TextView mtxtTitle;
    @Bind(R.id.iv_avatar)
    ImageView mivAvatar;
    @Bind(R.id.tv_Username)
    TextView mtvUsername;
    @Bind(R.id.tv_weixinhao)
    TextView mtvWeixinhao;
    User user = null;
    @Bind(R.id.btn_send_msg)
    Button mbtnSendMsg;
    @Bind(R.id.btn_send_video)
    Button mbtnSendVideo;
    @Bind(R.id.btn_add_contact)
    Button btnAddContact;
    String username = null;
    boolean isFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        username =  getIntent().getStringExtra(I.User.USER_NAME);
        if (username == null) {
            MFGT.finish(this);
            return;
        }
        initView();
        user=SuperWeChatHelper.getInstance().getAppContactList().get(username);
        if (user == null) {
            isFriend=false;
        } else {
            setUserInfo();
            isFriend=true;
        }
        isFriend(isFriend);
        synUserInfo();
    }

private void syncFail(){
    MFGT.finish(this);
    return;
}

    private void synUserInfo() {
        NetDao.syncUserInfo(this, username, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null && result.isRetMsg()) {
                        user = (User) result.getRetData();
                        if (user != null) {
                            setUserInfo();
                            if (isFriend) {
                                SuperWeChatHelper.getInstance().saveAppContact(user);
                            }
                        } else {
                            syncFail();
                        }
                    } else {
                        syncFail();
                    }
                } else {
                    syncFail();
                }
            }

            @Override
            public void onError(String error) {
                syncFail();
            }
        });
    }

    private void initView() {
        mivBack.setVisibility(View.VISIBLE);
        mtxtTitle.setVisibility(View.VISIBLE);
        mtxtTitle.setText(getString(R.string.userinfo_txt_profile));

       // isFriend(true);
    }

    private void isFriend(boolean isFriend) {
        if (isFriend) {
            mbtnSendMsg.setVisibility(View.VISIBLE);
            mbtnSendVideo.setVisibility(View.VISIBLE);
        } else {
            btnAddContact.setVisibility(View.VISIBLE);
        }
    }

    private void setUserInfo() {
        EaseUserUtils.setAppUserAvatar(this, user.getMUserName(), mivAvatar);
        EaseUserUtils.setAppUserNick(user.getMUserNick(), mtvUsername);
        EaseUserUtils.setAppUserNamewithno(user.getMUserName(), mtvWeixinhao);
    }

    @OnClick({R.id.ivBack, R.id.btn_send_msg, R.id.btn_send_video,R.id.btn_add_contact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(this);
                break;
            case R.id.btn_send_msg:
                MFGT.gotoChat(this,user.getMUserName());
                break;
            case R.id.btn_send_video:
                if (!EMClient.getInstance().isConnected())
                    Toast.makeText(this, R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
                else {
                    startActivity(new Intent(this, VideoCallActivity.class).putExtra("username", user.getMUserName())
                            .putExtra("isComingCall", false));
                    // videoCallBtn.setEnabled(false);
                  //  inputMenu.hideExtendMenuContainer();
                }
                break;
            case R.id.btn_add_contact:
                MFGT.gotoAddFriendMsg(this, user.getMUserName());
                break;

        }
    }

 /*   @OnClick(R.id.ivBack)
    public void onClick() {
        MFGT.finish(this);
    }*/

}
