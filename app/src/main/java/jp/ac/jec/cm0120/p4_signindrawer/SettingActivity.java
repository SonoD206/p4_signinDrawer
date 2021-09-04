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
                     * ユーザーのメルアドに＠が含まれているか
                     */
                    if (textViewSettingMail.getEditText().getText().toString().contains("@")) {
                        textViewSettingMail.setError("");
                    } else {
                        textViewSettingMail.setError("Error");
                    }
                    //キーボードを閉じる
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
                    //キーボードを閉じる
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
                     * 電話番号が11桁か
                     */
                    if (textViewSettingPhone.getEditText().getText().toString().length() == 11) {
                        textViewSettingPhone.setError("");
                    } else {
                        textViewSettingPhone.setError("Error");
                    }
                    //キーボードを閉じる
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
                     * passwordに大文字小文字英数字全てを含んだ６文字以上か
                     */
                    if (isContainPasswordPattern()) {
                        textViewSettingPassword.setError("");
                    } else {
                        textViewSettingPassword.setError("Error");
                    }
                    //キーボードを閉じる
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
                    //キーボードを閉じる
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
                    Toast.makeText(this, "登録できました", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "登録に失敗しました。ユーザー名が使えません。" , Toast.LENGTH_SHORT).show();
                }

            } else {
//                Toast.makeText(this, "そのユーザー名は使われています", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "エラー箇所が存在します", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * UserがSignInで切るかどうかのチェッカー
     *
     * @return 成功した数
     */
    private int checkUserSignIn() {

        int trueCount = 0;

        String userMail = textViewSettingMail.getEditText().getText().toString();
        String userName = textViewSettingName.getEditText().getText().toString();
        String userPhone = textViewSettingPhone.getEditText().getText().toString();
        String userPassword = textViewSettingPassword.getEditText().getText().toString();
        String userRepeatPassword = textViewSettingRepeatPassword.getEditText().getText().toString();

        /**
         * 全て入力済みか
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
         * ユーザーのメルアドに＠が含まれているか
         */
        if (userMail.contains("@")) {
            trueCount++;
        } else {
            textViewSettingMail.setError("Error");
        }
        /**
         * 電話番号が11桁か
         */
        if (userPhone.length() == 11) {
            trueCount++;
        } else {
            textViewSettingPhone.setError("Error");
        }
        /**
         * passwordに大文字小文字英数字全てを含んだ６文字以上か
         */
        if (isContainPasswordPattern()) {
            trueCount++;
        } else {
            textViewSettingPassword.setError("Error");
        }
        /**
         * PasswordとRepeat Passwordは同じものか
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
     * 正規表現を含んでいるか
     *
     * @return
     */
    private boolean isContainPasswordPattern() {
        boolean result = false;

        //半角英数字
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