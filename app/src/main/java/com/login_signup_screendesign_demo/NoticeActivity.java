package com.login_signup_screendesign_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.login_signup_screendesign_demo.api.IntelliSApi;
import com.login_signup_screendesign_demo.api.JsonPlaceHolderApi;
import com.login_signup_screendesign_demo.api.NetworkUtil;
import com.login_signup_screendesign_demo.models.IntellisPost;
import com.login_signup_screendesign_demo.utils.ListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class NoticeActivity extends AppCompatActivity {
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private IntelliSApi intellisApi;
    private ListViewAdapter listAdapter;
    private List<String> list;
    private List<String> textlist;
    ListView listView;
    Integer id;
    String text;
    //List<Integer>idarr = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_list);

       listView = (ListView) findViewById(R.id.listView);

        Retrofit retrofit = NetworkUtil.getRetrofit();
        intellisApi = retrofit.create(IntelliSApi.class);

        getPosts();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                //id = idarr.get(i);
                text = textlist.get(i);
                intent.putExtra("text",text);
                startActivity(intent);
                finish();
            }
        });

    }


    private void getPosts(){

        list = new ArrayList<>();

        Map<String, String> parameters = new HashMap<>();
        int bno =2;
        //parameters.put("_sort","id");
        //parameters.put("_order","desc");
        Call<List<IntellisPost>> call = intellisApi.getPosts(bno);
        call.enqueue(new Callback<List<IntellisPost>>() {
            @Override
            public void onResponse(Call<List<IntellisPost>> call, retrofit2.Response<List<IntellisPost>> response) {

                if (!response.isSuccessful()) {
                    //list.add("Code:" + response.code());

                    list.add("error");
                    return;
                }
                List<IntellisPost> posts = response.body();
                for (IntellisPost post : posts) {
                    String content = "";
                    content += "ID:" + post.getId() + "\n";
                    content += "Writer: " + post.getWriter() + "\n";
                    content += "Title:" + post.getTitle() + "\n";
                    //   textViewResult.append(content);
                    list.add(content);
                    textlist.add(post.getContent());

                }
                listAdapter = new ListViewAdapter((getApplicationContext()), list);
                listView.setAdapter(listAdapter);
            }

            @Override
            public void onFailure(Call<List<IntellisPost>> call, Throwable t) {
                list.add("failure");
                //  textViewResult.setText(t.getMessage());
                //list.add( t.getMessage());

            }
        });


    }
}