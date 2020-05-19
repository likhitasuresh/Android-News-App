package com.example.my_hw_9.ui.headlines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.my_hw_9.HeadlinesAdapter;
import com.example.my_hw_9.R;
import com.google.android.material.tabs.TabLayout;

public class HeadlinesFragment extends Fragment {

    private HeadlinesViewModel headlinesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        headlinesViewModel =
                ViewModelProviders.of(this).get(HeadlinesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_headlines, container, false);

        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        HeadlinesAdapter adapter = new HeadlinesAdapter(getActivity(), getChildFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.slidingTabs);
        tabLayout.setupWithViewPager(viewPager);

        headlinesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
}
