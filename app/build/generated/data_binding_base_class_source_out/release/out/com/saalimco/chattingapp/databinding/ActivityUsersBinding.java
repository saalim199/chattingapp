// Generated by view binder compiler. Do not edit!
package com.saalimco.chattingapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.saalimco.chattingapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityUsersBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatImageView imgBack;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final TextView txtErrorMessage;

  @NonNull
  public final RecyclerView usersRecyclerView;

  private ActivityUsersBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatImageView imgBack, @NonNull ProgressBar progressBar,
      @NonNull TextView txtErrorMessage, @NonNull RecyclerView usersRecyclerView) {
    this.rootView = rootView;
    this.imgBack = imgBack;
    this.progressBar = progressBar;
    this.txtErrorMessage = txtErrorMessage;
    this.usersRecyclerView = usersRecyclerView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityUsersBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityUsersBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_users, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityUsersBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imgBack;
      AppCompatImageView imgBack = ViewBindings.findChildViewById(rootView, id);
      if (imgBack == null) {
        break missingId;
      }

      id = R.id.progressBar;
      ProgressBar progressBar = ViewBindings.findChildViewById(rootView, id);
      if (progressBar == null) {
        break missingId;
      }

      id = R.id.txtErrorMessage;
      TextView txtErrorMessage = ViewBindings.findChildViewById(rootView, id);
      if (txtErrorMessage == null) {
        break missingId;
      }

      id = R.id.usersRecyclerView;
      RecyclerView usersRecyclerView = ViewBindings.findChildViewById(rootView, id);
      if (usersRecyclerView == null) {
        break missingId;
      }

      return new ActivityUsersBinding((ConstraintLayout) rootView, imgBack, progressBar,
          txtErrorMessage, usersRecyclerView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
