package com.example.ejemplo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTrackDialog extends DialogFragment {

    public interface NoticeDialogListener {
        public void onDialogSongEdited(DialogFragment dialog);
        public void errorEditingSong(DialogFragment dialog);
    }

    NoticeDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (EditTrackDialog.NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("activity" + " must implement NoticeDialogListener");
        }
    }

    public TextInputLayout nameInput,singerInput;


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.popup_layout, null);
        nameInput = dialog.findViewById(R.id.songLayout);
        singerInput = dialog.findViewById(R.id.authorLayout);
        builder.setTitle("Editar Track");
        builder.setView(dialog);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String song = nameInput.getEditText().getText().toString();
                String author = singerInput.getEditText().getText().toString();
                String id = MainActivity.clickedID;
                Track track = new Track(song,author,id);
                Call<Track> trackAns = MainActivity.tracksAPI.editTrack(track);
                final Boolean[] error = {false};
                trackAns.enqueue(new Callback<Track>() {
                    @Override
                    public void onResponse(Call<Track> call, Response<Track> response) {
                        listener.onDialogSongEdited(EditTrackDialog.this);
                    }

                    @Override
                    public void onFailure(Call<Track> call, Throwable throwable) {
                        error[0] = true;
                        listener.errorEditingSong(EditTrackDialog.this);
                    }

                });

                if (!error[0])
                    listener.onDialogSongEdited(EditTrackDialog.this);
                EditTrackDialog.this.getDialog().cancel();

            }

        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditTrackDialog.this.getDialog().cancel();
            }
        });

        return builder.create();

    }

}

