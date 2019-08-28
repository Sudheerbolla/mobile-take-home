package com.guestlogix.views.fragments;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.R;
import com.guestlogix.views.activities.MainActivity;
import com.guestlogix.views.adapters.CharactersAdapter;
import com.guestlogix.controllers.CharactersController;
import com.guestlogix.listeners.IClickListener;
import com.guestlogix.listeners.IParserListener;
import com.guestlogix.models.CharacterModel;
import com.guestlogix.utils.Constants;
import com.guestlogix.utils.DialogUtils;
import com.guestlogix.utils.StaticUtils;

import org.json.JSONArray;

import java.util.ArrayList;

public class CharactersFragment extends BaseFragment implements IParserListener, IClickListener, View.OnClickListener {

    private View rootView;
    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private RelativeLayout relBody;
    private ProgressBar progressBar;
    private AppCompatTextView txtNoData, txtAll, txtAlive, txtDead;
    private ArrayList<CharacterModel> characterModelArrayList, adapterArrayList;

    private CharactersAdapter charactersAdapter;
    private String episodeName, characterIds;
    private CharactersController charactersController;

    public CharactersFragment() {
    }

    static CharactersFragment newInstance(String episodeName, ArrayList<String> characters) {
        CharactersFragment charactersFragment = new CharactersFragment();
        Bundle bundle = new Bundle();
        bundle.putString("episodeName", episodeName);
        bundle.putStringArrayList("characters", characters);
        charactersFragment.setArguments(bundle);
        return charactersFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainActivity != null)
            mainActivity.showTopBar(TextUtils.isEmpty(episodeName) ? getString(R.string.app_name) : episodeName, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mainActivity != null) mainActivity.showTopBar(getString(R.string.episodes), false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        ArrayList<String> characters;
        try {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                episodeName = bundle.getString("episodeName");
                characters = bundle.getStringArrayList("characters");
                if (characters != null && !characters.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    String temp = "";
                    for (int i = 0; i < characters.size(); i++) {
                        temp = characters.get(i);
                        if (!TextUtils.isEmpty(temp) && temp.contains("/")) {
                            sb.append(temp.substring(temp.lastIndexOf("/") + 1)).append(",");
                        }
                    }
                    characterIds = sb.toString();
                    if (!TextUtils.isEmpty(characterIds) && characterIds.endsWith(",")) {
                        characterIds = characterIds.substring(0, characterIds.length() - 1);
                    }
                } else
                    characterIds = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_characters_list, container, false);
        initComponents();
        return rootView;
    }

    @Override
    protected void initComponents() {
        characterModelArrayList = new ArrayList<>();
        adapterArrayList = new ArrayList<>();
        setReferences();
        setListeners();
        charactersController = new CharactersController(this);
        requestForCharactersAPI();
    }

    private void requestForCharactersAPI() {
        if (StaticUtils.checkInternetConnection(mainActivity)) {
            charactersController.fetchCharactersFromServer(Constants.WS_GET_CHARACTERS + characterIds);
        } else noInternetConnection(Constants.WS_REQUEST_GET_CHARACTERS);
    }

    private void setListeners() {
        txtDead.setOnClickListener(this);
        txtAlive.setOnClickListener(this);
        txtAll.setOnClickListener(this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                CharacterModel characterModel = adapterArrayList.get(viewHolder.getAdapterPosition());
                if (characterModel.status.equalsIgnoreCase(getString(R.string.alive))) {
                    return super.getSwipeDirs(recyclerView, viewHolder);
                } else return 0;
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return super.getMovementFlags(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                final CharacterModel characterModel = adapterArrayList.get(position);
                DialogUtils.showTwoButtonDialog(mainActivity, "Are you sure you want to temporarily kill " + characterModel.name, getString(R.string.yes_kill), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = viewHolder.getAdapterPosition();
                        characterModel.status = "Dead";
                        if (txtAll.isSelected()) {
                            charactersAdapter.notifyItemChanged(position);
                        } else charactersAdapter.notifyItemRemoved(position);
                    }
                }, getString(R.string.no_leave), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        charactersAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setReferences() {
        recyclerView = rootView.findViewById(R.id.recyclerView);
        relBody = rootView.findViewById(R.id.relBody);
        progressBar = rootView.findViewById(R.id.progressBar);
        txtNoData = rootView.findViewById(R.id.txtNoData);

        txtDead = rootView.findViewById(R.id.txtDead);
        txtAlive = rootView.findViewById(R.id.txtAlive);
        txtAll = rootView.findViewById(R.id.txtAll);
    }

    private void setCharactersAdapter(int selectedTab) {
        adapterArrayList.clear();
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        switch (selectedTab) {
            case 1:
                for (CharacterModel characterModel : characterModelArrayList) {
                    if (characterModel.status.equalsIgnoreCase("Alive")) {
                        adapterArrayList.add(characterModel);
                    }
                }
                break;
            case 2:
                for (CharacterModel characterModel : characterModelArrayList) {
                    if (characterModel.status.equalsIgnoreCase("Dead")) {
                        adapterArrayList.add(characterModel);
                    }
                }
                break;
            default:
                adapterArrayList.addAll(characterModelArrayList);
                break;
        }
        charactersAdapter = new CharactersAdapter(mainActivity, adapterArrayList, this);
        recyclerView.setAdapter(charactersAdapter);

        relBody.setVisibility(View.VISIBLE);
        if (adapterArrayList.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);
        }
    }

    private void parseGetCharactersWSResponse(JSONArray resultsArray) {
        try {
            if (resultsArray.length() > 0) {
                for (int i = 0; i < resultsArray.length(); i++) {
                    characterModelArrayList.add(new CharacterModel(resultsArray.getJSONObject(i)));
                }
                txtAll.callOnClick();
            } else {
                progressBar.setVisibility(View.GONE);
                relBody.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successResponse(int requestCode, Object response) {
        try {
            String data = (String) response;
            if (requestCode == Constants.WS_REQUEST_GET_CHARACTERS) {
                parseGetCharactersWSResponse(new JSONArray(data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void errorResponse(int requestCode, final String error) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                relBody.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
                StaticUtils.showToast(mainActivity, TextUtils.isEmpty(error) ? "Something went wrong. Please try again later." : error);
            }
        });
    }

    @Override
    public void noInternetConnection(int requestCode) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                relBody.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
                StaticUtils.showIndefiniteToast(rootView, getString(R.string.no_internet_connection_please_try_later), getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestForCharactersAPI();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view, final int position) {
        CharacterDetailsFragment characterDetailsFragment = CharacterDetailsFragment.newInstance(adapterArrayList.get(position));
        characterDetailsFragment.show(mainActivity.fragmentManager, CharacterDetailsFragment.class.getSimpleName());
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtAll:
                setTabSelected(0);
                break;
            case R.id.txtAlive:
                setTabSelected(1);
                break;
            case R.id.txtDead:
                setTabSelected(2);
                break;
            default:
                break;
        }
    }

    private void setTabSelected(int tabPosition) {
        txtAll.setSelected(false);
        txtAlive.setSelected(false);
        txtDead.setSelected(false);
        switch (tabPosition) {
            case 1:
                txtAlive.setSelected(true);
                break;
            case 2:
                txtDead.setSelected(true);
                break;
            default:
                txtAll.setSelected(true);
                break;
        }
        setCharactersAdapter(tabPosition);
    }

}
