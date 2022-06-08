package irass;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author ehab
 */
/*
 * InvertedIndex - Given a set of text files, implement a program to create an 
 * inverted index. Also create a user interface to do a search using that inverted 
 * index which returns a list of files that contain the query term / terms.
 * The search index can be in memory. 
 * 

 */
import java.io.*;
import java.util.*;

//=====================================================================
class DictEntry2 {

    public int doc_freq = 0; // number of documents that contain the term
    public int term_freq = 0; //number of times the term is mentioned in the collection
    public HashSet<Integer> postingList;

    DictEntry2() {
        postingList = new HashSet<Integer>();
    }
}

//=====================================================================
class Index2 {

    //--------------------------------------------
    Map<Integer, String> sources;  // store the doc_id and the file name
    HashMap<String, DictEntry2> index; // THe inverted index
    //--------------------------------------------

    Index2() {
        sources = new HashMap<Integer, String>();
        index = new HashMap<String, DictEntry2>();
    }

    //---------------------------------------------
    public void printDictionary() {
        Iterator it = index.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            DictEntry2 dd = (DictEntry2) pair.getValue();
            HashSet<Integer> hset = dd.postingList;// (HashSet<Integer>) pair.getValue();
            System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "] <" + dd.term_freq + "> =--> ");
            Iterator<Integer> it2 = hset.iterator();
            while (it2.hasNext()) {
                System.out.print(it2.next() + ", ");
            }
            System.out.println("");
            //it.remove(); // avoids a ConcurrentModificationException
        }
        System.out.println("------------------------------------------------------");
        System.out.println("*** Number of terms = " + index.size());
    }

    //-----------------------------------------------
    public void buildIndex(String[] files) {
        int i = 0;
        for (String fileName : files) {
            try ( BufferedReader file = new BufferedReader(new FileReader(fileName))) {
                sources.put(i, fileName);
                String ln;
                while ((ln = file.readLine()) != null) {
                    String[] words = ln.split("\\W+");
                    for (String word : words) {
                        word = word.toLowerCase();
                        // check to see if the word is not in the dictionary
                        if (!index.containsKey(word)) {
                            index.put(word, new DictEntry2());
                        }
                        // add document id to the posting list
                        if (!index.get(word).postingList.contains(i)) {
                            index.get(word).doc_freq += 1; //set doc freq to the number of doc that contain the term 
                            index.get(word).postingList.add(i); // add the posting to the posting:ist
                        }
                        //set the term_fteq in the collection
                        index.get(word).term_freq += 1;
                    }
                }
                printDictionary();
            } catch (IOException e) {
                System.out.println("File " + fileName + " not found. Skip it");
            }
            i++;
        }
    }

   
    //----------------------------------------------------------------------------
    HashSet<Integer> AND(HashSet<Integer> PL1, HashSet<Integer> PL2) {
        HashSet<Integer> answer = new HashSet<Integer>();;
        List<Integer> p1 = new ArrayList<Integer>(PL1);
        List<Integer> p2 = new ArrayList<Integer>(PL2);
        List<Integer> ans= new ArrayList<>(answer);

        int i=0 , j=0;
        while (i!=p1.size()&&j!=p2.size()) {
        	if (p1.get(i)<p2.get(j)) {
        		i++;
        	}
        	else if (p1.get(i)>p2.get(j)) {
        		j++;
        	}
        	else {
        		ans.add(p1.get(i));
        		i++;
        		j++;	
        	}
        	
        }
        
        HashSet<Integer> res =new HashSet<Integer>(ans);
        return res;
    }
    
    //-------------------------------------------------------
   
    HashSet<Integer> NOT(HashSet<Integer> PL) {
        HashSet<Integer> answer = new HashSet<Integer>(sources.keySet());
        Object[]array=PL.toArray();
        for (int i=0;i<PL.size();i++) {
        if(answer.contains(array[i]))
        {
        	answer.remove(array[i]);
        	}
        }
        return answer;
        }
        
    
    //------------------------------------------------------
    
    HashSet<Integer> OR(HashSet<Integer> pL1, HashSet<Integer> pL2) {
        HashSet<Integer> answer = new HashSet<Integer>();;
        List<Integer> p1 = new ArrayList<Integer>(pL1);
        List<Integer> p2 = new ArrayList<Integer>(pL2);
        
        for (int i=0;i<pL1.size();i++) {
        	
            answer.add(p1.get(i));

        }
        
        for (int i=0;i<pL2.size();i++) {

        	
            answer.add(p2.get(i));

        }
        
        return answer;
    }
    
    
//-----------------------------------------------------------------------
    /*HashSet<Integer> OR1(HashSet<Integer> pL1, HashSet<Integer> pL2) {
        HashSet<Integer> answer = new HashSet<Integer>();;
        List<Integer> p1 = new ArrayList<Integer>(pL1);
        List<Integer> p2 = new ArrayList<Integer>(pL2);
        List<Integer> ans = new ArrayList<Integer>(answer);

    
        for(int x=0;x<p1.size();x++)
        {
        ans.add(p1.get(x));
        
        }
        int i=0 ,j=0;
        
        while (i!=p1.size()&&j!=p2.size()) {
        	if (p1.get(i)!=p2.get(j)) {
        		ans.add(p2.get(j));
        		i++;
        		j++;
        		
        	}
        
        	else {
        		i++;
        		j++;	
        	}
        	
        }
        
        HashSet<Integer> res =new HashSet<Integer>(ans);
        return res;
        
        
        }*/
 //-----------------------------------------------------------------------

  
    public String find_OR(String phrase) { // 2 term phrase  2 postingsLists
        String result = "";
        String[] words = phrase.split("\\W+");
        // 1- get first posting list
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        // 2- get second posting list
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
        // 3- apply the algorithm
        HashSet<Integer> answer = OR(pL1, pL2);
        System.out.println("Found in: "+ "\n");
        for (int num : answer) {
            //System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }
        return result;
    }
//-----------------------------------------------------------------------         
    
    
    public String find_AND(String phrase) { // 2 term phrase  2 postingsLists
        String result = "";
        String[] words = phrase.split("\\W+");
        // 1- get first posting list
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        // 2- get second posting list
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
        // 3- apply the algorithm
        HashSet<Integer> answer = AND(pL1, pL2);
        if(answer.size()==0) {
            System.out.println("\n"+"Common Lists Cannot found"+ "\n");

        }
        else {
        System.out.println("Found in: "+ "\n");
        for (int num : answer) {
            //System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }
        }
        return result;
    }
//-----------------------------------------------------------------------
    
    public String find_NOT(String phrase) { // NOT
        String result = "";
        String[] words = phrase.split("\\W+");
        // 1- get first posting list
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        // 2- get second posting list
        // 3- apply the algorithm
        HashSet<Integer> answer = NOT(pL1);
        if(answer.size()==0) {
            System.out.println("\n"+"Common Lists Cannot found"+ "\n");

        }
        else {
        System.out.println("Not Found in: "+ "\n");
        for (int num : answer) {
            //System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }
        }
        return result;
    }
//-----------------------------------------------------------------------   

    public String find_AND_OR(String phrase) { // 3 lists
    	 
        String result = "";
        String[] words = phrase.split("\\W+");
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        //printPostingList(pL1);
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
        //printPostingList(pL2);
        HashSet<Integer> pL3 = new HashSet<Integer>(index.get(words[2].toLowerCase()).postingList);        
        //printPostingList(pL3);
        HashSet<Integer> answer1 = OR(pL2, pL3);
        //printPostingList(answer1);

        HashSet<Integer> answer2 = AND(pL1, answer1);
               // printPostingList(answer2);
        if(answer2.size()==0) {
            System.out.println("\n"+"Common Lists Cannot found"+ "\n");

        }
        else {
//        result = "Found in: \n";
        System.out.println("Found in: "+ "\n");

        for (int num : answer2) {
            //System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }     
        }   
        return result;

    }
  //-----------------------------------------------------------------------   

    public String find_AND_OR_NOT(String phrase) { // 3 lists
    	 
        String result = "";
        String[] words = phrase.split("\\W+");
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        //printPostingList(pL1);
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
        //printPostingList(pL2);
        HashSet<Integer> pL3 = new HashSet<Integer>(index.get(words[2].toLowerCase()).postingList);        
        //printPostingList(pL3);
        HashSet<Integer> answer0 = NOT( pL3);

        
        HashSet<Integer> answer1 = OR(pL2, answer0);
        //printPostingList(answer1);

        HashSet<Integer> answer2 = AND(pL1, answer1);
               // printPostingList(answer2);

        if(answer2.size()==0) {
            System.out.println("\n"+"Common Lists Cannot found"+ "\n");

        }
        else {
//        result = "Found in: \n";
        System.out.println("Found in: "+ "\n");

        for (int num : answer2) {
            //System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }        
        }
        return result;

    }
    
    //-----------------------------------------------------------------------   

    public String find_AND_OR_AND_NOT(String phrase) { // 4 lists
    	 
        String result = "";
        String[] words = phrase.split("\\W+");
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        //printPostingList(pL1);
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
        //printPostingList(pL2);
        HashSet<Integer> pL3 = new HashSet<Integer>(index.get(words[2].toLowerCase()).postingList);        
        //printPostingList(pL3);
        HashSet<Integer> pL4 = new HashSet<Integer>(index.get(words[3].toLowerCase()).postingList);        
        //printPostingList(pL4);
        HashSet<Integer> answer0 = NOT( pL4);
        //printPostingList(answer0);

        
        HashSet<Integer> answer1 = OR(pL2, pL3);
        //printPostingList(answer1);

        HashSet<Integer> answer2 = AND(pL1, answer1);
               // printPostingList(answer2);
        
        HashSet<Integer> answer3 = AND(answer0,answer2);
        //printPostingList(answer3);
if(answer3.size()==0) {
    System.out.println("\n"+"Common Lists Cannot found"+ "\n");

}
else {
//        result = "Found in: \n";
        System.out.println("Found in: "+ "\n");

        for (int num : answer3) {
            //System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }
        }        
        return result;

    }
    
    
    //-----------------------------------------------------------------------         

   
}