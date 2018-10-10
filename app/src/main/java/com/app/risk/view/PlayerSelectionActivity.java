package com.app.risk.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.R;

import java.util.ArrayList;

public class PlayerSelectionActivity extends AppCompatActivity {


    private String mapInfo = "";
    private ListView listView;
    private TextView playerDisplay;
    private SeekBar seekBar;
    private FloatingActionButton nextButton;
    private ArrayList<String> userNames;
    private String playerName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);


        setUpData();
        setUpNextButton();
        getMapInfo();
    }

    public void getMapInfo() {
        Intent intent = getIntent();
        mapInfo = intent.getStringExtra("MAP_INFO");
        Toast.makeText(this, ""+mapInfo, Toast.LENGTH_SHORT).show();

    }


    public void setUpData()
    {
        listView = findViewById(R.id.player_selection_listview);
        seekBar = findViewById(R.id.player_selection_seekbar);
        playerDisplay = findViewById(R.id.userselection_textview);

        userNames = new ArrayList<>();

        userNames.add("Player 1");
        userNames.add("Player 2");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,userNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final View inflaterView = getLayoutInflater().inflate(R.layout.player_selection_option,null);
                new AlertDialog.Builder(PlayerSelectionActivity.this)
                        .setView(inflaterView)
                        .setTitle("Player Name")
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        EditText editText = inflaterView.findViewById(R.id.player_selection_option_edittext);
                                        playerName = editText.getText().toString().trim();
                                        if(!playerName.equals("")){
                                            userNames.set(position,playerName);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                })
                        .create().show();

                    EditText editText = inflaterView.findViewById(R.id.player_selection_option_edittext);
                    editText.setText(userNames.get(position));
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                playerDisplay.setText("" + progress);
                int display = Integer.parseInt(playerDisplay.getText().toString().trim());
                if(display > userNames.size()) {
                    for(int i=userNames.size();i<display;i++) {
                        userNames.add("Player " + (i + 1));
                    }
                    adapter.notifyDataSetChanged();
                }
                else if(display < userNames.size()){
                    for(int i=userNames.size();i>display;i--){
                        userNames.remove(userNames.size()-1);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setUpNextButton()
    {
        nextButton = findViewById(R.id.player_selection_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(PlayerSelectionActivity.this)
                        .setTitle("Alert")
                        .setMessage("Are You sure?")
                        .setNegativeButton("No",null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PlayerSelectionActivity.this,PlayScreenActivity.class);
                                intent.putStringArrayListExtra("PLAYER_INFO",userNames);
                                intent.putExtra("MAP_INFO",mapInfo);
                                startActivity(intent);
                            }
                        }).create().show();
            }
        });
    }
}
