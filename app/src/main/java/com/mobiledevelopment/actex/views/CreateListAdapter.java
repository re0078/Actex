package com.mobiledevelopment.actex.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.lists.ListResult;

import java.util.List;

public class CreateListAdapter extends RecyclerView.Adapter<CreateListAdapter.ViewHolder> {


    public interface OnListItemDeleted {
        void onListDeleted(String listId);
    }

    private List<ListResult> lists;
    private OnListItemClickedListener onListItemClickedListener;
    private OnListItemDeleted onListItemDeleted;

    public void setOnListItemDeleted(OnListItemDeleted onListItemDeleted) {
        this.onListItemDeleted = onListItemDeleted;
    }

    public CreateListAdapter(List<ListResult> lists) {
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
//        View contactView = inflater.inflate(R.layout.list_presenter, parent, false);
//        return new ViewHolder(contactView);
        return new ViewHolder(new Button(context)); // TODO
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
        private Button del;

        public ViewHolder(View itemView) {
            super(itemView);
//            listName = itemView.findViewById(R.id.listNameSet);
//            listDesc = itemView.findViewById(R.id.listDesc);
//            del = itemView.findViewById(R.id.delete_list);
            del = new Button(itemView.getContext()); // TODO
            del.setOnClickListener(view -> {
                final int position = getAdapterPosition();
                onListItemDeleted.onListDeleted(lists.get(position).getId().toString());
            });
            itemView.setOnClickListener(view -> {
                Log.e("click", "MovieAdapter");
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onListItemClickedListener.onListClick(lists.get(position));
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void addAll(com.mobiledevelopment.actex.models.lists.List results) {
        lists.addAll(results.getListResults());
        Log.e("size", results.getListResults().size() + "");
        this.notifyDataSetChanged();
    }
    public void clear(){
        lists.clear();
        this.notifyDataSetChanged();
    }

}
