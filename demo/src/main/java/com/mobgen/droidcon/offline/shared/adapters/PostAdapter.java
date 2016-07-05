package com.mobgen.droidcon.offline.shared.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final PostViewHolder viewHolder = new PostViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_post, parent, false));
        //Set clicks
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = mPosts.get(viewHolder.getAdapterPosition());
                if (mCallback != null) {
                    mCallback.onPostSelected(post);
                }
            }
        });
        return viewHolder;
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

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.tv_body)
        TextView mBody;

        public PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(@NonNull Post post) {
            mTitle.setText(post.title());
            mBody.setText(post.body());
        }
    }

    public interface PostListener {
        void onPostSelected(@NonNull Post post);
    }
}
