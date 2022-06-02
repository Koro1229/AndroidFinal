package com.example.a108820027_final;

import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int N = 4;
    private GridView mGridView;
    private CardAdapter mCardAdapter;
    private ArrayList<Card> mCardData;
    private GameReceiver mReceiver;
    private static final String GAME_FINISH_BROADCAST = BuildConfig.APPLICATION_ID + ".GAME_FINISH_CALL";


    private Card tempCard = null;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GameFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFrag newInstance(String param1, String param2) {
        GameFrag fragment = new GameFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mReceiver = new GameReceiver();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mReceiver.setFragmentTransaction(ft);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter(GAME_FINISH_BROADCAST));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);

        mGridView = getView().findViewById(R.id.gameContent);

        Button homeButton = getView().findViewById(R.id.homeButton);
        Button revealButton = getView().findViewById(R.id.revealButton);

        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_container, MainFrag.class, null);
                ft.commit();
            }
        });

        revealButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_container, ResultFrag.class, null);
                ft.commit();
            }
        });
        createGridCardView();
    }


    private void createGridCardView() {

        mCardData = new ArrayList<>();
        mCardAdapter = new CardAdapter(getContext(), mCardData);
        mGridView.setAdapter(mCardAdapter);


        TypedArray imageResource = getResources().obtainTypedArray(R.array.cards_images);

        mCardData.clear();
        for(int id = 0; id < imageResource.length(); id++){
            mCardData.add(new Card(id, imageResource.getResourceId(id, 0)));
            mCardData.add(new Card(id, imageResource.getResourceId(id, 0)));
        }
        Collections.shuffle(mCardData);

        mCardAdapter.notifyDataSetChanged();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String message = "U are clicking " + Integer.toString(i / 4) + "row and " + Integer.toString(i % 4) + "column";
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}