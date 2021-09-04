package jp.ac.jec.cm0120.p4_signindrawer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    TextInputLayout textViewUserMail;
    TextInputLayout textViewUserPassword;

    public static final String TAG = "###";
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewUserMail = findViewById(R.id.textViewUserMail);
        textViewUserPassword = findViewById(R.id.textViewUserPassword);
        MaterialButton buttonSignIn = findViewById(R.id.buttonSignIn);
        MaterialButton buttonSignUp = findViewById(R.id.buttonSignUp);
        MaterialButton buttonSignInWithGoogle = findViewById(R.id.buttonSignInWithGoogle);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

        buttonSignInWithGoogle.setOnClickListener(view -> {
            Intent signIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signIntent,RC_SIGN_IN);
        });

        buttonSignIn.setOnClickListener(view -> {
            if (isSignIn(textViewUserMail.getEditText().getText().toString(),textViewUserPassword.getEditText().getText().toString())) {
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "ユーザー名とパスワードが間違っています", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,SettingActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            updateUI(account);
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {

    }

    /**
     * ログインが成功するか
     * @return
     */
    private boolean isSignIn(String name, String password){

        UserInfoOpenHelper helper = new UserInfoOpenHelper(this);
        ArrayList<UserInfo> arrayList = helper.findUser(name, password);

        String dbUserMail = "";
        String dbUserPassWord = "";

        for (UserInfo tmp: arrayList){
            Log.i(TAG, "isSignIn: ");

            dbUserMail = tmp.getMail();
            dbUserPassWord = tmp.getPassword();

            Log.i(TAG, "isSignIn:" + dbUserMail + ":" + dbUserPassWord);
        }

        if (dbUserMail.equals(textViewUserMail.getEditText().getText().toString()) &&
                dbUserPassWord.equals(textViewUserPassword.getEditText().getText().toString())){
            return true;
        }
        
        return false;
    }
}