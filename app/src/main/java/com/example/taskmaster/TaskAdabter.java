package com.example.taskmaster;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdabter extends RecyclerView.Adapter<TaskAdabter.TaskViewHolder>{

    List<Task> allTasks=new ArrayList<Task>();

    public TaskAdabter(List<Task> allTasks) {
        this.allTasks = allTasks;
    }

    public static  class  TaskViewHolder extends RecyclerView.ViewHolder{
        public Task task;

        View itemView;

        public TaskViewHolder(@NonNull  View itemView) {
            super(itemView);
            this.itemView = itemView;

            itemView.findViewById(R.id.fragmentTaskItemID).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goDetail= new Intent(v.getContext(),TaskDetail.class);
                    goDetail.putExtra("title",task.title);
                    goDetail.putExtra("body",task.body);
                    goDetail.putExtra("state",task.state);

                    v.getContext().startActivity(goDetail);

                }
            });


        }
    }// End class Task ViewHolder

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_item,parent,false);
        TaskViewHolder taskViewHolder=new TaskViewHolder(view);

        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  TaskAdabter.TaskViewHolder holder, int position) {

        holder.task=allTasks.get(position);
        TextView title = holder.itemView.findViewById(R.id.titleFragment);
        TextView body = holder.itemView.findViewById(R.id.bodyFragment);
        TextView state = holder.itemView.findViewById(R.id.stateFragment);

        title.setText(holder.task.title);
        body.setText(holder.task.body);
        state.setText(holder.task.state);

    }

    @Override
    public int getItemCount() {
        return allTasks.size();
    }


}
