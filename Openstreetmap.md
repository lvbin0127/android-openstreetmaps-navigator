# Introduction #

This page describes how to use SAX XML parser with [OpenStreetMap](http://www.openstreetmap.org) _.osm_ xml-based format in _eclipse IDE_.


# Details #
  1. insert insert .osm file]

## insert _.osm_ file into package structure ##

---


Copy generic file _.osm_ (in our code, _milano.osm_) in project folder _res/raw_.

See Android tutorial http://http://code.google.com/android/intro/hello-android.html to understand such a structure.

Automagically (or press F5) the _file id Resource_ (an integer value) referring to the xml file appears in file **R.java**. Don't care about this file, Eclipse IDE will do it for you.

The _file id resource_ is a easy and short way to access data and files. If our file is stored under _res/raw_ the code is simply this:
```
   InputStream xmlInputStream = context.getResources().openRawResource(R.raw.milano);
```
and you get your **java.io.InputStream**


## reading _.osm_ file ##

---

Now is time to read something from our InputStream.
Please note that you should use a **small sized** osm file because the String we got would be too big to evaluate.

SAX xml parsing will do the right way to obtain xml data.
```

  String xmlfile =""; // contains file chars
  String exceptionString = ""; // // contains message exception
  
  try{
    InputStream xmlInputStream = context.getResources().openRawResource(R.raw.milano); 
    
    int i=0;
    
    while((i=xmlInputStream.read())!=-1){
        xmlfile +=(char)i;
    }

  } catch(Exception e){
      exceptionString  = e.getMessage();
  }

```

## SAX xml parsing ##

---

Here the main step:
  1. create class that will interprete the xml flow (we called it OsmHandler)
  1. read the InputStream
  1. parse xml data

and here sample code
```
// package: com.android.OsmNavigator
// java file: OsmFileReader.java
// ... //

  public boolean parseStructure() throws ParserConfigurationException{
  
    try{
      /* 'standard' SAX way to parse xml*/
      // create the factory
      SAXParserFactory factory = SAXParserFactory.newInstance();
      
      // create a parser
      SAXParser parser = factory.newSAXParser();
      
      // create the reader (scanner)
      XMLReader xmlreader = parser.getXMLReader();
      
      /* end 'standard' SAX way to parse xml*/

      // instantiate our handler
      osmHandler = new OsmHandler();

      // assign our handler
      xmlreader.setContentHandler(osmHandler);
	
      // read from file           
      xmlInputStream = context.getResources().openRawResource(R.raw.milano);
	           
      // XMLReader parse() method
      xmlreader.parse(new InputSource(xmlInputStream));
	           
    }
}

```