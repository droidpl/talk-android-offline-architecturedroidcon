package com.mobgen.droidcon.offline.demos.online;

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
import com.mobgen.droidcon.offline.shared.models.Post;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewPostDialogFragment extends DialogFragment {

    @BindView(R.id.et_title)
    EditText mTitle;
    @BindView(R.id.et_body)
    EditText mBody;
    private NewPostListener mCallback;

    public static NewPostDialogFragment create() {
        return new NewPostDialogFragment();
    }


    public void setPostListener(@NonNull NewPostListener listener) {
        mCallback = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_post, null, false);
        ButterKnife.bind(this, customView);
        return new AlertDialog.Builder(getActivity())
                .setPositiveButton(getString(R.string.general_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notifyNewPostAndClose();
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

    private void notifyNewPostAndClose() {
        Date now = new Date();
        Post post = Post.builder()
                .title(mTitle.getText().toString())
                .body(mBody.getText().toString())
                .createdAt(now.getTime())
                .updatedAt(now.getTime())
                .build();
        if (mCallback != null) {
            mCallback.onPostCreated(post);
        }
        dismiss();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface NewPostListener {
        void onPostCreated(@NonNull Post post);
    }
}
