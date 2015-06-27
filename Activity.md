## setResult() method ##
This method send back some data of the sub-activity to the main activity

  * assign a clickListener to button in **onCreate(Bundle savedInstanceState)**
```
  CONFIRM = (Button)findViewById(R.id.ok);
  CONFIRM.setOnClickListener(confirmListener);
```
  * code
```
  public class WaySelect extends Activity {
    /*
    ...
    */
    private View.OnClickListener confirmListener = new View.OnClickListener(){
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            
            bundle.putString("tag", "titolo");
            bundle.putString("lat","345667");
        	
            Intent mIntent = new Intent();
            mIntent.putExtras(bundle);
            
            setResult(RESULT_OK, mIntent);
        	
            finish();
        }
    };
}
```
the method call **finish()**, and the screen point to main activity

## Listening for sub-activity result ##

---

```
  protected void onActivityResult(int requestCode, int resultCode, Intent data){
   
    /*
    ... control the sub-activity checking the requestCode and the resultCode
    */
 
    Bundle result = data.getExtras();
    
    /* recover the bundle */
		String tag = result.getString("tag"); // return 'titolo'
		String lat = result.getString("lat"); // return '345667'
		
  }
```
  * the method argument _requestCode_ identify the sub-activity committed
  * _data_ refers to the **Intent** _mIntent_ created above.
Thus, we can extract the data we stored in the **Bundle** _bundle_ before we finish the sub-activity.
The Loop is now completed