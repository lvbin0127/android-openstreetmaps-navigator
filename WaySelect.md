## Structure ##

---

The user interface presents:
  * a [Widget.EditText](http://code.google.com/android/reference/android/widget/EditText.html) editable text field
  * an OK [Widget.Button](http://code.google.com/android/reference/android/widget/Button.html) and a BACK button

## Activity reponse ##

---

Pressing OK or BACK button makes this activity finish. The _on click handlers_ methods are these
  1. **confirmListener**  listens OK click
  1. **backListener** listens BACK click
Each method must ovverride an _onClick(View v)_ method
```
  private View.OnClickListener confirmListener = new View.OnClickListener(){
    public void onClick(View v) {
    /*... OK Pressed */
       Bundle bundle = new Bundle(); // piece of information key-value string
            
       bundle.putString("tag", searchEditText.getText().toString());
       bundle.putString("lat","345667");
        	
       
        	Intent mIntent = new Intent();
        	mIntent.putExtras(bundle);
        	setResult(RESULT_OK, mIntent);
        	
            finish();
    }
  }

```
Then, if the user press OK, an Intent instance with some information is set, and the **setResult(OK, Intent)** allows this activity to send back data to main activity, OsmReader.
Otherwise, simply calling **finish()** causes the activity end:
```
            setResult(RESULT_CANCELED);
        	
            finish();
```