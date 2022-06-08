package irass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;  



public class Main {

	public static void main(String args[]) throws IOException {
        Index2 index = new Index2();
        String phrase = "";

        index.buildIndex(new String[]{
        		"C:\\Users\\www\\Downloads\\Music\\Newfolder\\100.txt", 
	            "C:\\Users\\www\\Downloads\\Music\\Newfolder\\101.txt",
	            "C:\\Users\\www\\Downloads\\Music\\Newfolder\\102.txt",
	            "C:\\Users\\www\\Downloads\\Music\\Newfolder\\103.txt", 
	            "C:\\Users\\www\\Downloads\\Music\\Newfolder\\104.txt",
	            "C:\\Users\\www\\Downloads\\Music\\Newfolder\\105.txt",
	            "C:\\Users\\www\\Downloads\\Music\\Newfolder\\106.txt", 
	            "C:\\Users\\www\\Downloads\\Music\\Newfolder\\107.txt",
	            "C:\\Users\\www\\Downloads\\Music\\Newfolder\\108.txt",
	            "C:\\Users\\www\\Downloads\\Music\\Newfolder\\109.txt"

        });
 
        int x=0;
        boolean flag=true;
        while(flag) {
        	
            System.out.println("1:NOT W1");
            System.out.println("2:W1 OR W2");
            System.out.println("3:W1 AND W2");
            System.out.println("4:W1 AND W2 OR W3");
            System.out.println("5:W1 AND W2 OR NOT W3");
            System.out.println("6:W1 AND W2 OR W3 AND NOT W4");
            System.out.println("7:EXIT");
			Scanner sc= new Scanner(System.in);      

            System.out.println("\n"+"Enter Your Choies:-");
			x= sc.nextInt();  
			if(x>7) {
				throw new IllegalArgumentException("\n"+"Unexpected value: " + x);

			}

            

        	switch (x) {
			case 1: {
				
				System.out.println("Enter phrase: "+ "\n");
	            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	            phrase = in.readLine();
	            System.out.println(index.find_NOT(phrase));
				break;
			}
			case 2: {
				System.out.println("Enter phrase: "+ "\n");
	            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	            phrase = in.readLine();
	            System.out.println(index.find_OR(phrase));
	            break;
				}
			case 3: {
				System.out.println("Enter phrase: "+ "\n");
	            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	            phrase = in.readLine();
	            System.out.println(index.find_AND(phrase));
	            break;
			}
			case 4: {
				System.out.println("Enter phrase: "+ "\n");
	            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	            phrase = in.readLine();
	            System.out.println(index.find_AND_OR(phrase));
	            break;
	
			}
			case 5: {
				System.out.println("Enter phrase: "+ "\n");
	            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	            phrase = in.readLine();
	            System.out.println(index.find_AND_OR_NOT(phrase));
	            break;
	
			}
			case 6: {
				System.out.println("Enter phrase: "+ "\n");
	            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	            phrase = in.readLine();
	            System.out.println(index.find_AND_OR_AND_NOT(phrase));
	            break;
	
			}
			case 7: {
				flag=false;

			}
			}	
        }
	}
}
