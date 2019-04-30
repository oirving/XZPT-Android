//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.TextView;
//
//import com.djylrz.xzpt.R;
//import com.djylrz.xzpt.bean.User;
//import com.djylrz.xzpt.net.HttpClient;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class TestHttpActivity extends AppCompatActivity {
//
//    private TextView textView;
//
//    private String token;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        textView= findViewById(R.id.hello);
//
////        loginTest();
//        loginWithTokenTest();
//    }
//
//
//    private void loginWithTokenTest() {
//        //while(token==null);
//        token="eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJQQVNTV09SRCI6IjM4M25tdmRwb25xYmlic2VpODkwcXZpbTVwMHIyajYiLCJVU0VSX0lEIjoiOGZjNjRmNjY1ZDUwNDIzMDkzNGVkOGU2MzlmNTI5MzAiLCJleHAiOjE1NTcyMjU4NzF9.Rvcv67pmbj4udItZrk4ql0tewqRY3NsHd46My4OGOBU";
//        if(token!=null){
//
//            HttpClient myOkHttp = HttpClient.getInstance();
//            Map<String,Object> map = new HashMap<>();
//            map.put("token",token);
//            HttpClient.MyCallBack myCallBack=myOkHttp.new MyCallBack(getApplicationContext()) {
//                @Override
//                protected void onSuccess(TempResponseData responseData) {
//                    // 因为当前线程属于子线程，需要使用runOnUiThread来重绘UI（只能在主线程中进行）
//                    runOnUiThread(new Thread(){
//                        @Override
//                        public void run() {
//                            textView.setText("Login in with token success!");
//                        }
//                    });
//                }
//            };
//            myOkHttp.post("/user/vertifytoken", map, null,myCallBack);
//
//        }
//    }
//
//    private void loginTest() {
//
//        User user = new User();
//        user.setEmail("1732626355@qq.com");
//        user.setPasswd("625326143");
//
//        HttpClient myOkHttp = HttpClient.getInstance();
//
//        // 构建回调函数
//        HttpClient.MyCallBack myCallBack=myOkHttp.new MyCallBack(getApplicationContext()) {
//            @Override
//            protected void onSuccess(TempResponseData responseData) {
//                // 因为当前线程属于子线程，需要使用runOnUiThread来重绘UI（只能在主线程中进行）
//                runOnUiThread(new Thread(){
//                    @Override
//                    public void run() {
//                        textView.setText("Login in with passwd success!");
//                        token = (String)(responseData.getResultObject());
//                    }
//                });
//            }
//        };
//        myOkHttp.post("/user/login", null, user,myCallBack);
//    }
//}
//
//
//
