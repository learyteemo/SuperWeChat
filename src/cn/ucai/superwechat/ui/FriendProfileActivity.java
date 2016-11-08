package cn.ucai.superwechat.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.utils.MFGT;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra(I.User.USER_NAME);
        if (user == null) {
            MFGT.finish(this);
        }
        initView();

    }

    private void initView() {
        mivBack.setVisibility(View.VISIBLE);
        mtxtTitle.setVisibility(View.VISIBLE);
        mtxtTitle.setText(getString(R.string.userinfo_txt_profile));
        setUserInfo();
        isFriend();
    }

    private void isFriend() {
        if (SuperWeChatHelper.getInstance().getAppContactList().containsKey(user.getMUserName())) {
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
                break;
            case R.id.btn_send_video:
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
