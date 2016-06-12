package com.xilu.wybz.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.RecycleViewAdapter;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;


public class RecycleViewFragment extends BaseFragment {


    @Bind(R.id.id_stickynavlayout_innerscrollview)
    RecyclerView mRecycleView;

    public static RecycleViewFragment newInstance() {
        RecycleViewFragment fragment = new RecycleViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_recycle_view;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false));
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("RecycleView----item-->>>>" + i);
        }

        RecycleViewAdapter adapter = new RecycleViewAdapter(list);
        mRecycleView.setAdapter(adapter);
        adapter.setOnItemOnClickListener(new RecycleViewAdapter.OnItemOnClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Toast.makeText(getActivity(), "click " + position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClickListener(View view, int position) {
                Toast.makeText(getActivity(), "Long-click " + position, Toast.LENGTH_LONG).show();
            }
        });
    }
}
