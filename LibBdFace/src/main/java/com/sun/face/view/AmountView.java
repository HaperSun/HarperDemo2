package com.sun.face.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sun.face.R;

/**
 * @author: Harper
 * @date: 2022/5/13
 * @note: 带减少增加按钮
 */
public class AmountView extends LinearLayout implements View.OnClickListener, TextWatcher {

    public static final String QUALITY_ILLUM = "illum";
    public static final String QUALITY_HEADPOSE = "headPose";
    public static final String QUALITY_BLUR = "blur";
    public static final String QUALITY_OCCLU = "occlu";

    private float amount;         // 默认数值
    private float maxNum;         // 最大数值
    private float minNum;         // 最小数值
    private float interval;       // 设置间隔
    private String quality;       // 质量检测类型

    private OnAmountChangeListener mListener;

    private EditText etAmount;
    private Button btnDecrease;
    private Button btnIncrease;

    public AmountView(Context context) {
        this(context, null);
    }

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.view_amount, this);
        etAmount = (EditText) view.findViewById(R.id.etAmount);
        btnDecrease = (Button) view.findViewById(R.id.btnDecrease);
        btnIncrease = (Button) view.findViewById(R.id.btnIncrease);
        etAmount.setEnabled(false);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        etAmount.addTextChangedListener(this);
    }

    public void setOnAmountChangeListener(OnAmountChangeListener onAmountChangeListener) {
        this.mListener = onAmountChangeListener;
    }

    // 设置默认数值
    public void setAmount(float amount) {
        this.amount = amount;
        refreshView();
    }

    private void refreshView() {
        etAmount.post(() -> {
            if (QUALITY_ILLUM.equals(quality) || QUALITY_HEADPOSE.equals(quality)) {
                etAmount.setText((int) amount + "");
            } else {
                etAmount.setText(String.format("%1.2f", amount));
            }
        });
    }

    // 设置最大范围
    public void setMaxNum(float maxNum) {
        this.maxNum = maxNum;
    }

    // 设置最小范围
    public void setMinNum(float minNum) {
        this.minNum = minNum;
    }

    // 设置间隔
    public void setInterval(float interval) {
        this.interval = interval;
    }

    // 设置质量检测类型
    public void setQuality(String quality) {
        this.quality = quality;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        // 点击『-』
        if (i == R.id.btnDecrease) {
            if (amount > minNum) {
                amount -= interval;
                if (QUALITY_ILLUM.equals(quality) || QUALITY_HEADPOSE.equals(quality)) {
                    etAmount.setText((int) amount + "");
                } else {
                    etAmount.setText(String.format("%1.2f", amount));
                }
            }
            // 点击『+』
        } else if (i == R.id.btnIncrease) {
            if (amount < maxNum) {
                amount += interval;
                if (QUALITY_ILLUM.equals(quality) || QUALITY_HEADPOSE.equals(quality)) {
                    etAmount.setText((int) amount + "");
                } else {
                    etAmount.setText(String.format("%1.2f", amount));
                }
            }
        }

        etAmount.clearFocus();

        if (mListener != null) {
            if (QUALITY_ILLUM.equals(quality) || QUALITY_HEADPOSE.equals(quality)) {
                mListener.onAmountChange(this, (int) amount + "");
            } else {
                mListener.onAmountChange(this, String.format("%1.2f", amount));
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty()) {
            return;
        }
        amount = Float.valueOf(s.toString());
        if (amount > maxNum) {
            if (QUALITY_ILLUM.equals(quality) || QUALITY_HEADPOSE.equals(quality)) {
                etAmount.setText((int) maxNum + "");
            } else {
                etAmount.setText(String.format("%1.2f", maxNum));
            }
            return;
        } else if (amount < minNum) {
            if (QUALITY_ILLUM.equals(quality) || QUALITY_HEADPOSE.equals(quality)) {
                etAmount.setText((int) minNum + "");
            } else {
                etAmount.setText(String.format("%1.2f", minNum));
            }
            return;
        }
        if (amount >= maxNum) {
            btnIncrease.setBackgroundResource(R.mipmap.icon_increase_grey);
        } else {
            btnIncrease.setBackgroundResource(R.drawable.selector_setting_increase);
        }
        if (amount <= minNum) {
            btnDecrease.setBackgroundResource(R.mipmap.icon_decrease_grey);
        } else {
            btnDecrease.setBackgroundResource(R.drawable.selector_setting_decrease);
        }
        if (mListener != null) {
            if (QUALITY_ILLUM.equals(quality) || QUALITY_HEADPOSE.equals(quality)) {
                mListener.onAmountChange(this, (int) amount + "");
            } else {
                mListener.onAmountChange(this, String.format("%1.2f", amount));
            }
        }
    }

    public interface OnAmountChangeListener {
        void onAmountChange(View view, String amount);
    }

}
