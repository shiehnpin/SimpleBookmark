package com.enping.simplebookmark;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private BookmarkManager.BookmarkService bookmarkManager;
    private ListView lvBookmarks;
    private ArrayList<Bookmark> bookmarksList;
    private ArrayAdapter<Bookmark> bookmarksAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bookmarkManager = BookmarkManager.getInstance();
        bookmarksList = new ArrayList<>();
        bookmarksAdapter = new ArrayAdapter<Bookmark>(
                getBaseContext(),
                android.R.layout.simple_list_item_1,
                bookmarksList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                v.setTag(getItem(position).getId());
                ((TextView) v.findViewById(android.R.id.text1)).setText(getItem(position).getText());
                return v;
            }
        };
        lvBookmarks = (ListView) findViewById(R.id.lv_bookmark);
        lvBookmarks.setAdapter(bookmarksAdapter);
        lvBookmarks.setOnItemClickListener(this);
        lvBookmarks.setOnItemLongClickListener(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);


        getBookmarks();

    }


    //Add
    @Override
    public void onClick(View view) {
        View v = getLayoutInflater().inflate(R.layout.dialog_edit_bookmark, null);

        new AlertDialog
                .Builder(MainActivity.this)
                .setView(v)
                .setTitle("Add Bookmark")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_bookmark_text);
                        addBookmark(et.getText().toString());
                    }
                })
                .show();
    }

    //Edit
    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        final View v = getLayoutInflater().inflate(R.layout.dialog_edit_bookmark, null);
        ((TextView) v.findViewById(R.id.et_bookmark_text))
                .setText(bookmarksAdapter.getItem(position).getText());

        new AlertDialog
                .Builder(MainActivity.this)
                .setView(v)
                .setTitle("Edit Bookmark")
                .setPositiveButton("Modify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = (EditText) v.findViewById(R.id.et_bookmark_text);
                        editBookmark((int) view.getTag(), et.getText().toString());
                    }
                })
                .show();
    }


    //Remove
    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        deleteBookmark((int) view.getTag());
        return true;
    }

    private void getBookmarks() {
        bookmarkManager.getBookmarks().enqueue(new Callback<List<Bookmark>>() {
            @Override
            public void onResponse(Call<List<Bookmark>> call, Response<List<Bookmark>> response) {
                bookmarksAdapter.clear();
                bookmarksAdapter.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Bookmark>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBookmark(String text) {
        bookmarkManager.addBookmark(new Bookmark(text)).enqueue(new Callback<List<Bookmark>>() {
            @Override
            public void onResponse(Call<List<Bookmark>> call, Response<List<Bookmark>> response) {
                bookmarksAdapter.clear();
                bookmarksAdapter.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Bookmark>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editBookmark(int id, String text) {
        bookmarkManager.editBookmark(id, new Bookmark(text)).enqueue(new Callback<Bookmark>() {
            @Override
            public void onResponse(Call<Bookmark> call, Response<Bookmark> response) {
                bookmarksAdapter.remove(response.body());
                bookmarksAdapter.add(response.body());
            }

            @Override
            public void onFailure(Call<Bookmark> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteBookmark(final int id) {
        bookmarkManager.delBookmark(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                bookmarksAdapter.remove(new Bookmark(id));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}


