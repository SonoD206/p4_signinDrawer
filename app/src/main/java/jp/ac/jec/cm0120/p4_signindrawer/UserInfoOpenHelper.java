package jp.ac.jec.cm0120.p4_signindrawer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UserInfoOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "P4_DB";
    public static final String TABLE_NAME = "user_info";
    public static final String TAG = "###";

    public UserInfoOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "(name TEXT PRIMARY KEY, mail TEXT, phone_num TEXT, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * ユーザー名とパスワードを使いデータをとってくる
     * @param name
     * @param password
     * @return
     */
    public ArrayList<UserInfo> findUser(String name, String password){
        ArrayList<UserInfo> ary = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        if (database == null){
            return null;
        }
        try {
            Cursor cursor = database.query(TABLE_NAME,new String[]{"name","mail","phone_num","password"},"mail=? and password=?"
                    ,new String[]{name,password}
                    ,null,null,null);
            while (cursor.moveToNext()){
                UserInfo tmp = new UserInfo();
                tmp.setName(cursor.getString(0));
                tmp.setMail(cursor.getString(1));
                tmp.setPhoneNum(cursor.getString(2));
                tmp.setPassword(cursor.getString(3));
                ary.add(tmp);
            }
        } catch (SQLiteException e){
            e.printStackTrace();
        } finally {
            database.close();
        }
        return ary;
    }

    /**
     * user_infoにデータを入れる
     * @param info
     * @return
     */
    public boolean insertUserInfoData(UserInfo info){
        ContentValues values  = new ContentValues();
        values.put("name", info.getName());
        values.put("mail", info.getMail());
        values.put("phone_num", info.getPhoneNum());
        values.put("password", info.getPassword());

        SQLiteDatabase database = getWritableDatabase();
        long ret = -1;
        try {
            ret = database.insert(TABLE_NAME,"",values);
        } catch (SQLiteException e){
            e.printStackTrace();
        } finally {
            database.close();
        }
        return ret > 0;
    }
}
