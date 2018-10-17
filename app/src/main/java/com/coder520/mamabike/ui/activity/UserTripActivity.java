package com.coder520.mamabike.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder520.mamabike.R;
import com.coder520.mamabike.entity.vo.RideRecordItem;
import com.coder520.mamabike.presenter.RodeRoutePresneter;
import com.coder520.mamabike.presenter.UserTripPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2017/9/17.
 */

public class UserTripActivity extends BaseActivity<UserTripPresenter> {
    @BindView(R.id.list_trip)
    ListView listTrip;
    private MyListAdapter mAdapter;
    private SimpleDateFormat mShowingFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected UserTripPresenter createPresenter() {
        return new UserTripPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trip);
        ButterKnife.bind(this);
        mAdapter = new MyListAdapter();
        listTrip.setAdapter(mAdapter);
        listTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RideRecordItem recordItem = (RideRecordItem) mAdapter.getItem(position);
                if (recordItem == null) {
                    return;
                }
                Intent intent = new Intent(UserTripActivity.this, RodeRouteActivity.class);
                intent.putExtra(RodeRoutePresneter.EXTRA_ROUTE_ID, recordItem.getRecordNo());
                launchActivity(true, intent);
            }
        });
    }

    private Handler mHanlder = new Handler();

    @Override
    protected String getActionTitle() {
        return getString(R.string.my_rode_record);
    }

    public void setTripDatas(final List<RideRecordItem> datas) {
        mHanlder.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.setRecordDatas(datas);
            }
        });
    }

    private class MyListAdapter extends BaseAdapter {
        private List<RideRecordItem> recordDatas = new ArrayList<>();

        public void setRecordDatas(List<RideRecordItem> datas) {
            this.recordDatas = datas;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return recordDatas == null ? 0 : recordDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return recordDatas == null ? null : recordDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_trip, parent, false);
            }
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.timeText = (TextView) convertView.findViewById(R.id.text_time);
                viewHolder.bikeNumberText = (TextView) convertView.findViewById(R.id.text_bike_number);
                viewHolder.rodeTimeText = (TextView) convertView.findViewById(R.id.text_rode_time);
                viewHolder.rodeCostText = (TextView) convertView.findViewById(R.id.text_rode_cost);
                viewHolder.divider1 = convertView.findViewById(R.id.divider_1);
                convertView.setTag(viewHolder);
            }
            RideRecordItem recordItem = recordDatas.get(position);
            Long time = -1L;
            try {
                time = Long.parseLong(recordItem.getStartTime());
            } catch (NumberFormatException e) {
            }
            if (time == -1L) {
                viewHolder.timeText.setText(R.string.get_time_fail);
            } else {
                viewHolder.timeText.setText(mShowingFormat.format(new Date(time)));
            }
            viewHolder.bikeNumberText.setText(String.format(
                    getString(R.string.bike_number_format), recordItem.getBikeNo() + ""));
            viewHolder.rodeTimeText.setText(String.format(getString(R.string.rode_time_format),
                    recordItem.getRideTime()));
            viewHolder.rodeCostText.setText(String.format(getString(R.string.rode_cost_format),
                    recordItem.getRideCost()));
            //配置左边两条竖线的显示
            configDivider(position, viewHolder.divider1);
            return convertView;
        }

        /**
         * 配置两条线的高度,
         *
         * @param currentPosition
         * @param dividerView
         */
        private void configDivider(int currentPosition, View dividerView) {
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) dividerView.getLayoutParams();
            int hight = getResources().getDimensionPixelSize(R.dimen.trip_item_time_hight);
            if (currentPosition == 0) {
                layoutParams.topMargin = hight / 2;
                layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            } else if (currentPosition == getCount() - 1) {
                layoutParams.topMargin = 0;
                layoutParams.height = hight / 2;
            } else {
                layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                layoutParams.topMargin = 0;
            }
            dividerView.setLayoutParams(layoutParams);
        }

        private class ViewHolder {
            TextView timeText;
            TextView bikeNumberText;
            TextView rodeTimeText;
            TextView rodeCostText;
            View divider1;
        }
    }
}
