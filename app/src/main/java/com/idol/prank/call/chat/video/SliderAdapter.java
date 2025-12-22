package com.idol.prank.call.chat.video;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.activities.Home;
import com.idol.prank.call.chat.video.utils.Constant;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private static List<SliderItem> sliderItems;
    String  str;
    private ViewPager2 viewPager2;

    public SliderAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }
    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));
     /*   Resources abc =holder.itemView.getResources();*/
        int abc =position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (abc==0){
                    str = "selected";
                    Constant.character_no = 1;
                    Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(),  R.drawable.new_1);
                    Log.d("cauliflower", " Image Selected = " + Constant.character_no);
                    Constant.CHAR_BITMAP = bitmap;
                    Intent intent = new Intent(holder.itemView.getContext(), Home.class);
                    intent.putExtra("str", "selected");
                    // Start the activity
                    holder.itemView.getContext().startActivity(intent);

                } else if (abc == 1) {
                    str = "selected";
                    Constant.character_no = 2;
                    Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(),  R.drawable.new_2);
                    Log.d("cauliflower", " Image Selected = " + Constant.character_no);
                    Constant.CHAR_BITMAP = bitmap;
                    Intent intent = new Intent(holder.itemView.getContext(), Home.class);
                    intent.putExtra("str", "selected");
                    // Start the activity
                    holder.itemView.getContext().startActivity(intent);

                } else if (abc == 2) {
                    str = "selected";
                    Constant.character_no = 3;
                    Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(),  R.drawable.new_3);
                    Log.d("cauliflower", " Image Selected = " + Constant.character_no);
                    Constant.CHAR_BITMAP = bitmap;
                    Intent intent = new Intent(holder.itemView.getContext(), Home.class);
                    intent.putExtra("str", "selected");
                    // Start the activity
                    holder.itemView.getContext().startActivity(intent);

                } else if (abc==3) { str = "selected";
                    Constant.character_no = 4;
                    Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(),  R.drawable.new_4);
                    Log.d("cauliflower", " Image Selected = " + Constant.character_no);
                    Constant.CHAR_BITMAP = bitmap;
                    Intent intent = new Intent(holder.itemView.getContext(), Home.class);
                    intent.putExtra("str", "selected");
                    // Start the activity
                    holder.itemView.getContext().startActivity(intent);

                } else if (abc==4) { str = "selected";
                    Constant.character_no = 5;
                    Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(),  R.drawable.new_5);
                    Log.d("cauliflower", " Image Selected = " + Constant.character_no);
                    Constant.CHAR_BITMAP = bitmap;
                    Intent intent = new Intent(holder.itemView.getContext(), Home.class);
                    intent.putExtra("str", "selected");
                    // Start the activity
                    holder.itemView.getContext().startActivity(intent);

                }else { str = "selected";
                    Constant.character_no = 6;
                    Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(),  R.drawable.new_6);
                    Log.d("cauliflower", " Image Selected = " + Constant.character_no);
                    Constant.CHAR_BITMAP = bitmap;
                    Intent intent = new Intent(holder.itemView.getContext(), Home.class);
                    intent.putExtra("str", "selected");
                    // Start the activity
                    holder.itemView.getContext().startActivity(intent);

                }

            }
        });

        /*holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str = "selected";
                Constant.character_no = 1;
                Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.new_1);
                Log.d("cauliflower", " Image Selected = " + Constant.character_no);
                Constant.CHAR_BITMAP = bitmap;
            }
        });*/


    }

@Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public SliderViewHolder(@NonNull View itemview){
            super(itemview);
            imageView=itemview.findViewById(R.id.image_slide);

        }
        void setImage(SliderItem sliderItem){
            imageView.setImageResource(sliderItem.getImage());
        }
    }


}
