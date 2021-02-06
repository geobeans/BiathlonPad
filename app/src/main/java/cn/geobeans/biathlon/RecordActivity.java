package cn.geobeans.biathlon;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import cn.geobeans.biathlon.entity.Shooting;
import cn.geobeans.biathlon.utils.ShotListAdapter;

public class RecordActivity extends AppCompatActivity {

    private List<Shooting> mDatas = new ArrayList<>();
    private ShotListAdapter mAdapter;

    private int datacount = 0;;
    private int loaddatacount = 0;
    private int pagenum = 1;
    private int scrolledX;
    private int scrolledY;

    private ListView item_view_lv;
    private View item_view_null;

    private String mName = "ZHU";
    private int pagecount = 20;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        item_view_lv = findViewById(R.id.item_view_lv);
        item_view_null = findViewById(R.id.item_view_null);
        initViews();

        //loadDatas(true);
    }

    public boolean isListViewReachBottomEdge(final ListView listView){
        boolean result = false;
        if(listView.getLastVisiblePosition()==listView.getCount()-1){
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition()-
            listView.getFirstVisiblePosition());
            result = listView.getHeight()>=bottomChildView.getBottom();
        }
        return result;
    }

    private void loadDatas(boolean refresh) {
        if(refresh){
            mDatas.clear();
            mAdapter.notifyDataSetChanged();
            pagenum = 1;
            datacount = 0;
            scrolledX = 0;
            scrolledY = 0;
         }

        List<Shooting> shootings = LitePal.where("name=?",mName)
                .order("time1 desc").limit(pagecount).offset((pagenum-1)*pagecount)
                .find(Shooting.class);
        showDatas(shootings);
    }

    private void showDatas(List<Shooting> shootings) {
        loaddatacount += shootings.size();
        mDatas.addAll(shootings);
        mAdapter.notifyDataSetChanged();
        item_view_lv.scrollTo(scrolledX,scrolledY);
    }

    private void initViews() {
        item_view_lv.setEmptyView(item_view_null);
        item_view_lv.setDivider(new ColorDrawable(0xffd4d5d6));
        item_view_lv.setDividerHeight(5);
        mAdapter = new ShotListAdapter(this,mDatas);
        item_view_lv.setAdapter(mAdapter);

        item_view_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        item_view_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                loaddatacount<datacount && isListViewReachBottomEdge(item_view_lv)){
                    pagenum += 1;
                    loadDatas(false);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                scrolledX = item_view_lv.getScrollX();
                scrolledY = item_view_lv.getScrollY();
            }
        });

        final String[] athele_name =new String[5];
        athele_name [0]="Tom";
        athele_name [1]="Jack";
        athele_name [2]="John";
        athele_name [3]="Mary";
        athele_name [4]="Lisa";

        Spinner sz =  findViewById(R.id.query_name);

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, athele_name );
        sz.setAdapter(adapter);
        sz.setSelection(0,true);
        sz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                TextView tv = (TextView)view;
                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL); //设置居中
                mName = athele_name [position];
                loadDatas(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }


}