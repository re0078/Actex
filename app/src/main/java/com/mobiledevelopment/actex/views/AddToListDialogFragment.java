package com.mobiledevelopment.actex.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.request_bodies.AddToListBody;
import com.mobiledevelopment.actex.models.User;
import com.mobiledevelopment.actex.models.lists.List;
import com.mobiledevelopment.actex.models.lists.ListResult;
import com.mobiledevelopment.actex.clients.RetrofitBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddToListDialogFragment extends DialogFragment {
    private RecyclerView recyclerView;

    public AddToListDialogFragment() {
    }

    public static com.mobiledevelopment.actex.views.AddToListDialogFragment newInstance(String title, int movieId) {
        com.mobiledevelopment.actex.views.AddToListDialogFragment frag = new com.mobiledevelopment.actex.views.AddToListDialogFragment();
        Bundle args = new Bundle();
        args.putInt("movieId", movieId);
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_add_to_lists, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //recyclerView = view.findViewById(R.id.recycler_view);
        AddToListAdapter addToListAdapter = new AddToListAdapter(new ArrayList<>());
        final int movieId = getArguments().getInt("movieId");
        OnListItemClickedListener onListItemClickedListener = listResult -> {
            Toast.makeText(getContext(), "Added", Toast.LENGTH_LONG).show();
            addToList(listResult, movieId);
        };
        addToListAdapter.setOnListItemClickedListener(onListItemClickedListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(addToListAdapter);
        String title = getArguments().getString("title", "Choose List");
        getDialog().setTitle(title);
        getLists();
    }

    private void addToList(final ListResult listResult, int movieId) {
        Call<Object> addTolist = RetrofitBuilder.getCreateListApi().addMovieToList(listResult.getId().toString(), User.getApiKey(), User.getUser().getSessionToken().getSessionId(), new AddToListBody(movieId));
        addTolist.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "movie added to list" + listResult.getName(), Toast.LENGTH_SHORT);
                } else {
                    Log.e("list error", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("list error", t.getMessage());
                Toast.makeText(getContext(), "could not fetch your list. try again later", Toast.LENGTH_LONG);
            }
        });
    }

    public void getLists() {
        Call<List> myList = RetrofitBuilder.getCreateListApi()
                .getLists(User.getUser().getAccount().getId(), getResources().getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), 1);
        myList.enqueue(new Callback<List>() {
            @Override
            public void onResponse(Call<List> call, Response<List> response) {
                Log.e("list error", response.code() + "");
                if (response.isSuccessful()) {
                    Log.e("list comp", response.code() + "");
                    ((AddToListAdapter) recyclerView.getAdapter()).addAll(response.body());
                } else {
                    Log.e("list error", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<List> call, Throwable t) {
                Log.e("list error", t.getMessage());
                Toast.makeText(getContext(), "could not fetch your list. try again later", Toast.LENGTH_SHORT);
            }
        });
    }
}
