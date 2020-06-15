package com.tmstudios.fontprev;

import android.app.*;
import android.os.*;
import android.graphics.*;
import java.io.*;
import android.util.*;
import android.widget.*;

public class MainActivity extends Activity 
{

	private File temp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		if (getIntent().getData()==null){
			finish();
			return;
		}
		String extension = getIntent().getData().getPath();
		if(extension.contains(".")){
			extension=extension.substring(extension.lastIndexOf("."));
		}
		else extension="";
		InputStream k=null;
		FileOutputStream c=null;
		try
		{
			k = getContentResolver().openInputStream(getIntent().getData());
			byte[] buffer = new byte[1024];
			try
			{
				temp = File.createTempFile("temp", "",getCacheDir());
			}
			catch (IOException e)
			{
				Log.e("temp issues","cant create");
			}

			c = new FileOutputStream(temp);
			while(k.available()>0){
				int b =k.read(buffer,0,1024);
				c.write(buffer,0,b);
			}
		}
		catch (FileNotFoundException e)
		{
			Log.e("file not found",e.toString());
		}
		catch(IOException e){
			Log.e("error writing",e.toString());
		}
		finally{
			try
			{
				if(c!=null)
					c.close();
				if(k!=null)
					k.close();
			}
			catch (IOException e)
			{
				Log.e("bah","error closing file",e);
			}
		}
		try{
		Typeface font = Typeface.createFromFile(temp);
		((TextView)findViewById(R.id.mainTextView)).setTypeface(font);
		}
		catch(RuntimeException e){
			((TextView)findViewById(R.id.mainTextView)).setText("Error opening font");
			((TextView)findViewById(R.id.mainTextView)).setTextColor(Color.RED);
		}
		
    }

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		if(temp!=null && temp.exists())
			temp.delete();
		super.onDestroy();
	}
	
}
