package com.treehouse.android.rxjavaworkshop;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import rx.Subscription;
import rx.functions.Action;
import rx.functions.Action1;
import rx.functions.Func1;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> implements Action1<List<Todo>> {

    LayoutInflater inflater;

    Action1<Todo> subscriber;

    List<Todo> data = Collections.EMPTY_LIST;

    private MainActivity mactivity;

    public TodoAdapter(Activity activity, Action1<Todo> subscriber) {
        inflater = LayoutInflater.from(activity);
        this.subscriber = subscriber;
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TodoHolder(inflater.inflate(R.layout.item_todo, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(final TodoHolder holder, final int position) {
        final Todo todo = data.get(position);
        holder.checkbox.setText(todo.description);

        holder.checkbox.setChecked(todo.isCompleted);
        holder.subscription = RxCompoundButton.checkedChanges(holder.checkbox)
                .skip(1)
                .map(new Func1<Boolean, Todo>() {
                    @Override
                    public Todo call(Boolean aBoolean) {
                        return todo;
                    }
                })
                .subscribe(subscriber);

    }


    @Override
    public void call(List<Todo> todoList) {
        data = todoList;
        notifyDataSetChanged();
    }

    @Override
    public void onViewDetachedFromWindow(TodoHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.subscription.unsubscribe();
    }

    public class TodoHolder extends RecyclerView.ViewHolder {

        public CheckBox checkbox;
        public Subscription subscription;

        public TodoHolder(View itemView) {
            super(itemView);
            checkbox = (CheckBox) itemView;
        }

    }
}
