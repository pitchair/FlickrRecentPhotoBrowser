/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.pitchai.flickrrecentphotobrowser.presentation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pitchai.flickrrecentphotobrowser.R;

import java.util.Collections;
import java.util.List;


/**
 * Adaptar that manages a collection of.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

  private List<String> usersCollection;
  private final LayoutInflater layoutInflater;


  public UsersAdapter(Context context) {
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.usersCollection = Collections.emptyList();
  }

  @Override
  public int getItemCount() {
    return 30;
  }

  @Override
  public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.row_user, parent, false);
    return new UserViewHolder(view);
  }

  @Override
  public void onBindViewHolder(UserViewHolder holder, final int position) {
    holder.textViewTitle.setText("Hello" + position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }



  static class UserViewHolder extends RecyclerView.ViewHolder {
    TextView textViewTitle;

    UserViewHolder(View itemView) {
      super(itemView);
      textViewTitle = (TextView) itemView.findViewById(R.id.title);
    }
  }
}
