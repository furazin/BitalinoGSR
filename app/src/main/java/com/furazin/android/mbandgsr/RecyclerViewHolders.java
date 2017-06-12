package com.furazin.android.mbandgsr;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewHolders extends RecyclerView.ViewHolder{
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();
//    public ImageView markIcon;
    public TextView Name;
//    public ImageView deleteIcon;
    private List<String> experienciasObject;
    public RecyclerViewHolders(final View itemView, final List<String> experienciasObject) {
        super(itemView);
        this.experienciasObject = experienciasObject;
        Name = (TextView)itemView.findViewById(R.id.task_title);
//        markIcon = (ImageView)itemView.findViewById(R.id.task_icon);
//        deleteIcon = (ImageView)itemView.findViewById(R.id.task_delete);
//        deleteIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Delete icon has been clicked", Toast.LENGTH_LONG).show();
//                String taskTitle = experienciasObject.get(getAdapterPosition()).getNombre();
//                Log.d(TAG, "Task Title " + taskTitle);
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//                Query applesQuery = ref.orderByChild("task").equalTo(taskTitle);
//                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
//                            appleSnapshot.getRef().removeValue();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.e(TAG, "onCancelled", databaseError.toException());
//                    }
//                });
//            }
//        });
    }
}