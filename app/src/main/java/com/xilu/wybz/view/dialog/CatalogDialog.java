//package com.xilu.wybz.view.dialog;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.xilu.wybz.R;
//import com.xilu.wybz.adapter.ActionAdapter;
//import com.xilu.wybz.bean.ActionBean;
//import com.xilu.wybz.bean.HotCatalog;
//import com.xilu.wybz.utils.DensityUtil;
//
//import java.util.List;
//
//
//public class CatalogDialog extends Dialog{
//    Context content;
//    RecyclerView recyclerView;
//    AdapterView.OnItemClickListener ipl2;
//    AdapterView.OnItemClickListener ipl1;
//    List<HotCatalog> mList;
//    List<String> types;
//    public CatalogDialog(Context content, AdapterView.OnItemClickListener ipl1,AdapterView.OnItemClickListener ipl2, List<HotCatalog> actionBeans,List<String> types) {
//        super(content, R.style.ToastDialog);
//        this.content = content;
//        this.mList = actionBeans;
//        this.types = types;
//        this.ipl1 = ipl1;
//        this.ipl2 = ipl2;
//        setCanceledOnTouchOutside(true);
//        getWindow().setGravity(Gravity.TOP);
//        getWindow().setWindowAnimations(R.style.TopDialogAnim); //设置窗口弹出动画
//        setContentView(getDialogView());
//    }
//
//    public View getDialogView() {
//        View rootView = LayoutInflater.from(content).inflate(
//                R.layout.ll_catalog, null);
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.catalog_recyclerview);
//        return rootView;
//    }
//
//
//    public void showDialog() {
//        show();
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.dimAmount=0.15f;
//        params.x = 0; // 新位置X坐标
//        params.y = DensityUtil.getStatusBarHeight(content)+DensityUtil.getActionBarHeight(content); // 新位置Y坐标
//        params.width = DensityUtil.getScreenW(content);
//        getWindow().setAttributes(params);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//    }
//
//}
