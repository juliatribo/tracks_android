package com.example.ejemplo1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;


import android.util.Log;
import android.view.View;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AddTrackDialog.NoticeDialogListener, EditTrackDialog.NoticeDialogListener {
    public static String clickedSong;
    public static String clickedArtist;
    public static String clickedID;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public View view;
    public static FragmentManager fragmentManager;
    public static TracksAPI tracksAPI;
    List<Track> trackList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        fragmentManager = getSupportFragmentManager();
        view = findViewById(android.R.id.content).getRootView();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        context = getApplicationContext();

        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(tracksAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        tracksAPI = retrofit.create(TracksAPI.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Call<List<Track>> tracks = tracksAPI.getTracks();
        tracks.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                trackList = response.body();
                Log.i("Julia", trackList.toString());
                mAdapter = new MyAdapter(response.body());
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable throwable) {

            }

        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                    target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                Call<Void> del = tracksAPI.deleteTrack(trackList.get(viewHolder.getAdapterPosition()).getId());
                del.enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        reloadAction(view);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void reloadAction(View w)
    {
        Call<List<Track>> tracks = tracksAPI.getTracks();
        tracks.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                trackList = response.body();
                Log.i("Julia", trackList.toString());
                mAdapter = new MyAdapter(response.body());
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable throwable) {

            }

        });
    }

    public void addPopUpOpener(View v) {
        AddTrackDialog d = new AddTrackDialog();
        d.show(getSupportFragmentManager(),"newdialog");

    }

    public static void ClickedSong() {
        EditTrackDialog d = new EditTrackDialog();
        d.show(fragmentManager,"newdialog");
    }

    @Override
    public void onDialogSongAdded(DialogFragment dialog) {
        reloadAction(view);
        Toast.makeText(context,"Canción añadida", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDialogSongEdited(DialogFragment dialog) {
        reloadAction(view);
        Toast.makeText(context,"Cancion Editada",Toast.LENGTH_LONG).show();
    }

    @Override
    public void errorEditingSong(DialogFragment dialog) {
        Toast.makeText(context,"No se pudo editar la cancion",Toast.LENGTH_LONG).show();
    }

    @Override
    public void errorAddingSong(DialogFragment dialog) {
        Toast.makeText(context,"No se pudo añadir la cancion",Toast.LENGTH_LONG).show();
    }
}



/*
    public void getTrackById(String id) {
            Call<Track> track = tracksAPI.getTrack(id);
            track.enqueue(new Callback<Track>() {
    @Override
    public void onResponse(Call<Track> call, Response<Track> response) {
            Track track = response.body();
            Log.i("Julia", track.toString());
            trackList.add(mAdapter.getItemCount(),response.body());
            recyclerView.setAdapter(mAdapter);
            }

    @Override
    public void onFailure(Call<Track> call, Throwable t) {

        }
    });
}

 */
