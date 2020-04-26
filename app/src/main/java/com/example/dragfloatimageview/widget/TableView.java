package com.example.dragfloatimageview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;

import com.example.dragfloatimageview.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 简单的表格控件；
 * 支持设置行列数、边框圆角、网格线粗细、颜色, 可设置列的宽度比例；
 * 也可通过 {@link TableView#setConfig(Config)} 监听/自定义每个格子（目前一个TextView即是一个小格）样式
 *
 * @author wzw
 * @date: 2020/4/22 16:19
 */
public class TableView extends LinearLayout {
    private static final String TAG = "DataTableView";
    /**
     * 行数
     */
    private int columns;
    private int rows;
    float dividerWidth;
    int dividerColor;
    float radius;
    private List<Float> columnsWeights;
    private OnCellViewAddedListener listener;
    private Config config;
    private int cellLayoutId;
    private Paint paint;

    public TableView(Context context) {
        super(context);
    }

    public TableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TableView, defStyleAttr, 0);
        columns = ta.getInt(R.styleable.TableView_tableColumns, 2);
        rows = ta.getInt(R.styleable.TableView_tableRows, 2);
        radius = ta.getDimension(R.styleable.TableView_tableCornerRadius, 0);
        columnsWeights = getWeights(ta.getString(R.styleable.TableView_tableColumnsWeights));
        dividerWidth = ta.getDimension(R.styleable.TableView_tableDividerWidth, 1);
        dividerColor = ta.getColor(R.styleable.TableView_tableDividerColor, Color.LTGRAY);
        cellLayoutId = ta.getResourceId(R.styleable.TableView_tableCellLayout, R.layout.bures_table_view_item);
        ta.recycle();

        config = initConfig();

        setOrientation(VERTICAL);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        initViews();
    }

    private void initViews() {
        removeAllViews();
        if (cellLayoutId == 0) {
            throw new IllegalArgumentException("cellLayoutId must not 0。");
        }
        checkWeights();
        paint.setColor(dividerColor);
        paint.setStrokeWidth(dividerWidth);
        int divider = (int) this.dividerWidth;
        setPadding(getPaddingLeft() + divider, getPaddingTop() + divider,
                getPaddingRight() + divider, getPaddingBottom() + divider);
        for (int i = 0; i < rows; i++) {
            LinearLayout ll = new LinearLayout(getContext());
            for (int j = 0; j < columns; j++) {
                TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(cellLayoutId, this, false);
                LinearLayout.LayoutParams lp = (LayoutParams) tv.getLayoutParams();
                lp.width = 0;
                lp.weight = columnsWeights.get(j);
                lp.gravity = Gravity.CENTER_VERTICAL;
                ll.addView(tv, lp);
                if (listener != null) {
                    listener.onViewAdded(new Cell(tv, rows, columns, i, j, i * columns + j));
                }
            }
            addView(ll, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //裁边
        clipBorder(canvas);
        super.dispatchDraw(canvas);
        //画竖线
        drawVerticalLine(canvas);
        //画横线
        drawHorizontalLine(canvas);
        //圆角边框
        drawBorder(canvas);
    }

    /**
     * 裁边
     */
    private void clipBorder(Canvas canvas) {
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        float radius = this.radius + dividerWidth / 2;
        Path path = new Path();
        path.addRoundRect(rectF, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
    }

    private void drawVerticalLine(Canvas canvas) {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null) {
            return;
        }
        for (int i = 0; i < viewGroup.getChildCount() - 1; i++) {
            View cellView = viewGroup.getChildAt(i);
            float startX = cellView.getX() + cellView.getWidth() + getPaddingLeft();
            canvas.drawLine(startX, 0, startX, getHeight(), paint);
        }
    }

    private void drawHorizontalLine(Canvas canvas) {
        for (int i = 0; i < getChildCount() - 1; i++) {
            View child = getChildAt(i);
            float startY = child.getY() + child.getHeight();
            canvas.drawLine(0, startY, getWidth(), startY, paint);
        }
    }

    /**
     * 画边框
     */
    private void drawBorder(Canvas canvas) {
        float padding = dividerWidth / 2;
        RectF r = new RectF(padding, padding, getWidth() - padding, getHeight() - padding);
        canvas.drawRoundRect(r, radius, radius, paint);
    }

    /**
     * 当权重columnsWeights的值不够时，用最后一个值代替
     */
    private void checkWeights() {
        if (columnsWeights.isEmpty()) {
            columnsWeights.add(1f);
        }
        if (columnsWeights.size() < columns) {
            columnsWeights.add(columnsWeights.get(columnsWeights.size() - 1));
            checkWeights();
        }
    }

    @NonNull
    private List<Float> getWeights(String weightStr) {
        List<Float> weights = new ArrayList<>();
        if (!TextUtils.isEmpty(weightStr)) {
            weightStr = weightStr.replace(" ", "");
            String[] list = weightStr.split(",");
            for (String s : list) {
                weights.add(Float.valueOf(s));
            }
        }
        return weights;
    }

    private Config initConfig() {
        Config config = new Config(this);
        config.columns = columns;
        config.rows = rows;
        config.radius = radius;
        config.columnsWeights = columnsWeights;
        config.dividerWidth = dividerWidth;
        config.dividerColor = dividerColor;
        config.cellLayoutId = cellLayoutId;
        config.listener = listener;
        return config;
    }

    /**
     * 通过这个方法修改表格样式
     */
    public void setConfig(Config config) {
        columns = config.columns;
        rows = config.rows;
        radius = config.radius;
        columnsWeights = config.columnsWeights;
        dividerWidth = config.dividerWidth;
        dividerColor = config.dividerColor;
        cellLayoutId = config.cellLayoutId;
        listener = config.listener;
        this.config = config;
        initViews();
    }

    public Config getConfig() {
        return config;
    }

    public static class Cell {
        public TextView cellView;
        public int totalRows;
        public int totalColumns;
        public int rowNum;
        public int columnNum;
        /**
         * 在表格中从左到右从上到下开始数的位置编号
         */
        public int position;

        private Cell(TextView cellView, int totalRows, int totalColumns, int rowNum, int columnNum, int position) {
            this.cellView = cellView;
            this.totalRows = totalRows;
            this.totalColumns = totalColumns;
            this.rowNum = rowNum;
            this.columnNum = columnNum;
            this.position = position;
        }
    }


    public interface OnCellViewAddedListener {
        void onViewAdded(Cell cell);
    }

    public static class Config {
        int columns;
        int rows;
        float dividerWidth;
        int dividerColor;
        float radius;
        List<Float> columnsWeights;
        OnCellViewAddedListener listener;
        int cellLayoutId;
        SoftReference<TableView> tableViewReference;

        private Config(TableView tableView) {
            tableViewReference = new SoftReference<>(tableView);
        }

        /**
         * 设置列数
         */
        public Config setColumns(int columnsCount) {
            this.columns = columnsCount;
            return this;
        }

        /**
         * 设置行数
         */
        public Config setRows(int rowsCount) {
            this.rows = rowsCount;
            return this;
        }

        /**
         * 表格线颜色
         */
        public Config setDividerWidth(@Px float dividerWidth) {
            this.dividerWidth = dividerWidth;
            return this;
        }

        /**
         * 表格线颜色
         */
        public Config setDividerColor(@ColorInt int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        /**
         * 边框角半径
         */
        public Config setRadius(@Px float radius) {
            this.radius = radius;
            return this;
        }

        /**
         * 设置列宽度权重，可以和列数column不一致;
         * 如果权重是 1：1：1 则可写为 setColumnsWeights(1) 或 setColumnsWeights(1,1,1)
         */
        public Config setColumnsWeights(float... columnsWeights) {
            this.columnsWeights.clear();
            for (float v : columnsWeights) {
                this.columnsWeights.add(v);
            }
            return this;
        }

        /**
         * 监听每个小格子，用于自定义设置小格的属性等
         */
        public Config setListener(OnCellViewAddedListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 每个小格的布局，目前只支持根布局为TextView
         */
        public Config setCellLayoutId(@LayoutRes int cellLayoutId) {
            this.cellLayoutId = cellLayoutId;
            return this;
        }

        public void update() {
            if (tableViewReference.get() != null) {
                tableViewReference.get().setConfig(this);
            }
        }
    }

}
