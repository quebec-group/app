package com.quebec.app;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 03/02/2017.
 */


public class EventListAdapterItem extends RecyclerView.Adapter<EventListAdapterItem.EventHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private final Context mContext;

    private ArrayList<Event> mDataset;
    private static EventItemClickInterface myClickListener;

    public static class EventHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        private TextView eventName;

        private RecyclerView eventItemTicker;
        public EventHolder(View itemView) {
            super(itemView);
            eventName = (TextView) itemView.findViewById(R.id.eventItemName);
            eventItemTicker = (RecyclerView) itemView.findViewById(R.id.eventItemTicker);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(EventItemClickInterface myClickListener) {
        this.myClickListener = myClickListener;
    }

    public EventListAdapterItem(ArrayList<Event> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_event_item, parent, false);

        EventHolder dataObjectHolder = new EventHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        holder.eventName.setText(mDataset.get(position).getEventName());


        EventUsersTickerAdapterItem adapter = new EventUsersTickerAdapterItem(mDataset.get(position).getAttendees());

             /* Makes use of the RecyclerView for the horizontal scrolling field. */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.mContext , LinearLayoutManager.HORIZONTAL, false);

        holder.eventItemTicker.setLayoutManager(layoutManager);
        holder.eventItemTicker.setAdapter(adapter);
    }


    /**
     * Add item to the list of events on the events feed.
     * @param dataObj
     * @param index
     */
    public void addItem(Event dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    /**
     * Deletes an item from the list on the events feed.
     * @param index
     */
    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * Counts the number of items in the events feed.
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface EventItemClickInterface {
        void onItemClick(int position, View v);
    }
}

