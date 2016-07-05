package com.mobgen.droidcon.offline.shared.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.shared.models.Post;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context mContext;
    private List<Post> mPosts;
    private PostListener mCallback;

    public PostAdapter(@NonNull Context context, @Nullable List<Post> posts, PostListener postListener) {
        mContext = context;
        mPosts = posts;
        mCallback = postListener;
    }

    public List<Post> posts() {
        return mPosts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_post, parent, false));
    }

    @Override
    public int getItemCount() {
        if (mPosts != null) {
            return mPosts.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bind(mPosts.get(position));
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.tv_body)
        TextView mBody;
        @BindView(R.id.iv_delete)
        View mDelete;

        public PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mDelete.setOnClickListener(this);
        }

        public void bind(@NonNull Post post) {
            mTitle.setText(post.title());
            mBody.setText(post.body());
            int colorId = post.deletedAt() != null ? R.color.deletedColor : R.color.white;
            int color = ContextCompat.getColor(itemView.getContext(), colorId);
            itemView.setBackgroundColor(color);
        }

        @Override
        public void onClick(View view) {
            Post post = mPosts.get(getAdapterPosition());
            if (view == itemView) {
                if (mCallback != null) {
                    mCallback.onSelected(post);
                }
            } else if (view == mDelete) {
                if (mCallback != null) {
                    mCallback.onDeleted(post, getAdapterPosition());
                }
            }
        }
    }

    public interface PostListener {
        void onSelected(@NonNull Post post);

        void onDeleted(@NonNull Post post, int position);
    }
}