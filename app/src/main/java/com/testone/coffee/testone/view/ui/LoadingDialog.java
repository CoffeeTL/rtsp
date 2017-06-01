package com.testone.coffee.testone.view.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.testone.coffee.testone.R;
import com.testone.coffee.testone.utils.DensityUtil;

/**
 * Created by coffee on 2017/5/25.
 */

public class LoadingDialog extends Dialog {
    private Context context;

    public LoadingDialog(Context context) {
        super(context, R.style.loadingdialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loading_dialog, null);
        setContentView(view);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = DensityUtil.dip2px(context,100);
        lp.height = DensityUtil.dip2px(context,100);
        dialogWindow.setAttributes(lp);
        setCanceledOnTouchOutside(true);
        //setCancelable(false);
    }
}
