package com.example.dragfloatimageview;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.library.customviews.RichTextView;
import com.library.customviews.TableView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RichTextView rtv = findViewById(R.id.rtv);
        rtv.setOnClickListener(v -> {
            rtv.setText_1("替换");
        });

        dataTableViewTest();
    }

    private void dataTableViewTest() {
        List<CharSequence[]> list = new ArrayList<>();
        list.add(new CharSequence[]{"Header 1", "Header 2", "Header 3"});
        String r = "";
        for (int i = 0; i < 3; i++) {
            r += " | ";
            String c = "";
            c += r;
            CharSequence[] columnList = new CharSequence[3];
            for (int j = 0; j < 3; j++) {
                if (j == 0) {
                    columnList[j] = "a \n bbb\ncccc";
                } else {
                    c += " = ";
                    columnList[j] = c;
                }

            }
            list.add(columnList);
        }

        TableView table = findViewById(R.id.dataTable);
        //通过代码设置
        table.setConfig(table.getConfig()
                .setCellLayoutId(R.layout.table_item)
                .setColumns(3)
                .setRows((int) Math.ceil(list.size()))
                .setColumnsWeights(2, 4, 6f)
                .setRadius(15)
                .setDividerWidth(66)
                .setListener((cell) -> {
                    int rowNum = cell.rowNum;
                    TextView cellView = cell.cellView;
                    int columnNum = cell.columnNum;
                    if (rowNum == 0) {
                        cellView.setBackgroundColor(Color.GRAY);
                    } else {
                    }
                    if (rowNum == list.size() - 1 && columnNum == list.get(0).length - 1) {
                        return;
                    }
                    cellView.setText(list.get(rowNum)[columnNum]);
                    Log.e(TAG, "onViewAdded: " + rowNum + ", " + columnNum);
                }));

    }
}
