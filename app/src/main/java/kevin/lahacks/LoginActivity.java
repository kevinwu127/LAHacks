package kevin.lahacks;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kylemn on 4/4/15.
 */
public class LoginActivity extends Activity{
    private String user_ID;
    private Dialog progressDialog;
    private Intent intent;
    private static final String TAG = "SwipeMe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        intent = new Intent(this, HomeActivity.class);

        setContentView(R.layout.activity_main);

        // Check if there is a currently logged in user
        // and it's linked to a Facebook account.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Go to the user info activity
            showHomeActivity();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    public void onLoginClick(View v) {
        progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);

        List<String> permissions = Arrays.asList("public_profile", "email");
        // NOTE: for extended permissions, like "user_about_me", your app must be reviewed by the Facebook team
        // (https://developers.facebook.com/docs/facebook-login/permissions/)

        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                progressDialog.dismiss();
                if (user == null) {
                    Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d(TAG, "User signed up and logged in through Facebook!");
                    makeMeRequest();
                    showHomeActivity();
                } else {
                    Log.d(TAG, "User logged in through Facebook!");
                    makeMeRequest();
                    showHomeActivity();
                }
            }
        });
    }

    //add facebook name to user object in parse
    private void makeMeRequest() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    ParseUser.getCurrentUser().put("fbName", user.getFirstName());
                    ParseUser.getCurrentUser().saveInBackground();
                }
            }
        });
        request.executeAsync();
    }

    private void showHomeActivity() {
        // Start sinch service here as well
        // Get user id
        Log.i("LoginActivity", "Attempt to get user id");
        user_ID = ParseUser.getCurrentUser().getUsername();
        Log.i("LoginActivity", "user_ID: " + user_ID);
        startActivity(intent);
    }
}
