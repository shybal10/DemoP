package mawaqaa.parco.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mawaqaa.parco.Model.NotificationModel;
import mawaqaa.parco.R;

/**
 * Created by Ayadi on 4/19/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificatioViewHolder> {

    Context context;
    List<NotificationModel> notifictionlList;

    public NotificationAdapter(Context context, List<NotificationModel> notifictionlList) {
        this.context = context;
        this.notifictionlList = notifictionlList;
    }

    @Override
    public NotificationAdapter.NotificatioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_row, parent, false);
        NotificatioViewHolder viewholder = new NotificatioViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.NotificatioViewHolder holder, final int position) {
        final NotificationModel info = notifictionlList.get(position);
        holder.title.setText(info.getTitle());
        holder.message.setText(info.getMessage());
//
//        String dateS = info.getDate();
//        String dateString = dateS.substring(0, 10);

        holder.date.setText(info.getDate());

    }

    @Override
    public int getItemCount() {
        return notifictionlList.size();
    }

    public static class NotificatioViewHolder extends RecyclerView.ViewHolder {

        TextView title, message, date;


        public NotificatioViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            message = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);

        }
    }
}