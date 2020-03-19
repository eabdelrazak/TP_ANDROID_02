package com.example.myapplication1;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class Ecouteur implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Chat chat;
    private Preferences preferences;
    private Socket socket;
    //private String ip = "134.59.2.27";
    private String ip = "192.168.1.65";
    private int port = 10101;


    public Ecouteur(final Chat chat, Preferences preferences){
        this.chat = chat;
        this.preferences = preferences;

        try {
            socket = IO.socket("http://"+ip+":"+port);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on("chatevent", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("on arrive ici");
                try {
                    System.out.println("JSON");
                    JSONObject messageSent = new JSONObject(args[0].toString());
                    String pseudo = messageSent.getString("userName");
                    String message = messageSent.getString("message");
                    System.out.println(pseudo+" "+message);
                    chat.addMessage(pseudo+" > "+message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        socket.on("connected list", new Emitter.Listener() {

            public void call(Object... args) {
                try {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray connected = data.getJSONArray("connected");
                    for (int i = 0; i < connected.length(); i++) {
                        chat.addMessageC(connected.get(i).toString()+" is connected !!!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void connect(){
        socket.connect();
    }

    public void disconnect(){
        socket.disconnect();
    }


    @Override
    public void onClick(View v) {
        //chat.addMessage(chat.getWrittenText());
        String message = chat.getWrittenText();
        String surname = this.preferences.getSurname();
        try {
            JSONObject obj = new JSONObject(" { userName : "+surname+"; message : "+message+"}");
            socket.emit("chatevent",obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void demandeListesConnectes() {
        socket.emit("queryconnected", new JSONObject());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            System.out.println("On se connecte !!!!!!");
            connect();
        }else{
            System.out.println(" On se deconnecte !!!!!!");
            disconnect();
        }
    }
}
