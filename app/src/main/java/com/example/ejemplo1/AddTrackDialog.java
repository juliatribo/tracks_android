package com.example.ejemplo1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTrackDialog extends DialogFragment {

    public interface NoticeDialogListener {
        public void onDialogSongAdded(DialogFragment dialog);
        public void errorAddingSong(DialogFragment dialog);
    }

    NoticeDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("activity" + " must implement NoticeDialogListener");
        }
    }

    public TextInputLayout nameInput,singerInput;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.popup_layout, null);
        nameInput = dialog.findViewById(R.id.songLayout);
        singerInput = dialog.findViewById(R.id.authorLayout);
        builder.setTitle("Añadir Track");
        builder.setView(dialog);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String song = nameInput.getEditText().getText().toString();
                String author = singerInput.getEditText().getText().toString();
                Track track = new Track(song,author);
                Call<Track> trackAns = MainActivity.tracksAPI.createTrack(track);
                trackAns.enqueue(new Callback<Track>() {
                    @Override
                    public void onResponse(Call<Track> call, Response<Track> response) {
                        listener.onDialogSongAdded(AddTrackDialog.this);
                        Log.i("Julia", "added traack = "+track.toString());
                    }

                    @Override
                    public void onFailure(Call<Track> call, Throwable throwable) {
                        listener.errorAddingSong(AddTrackDialog.this);
                    }

                });

                AddTrackDialog.this.getDialog().cancel();

            }

        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddTrackDialog.this.getDialog().cancel();
            }
        });

        return builder.create();

    }

}
