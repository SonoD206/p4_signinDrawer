package jp.ac.jec.cm0120.p4_signindrawer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingActivity extends AppCompatActivity {

    private TextInputLayout textViewSettingMail;
    private TextInputLayout textViewSettingName;
    private TextInputLayout textViewSettingPhone;
    private TextInputLayout textViewSettingPassword;
    private TextInputLayout textViewSettingRepeatPassword;

    public static final String TAG = "###";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        textViewSettingMail = findViewById(R.id.textViewSettingMail);
        textViewSettingName = findViewById(R.id.textViewSettingName);
        textViewSettingPhone = findViewById(R.id.textViewSettingPhone);
        textViewSettingPassword = findViewById(R.id.textViewSettingPassword);
        textViewSettingRepeatPassword = findViewById(R.id.textViewSettingRepeatPassword);

        MaterialButton buttonSettingSignIn = findViewById(R.id.buttonSettingSignIn);
        MaterialButton buttonSettingSignUp = findViewById(R.id.buttonSettingSignUp);

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        buttonSettingSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            startActivity(intent);
        });

        textViewSettingMail.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                    /**
                     * ?????????????????????????????????????????????????????????
                     */
                    if (textViewSettingMail.getEditText().getText().toString().contains("@")) {
                        textViewSettingMail.setError("");
                    } else {
                        textViewSettingMail.setError("Error");
                    }
                    //???????????????????????????
                    inputMethodManager.hideSoftInputFromWindow(textViewSettingMail.getEditText().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        textViewSettingName.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                    //???????????????????????????
                    inputMethodManager.hideSoftInputFromWindow(textViewSettingMail.getEditText().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        textViewSettingPhone.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                    /**
                     * ???????????????11??????
                     */
                    if (textViewSettingPhone.getEditText().getText().toString().length() == 11) {
                        textViewSettingPhone.setError("");
                    } else {
                        textViewSettingPhone.setError("Error");
                    }
                    //???????????????????????????
                    inputMethodManager.hideSoftInputFromWindow(textViewSettingMail.getEditText().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        textViewSettingPassword.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                    /**
                     * password??????????????????????????????????????????????????????????????????
                     */
                    if (isContainPasswordPattern()) {
                        textViewSettingPassword.setError("");
                    } else {
                        textViewSettingPassword.setError("Error");
                    }
                    //???????????????????????????
                    inputMethodManager.hideSoftInputFromWindow(textViewSettingMail.getEditText().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        textViewSettingRepeatPassword.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                    //???????????????????????????
                    inputMethodManager.hideSoftInputFromWindow(textViewSettingMail.getEditText().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        buttonSettingSignUp.setOnClickListener(view -> {
            if (checkUserSignIn() == 5) {
                UserInfo tmp = new UserInfo();
                tmp.setMail(textViewSettingMail.getEditText().getText().toString());
                tmp.setName(textViewSettingName.getEditText().getText().toString());
                tmp.setPhoneNum(textViewSettingPhone.getEditText().getText().toString());
                tmp.setPassword(textViewSettingPassword.getEditText().getText().toString());

                UserInfoOpenHelper helper = new UserInfoOpenHelper(this);

                if (helper.insertUserInfoData(tmp)){
                    Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "??????????????????????????????????????????????????????????????????" , Toast.LENGTH_SHORT).show();
                }

            } else {
//                Toast.makeText(this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * User???SignIn???????????????????????????????????????
     *
     * @return ???????????????
     */
    private int checkUserSignIn() {

        int trueCount = 0;

        String userMail = textViewSettingMail.getEditText().getText().toString();
        String userName = textViewSettingName.getEditText().getText().toString();
        String userPhone = textViewSettingPhone.getEditText().getText().toString();
        String userPassword = textViewSettingPassword.getEditText().getText().toString();
        String userRepeatPassword = textViewSettingRepeatPassword.getEditText().getText().toString();

        /**
         * ?????????????????????
         */
        if (!userMail.equals("") && !userName.equals("") && !userPhone.equals("") && !userPassword.equals("") && !userRepeatPassword.equals("")) {
            trueCount++;
            textViewSettingMail.setError("");
            textViewSettingName.setError("");
            textViewSettingPhone.setError("");
            textViewSettingPassword.setError("");
            textViewSettingRepeatPassword.setError("");
        } else if (userMail.equals("")) {
            textViewSettingMail.setError("Error");
        } else if (userName.equals("")) {
            textViewSettingName.setError("Error");
        } else if (userPhone.equals("")) {
            textViewSettingPhone.setError("Error");
        } else if (userPassword.equals("")) {
            textViewSettingPassword.setError("Error");
        } else if (userRepeatPassword.equals("")) {
            textViewSettingRepeatPassword.setError("Error");
        }

        /**
         * ?????????????????????????????????????????????????????????
         */
        if (userMail.contains("@")) {
            trueCount++;
        } else {
            textViewSettingMail.setError("Error");
        }
        /**
         * ???????????????11??????
         */
        if (userPhone.length() == 11) {
            trueCount++;
        } else {
            textViewSettingPhone.setError("Error");
        }
        /**
         * password??????????????????????????????????????????????????????????????????
         */
        if (isContainPasswordPattern()) {
            trueCount++;
        } else {
            textViewSettingPassword.setError("Error");
        }
        /**
         * Password???Repeat Password??????????????????
         */
        if (userPassword.equals(userRepeatPassword)) {
            trueCount++;
            textViewSettingRepeatPassword.setError("");
        } else {
            textViewSettingRepeatPassword.setError("Error");
        }

        return trueCount;
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    private boolean isContainPasswordPattern() {
        boolean result = false;

        //???????????????
        String lowerCasePattern = "([a-z])";
        String upperCasePattern = "([A-Z])";
        String numberPattern = "([0-9])";

        String userPassword = textViewSettingPassword.getEditText().getText().toString();

        Pattern lowerPattern = Pattern.compile(lowerCasePattern);
        Matcher lowerMatcher = lowerPattern.matcher(userPassword);

        Pattern upperPattern = Pattern.compile(upperCasePattern);
        Matcher upperMatcher = upperPattern.matcher(userPassword);

        Pattern numPattern = Pattern.compile(numberPattern);
        Matcher numberMatcher = numPattern.matcher(userPassword);

        if (lowerMatcher.find() && upperMatcher.find() && numberMatcher.find()) {
            result = true;
        }
        return result;
    }

}