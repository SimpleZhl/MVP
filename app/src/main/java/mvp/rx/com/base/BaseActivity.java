package mvp.rx.com.base;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by golfeven_zhl on 2018/6/9.
 */

public class BaseActivity extends Activity{
    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        //new ZTLUtils(mActivity).setTranslucentStatus();
    }
}
