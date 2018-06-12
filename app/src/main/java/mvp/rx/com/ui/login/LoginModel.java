package mvp.rx.com.ui.login;

import android.content.Context;

import com.socks.library.KLog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.functions.Consumer;
import mvp.rx.com.base.APIService;
import mvp.rx.com.base.GlobalField;
import mvp.rx.com.base.OnHttpCallBack;
import mvp.rx.com.bean.TokenResult;
import mvp.rx.com.bean.UserHttpResult;
import mvp.rx.com.bean.UserInfo;
import mvp.rx.com.common.rxhelper.RxSchedulerHepler;
import mvp.rx.com.http.RetrofitUtils;
import retrofit2.HttpException;

/**
 * 登录的业务处理类
 * Created by zhl on 2016/7/22.
 */
public class LoginModel implements LoginContract.ILoginModel {

    /**
     * 登录的具体业务处理--------网络请求都在这里做喽
     * --------V层   负责响应用户的交互(获取数据---->提示操作结果)
     * --------P层   传递完数据给M层处理之后,实例化回调对象,成功了就通知V层登录成功,失败了就通知V层显示错误信息
     * --------M层   对P层传递过来的userInfo进行登录(网络请求)判断,处理完成之后将处理结果回调给P层
     *
     * @param userInfo P层传递过来的数据
     * @param callBack P层传递过来的回调对象----------说白了就是成功还是失败了,你总的告诉我一声,我好通知V层来处理[这里可以不用回调,在代码中使用EventBus或者传递一个Handler过来也可以,不建议这样操作]
     */
    @Override
    public void login(UserInfo userInfo, final OnHttpCallBack<TokenResult> callBack) {

        //登录的网络请求
        RetrofitUtils.newInstence(GlobalField.BASEURL)//实例化Retrofit对象
                .create(APIService.class)//创建Rxjava---->LoginService对象
                .userLogin(userInfo.getUserName(), userInfo.getPwd())//调用登录的接口
                .compose(RxSchedulerHepler.<UserHttpResult<TokenResult>>io_main())
                .subscribe(new Consumer<UserHttpResult<TokenResult>>() {
                    @Override
                    public void accept(UserHttpResult<TokenResult> loginResultUserHttpResult) throws Exception {
                        if (loginResultUserHttpResult.getResultCode() == 0) {
                            callBack.onSuccessful(loginResultUserHttpResult.getData());//登录成功------获取完数据,返回给P---P获取到数据之后就将数据交回给V
                        } else {
                            callBack.onFaild("用户名或密码错误!");//登录失败
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) throws Exception {
                        KLog.e(e.getMessage() + "--");
                        e.printStackTrace();
                        //失败的时候调用-----一下可以忽略 直接 callBack.onFaild("请求失败");
                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            //httpException.response().errorBody().string()
                            int code = httpException.code();
                            if (code == 500 || code == 404) {
                                callBack.onFaild("服务器出错");
                            }
                        } else if (e instanceof ConnectException) {
                            callBack.onFaild("网络断开,请打开网络!");
                        } else if (e instanceof SocketTimeoutException) {
                            callBack.onFaild("网络连接超时!!");
                        } else {
                            callBack.onFaild("发生未知错误" + e.getMessage());
                            KLog.e("Myloy", e.getMessage());
                        }
                    }
                });
    }

    @Override
    public void saveUserInfo(Context context, UserInfo user, String token) {
        KLog.e("开始保存用户信息" + user.toString());
        context.getSharedPreferences(GlobalField.USERINFO_SP_NAME, Context.MODE_PRIVATE).edit()
                .putString("userName", user.getUserName())
                .putString("pwd", user.getPwd())
                .putString("address", user.getAddress())
                .putString("phone", user.getPhone())
                .putString("token", token)
                .commit();
    }
}
