package com.app.bookstylist;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.bookstylist.book.BookServiceFrag;
import com.app.bookstylist.book.BookTimeFrag;
import com.app.bookstylist.book.BookingFrag;
import com.google.android.material.tabs.TabLayout;

public class BookActivity extends AppCompatActivity {

    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Button nextFrag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        tabLayout = (TabLayout) findViewById(R.id.book_nav);
        Toolbar toolbar =findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_x);
        frameLayout = (FrameLayout) findViewById(R.id.frame_book);
        fragment = new BookServiceFrag();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_book,fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        // Disable nav
//        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
//        for(int i = 0; i < tabStrip.getChildCount(); i++) {
//            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            });
//        }
        // Nav Choose
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Fragment fragment = null;
                switch (tab.getPosition()){
                    case 0:
                        fragment = new BookServiceFrag();
                        break;
                    case 1:
                        fragment = new BookTimeFrag();
                        break;
                    case 2:
                        fragment = new BookingFrag();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_book,fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }

}

