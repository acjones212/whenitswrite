package com.austinjones.whenitswrite;

/**
 * Created by austinjones on 3/31/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends FirebaseLoginBaseActivity {

    Firebase ref;
    ImageButton mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ref = new Firebase("https://whenitswrite.firebaseio.com");

        // Custom button that displays the Firebase login prompt
        mLoginBtn = (ImageButton) findViewById(R.id.loginButton);
        mLoginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFirebaseLoginPrompt();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        setEnabledAuthProvider(AuthProviderType.FACEBOOK);
    }

    @Override
    protected Firebase getFirebaseRef() {
        return ref;
    }

    @Override
    protected void onFirebaseLoginProviderError(FirebaseLoginError firebaseLoginError) {

    }

    @Override
    protected void onFirebaseLoginUserError(FirebaseLoginError firebaseLoginError) {

    }

    @Override
    public void onFirebaseLoggedIn(AuthData authData) {
        // Use a map to point to the user's songs
        // On successful login, an intent send the user to the MainActivity

        Map<String, Object> map = new HashMap<String, Object>();
        map = authData.getProviderData();
        Log.d("jiggery-pokery", String.valueOf(map.get("accessToken")));
        Log.d("jiggery-pokery2", String.valueOf(map.get("cachedUserProfile")));

        ref.child("users").child(authData.getUid()).setValue(map);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onFirebaseLoggedOut() {
        // TODO: Handle logout
    }
}

// Simple Firebase login below:

//    protected EditText emailEditText;
//    protected EditText passwordEditText;
//    protected Button loginBtn;
//    protected Button signUpButton;
//    CallbackManager callbackManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Map<String, Object> map = new HashMap<String, Object>();
//                Log.d("Loggin","loggin successful");
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
//
//        signUpButton = (Button) findViewById(R.id.signUpButton1);
//        emailEditText = (EditText) findViewById(R.id.emailField);
//        passwordEditText = (EditText) findViewById(R.id.passwordField);
//        loginBtn = (Button) findViewById(R.id.loginButton);
//
//        final Firebase ref = new Firebase("https://whenitswrite.firebaseio.com");
//
//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = emailEditText.getText().toString();
//                String password = passwordEditText.getText().toString();
//
//                email = email.trim();
//                password = password.trim();
//
//                if (email.isEmpty() || password.isEmpty()) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                    builder.setMessage(R.string.login_error_message)
//                            .setTitle(R.string.login_error_title)
//                            .setPositiveButton(android.R.string.ok, null);
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                } else {
//                    final String emailAddress = email;
//
//                    //Login with an email/password combination
//                    ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
//                        @Override
//                        public void onAuthenticated(AuthData authData) {
//                            // Authenticated successfully with payload authData
//                            Map<String, Object> map = new HashMap<String, Object>();
//                            map.put("email", emailAddress);
//                            ref.child("users").child(authData.getUid()).updateChildren(map);
//
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                        }
//
//                        @Override
//                        public void onAuthenticationError(FirebaseError firebaseError) {
//                            // Authenticated failed with error firebaseError
//                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                            builder.setMessage(firebaseError.getMessage())
//                                    .setTitle(R.string.login_error_title)
//                                    .setPositiveButton(android.R.string.ok, null);
//                            AlertDialog dialog = builder.create();
//                            dialog.show();
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//}
