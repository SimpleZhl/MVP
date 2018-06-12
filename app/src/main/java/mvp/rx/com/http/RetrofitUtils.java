package mvp.rx.com.http;



import com.socks.library.KLog;

import java.util.concurrent.TimeUnit;

import mvp.rx.com.common.converter.FastJsonConvertFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Retofit网络请求工具类
 * Created by HDL on 2016/7/29.
 */
public class RetrofitUtils {
    private static final int READ_TIMEOUT = 60;//读取超时时间,单位  秒
    private static final int CONN_TIMEOUT = 12;//连接超时时间,单位  秒

    private static Retrofit mRetrofit;

    private RetrofitUtils() {

    }

    public static Retrofit newInstence(String url) {
        mRetrofit = null;
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        KLog.e("HttpTAG", message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BASIC))
                //.cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
                .readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)//设置读取时间为一分钟
                .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)//设置连接时间为12s
                .build();//初始化一个client,不然retrofit会自己默认添加一个

        mRetrofit = new Retrofit.Builder()
                .client(client)//添加一个client,不然retrofit会自己默认添加一个
                .baseUrl(url)
                //.addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(FastJsonConvertFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return mRetrofit;
    }
}
