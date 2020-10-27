package com.example.group_w01_07_3.features.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_w01_07_3.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OpenedCapsuleAdapter extends RecyclerView.Adapter<OpenedCapsuleAdapter.capsuleCardViewHolder> {

    Context mcontext;
    List<OpenedCapsule> mData;

    CapsuleCallback callback;

    public OpenedCapsuleAdapter(Context mcontext, List<OpenedCapsule> mData, CapsuleCallback callback) {
        this.mcontext = mcontext;
        this.mData = mData;
        this.callback = callback;
    }

    @NonNull
    @Override
    public capsuleCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewgroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View layout = inflater.inflate(R.layout.capsule_material_card, viewgroup, false);

        return new capsuleCardViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull capsuleCardViewHolder holder, int position) {

        // animation for capsule image
        holder.capsule_image.setAnimation(AnimationUtils.loadAnimation(mcontext,R.anim.fade_transition_animation));

        // animation for the whole card
        holder.megaCardLayout.setAnimation(AnimationUtils.loadAnimation(mcontext,R.anim.fade_scale_animation));

        //all elements defined here will be updated based on the data provided

        holder.capsule_title.setText(mData.get(position).getCapsule_title());
        holder.opened_date.setText(mData.get(position).getOpened_date());
        holder.private_tag.setText(mData.get(position).getTag());
        holder.capsule_content.setText(mData.get(position).getContent());
        holder.username.setText(mData.get(position).getUsername());

        //TODO: @CHENFU, 这是一个从URL读取图片的例子
        //resize with center crop to make sure not a stretch image display in the material card preview
        Picasso.with(mcontext).load(mData.get(position).getCapsule_url()).fit().centerCrop().into(holder.capsule_image);
        Picasso.with(mcontext).load(mData.get(position).getAvatar_url()).fit().into(holder.original_user_avatar);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class capsuleCardViewHolder extends RecyclerView.ViewHolder {
        ImageView capsule_image, original_user_avatar;
        TextView capsule_title, opened_date, private_tag, capsule_content, static_text_by, username;
        ConstraintLayout megaCardLayout;


        public capsuleCardViewHolder(View itemView){
            super(itemView);
            megaCardLayout = itemView.findViewById(R.id.history_capsule_card_layout);
            capsule_title = itemView.findViewById(R.id.history_opened_capsule_title);
            opened_date = itemView.findViewById(R.id.history_opened_capsule_openDate);
            private_tag = itemView.findViewById(R.id.history_opened_capsule_tag);
            capsule_content = itemView.findViewById(R.id.history_opened_capsule_content);
            username = itemView.findViewById(R.id.history_capsule_original_username);
            //This is the [static text] "By", so [no need] to retrieve any information from the capsule object
            static_text_by = itemView.findViewById(R.id.history_capsule_text_by);

            //Image section
            capsule_image = itemView.findViewById(R.id.history_capsule_card_background);
            original_user_avatar = itemView.findViewById(R.id.history_capsule_original_user_avatar);

            //Voice section


            //根据callback,这里设置的view支持transition 【要真正进行transition这个动画，得在主activity的onCapsuleItemClick里吧pair传进去
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onCapsuleItemClick(getAdapterPosition(), capsule_title, opened_date, capsule_image, private_tag, capsule_content, original_user_avatar,static_text_by, username);
                }
            });
        }
    }
}
