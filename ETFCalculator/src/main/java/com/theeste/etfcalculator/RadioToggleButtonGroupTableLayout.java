package com.theeste.etfcalculator;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by Ryan on 6/9/2014.
 */
public class RadioToggleButtonGroupTableLayout extends TableLayout {
    private static final String TAG = "ToggleViewGroupTableLayout";
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private int mCheckedId = -1;
    private boolean mIgnoreCheckChanged = false;

    private PassThroughHierarchyChangeListener mPassThroughListener;
    private CheckedStateTracker mChildOnCheckedChangeListener;

    /**
     * @param context
     */
    public RadioToggleButtonGroupTableLayout(Context context) {
        super(context);
        init();
    }

    /**
     * @param context
     * @param attrs
     */
    public RadioToggleButtonGroupTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes =
                context.obtainStyledAttributes(attrs, R.styleable.RadioToggleButtonGroupTableLayout);
        int defaultCheckedViewId =
                attributes.getResourceId(R.styleable.RadioToggleButtonGroupTableLayout_defaultChecked, View.NO_ID);
        if (defaultCheckedViewId != View.NO_ID) {
            mCheckedId = defaultCheckedViewId;
        }
        init();
    }

    private void init() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (mCheckedId != -1) {
            mIgnoreCheckChanged = true;
            setCheckedStateForView(mCheckedId, true);
            mIgnoreCheckChanged = false;
            setCheckedId(mCheckedId);
        }
    }

    public void clearCheck() {
        check(-1);
    }

    public void check(int id) {
        if (id != -1 && id == mCheckedId)
            return;

        if (mCheckedId != -1)
            setCheckedStateForView(mCheckedId, false);

        if (id != -1)
            setCheckedStateForView(id, true);

        setCheckedId(id);
    }

    private void setCheckedStateForView(int id, boolean checked) {
        View view = findViewById(id);
        if (view != null && view instanceof Checkable) {
            ((Checkable)view).setChecked(checked);
        }
    }

    private void setCheckedId(int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null)
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
    }

    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, int, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        TableRow tableRow = (TableRow)child;
        final int cc = tableRow.getChildCount();
        for (int i = 0; i < cc; i++) {
            final View rowChild = tableRow.getChildAt(i);
            if (rowChild instanceof RadioToggleButton) {
                final RadioToggleButton toggleButton = (RadioToggleButton)rowChild;
                if (toggleButton.isChecked()) {
                    mIgnoreCheckChanged = true;
                    if (mCheckedId != -1) {
                        setCheckedStateForView(mCheckedId, false);
                    }
                    mIgnoreCheckChanged = false;
                    setCheckedId(rowChild.getId());
                }
            }
        }
        super.addView(child, index, params);
    }

    public int getCheckedViewId() {
        return mCheckedId;
    }

    public void setOnCheckChangedListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(RadioToggleButtonGroupTableLayout view, int viewId);
    }

    private class PassThroughHierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == RadioToggleButtonGroupTableLayout.this && child instanceof TableRow) {
                TableRow tableRow = (TableRow) child;
                final int c = tableRow.getChildCount();
                for (int i=0; i < c; i++) {
                    View rowChild = tableRow.getChildAt(i);
                    if (rowChild instanceof RadioToggleButton) {
                        int id = rowChild.getId();
                        if (id == View.NO_ID) {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                id = child.hashCode();
                            } else {
                                id = View.generateViewId();
                            }
                            rowChild.setId(id);
                        }

                        ((RadioToggleButton)rowChild).setOnCheckedChangeListener(mChildOnCheckedChangeListener);
                    }
                }
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (parent == RadioToggleButtonGroupTableLayout.this && child instanceof TableRow) {
                TableRow tableRow = (TableRow)child;
                final int c = tableRow.getChildCount();
                for (int i=0; i < c; i++) {
                    View rowChild = tableRow.getChildAt(i);
                    if (rowChild instanceof RadioToggleButton) {
                        ((RadioToggleButton) rowChild).setOnCheckedChangeListener(null);
                    }
                }
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            if (mIgnoreCheckChanged)
                return;

            mIgnoreCheckChanged = true;
            if (mCheckedId != -1)
                setCheckedStateForView(mCheckedId, false);
            mIgnoreCheckChanged = false;

            setCheckedId(compoundButton.getId());
        }
    }
}
