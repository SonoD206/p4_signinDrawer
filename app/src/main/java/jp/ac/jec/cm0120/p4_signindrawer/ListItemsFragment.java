package jp.ac.jec.cm0120.p4_signindrawer;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListItemsFragment extends Fragment {

    private Activity _parentActivity;
    private static List<Map<String,String>> data = new ArrayList<>();

    static {
        Map<String,String> map = new HashMap<>();
        map.put("title","Information");
        map.put("icon",String.valueOf(R.drawable.infomation));
        data.add(map);

        Map<String, String> map1 = new HashMap<>();
        map1.put("title","Memo");
        map1.put("icon",String.valueOf(R.drawable.memo));
        data.add(map1);

        Map<String, String> map2 = new HashMap<>();
        map2.put("title","Train");
        map2.put("icon",String.valueOf(R.drawable.train));
        data.add(map2);

        Map<String, String> map3 = new HashMap<>();
        map3.put("title","Logout");
        map3.put("icon",String.valueOf(R.drawable.logout));
        data.add(map3);
    }

    public ListItemsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Activityオブジェクト取得
        _parentActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_items, container, false);

        ListView listView = view.findViewById(R.id.listViewItems);
        SimpleAdapter adapter = new SimpleAdapter(
                _parentActivity
                ,data
                ,R.layout.row_item
                ,new String[]{"title","icon"}
                ,new int[]{R.id.textViewListTitle,R.id.imageViewIcon});
        listView.setAdapter(adapter);
        return view;
    }
}