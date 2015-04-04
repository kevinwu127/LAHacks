package kevin.lahacks;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;


public class PartySafeApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "60BFRxPwF1avRGGfSVooTNRBVEprTE79h8gOab1Z";
    public static final String YOUR_CLIENT_KEY = "omaruM0uBenX29MMjPqnu0lCs82CHkBUkTsotbkW";

    @Override
    public void onCreate() {
        super.onCreate();
        // Register your parse models here
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }



}
