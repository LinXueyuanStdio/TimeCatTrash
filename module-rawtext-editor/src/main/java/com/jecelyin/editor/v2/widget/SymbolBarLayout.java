package com.jecelyin.editor.v2.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jecelyin.editor.v2.Pref;
import com.jecelyin.editor.v2.R;

import java.util.List;

import my.shouheng.palmmarkdown.MDItemView;
import my.shouheng.palmmarkdown.tools.MarkdownFormat;

import static my.shouheng.palmmarkdown.MDItemView.tintDrawable;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class SymbolBarLayout extends FrameLayout implements View.OnClickListener {
    private OnSymbolCharClickListener onSymbolCharClickListener;
    private String[] charList;
    LinearLayout llContainer;
    ImageView ivEnableFormat;
    ImageView ivSelfDesign;
    ImageView ivSetting;
    RelativeLayout rlBottomEditors;
    RelativeLayout rlBottom;

    ImageView ivRedo;
    ImageView ivUndo;

    public interface OnSymbolCharClickListener {
        void onClick(View v, String text);

        List<MarkdownFormat> getMarkdownFormats();

        void showTableEditor();

        void showLinkEditor();

        void showAttachmentPicker();

        void redo();

        void undo();

        void onClickFormat(MarkdownFormat markdownFormat);

        void onClickSetting();
        void onClickTvSetting();
    }

    public SymbolBarLayout(Context context) {
        super(context);
        init();
    }

    public SymbolBarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public SymbolBarLayout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init();
    }

    private void init() {
//        mGestureDetector = new GestureDetector(getContext(), new YScrollDetector());
        setScrollContainer(true);
        String symbol = Pref.getInstance(getContext()).getSymbol();
        charList = TextUtils.split(symbol, "\n");
        makeItemViews(getContext());
    }
    int switchMode = 0;
    private void makeItemViews(Context context) {
        if (charList == null || charList.length == 0)
            return;

        LayoutInflater inflater = LayoutInflater.from(context);
        rlBottom = (RelativeLayout) inflater.inflate(R.layout.editor_md_toolbar, this, false);
        llContainer = rlBottom.findViewById(R.id.ll_container);
        ivEnableFormat = rlBottom.findViewById(R.id.iv_enable_format);
        ivSelfDesign = rlBottom.findViewById(R.id.iv_self_design);
        ivSetting = rlBottom.findViewById(R.id.iv_setting);
        rlBottomEditors = rlBottom.findViewById(R.id.rl_bottom_editors);
        ivRedo = rlBottom.findViewById(R.id.iv_redo);
        ivUndo = rlBottom.findViewById(R.id.iv_undo);
        addView(rlBottom);

        rlBottomEditors.setVisibility(View.GONE);

        int[] ids = new int[]{R.id.iv_redo, R.id.iv_undo, R.id.iv_insert_picture, R.id.iv_insert_link, R.id.iv_table};
        for (int id : ids) {
            findViewById(id).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFormatClick(v);
                }
            });
        }

        addBottomMenus();

        ivEnableFormat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchMode != 1) addBottomMenus();
                switchFormat();
            }
        });
        ivSelfDesign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchMode != 2) addBottomMenusTv();
                switchSelfDesign();
            }
        });
        ivSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSymbolCharClickListener != null) {
                    if (switchMode == 2) {
                        onSymbolCharClickListener.onClickTvSetting();
                    } else {
                        onSymbolCharClickListener.onClickSetting();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (onSymbolCharClickListener == null)
            return;

        String str = ((TextView) v).getText().toString();
        if ("\\t".equals(str)) {
            str = "\t";
        } else if ("\\n".equals(str)) {
            str = "\n";
        }
        onSymbolCharClickListener.onClick(v, str);
    }

    public void setOnSymbolCharClickListener(OnSymbolCharClickListener listener) {
        this.onSymbolCharClickListener = listener;
    }

    // region Bottom Menus
    public void setUndoEnable(boolean enable){
        if (ivUndo != null) {
            ivUndo.setEnabled(enable);
            ivUndo.setImageResource(enable
                    ? R.drawable.m_undo_white
                    : R.drawable.m_undo_disabled);
        }
    }

    public void setRedoEnable(boolean enable){
        if (ivRedo != null) {
            ivRedo.setEnabled(enable);
            ivRedo.setImageResource(enable
                    ? R.drawable.m_redo_white
                    : R.drawable.m_redo_disabled);
        }
    }

    public void addBottomMenus() {
        switchMode = 1;
        llContainer.removeAllViews();
        int dp12 = dp2Px(getContext(), 9);
        List<MarkdownFormat> markdownFormats = getMarkdownFormats();
        for (final MarkdownFormat markdownFormat : markdownFormats) {
            MDItemView mdItemView = new MDItemView(getContext());
            mdItemView.setMarkdownFormat(markdownFormat);
            mdItemView.setPadding(dp12, dp12, dp12, dp12);
            llContainer.addView(mdItemView);
            mdItemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickFormat(markdownFormat);
                }
            });
        }
    }

    public void addBottomMenusTv() {
        switchMode = 2;
        String symbol = Pref.getInstance(getContext()).getSymbol();
        charList = TextUtils.split(symbol, "\n");
        llContainer.removeAllViews();
        int dp12 = dp2Px(getContext(), 9);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (final String str : charList) {
            TextView mdItemView = (TextView) inflater.inflate(R.layout.symbol_item, this, false);
            mdItemView.setText(str);
            mdItemView.setPadding(dp12, dp12, dp12, dp12);
            llContainer.addView(mdItemView);
            mdItemView.setOnClickListener(this);
        }
    }

    private void onClickFormat(MarkdownFormat markdownFormat) {
        if (onSymbolCharClickListener != null) {
            onSymbolCharClickListener.onClickFormat(markdownFormat);
        }
    }

    public List<MarkdownFormat> getMarkdownFormats() {
        if (onSymbolCharClickListener != null) {
            return onSymbolCharClickListener.getMarkdownFormats();
        }
        return MarkdownFormat.defaultMarkdownFormats;
    }

    private void onFormatClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_undo) {
            undo();
        } else if (i == R.id.iv_redo) {
            redo();
        } else if (i == R.id.iv_insert_picture) {
            showAttachmentPicker();
        } else if (i == R.id.iv_insert_link) {
            showLinkEditor();
        } else if (i == R.id.iv_table) {
            showTableEditor();
        }
    }

    private void switchFormat() {
        boolean rlBottomVisible = rlBottomEditors.getVisibility() == View.VISIBLE;
        rlBottomEditors.setVisibility(rlBottomVisible ? View.GONE : View.VISIBLE);
        ivEnableFormat.setImageDrawable(tintDrawable(
                getResources().getDrawable(R.drawable.ic_text_format_black_24dp),
                rlBottomVisible ? Color.WHITE : getResources().getColor(R.color.colorPrimary)));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                ivEnableFormat.getHeight() * (rlBottomVisible ? 1 : 2));

        rlBottom.setLayoutParams(params);
    }

    private void switchSelfDesign() {
        boolean rlBottomVisible = rlBottomEditors.getVisibility() == View.VISIBLE;
        rlBottomEditors.setVisibility(rlBottomVisible ? View.GONE : View.VISIBLE);
        ivSelfDesign.setImageDrawable(tintDrawable(
                getResources().getDrawable(R.drawable.ic_selfdesign),
                rlBottomVisible ? Color.WHITE : getResources().getColor(R.color.colorPrimary)));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                ivSelfDesign.getHeight() * (rlBottomVisible ? 1 : 2));

        rlBottom.setLayoutParams(params);
    }

    private void undo() {
        if (onSymbolCharClickListener != null) {
            onSymbolCharClickListener.undo();
        }
    }

    private void redo() {
        if (onSymbolCharClickListener != null) {
            onSymbolCharClickListener.redo();
        }
    }

    private void showTableEditor() {
        if (onSymbolCharClickListener != null) {
            onSymbolCharClickListener.showTableEditor();
        }
    }

    private void showLinkEditor() {
        if (onSymbolCharClickListener != null) {
            onSymbolCharClickListener.showLinkEditor();
        }
    }

    private void showAttachmentPicker() {
        if (onSymbolCharClickListener != null) {
            onSymbolCharClickListener.showAttachmentPicker();
        }
    }

    public static int dp2Px(Context context, float dpValues) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValues * scale + 0.5f);
    }
    // endregion

}
