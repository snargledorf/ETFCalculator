package com.theeste.etfcalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by Ryan on 6/9/2014.
 */
public class ToggleViewGroupTableLayout extends TableLayout implements View.OnClickListener {
    private static final String TAG = "ToggleButtonGroupTableLayout";
    private View activeView;
    private OnCheckedChangeListener onCheckedChangeListner;

    /**
     * @param context
     */
    public ToggleViewGroupTableLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     */
    public ToggleViewGroupTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onClick(View v) {
        if (activeView != null) {
            ((Checkable)activeView).setChecked(false);
            sendOnCheckedChangeNotification(activeView, false);
        }
        ((Checkable)v).setChecked(true);
        activeView = v;
        sendOnCheckedChangeNotification(activeView, true);
    }

    private void sendOnCheckedChangeNotification(View view, boolean checked) {
        if (onCheckedChangeListner != null) {
            onCheckedChangeListner.onCheckedChange(view, checked);
        }
    }

    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, int, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setChildrenOnClickListener((TableRow) child);
    }


    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        setChildrenOnClickListener((TableRow)child);
    }

    private void setChildrenOnClickListener(TableRow tr) {
        final int c = tr.getChildCount();
        for (int i=0; i < c; i++) {
            final View v = tr.getChildAt(i);
            if ( v instanceof Checkable ) {
                v.setOnClickListener(this);
            }
        }
    }

    public int getCheckedViewId() {
        if ( activeView != null ) {
            return activeView.getId();
        }

        return -1;
    }

    public void setOnCheckChangedListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListner = onCheckedChangeListener;
    }

    public static interface OnCheckedChangeListener {
        void onCheckedChange(View view, boolean checked);
    }
}
