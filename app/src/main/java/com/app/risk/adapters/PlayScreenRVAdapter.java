package com.app.risk.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.Interfaces.PhaseManager;
import com.app.risk.R;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.view.AttackPhaseDialogManager;

import java.util.ArrayList;

/**
 * PlayScreenRVAdapter Adapter which iterates the recyclerview multiple times
 * @author Himanshu Kohli
 * @version 1.0.0
 */
public class PlayScreenRVAdapter extends RecyclerView.Adapter<PlayScreenRVAdapter.PlayScreenViewHolder> {


    private GamePlay gamePlay;
    private ArrayList<Country> countries;
    private Context context;
    private ArrayList<String> neighbouringCountries;
    private PhaseManager phaseManager;
    private boolean flagTransfer = true;
    private final RecyclerView recyclerView;


    /**
     * This is the paramerized constructor
     * @param context It is used to call any activity methods using reference
     * @param gamePlay The GamePlay object
     * @param recyclerView
     */
    public PlayScreenRVAdapter(Context context, GamePlay gamePlay, RecyclerView recyclerView) {
        this.context = context;
        this.gamePlay = gamePlay;
        this.countries = gamePlay.getCountryListByPlayerId(gamePlay.getCurrentPlayer().getId());
        this.neighbouringCountries = new ArrayList<>();
        this.recyclerView = recyclerView;
    }

    /**
     * This method inflates the view to be represented in recyclerview
     *
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public PlayScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_screen_card,parent,false);
        final PlayScreenViewHolder mainAdapter = new PlayScreenViewHolder(view);
        return mainAdapter;
    }

    /**
     * This method binds the data with the particular layout and
     * displays on the recyclerview
     *
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(@NonNull PlayScreenViewHolder holder, int position) {
        holder.countryName.setText(countries.get(position).getNameOfCountry());
        holder.countryName.setTextColor(gamePlay.getCurrentPlayer().getColorCode());
        holder.continentName.setText(countries.get(position).getBelongsToContinent().getNameOfContinent());
        holder.armies.setText(""+countries.get(position).getNoOfArmies());

        neighbouringCountries = gamePlay.getCountries().get(countries.get(position).getNameOfCountry()).getAdjacentCountries();
        CustomArrayAdapter adapter = new CustomArrayAdapter(context, R.layout.adapters_textview,R.id.adapter_textview_text, neighbouringCountries,gamePlay);
        holder.adjacentCountries.setAdapter(adapter);

        ViewGroup.LayoutParams layoutParams = holder.adjacentCountries.getLayoutParams();
        layoutParams.height = neighbouringCountries.size() * 158;
        holder.adjacentCountries.setLayoutParams(layoutParams);
        holder.adjacentCountries.requestLayout();

    }

    /**
     * This method returns the number of items in recyclerview adapter
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return countries.size();
    }

    /**
     * Inner class of recyclerview adapter which gets the reference to
     * view holder and add functionality to the views
     */
    public class PlayScreenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterView.OnItemClickListener {

        private CardView cardView;
        private TextView countryName,armies, continentName;
        private ListView adjacentCountries;
        private View view;

        /**
         * Default constructor of the class
         * @param itemView
         *
         * {@inheritDoc}
         */
        public PlayScreenViewHolder(final View itemView) {
            super(itemView);
            view = itemView;

            cardView = itemView.findViewById(R.id.play_screen_card_carview);
            countryName = itemView.findViewById(R.id.play_screen_card_country_name);
            armies = itemView.findViewById(R.id.play_screen_card_armies);
            continentName = itemView.findViewById(R.id.play_screen_card_continent);
            adjacentCountries = itemView.findViewById(R.id.play_screen_card_neighbours);

            adjacentCountries.setOnItemClickListener(this);
            cardView.setOnClickListener(this);
        }

        /**
         * This method holds the click evenets of every view
         * {@inheritDoc}
         *
         */
        @Override
        public void onClick(View v) {
            if(v == cardView){
                switch (gamePlay.getCurrentPhase()) {
                    case GamePlayConstants.REINFORCEMENT_PHASE:
                        showReinforcementDialogBox(getAdapterPosition());
                        break;

                    case GamePlayConstants.ATTACK_PHASE:
                        Toast.makeText(context, "Select Country from List", Toast.LENGTH_SHORT).show();
                        break;

                    case GamePlayConstants.FORTIFICATION_PHASE:

                        if(countries.get(getAdapterPosition()).getNoOfArmies() > 1){
                            final ArrayList<String> reachableCountries  = FortificationPhaseController.getInstance().init(context, gamePlay).getReachableCountries(countries.get(getAdapterPosition()), countries);
                            final String[] reachableCountryArray = new String[reachableCountries.size()];
                            reachableCountries.toArray(reachableCountryArray);

                            new AlertDialog.Builder(context)
                                    .setTitle("Move Armies")
                                    .setItems((CharSequence[]) reachableCountryArray, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int position) {
                                            moveArmies(reachableCountryArray[position],getAdapterPosition());
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                        else{
                            Toast.makeText(context, "Country must have armies greater than one", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(gamePlay.getCurrentPhase().equals(GamePlayConstants.ATTACK_PHASE)){
                    if(!gamePlay.getCountries().get(adjacentCountries.getAdapter().getItem(position)).getPlayer().equals(gamePlay.getCurrentPlayer())){

                        if(countries.get(getAdapterPosition()).getNoOfArmies() > 1){

                            AttackPhaseDialogManager attackPhaseDialogManager = new AttackPhaseDialogManager(context,gamePlay,countries.get(getAdapterPosition()),
                                    gamePlay.getCountries().get(adjacentCountries.getAdapter().getItem(position)),recyclerView,countries);
                            attackPhaseDialogManager.initiateAttack();
                        }
                        else{

                        }
                    }
                    else{
                        Toast.makeText(context, "Cannot Attack your own country", Toast.LENGTH_SHORT).show();
                    }
            }

        }
    }

    /**
     * This method shows the dailog box to place the reinforcement armies.
     * @param adapterPostion The position of country owned by the player.
     */
    public void showReinforcementDialogBox(final int adapterPostion){
        final AlertDialog.Builder reinforcementDialogBox = new AlertDialog.Builder(context);
        reinforcementDialogBox.setTitle("Place Armies");

        final View view = View.inflate(context,R.layout.play_screen_reinforcement_option,null);
        reinforcementDialogBox.setView(view);

        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(gamePlay.getCurrentPlayer().getReinforcementArmies());
        numberPicker.setWrapSelectorWheel(false);

        reinforcementDialogBox.setPositiveButton("Place", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gamePlay.getCurrentPlayer().decrementReinforcementArmies(numberPicker.getValue());
                countries.get(adapterPostion).incrementArmies(numberPicker.getValue());
                notifyDataSetChanged();
                if(gamePlay.getCurrentPlayer().getReinforcementArmies() == 0){
                    phaseManager.changePhase(GamePlayConstants.ATTACK_PHASE);
                }
            }
        });
        reinforcementDialogBox.setNegativeButton("Cancel",null);
        reinforcementDialogBox.create();
        reinforcementDialogBox.show();
    }

    /**
     * This method shows the selection box to move the armies.
     * @param countryNameDestination The position of country owned by the player.
     * @param adapterPostion The position of the invoking recyclerview elements
     */
    public void moveArmies(String countryNameDestination,final int adapterPostion){

            final AlertDialog.Builder reinforcementDialogBox = new AlertDialog.Builder(context);
            reinforcementDialogBox.setTitle("Place Armies");
            countryNameDestination = countryNameDestination.split(":")[0].trim();
            final View view = View.inflate(context,R.layout.play_screen_reinforcement_option,null);
            reinforcementDialogBox.setView(view);

            final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(countries.get(adapterPostion).getNoOfArmies() - 1);
            numberPicker.setWrapSelectorWheel(false);

            final String finalCountryNameDestination = countryNameDestination;
            reinforcementDialogBox.setPositiveButton("Move", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FortificationPhaseController.getInstance().fortifyCountry(countries.get(adapterPostion), gamePlay.getCountries().get(finalCountryNameDestination), numberPicker.getValue());
                    notifyDataSetChanged();
                    Toast.makeText(context, "Armies moved from " + countries.get(adapterPostion).getNameOfCountry() + " to "
                            + finalCountryNameDestination, Toast.LENGTH_SHORT).show();
                    phaseManager.changePhase(GamePlayConstants.REINFORCEMENT_PHASE);
                }
            });
            reinforcementDialogBox.setNegativeButton("cancel",null);
            reinforcementDialogBox.create();
            reinforcementDialogBox.show();

    }


    /**
     * This method returns the current phase of the game
     */
    public PhaseManager getPhaseManager() {
        return phaseManager;
    }


    /**
     * This method sets the current phase of the game
     */
    public void setPhaseManager(PhaseManager phaseManager) {
        this.phaseManager = phaseManager;
    }


}
