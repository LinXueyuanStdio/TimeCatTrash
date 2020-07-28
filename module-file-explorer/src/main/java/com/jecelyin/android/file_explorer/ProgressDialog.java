package com.jecelyin.android.file_explorer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.timecat.component.commonbase.listeners.ProgressInterface;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.StringRes;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class ProgressDialog extends Dialog implements ProgressInterface {
    private final TextView messageTextView;
    private OnDismissListener onDismissListener;
    private List<com.timecat.component.commonbase.listeners.OnDismissListener> onDismissListeners;

    public ProgressDialog(Context context) {
        this(context, null);
    }

    public ProgressDialog(Context context, @StringRes int titleRes) {
        this(context, context.getString(titleRes));
    }

    public ProgressDialog(Context context, CharSequence title) {
        super(context, R.style.ProgressDialog);

        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.progress_layout);
//        getWindow().setBackgroundDrawable(null);
        getWindow().getAttributes().gravity= Gravity.CENTER;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount=0.2f;
        getWindow().setAttributes(lp);
        messageTextView = (TextView)findViewById(R.id.messageTextView);
        setTitle(title);
    }

    @Override
    public void setTitle(CharSequence title) {
        if(messageTextView != null && title != null) {
            messageTextView.setText(title);
            messageTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
        this.onDismissListener = listener;
    }

    @Override
    public void addOnDismissListener(com.timecat.component.commonbase.listeners.OnDismissListener listener) {
        if (onDismissListeners == null)
            onDismissListeners = new ArrayList<>();
        onDismissListeners.add(listener);

        super.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (onDismissListener != null)
                    onDismissListener.onDismiss(dialog);

                for (com.timecat.component.commonbase.listeners.OnDismissListener l : onDismissListeners) {
                    l.onDismiss();
                }
            }
        });
    }

    @Override
    public void removeOnDismissListener(com.timecat.component.commonbase.listeners.OnDismissListener listener) {
        if (onDismissListeners == null)
            return;

        onDismissListeners.remove(listener);
    }

    @Override
    public void setMessage(CharSequence message) {
        setTitle(message);
    }
}
