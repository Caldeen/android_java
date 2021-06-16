package com.example.am;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

import java.util.List;

import data.classes.Meeting;
import data.classes.User;
import helpers.RetrofitClient;
import helpers.SharedPrefsHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.MeetingsService;
import services.UserService;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.ViewHolder> {
    private List<Meeting> meetings;
    private Context context;

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView secondary;
        private final TextView supporting;
        private final MaterialButton deleteButton;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            title = (TextView) view.findViewById(R.id.titleTextView);
            secondary = (TextView) view.findViewById(R.id.secondaryTextView);
            supporting = (TextView) view.findViewById(R.id.supportingTextView);
            deleteButton = view.findViewById(R.id.deleteButton);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getSecondary() {
            return secondary;
        }

        public TextView getSupporting() {
            return supporting;
        }

        public MaterialButton getDeleteButton() {
            return deleteButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param meetings String[] containing the data to populate views to be used
     *                 by RecyclerView.
     */
    public MeetingsAdapter(List<Meeting> meetings, Context context) {
        this.meetings = meetings;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.meeting_row, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTitle().setText(meetings.get(position).getName());
        viewHolder.getSecondary().setText(meetings.get(position).getDescription());
        viewHolder.getSupporting().setText(meetings.get(position).getPlace());

        viewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle(context.getResources().getString(R.string.alertTitle))
                        .setNegativeButton(context.getResources().getString(R.string.negative),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })

                        .setPositiveButton(context.getResources().getString(R.string.positive),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MeetingsService meetingsService = RetrofitClient.getRetrofit().create(MeetingsService.class);
                                        Call<String> call = meetingsService.deleteMeeting(SharedPrefsHandler.getToken(context),
                                                meetings.get(position).getId());
                                        call.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String > call, Response<String> response) {
                                                if (response.code() != 200) {
                                                    Log.println(Log.ERROR, "info", "failed delete meeting");
                                                    return;
                                                }
                                                notifyItemRemoved(position);
                                                meetings.remove(position);
                                                notifyItemRangeChanged(position,getItemCount());
                                                notifyDataSetChanged();

                                            }
                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                Log.println(Log.ERROR, "info", "failed connection" + t.getMessage());
                                            }
                                        });
                                    }
                                })
                        .show();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (meetings == null)
            return 0;
        return meetings.size();
    }
}
