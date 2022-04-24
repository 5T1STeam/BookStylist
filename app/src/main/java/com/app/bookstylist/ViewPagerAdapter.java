package com.app.bookstylist;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    int shopId;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, int shopId) { super(fragmentActivity); this.shopId = shopId;}
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new ServiceFragment(shopId);
            case 1: return new ReviewFragment(shopId);
            case 2: return new PictureFragment(shopId);
            case 3: return new DetailFragment(shopId);
            default: return new ServiceFragment(shopId);
        }
    }
    @Override
    public int getItemCount() {
        return 4;
    }
}
