package CS4551_HW2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class LZW {
	private File f;
	
	public LZW(String file) throws IOException {
		this.f = new File(file);
	encoder(f);
}

	public void encoder(File f){
		BufferedReader br = null;
        String c = "";
        String[] arr = null;
        System.out.println(this.f + " Results:");
        System.out.println("");
        System.out.println("Original Text:");
        
        try {
        	br = new BufferedReader(new FileReader(this.f));
			while ((c = br.readLine()) != null) {
				arr = c.split("");
				System.out.println(c);
			  
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        List<String> arrl = new ArrayList<String>();
        for(int i = 0; i < arr.length; i++){
        	arrl.add(arr[i]);
        }
        
        System.out.println("");
        System.out.println("Index | Dictionary");
        HashMap<Integer, String> dictionary = new HashMap<>();
        dictionary.put(0, "");
        
        //get initial values
        for(int counter = 0, i = 0; dictionary.size() < 256 && counter < arrl.size(); i++){
        	if(!dictionary.containsValue(arrl.get(counter))){
        		dictionary.put(i, arrl.get(counter));
        		counter++;
        	}
        	else{
        		counter++;
        		i--;
        	}
        }
        
        int iniValue = dictionary.size();
        
        boolean done = false;
        
        //longer sequence
        for(int counter = 0, i = iniValue; dictionary.size() < 256 && counter < arrl.size(); ){
        	int j = 1;
        	while(dictionary.containsValue(arrl.get(counter))){
        		
        		if(counter == arrl.size() - 1){
        			done = true;
        			counter++;
        			break;
        		}
        		
        		String temp = "";
        		temp = arrl.get(counter) + arrl.get(counter + j);
      			arrl.set(counter, temp);
      			
      			j++;
	
        	}
        	 
        	if(!done){
        		dictionary.put(i, arrl.get(counter));
        		counter++;
        		i++;
        		
        		if(j > 2){
            		int d = j - 2;
      				counter = counter + d;
      			}
        	}
        	

        }

        
        
        for(int i = 0; i < dictionary.size(); i++){
        	System.out.println( i + "   | " + dictionary.get(i));
        } 
        
        
        System.out.println("Encoded Text:");
        
        List<Integer> eText = new ArrayList<Integer>();
        
        for(int i = 0; i < arrl.size(); i++){
        	
        }
        
        
        
        
        
	}
 }
