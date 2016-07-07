package com.mobgen.droidcon.offline.shared.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewCommentDialogFragment extends DialogFragment {

    private static final String BUNDLE_POST = "post";

    @BindView(R.id.et_title)
    EditText mTitle;
    @BindView(R.id.et_email)
    EditText mEmail;
    @BindView(R.id.et_body)
    EditText mBody;
    private NewCommentListener mCallback;
    private Post mPost;

    public static NewCommentDialogFragment create(@NonNull Post post) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_POST, post);
        NewCommentDialogFragment fragment = new NewCommentDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    public void setCommentListener(@NonNull NewCommentListener listener) {
        mCallback = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_comment, null, false);
        ButterKnife.bind(this, customView);
        return new AlertDialog.Builder(getActivity())
                .setPositiveButton(getString(R.string.general_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notifyNewCommentAndClose();
                    }
                })
                .setNegativeButton(getString(R.string.general_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                }).setView(customView)
                .create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments == null){
            throw new IllegalArgumentException("You must use the create method");
        }
        mPost = arguments.getParcelable(BUNDLE_POST);
    }

    private void notifyNewCommentAndClose() {
        Date now = new Date();
        Comment comment = Comment.builder()
                .id(-1L)
                .name(mTitle.getText().toString())
                .body(mBody.getText().toString())
                .email(mEmail.getText().toString())
                .postId(mPost.id())
                .createdAt(now.getTime())
                .needsSync(true)
                .build();
        if (mCallback != null) {
            mCallback.onCommentCreated(comment);
        }
        dismiss();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface NewCommentListener {
        void onCommentCreated(@NonNull Comment comment);
    }
}
