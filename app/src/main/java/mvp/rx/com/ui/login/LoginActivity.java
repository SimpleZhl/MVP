package mvp.rx.com.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mvp.rx.com.R;
import mvp.rx.com.base.BaseActivity;
import mvp.rx.com.bean.UserInfo;

/**
 * Created by golfeven_zhl on 2018/6/9.
 */

public class LoginActivity extends BaseActivity implements LoginContract.ILoginView {

    @BindView(R.id.et_login_userName)
    EditText etLoginUserName;
    @BindView(R.id.et_login_pwd)
    EditText etLoginPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        tvTitle.setText("用户登录");
        mLoginPresenter = new LoginPresenter(this);
    }


    @Override
    public Context getCurContext() {
        return mActivity;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showInfo(String info) {

    }

    @Override
    public void showErrorMsg(String msg) {

    }

    @Override
    public void toMain() {

    }

    @Override
    public void toRegister() {

    }

    @Override
    public UserInfo getUserLoginInfo() {
        return null;
    }

    public void onRegister(View view) {
        toRegister();
    }

    public void onLogin(View view) {
        mLoginPresenter.login();
    }

    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
