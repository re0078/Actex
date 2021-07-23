package com.mobiledevelopment.actex.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.lists.ListResult;

import java.util.List;

public class AddToListAdapter extends RecyclerView.Adapter<AddToListAdapter.ViewHolder> {
    private List<ListResult> lists;
    private OnListItemClickedListener onListItemClickedListener;
    private int movieId;

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
        View contactView = inflater.inflate(R.layout.list_presenter_add_to_list, parent, false);
        return new com.mobiledevelopment.actex.views.AddToListAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListResult listResult = lists.get(position);
        TextView nameView = holder.listName;
        nameView.setText(listResult.getName());
        TextView descView = holder.listDesc;
        descView.setText(listResult.getDescription());
        Log.e("helloooo", "   dssd");
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView listName;
        public TextView listDesc;

        public ViewHolder(View itemView) {
            super(itemView);
//            listName = (TextView) itemView.findViewById(R.id.listNameSet);
//            listDesc = (TextView) itemView.findViewById(R.id.listDesc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("click", "MovieAdapter");
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onListItemClickedListener.onListClick(lists.get(position));
                    }
                }

            });
        }


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void addAll(com.mobiledevelopment.actex.models.lists.List results) {
        lists.clear();
        lists.addAll(results.getListResults());
        Log.e("size", results.getListResults().size() + "");
        this.notifyDataSetChanged();
    }

}
