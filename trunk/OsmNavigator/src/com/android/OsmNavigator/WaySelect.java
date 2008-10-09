package com.android.OsmNavigator;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

public class WaySelect extends Activity {
	private TextView msgTextView;
	private Button BACK;
	private Bundle intentBundle;
	//private ArrayList<Integer> results;
	private String[] matches;
	private String[] cleanupmatches;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wayselect);
        
        BACK = (Button)findViewById(R.id.back);
        BACK.setOnClickListener(backListener);
        
        msgTextView = (TextView)findViewById(R.id.title);
        msgTextView.setText("Cerca nella mappa corrente");
        
        
        intentBundle = getIntent().getExtras();
        
        simpleAlertDialog = new AlertDialog.Builder(this);
        
        
        matches = intentBundle.getString("hashstring").split("__");
        
        try{
        cleanupmatches = clearMatchesTags(matches);
        } catch(Exception e){
        	cleanupmatches = matches;
        	showSimpleAlertDialog("errore",e.getMessage());
        }
        
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, cleanupmatches);
       
        //resultsSearchTag.setText("Total records: " + matches.length);
        
        
        
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.editAutoComplete);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(itemClickListener);// setOnItemSelectedListener(itemClickListener);
        
        
    }
    private String[] clearMatchesTags(String[] m){
    	String[] cm = new String[m.length]; 
    	
    	for(int i = 0; i < m.length; i++){
    		if(m[i].lastIndexOf("-w-") != -1){
    		    cm[i] = m[i].substring(0,m[i].lastIndexOf("-w-"));
    		} else if(m[i].lastIndexOf("-n-") != -1){
    			cm[i] = m[i].substring(0,m[i].lastIndexOf("-n-"));
    		} else {
    			cm[i] = m[i];
    		}
    	}
    	return cm;
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	//
    }
    public void sendSearchResult(String r){
    	try{
    	Bundle bundle = new Bundle();
        
    	
    	bundle.putString("tag", r);
    	bundle.putString("lat","345667");
    	
    	Intent mIntent = new Intent();
    	mIntent.putExtras(bundle);
    	
    	
    	setResult(RESULT_OK, mIntent);
    	 finish();}
    	catch (Exception e){
    		Toast.makeText(getBaseContext()," " + e.getMessage() , 20).show();
    	}
    }
    private AdapterView.OnItemClickListener itemClickListener =  new AdapterView.OnItemClickListener(){
    	


		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Toast.makeText(getBaseContext(),"selected " + arg0.getItemAtPosition(arg2).toString(), 20).show();
			try{
			   sendSearchResult(arg0.getItemAtPosition(arg2).toString());
			} catch(Exception e){
				Toast.makeText(getBaseContext(),"selected " + e.getMessage() , 20).show();
			}
		}
    };
    
    
    private View.OnClickListener backListener = new View.OnClickListener(){
        public void onClick(View v){
            setResult(RESULT_CANCELED);
        	finish();
        }
        
    };
    
    public AlertDialog.Builder simpleAlertDialog;
    
    /**
     * Show a custom alert dialog
     * 
     * @param title 
     *   The title of our alert dialog
     * @param 
     *   message the message of our alert dialog
     * */
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
			
			// cancellazione del dialogo, accettazione
			// auto accettazione msgTextView.setText("Something");
		}
	};
}