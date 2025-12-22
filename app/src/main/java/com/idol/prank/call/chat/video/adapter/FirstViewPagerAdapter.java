package com.idol.prank.call.chat.video.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.idol.prank.call.chat.video.activities.fragments.FragOne;
import com.idol.prank.call.chat.video.activities.fragments.FragTwo;
import com.idol.prank.call.chat.video.activities.fragments.FragThree;


public class FirstViewPagerAdapter extends FragmentStateAdapter {

    public FirstViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new FragOne();
            case 1: return new FragTwo();
            case 2: return new FragThree();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
