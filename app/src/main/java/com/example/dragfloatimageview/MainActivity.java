package com.example.dragfloatimageview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dragfloatimageview.widget.RichTextView;
import com.example.dragfloatimageview.widget.TableView;

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
        for (int i = 0; i < 6; i++) {
            r += " | ";
            String c = "";
            c += r;
            CharSequence[] columnList = new CharSequence[3];
            for (int j = 0; j < 3; j++) {
                c += " = ";

                columnList[j] = c;
            }
            list.add(columnList);
        }

        TableView table = findViewById(R.id.dataTable);
        //通过代码设置
//        table.setConfig(table.getConfig()
//                .setCellLayoutId(R.layout.table_item)
//                .setColumns(3)
//                .setRows((int) Math.ceil(list.size()))
//                .setColumnsWeights(1.5f, 1, 2f)
//                .setRadius(15)
//                .setListener(new TableView.OnColumnViewAddedListener() {
//                    @Override
//                    public void onViewAdded(TextView cellView, int rowNum, int columnNum) {
//                        if (rowNum == 0) {
//                            cellView.setBackgroundColor(Color.GRAY);
//                        } else {
//                        }
//                        if (rowNum == list.size() - 1 && columnNum == list.get(0).length - 1) {
//                            return;
//                        }
//                        cellView.setText(list.get(rowNum)[columnNum]);
//                        Log.e(TAG, "onViewAdded: " + rowNum + ", " + columnNum);
//                    }
//                }));

    }
}
