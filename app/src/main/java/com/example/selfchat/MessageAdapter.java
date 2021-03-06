package com.example.selfchat;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder> {
    private MessageAdapter.OnItemClickListener mListener;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options: holds the query to retrieve data from fire-store
     */
    public MessageAdapter(FirestoreRecyclerOptions<Message> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageHolder messageHolder, int position, @NonNull Message message) {
        messageHolder.message.setText(message.getMessage());

    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_messageitem,
                parent, false);
        return new MessageHolder(view);

    }

    @Override
    public void onDataChanged() {
        notifyDataSetChanged();
        super.onDataChanged();
    }


    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot ds, int position);
    }

    public void setOnItemClickListener(MessageAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    class MessageHolder extends RecyclerView.ViewHolder{
        // here we add items that appear in each view holder
        TextView message;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setTag(this);
            message = itemView.findViewById(R.id.chatResult);
            message.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && mListener != null){
                            mListener.onItemClick(getSnapshots().getSnapshot(position), position);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
