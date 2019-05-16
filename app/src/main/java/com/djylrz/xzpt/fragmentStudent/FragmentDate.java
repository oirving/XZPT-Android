package com.djylrz.xzpt.fragmentStudent;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activityStudent.TimeLineAdapter;
import com.djylrz.xzpt.model.OrderStatus;
import com.djylrz.xzpt.model.Orientation;
import com.djylrz.xzpt.model.TimeLineModel;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentDate extends Fragment implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener {
    private static final String TAG = "FragmentDate";
    //日历部分
    private TextView mTextMonthDay;
    private TextView mTextYear;
    private TextView mTextLunar;
    private TextView mTextCurrentDay;
    private CalendarView mCalendarView;
    private RelativeLayout mRelativeTool;
    private int mYear;
    private CalendarLayout mCalendarLayout;

    //时间轴部分
    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    public final static String EXTRA_ORIENTATION = "EXTRA_ORIENTATION";
    public final static String EXTRA_WITH_LINE_PADDING = "EXTRA_WITH_LINE_PADDING";

    //全局View 用于获取控件
    private View globalView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_date, container, false);
        globalView = view;

        //初始化日历
        initView();
        initData();

        //初始化时间轴
        mOrientation = (Orientation) getActivity().getIntent().getSerializableExtra(FragmentDate.EXTRA_ORIENTATION);
        mWithLinePadding = getActivity().getIntent().getBooleanExtra(FragmentDate.EXTRA_WITH_LINE_PADDING, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

        initViewTimeLine();
        return view;
    }

    //时间轴方向控制
    private LinearLayoutManager getLinearLayoutManager() {
        if (mOrientation == Orientation.HORIZONTAL) {
            return new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
    }

    //时间轴视图初始化
    private void initViewTimeLine() {

        setDataListItems(0);
        mTimeLineAdapter = new TimeLineAdapter(mDataList, mOrientation, mWithLinePadding);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    //时间轴数据初始化
    private void setDataListItems(int day) {
        mDataList.clear();

        mDataList.add(new TimeLineModel("" + day, "", OrderStatus.INACTIVE));
        mDataList.add(new TimeLineModel("Courier is out to delivery your order", "2017-02-12 08:00", OrderStatus.ACTIVE));
        mDataList.add(new TimeLineModel("Item has reached courier facility at New Delhi", "2017-02-11 21:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item has been given to the courier", "2017-02-11 18:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item is packed and will dispatch soon", "2017-02-11 09:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order is being readied for dispatch", "2017-02-11 08:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order processing initiated", "2017-02-10 15:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order confirmed by seller", "2017-02-10 14:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order placed successfully", "2017-02-10 14:00", OrderStatus.COMPLETED));
        /*适配器不为空，通知适配器刷新*/
        if (mTimeLineAdapter != null) {
            mTimeLineAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("SetTextI18n")
    /**
     *@Description: 初始化日历视图
     *@Created in: 18-10-20 下午19:29
     *@Author: mingjun
     *@Method_Name: initView
     *@Param: []
     *@Return: void
     */
    protected void initView() {
        //setStatusBarDarkMode();
        mTextMonthDay = (TextView) globalView.findViewById(R.id.tv_month_day);
        mTextYear = (TextView) globalView.findViewById(R.id.tv_year);
        mTextLunar = (TextView) globalView.findViewById(R.id.tv_lunar);
        mRelativeTool = (RelativeLayout) globalView.findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) globalView.findViewById(R.id.calendarView);
        mTextCurrentDay = (TextView) globalView.findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mYear);
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        globalView.findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarLayout = (CalendarLayout) globalView.findViewById(R.id.calendarLayout);

        mCalendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(com.haibin.calendarview.Calendar calendar) {
//                Log.i(LOG_TAG+" onCalendarOutOfRange:",calendar.getYear()+" "+calendar.getMonth()+" "+calendar.getDay());
            }

            @Override
            public void onCalendarSelect(com.haibin.calendarview.Calendar calendar, boolean isClick) {
//                Log.i(LOG_TAG+" onCalendarSelect:",calendar.getYear()+" "+calendar.getMonth()+" "+calendar.getDay());
                Calendar realTime = Calendar.getInstance();
//                Log.i(LOG_TAG+" onCalendarSelect:",realTime.get(Calendar.YEAR)+" "+realTime.get(Calendar.MONTH)+" "+realTime.get(Calendar.DAY_OF_MONTH));
                realTime.set(Calendar.YEAR, calendar.getYear());
                realTime.set(Calendar.MONTH, calendar.getMonth() - 1);
                realTime.set(Calendar.DAY_OF_MONTH, calendar.getDay());
//                Log.i(LOG_TAG+" onCalendarSelect:",realTime.get(Calendar.YEAR)+" "+realTime.get(Calendar.MONTH)+" "+realTime.get(Calendar.DAY_OF_MONTH));
                setDataListItems(calendar.getDay());
            }
        });

        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    /**
     * @Description: 初始化日历视图数据
     * @Created in: 18-10-20 下午19:29
     * @Author: mingjun
     * @Method_Name: initData
     * @Param: []
     * @Return: void
     */
    protected void initData() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        //对日期添加上标
        Map<String, com.haibin.calendarview.Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "会").toString(),
                getSchemeCalendar(year, month, 3, 0xFF40db25, "会"));
        map.put(getSchemeCalendar(year, month, 6, 0xFF40db25, "会").toString(),
                getSchemeCalendar(year, month, 6, 0xFF40db25, "会"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);

    }

    /**
     * @Description: 设置点击事件
     * @Created in: 18-10-20 下午19:29
     * @Author: mingjun
     * @Method_Name: onClick
     * @Param: v
     * @Return: void
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * @Description: 在日历中添加标记
     * @Created in: 18-10-20 下午19:29
     * @Author: mingjun
     * @Method_Name: getSchemeCalendar
     * @Param year
     * @Param month
     * @Param day
     * @Param color
     * @Param text
     * @Return: com.haibin.calendarview.Calendar
     */
    private com.haibin.calendarview.Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new com.haibin.calendarview.Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    /**
     * @Description: 修改Toolbar上的月日信息
     * @Created in: 18-10-20 下午19:29
     * @Author: mingjun
     * @Method_Name: onCalendarSelect
     * @Param calendar
     * @Param isClick
     * @Return: com.haibin.calendarview.Calendar
     */
    @SuppressLint("SetTextI18n")
    public void onCalendarSelect(com.haibin.calendarview.Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }

    /**
     * @param year
     * @Description 点击Toolbar上的月日Textview时，将其修改成年份
     */
    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onCalendarOutOfRange(com.haibin.calendarview.Calendar calendar) {
    }

}
