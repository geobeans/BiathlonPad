package cn.geobeans.biathlon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.geobeans.biathlon.entity.Shooting;

public class RecordActivity extends AppCompatActivity {

    private List<Shooting> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
    }


}