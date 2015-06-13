package com.mccorby.wordcounter.app.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mccorby.wordcounter.R;
import com.mccorby.wordcounter.app.views.main.MainPresenter;
import com.mccorby.wordcounter.domain.entities.WordOccurrence;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JAC on 11/06/2015.
 */
public class WordOccurrenceListAdapter extends RecyclerView.Adapter<WordOccurrenceListAdapter.ViewHolder> {

    private static final String TAG = WordOccurrenceListAdapter.class.getSimpleName();

    private MainPresenter mPresenter;

    public WordOccurrenceListAdapter(MainPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_word_occurrence_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WordOccurrence wo = mPresenter.getWordOccurrence(position);
        holder.occurrencesTv.setText(" " + wo.getOccurrences());
        holder.wordTv.setText(wo.getWord());
    }

    @Override
    public int getItemCount() {
        return mPresenter.getWordListSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.item_word_occurrence_list_word_tv)
        TextView wordTv;
        @InjectView(R.id.item_word_occurrence_list_occurrences_tv)
        TextView occurrencesTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
