package com.example.myapplication1;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;


public class ChatActivity extends Activity implements Chat{

    EditText text;
    TextView zoneText;
    Button envoyer;
    ScrollView scroll;
    String surname;
    Preferences preferences;
    Switch aSwitch;
    Ecouteur ecouteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_layout);

        text = findViewById(R.id.inputText);
        zoneText = findViewById(R.id.chat);
        envoyer = findViewById(R.id.envoyer);
        aSwitch = findViewById(R.id.connexion);
        scroll = findViewById(R.id.scroll);
        surname = "Razak > ";

        preferences = new Preferences("Razak");
        ecouteur = new Ecouteur(this, preferences);

        envoyer.setOnClickListener(ecouteur);
        aSwitch.setOnCheckedChangeListener(ecouteur);
    }

    @Override
    public String getWrittenText() {
        return text.getText().toString();
    }

    @Override
    public void addMessage(final String msgP) {
        //String msg = msgP;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                zoneText.append(msgP+"\n");
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void addMessageC(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SpannableString formate = new SpannableString(msg);
                formate.setSpan(new ForegroundColorSpan(Color.RED), 0, msg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                zoneText.append(formate+"\n");
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.listesconnectes) {
            ecouteur.demandeListesConnectes();
        }
        return true;
    }
}
