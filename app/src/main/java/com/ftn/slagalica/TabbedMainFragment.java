package com.ftn.slagalica;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabbedMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabbedMainFragment extends Fragment {

    private DesignDemoPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabbedMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabbedMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabbedMainFragment newInstance(String param1, String param2) {
        TabbedMainFragment fragment = new TabbedMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

//    *TODO might prove helpful with bug/issue described below in onViewCreated() comment
//    @Override
//    public void onDestroyView() {
//        adapter = null;
//        viewPager = null;
//        tabLayout = null;
//        super.onDestroyView();
//    }
//
//    @Override
//    public void onDestroy() {
//        adapter = null;
//        viewPager = null;
//        tabLayout = null;
//        super.onDestroy();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tabbed_main, container, false);
    }
//    @Override
//    public void onAttachFragment(@NonNull Fragment childFragment) {
//        adapter = new DesignDemoPagerAdapter(getChildFragmentManager());
//        viewPager = getActivity().findViewById(R.id.viewpager);
//        viewPager.setAdapter(adapter);
//        tabLayout = getActivity().findViewById(R.id.tablayout);
//
//        tabLayout.setupWithViewPager(viewPager);
//
//        super.onAttachFragment(childFragment);
//    }


//    @Override
//    public void onDetach() {
//        viewPager.setAdapter(null);
//        super.onDetach();
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        *TODO might need an onDestroy() to erase adapter, viewpager & tablayout, upon replacing this fragment with Profile (and then building this fragment again by replacing back to this view)

        adapter = new DesignDemoPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager = getActivity().findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        tabLayout = getActivity().findViewById(R.id.tablayout);

        tabLayout.setupWithViewPager(viewPager);

//        *TODO: Uncomment and run it !   View initial creation & frag inflation "difference" Needs BUGFIX !
        //      * Selects default "GAMES" Tab upon activity creation
        tabLayout.getTabAt(1).select();
        //      * Middle Tab "GAMES" is main (wider bottom scroll indicator than other tabs)
        LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(1));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 1.5f;
        layout.setLayoutParams(layoutParams);
    }

    public static class DesignDemoFragment extends Fragment {
        private static final String TAB_POSITION = "tab_position";

        public DesignDemoFragment() {

        }

        public static DesignDemoFragment newInstance(int tabPosition) {
            DesignDemoFragment fragment = new DesignDemoFragment();
            Bundle args = new Bundle();
            args.putInt(TAB_POSITION, tabPosition);
            fragment.setArguments(args);
            return fragment;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            int tabPosition = args.getInt(TAB_POSITION);
            View v = null;
//            ArrayList<String> items = new ArrayList<String>();
            Toast.makeText(getActivity(), container.getClass().toString(), Toast.LENGTH_SHORT).show();

//            *TODO: ubacivanje fragmenata u Tabove
//             TODO to ce resiti vizuelni Bug sa scroll-indicator pri horizontalnom skrolovanju prstom
            if (tabPosition==0) {
//                v = inflater.inflate(R.layout.fragment_rankings, container, false);
            }
            else if (tabPosition==1) {
                v = inflater.inflate(R.layout.fragment_match_finding, container, false);
            }
            else if (tabPosition==2) {
//                v = inflater.inflate(R.layout.fragment_search_people, container, false);
            }

            return v;
        }
    }

    static class DesignDemoPagerAdapter extends FragmentStatePagerAdapter {

        public DesignDemoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DesignDemoFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String retval ="n/a";
            if(position==0) retval = "Rang";
            else if (position==1) retval = "Igre";
            else if (position==2) retval = "Igraci";

            return retval;
        }
    }
}