package com.android.OsmNavigator;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WaySelect extends Activity {
	private TextView msgTextView, resultsSearchTag;
	private EditText searchEditText;
	private Button CONFIRM, BACK;
	private Bundle intentBundle;
	private ArrayList<Integer> results;
	private String[] matches;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wayselect);
        
        CONFIRM = (Button)findViewById(R.id.ok);
        BACK = (Button)findViewById(R.id.back);
        
        msgTextView = (TextView)findViewById(R.id.title);
        msgTextView.setText("Cerca nella mappa corrente");
        
        resultsSearchTag = (TextView)findViewById(R.id.resultsSearchTag);
        
        
        searchEditText = (EditText)findViewById(R.id.editSearchtag);
        searchEditText.addTextChangedListener(onChangeWatcher);
        
        CONFIRM.setOnClickListener(confirmListener);
        BACK.setOnClickListener(backListener);
        
        resultsSearchTag.setText("nide c");
        
        intentBundle = getIntent().getExtras();
        resultsSearchTag.setText("Total records: " + intentBundle.getString("hashstring").split("__").length);
        
        simpleAlertDialog = new AlertDialog.Builder(this);
        
        results = new ArrayList<Integer>();
    }
    
    public void search(String pattern){
    	// reset arraylist
    	results.clear();
    	
    	matches = intentBundle.getString("hashstring").split("__");
    	
    	
    	for(int m = 0; m < matches.length; m++){
    		if(matches[m].toLowerCase().contains( pattern.toLowerCase().trim() ) ){
    			results.add(m);	
    		}
    	}
    	String resultsReport = "Currently " + results.size() + " matches";
    	String rrp = "";
    	if(results.size()<16 && results.size()>0){
    	  for(int c = 0; c < results.size(); c++){
    		  // remove tags
    		  if(matches[results.get(c)].lastIndexOf("-w-") != -1){
    		      rrp = matches[results.get(c)].substring(0,matches[results.get(c)].lastIndexOf("-w-"));
    		  } else {
    			  rrp = matches[results.get(c)].substring(0,matches[results.get(c)].lastIndexOf("-n-"));
    		  }
    		  resultsReport += "\n"+ rrp;
    	  }	
    	} 
    	
    	resultsSearchTag.setText(resultsReport);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	resultsSearchTag.setText("arrived somethign");
    }
    private View.OnClickListener confirmListener = new View.OnClickListener(){
        public void onClick(View v) {
        	
        	if(results.size() == 1 ){
	        	Bundle bundle = new Bundle();
	            
	        	
	        	bundle.putString("tag", matches[results.get(0)] );
	        	bundle.putString("lat","345667");
	        	
	        	Intent mIntent = new Intent();
	        	mIntent.putExtras(bundle);
	        	
	        	
	        	setResult(RESULT_OK, mIntent);
	        	
	            finish();
        	} else {
        		showSimpleAlertDialog("Attenzione","Selezionare un unico indirizzo");
        	}
        }
    };
    private View.OnClickListener backListener = new View.OnClickListener(){
        public void onClick(View v){
            setResult(RESULT_CANCELED);
        	finish();
        }
        
    };
    private TextWatcher onChangeWatcher = new TextWatcher(){
    	
    	
    	@Override
    	public void afterTextChanged(Editable e){
    		
    		//resultsSearchTag.setText("chacged after" + );
    		// cerca 
    		search(e.toString());
    	}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		
    };
    public AlertDialog.Builder simpleAlertDialog;
    
    public void showSimpleAlertDialog(String title, String message){
		simpleAlertDialog.setMessage(message);
		simpleAlertDialog.setTitle(title);
		simpleAlertDialog.setCancelable(true);
		simpleAlertDialog.setPositiveButton("OK",onSimpleAlertListener);
		simpleAlertDialog.show();
        
	}
    private DialogInterface.OnClickListener onSimpleAlertListener = new DialogInterface.OnClickListener(){
		

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			// cancellazione del dialogo, accettazione
			// auto accettazione msgTextView.setText("Something");
		}
	};
}