package com.bmi.cloudshelf_ebook_management;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EBookAdapter extends RecyclerView.Adapter<EBookAdapter.EbookViewHolder> {
    private List<EBook> list;

    public EBookAdapter(List<EBook> list) { this.list = list; }

    @NonNull
    @Override
    public EbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ebook, parent, false);
        return new EbookViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull EbookViewHolder holder, int position) {
        EBook ebook = list.get(position);
        holder.title.setText(ebook.getTitle());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(ebook.getUrl()), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            v.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class EbookViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public EbookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ebookTitle);
        }
    }
}
