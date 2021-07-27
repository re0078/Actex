package com.mobiledevelopment.actex.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.lists.ListResult;
import com.mobiledevelopment.actex.utils.UIUtils;

import java.util.List;

public class AddToListAdapter extends RecyclerView.Adapter<AddToListAdapter.ViewHolder> {

    private final List<ListResult> lists;
    private OnListItemClickedListener onListItemClickedListener;

    public AddToListAdapter(List<ListResult> lists) {
        this.lists = lists;
    }

    public void setOnListItemClickedListener(OnListItemClickedListener onListItemClickedListener) {
        this.onListItemClickedListener = onListItemClickedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.add_to_list_popup_item, parent, false);
        UIUtils.setupOnTouchListener(contactView);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListResult listResult = lists.get(position);
        TextView nameView = holder.listName;
        nameView.setText(listResult.getName());
        TextView descView = holder.listDesc;
        descView.setText(listResult.getDescription());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void addAll(com.mobiledevelopment.actex.models.lists.List results) {
        lists.clear();
        lists.addAll(results.getListResults());
        Log.d("size", results.getListResults().size() + "");
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView listName;
        public TextView listDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            listName = (TextView) itemView.findViewById(R.id.popup_playlist_name);
            listDesc = (TextView) itemView.findViewById(R.id.popup_playlist_description);
            itemView.setOnClickListener(view -> {
                Log.d("click", "MovieAdapter");
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                    onListItemClickedListener.onListClick(lists.get(position));
            });
        }
    }

}
