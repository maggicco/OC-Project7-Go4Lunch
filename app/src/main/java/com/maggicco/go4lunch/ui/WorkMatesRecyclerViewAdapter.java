//package com.maggicco.go4lunch.ui;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.maggicco.go4lunch.R;
//import com.maggicco.go4lunch.databinding.FragmentWorkMatesBinding;
//import com.maggicco.go4lunch.model.WorkMate;
//import com.maggicco.go4lunch.ui.placeholder.PlaceholderContent.PlaceholderItem;
//
//import butterknife.BindView;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
// */
//public class WorkMatesRecyclerViewAdapter extends RecyclerView.Adapter<WorkMatesRecyclerViewAdapter.ViewHolder> {
//
//    private final List<WorkMate> workMateList;
//
//    Context context;
//    ArrayList<WorkMate> workMateArrayList;
//
//
//    public WorkMatesRecyclerViewAdapter(List<WorkMate> workMateList, Context context) {
//        this.workMateList = workMateList;
//        this.context = context;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(context).inflate(R.layout.fragment_work_mates_item, parent, false);
//        return
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position) {
//
//        workMateList.get(position);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return workMateList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//
//        ImageView workmateImage;
//        TextView workmateName;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            workmateImage = itemView.findViewById(R.id.workmates_image_item);
//            workmateName = itemView.findViewById(R.id.workmates_name_item);
//        }
//
//    }
//}