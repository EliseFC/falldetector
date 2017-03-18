package com.android.falldetector;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A simple {@link ListFragment} subclass.
 * Use the {@link History#newInstance} factory method to
 * create an instance of this fragment.
 */
public class History extends ListFragment {

    private SimpleCursorAdapter mAdapter;
    private HistoryDBHelper mDbHelper;

    public History() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment History.
     */
    public static History newInstance() {
        return new History();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new HistoryDBHelper(getContext());

        Cursor cursor = mDbHelper.getCursor();
        String[] fromColumns = {"time", "location", "feedback"};
        int[] toViews = {R.id.text1, R.id.text2, R.id.text3};
        mAdapter = new SimpleCursorAdapter(getContext(),
                R.layout.history_list_item, cursor,
                fromColumns, toViews, 0);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("History", "------> onCreate");
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDbHelper.close();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public void updateView() {
        Cursor cursor = mDbHelper.getCursor();
        mAdapter.changeCursor(cursor); // the old cursor is closed automatically
    }
}
