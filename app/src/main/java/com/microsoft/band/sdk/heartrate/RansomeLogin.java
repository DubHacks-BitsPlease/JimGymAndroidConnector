package com.microsoft.band.sdk.heartrate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class RansomeLogin extends Activity {

    LoginButton loginButton;
    CallbackManager callbackManager;
    String userId;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(activity);
        setContentView(R.layout.activity_login_facebook);
        AppEventsLogger.activateApp(getApplicationContext());
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");

        // Other app specific specialization

        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                userId = loginResult.getAccessToken().getUserId();
                Log.d("facebookId", userId);
                Intent intent = new Intent(activity, BandHeartRateAppActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle("Error:");
                alertDialog.setMessage("GG");
                alertDialog.show();
            }

            @Override
            public void onError(FacebookException exception) {
                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle("Error:");
                alertDialog.setMessage(exception.getMessage());
                alertDialog.show();

            }
        });
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_facebook, menu);
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
