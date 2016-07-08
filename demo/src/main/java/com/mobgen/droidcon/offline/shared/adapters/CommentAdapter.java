package com.mobgen.droidcon.offline.shared.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.shared.ui.BasePostDetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private static final int VIEW_TYPE_POST = 1;
    private static final int VIEW_TYPE_COMMENT = 2;

    private Post mPost;
    private List<Comment> mComments;
    private CommentListener mCallback;

    public CommentAdapter(@NonNull Post post, @Nullable List<Comment> comments, @NonNull CommentListener callback) {
        mPost = post;
        mComments = comments;
        mCallback = callback;
    }



    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = -1;
        if (viewType == VIEW_TYPE_POST) {
            layoutId = R.layout.adapter_post;
        } else if (viewType == VIEW_TYPE_COMMENT) {
            layoutId = R.layout.adapter_comment;
        }
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        if (position == 0) {
            holder.bind(mPost);
        } else {
            holder.bind(mComments.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        int count = 1;
        if (mComments != null) {
            count += mComments.size();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_POST;
        }
        return VIEW_TYPE_COMMENT;
    }

    public void comments(@Nullable List<Comment> comments) {
        mComments = comments;
        notifyDataSetChanged();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.tv_body)
        TextView mBody;
        @Nullable
        @BindView(R.id.tv_email)
        TextView mEmail;
        @BindView(R.id.iv_delete)
        View mRemoveButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mRemoveButton.setOnClickListener(this);
        }

        public void bind(@NonNull Post post) {
            mTitle.setText(post.title());
            mBody.setText(post.body());
            //Do not show the remove for this view
            mRemoveButton.setVisibility(View.GONE);
        }

        public void bind(@NonNull Comment comment) {
            if (mEmail != null) {
                mEmail.setText(comment.email());
            }
            mTitle.setText(comment.name());
            mBody.setText(comment.body());
        }

        @Override
        public void onClick(View view) {
            if (mCallback != null) {
                mCallback.onRemoveComment(mComments.get(getAdapterPosition() - 1));
            }
        }
    }

    public interface CommentListener {
        void onRemoveComment(@NonNull Comment comment);
    }
}
