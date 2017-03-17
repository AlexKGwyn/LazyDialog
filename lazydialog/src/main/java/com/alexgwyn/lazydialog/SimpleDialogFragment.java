package com.alexgwyn.lazydialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Alex on 10/17/16.
 */

public abstract class SimpleDialogFragment extends DialogFragment {
    private FrameLayout mContent;
    private TextView mPositive;
    private TextView mNegative;
    private TextView mTitle;

    private Callback mCallback;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_simple, container, false);
        mContent = (FrameLayout) view.findViewById(R.id.dialog_content);
        View contentChild = inflateDialogContent(LayoutInflater.from(getContext()), mContent, savedInstanceState);
        if (contentChild != null) {
            mContent.addView(contentChild);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPositive = (TextView) view.findViewById(R.id.buttonDialogPositive);
        mPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPositiveButtonPressed(view);
            }
        });
        mNegative = (TextView) view.findViewById(R.id.buttonDialogNegative);
        mNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNegativeButtonPressed(view);
            }
        });
        mTitle = (TextView) view.findViewById(R.id.textViewDialogTitle);
    }

    public abstract View inflateDialogContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstance);

    public void setPositiveButton(String text) {
        if (!TextUtils.isEmpty(text)) {
            mPositive.setVisibility(View.VISIBLE);
            mPositive.setText(text);
        } else {
            mPositive.setVisibility(View.GONE);
        }
    }

    public void setPositiveButton(@StringRes int id) {
        mPositive.setVisibility(View.VISIBLE);
        mPositive.setText(id);
    }

    public void setNegativeButton(String text) {
        if (!TextUtils.isEmpty(text)) {
            mNegative.setVisibility(View.VISIBLE);
            mNegative.setText(text);
        } else {
            mNegative.setVisibility(View.GONE);
        }
    }

    public void setNegativeButton(@StringRes int id) {
        mNegative.setVisibility(View.VISIBLE);
        mNegative.setText(id);
    }

    public void setTitle(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(text);
        } else {
            mTitle.setVisibility(View.GONE);
        }
    }

    public void setTitle(@StringRes int id) {
        if (id == 0) {
            setTitle(null);
        } else {
            setTitle(getContext().getString(id));
        }
    }

    public void onPositiveButtonPressed(View view) {
        if (mCallback != null) {
            mCallback.onPositiveButtonClicked(this);
        }
    }

    public void onNegativeButtonPressed(View view) {
        if (mCallback != null) {
            mCallback.onNegativeButtonClicked(this);
        }
    }

    public TextView getPositiveButton() {
        return mPositive;
    }

    public TextView getNegativeButton() {
        return mNegative;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getParentFragment() != null) {
            if (getParentFragment() instanceof Callback) {
                mCallback = (Callback) getParentFragment();
                return;
            }
        } else if (activity instanceof Callback) {
            mCallback = (Callback) activity;
            return;
        }
        Log.d("SimpleDialogFragment", getClass().getSimpleName() + " should probably be attached to a parent that implements " + Callback.class.getSimpleName());
    }

    public interface Callback<T extends SimpleDialogFragment> {
        public void onPositiveButtonClicked(T fragment);

        public void onNegativeButtonClicked(T fragment);
    }

}
