package com.confortapp.leon.confortapp.ExpandableList;

import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.confortapp.leon.confortapp.R;

import java.util.List;

/**
 * Created by Leon on 19.03.2018.
 */

public class InfoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        SectionHeaderViewHolder.HeaderViewHolderCallback {
    private static final int USER_TYPE = 1;
    private static final int HEADER_TYPE = 2;
    private List<Info> infoList;
    private List<String> infoTypeList;

    private SparseArray<ViewType> viewTypes;
    private SparseIntArray headerExpandTracker;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case USER_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_info_list_item, parent, false);
                return new InfoViewHolder(view);
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_info_list_section_header, parent, false);
                return new SectionHeaderViewHolder(view, this);
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_info_list_item, parent, false);
                return new InfoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        ViewType viewType = viewTypes.get(position);

        if (itemViewType == USER_TYPE) {
            bindUserViewHolder(holder, viewType);
        } else {
            bindHeaderViewHolder(holder, position, viewType);
        }
    }

    private void bindHeaderViewHolder(RecyclerView.ViewHolder holder, int position, ViewType viewType) {
        int dataIndex = viewType.getDataIndex();
        SectionHeaderViewHolder headerViewHolder = (SectionHeaderViewHolder) holder;
        headerViewHolder.sectionTitle.setText(infoTypeList.get(dataIndex));

        if (isExpanded(position)) {
            headerViewHolder.sectionTitle
                    .setCompoundDrawablesWithIntrinsicBounds(null, null,
                            headerViewHolder.arrowUp, null);
        } else {
            headerViewHolder.sectionTitle
                    .setCompoundDrawablesWithIntrinsicBounds(null, null,
                            headerViewHolder.arrowDown, null);
        }
    }

    private void bindUserViewHolder(RecyclerView.ViewHolder holder, ViewType viewType) {
        int dataIndex = viewType.getDataIndex();
        ((InfoViewHolder) holder).username.setText(infoList.get(dataIndex).getName());
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (infoTypeList != null && infoList != null) {
            viewTypes.clear();
            int collapsedCount = 0;
            for (int i = 0; i < infoTypeList.size(); i++) {
                viewTypes.put(count, new ViewType(i, HEADER_TYPE));
                count += 1;
                String userType = infoTypeList.get(i);
                int childCount = getChildCount(userType);
                if (headerExpandTracker.get(i) != 0) {
                    // Expanded State
                    for (int j = 0; j < childCount; j++) {
                        viewTypes.put(count, new ViewType(count - (i + 1) + collapsedCount, USER_TYPE));
                        count += 1;
                    }
                } else {
                    // Collapsed
                    collapsedCount += childCount;
                }
            }
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (viewTypes.get(position).getType() == HEADER_TYPE) {
            return HEADER_TYPE;
        } else {
            return USER_TYPE;
        }
    }

    private int getChildCount(String type) {
        switch (type) {
            case "Chisinau":
                return 1;
            case "AneniiNoi":
                return 1;
            case "Balti":
                return 1;
            case "Basarabeasca":
                return 1;
            default:
                return 0;
        }
    }

    public void seInfoListAndType(List<Info> infoList, List<String> infoTypeList) {
        if (infoList != null && infoTypeList != null) {
            this.infoList = infoList;
            this.infoTypeList = infoTypeList;
            viewTypes = new SparseArray<>(infoList.size() + infoTypeList.size());
            headerExpandTracker = new SparseIntArray(infoTypeList.size());
            notifyDataSetChanged();
        }
    }

    @Override
    public void onHeaderClick(int position) {
        ViewType viewType = viewTypes.get(position);
        int dataIndex = viewType.getDataIndex();
        String infoType = infoTypeList.get(dataIndex);
        int childCount = getChildCount(infoType);
        if (headerExpandTracker.get(dataIndex) == 0) {
            // Collapsed. Now expand it
            headerExpandTracker.put(dataIndex, 1);
            notifyItemRangeInserted(position + 1, childCount);
        } else {
            // Expanded. Now collapse it
            headerExpandTracker.put(dataIndex, 0);
            notifyItemRangeRemoved(position + 1, childCount);
        }
    }

    @Override
    public boolean isExpanded(int position) {
        int dataIndex = viewTypes.get(position).getDataIndex();
        return headerExpandTracker.get(dataIndex) == 1;
    }
}
