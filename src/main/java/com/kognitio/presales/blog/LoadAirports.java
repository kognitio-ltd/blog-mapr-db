/*
  Inserts JSON records into a MapR-DB table using a sequence number as a key

  Derived from https://mapr.com/docs/home/MapR-DB/JSON_DB/insert-document.html
*/

package com.kognitio.presales.blog;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import org.ojai.Document;
import org.ojai.store.Connection;
import org.ojai.store.DocumentStore;
import org.ojai.store.DriverManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoadAirports {

  public static void main(String[] args) throws JSONException, IOException {
    
      System.out.println("=== Start Load ===");
      
      // Open the JSON file
      Reader in = new InputStreamReader(new FileInputStream(args[1]), "UTF8");
      BufferedReader bufferedReader = new BufferedReader(in);
      JSONArray jsonarray = new JSONArray(new JSONTokener(bufferedReader));
      
      // Create an OJAI connection to MapR cluster
      final Connection connection = DriverManager.getConnection("ojai:mapr:");

      // Get an instance of OJAI
      final DocumentStore store = connection.getStore(args[0]);

      for (int i = 0; i < jsonarray.length(); i++) {
        
          // Create an OJAI Document
          final Document userDocument = connection.newDocument(jsonarray.getJSONObject(i).toString());

          String id = String.format("%04d", i);
          
          System.out.print("\t inserting \r" + id);

          // insert the OJAI Document into the DocumentStore
          store.insertOrReplace(id, userDocument);
        
      }

      // Close this instance of OJAI DocumentStore
      store.close();

      // close the OJAI connection and release any resources held by the connection
      connection.close();

      System.out.println("\n=== End Load ===");
  }
}
