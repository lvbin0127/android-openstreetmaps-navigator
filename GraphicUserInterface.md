## xml layout ##

---

...
## animations ##

---

...
## fonts and typefaces ##

---

First of all, decide wich ttf file (e.g., _samplefont.ttf_) to use and put it to the right place in the package
```
     OsmNavigator
     |
     |_ src
     |_ assets
        |_ fonts
           |_ samplefont.ttf
```
##  ##
### Assign the Typeface object to TextView ###

---

```
   int resourceId = R.id.someTextWidgetId;

   TextView someTextWidget=(TextView)findViewById(resourceId);  
   
   Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/samplefont.ttf"); 
        
   someTextWidget.setTypeface(font); 
```
##  ##
### drawing on canvas ###

---

**Note: use only in classes that impements and @Override the _onDraw(Canvas canvas)_ protected method**
##  ##



Declare a TypeFace intance and a Paint instance to write with
```
  private Typeface tFace;
  private Paint tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
```
In our main method maybe "constructor"
```
  
    tFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/samplefont.ttf"); // set file
    tPaint.setTextSize(64); // set text size
```
In **onDraw** method, set the type face and draw text on Canvas
```
  @Override protected void onDraw(Canvas canvas) {
    // ...
    tPaint.setTypeface(mFace);
    
    canvas.drawText("Custom", 10, 200, tPaint);
  
  }
```
