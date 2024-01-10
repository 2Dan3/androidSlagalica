package com.ftn.slagalica;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ftn.slagalica.data.model.AuthBearer;
import com.ftn.slagalica.util.AuthHandler;
import com.google.android.material.tabs.TabLayout;

public class TabbedMainFragment extends Fragment {

    private TabLayout tabLayout;
    private static final int[] icons = {R.drawable.trophy, R.drawable.play, R.drawable.players};
    private static final String[] titles = {"Plasmani", "Igraj", "Prijatelji"};

    private AuthBearer loggedUser;

    public TabbedMainFragment() { }

    // TODO: Rename and change types and number of parameters
    public static TabbedMainFragment newInstance(String param1, String param2) {
        TabbedMainFragment fragment = new TabbedMainFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loggedUser = AuthHandler.Login.getLoggedPlayerCache(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabbed_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabPagerAdapter adapter = new TabPagerAdapter(TabbedMainFragment.this);
        ViewPager viewPager = getActivity().findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        tabLayout = getActivity().findViewById(R.id.tablayout);

        tabLayout.setupWithViewPager(viewPager);

        //        Tab Icons setup
        if (loggedUser == null) {
            tabLayout.getTabAt(0).setIcon(icons[1]);
            setWiderMainTabIndicator(0);
        }
        else {
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                tabLayout.getTabAt(i).setIcon(icons[i]);
            }
//      * Selects default "PLAY" Tab upon activity creation
            tabLayout.getTabAt(1).select();
            setWiderMainTabIndicator(1);
        }
    }

    private void setWiderMainTabIndicator(int tabPosition){
//              * Middle Tab "PLAY" is main (wider bottom scroll indicator than other tabs)
        LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tabPosition));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 1.23f;
        layout.setLayoutParams(layoutParams);
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter {
        private TabbedMainFragment fragment;

        public TabPagerAdapter(TabbedMainFragment frag) {
            super(frag.getActivity().getSupportFragmentManager());
            fragment = frag;
        }

        @Override
        public Fragment getItem(int position) {

            position = fragment.loggedUser == null ? 1 : position;

            switch (position){
                case 0:
                    return new RankingsFragment();
                case 1:
                    return new MatchFindingFragment();
                case 2:
                    return new PlayersFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragment.loggedUser == null ? 1 : 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragment.loggedUser == null ? titles[1] : titles[position];
        }
    }

    public void showFriendsFragment() {
        TabLayout.Tab tab;
        if ( (tab = tabLayout.getTabAt(2) ) != null )
            tab.select();
    }

}