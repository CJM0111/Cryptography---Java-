import java.io.*;
import java.util.*;

/** 
 *  Classical Encryption and Decryption Program -
 *  Encrypts and Decrypts Messages using various ciphers - (Shift, Substitution, Affine, Vigenere)
 *  @author Chris McDonald
*/
public class crypto
{	
	// Global variables
	static int input;
	static String end;
	static String localend;
	static char EorD;
	static String toText;
	static char [] text;
	static String keySub;
	static char [] charKeySub;
	static char [] Sub;
	static char [] alphabet;
	static int keyInt;
	static int keyA;
	static int keyB;
	static int inverseA;
	static char [] keyV;
	/** 
	 * Main Method
	 */
	public static void main(String [] args)
	throws IOException
	{
		do {
			welcome();
			if(input == 1){shift();}
			if(input == 2){sub();}
			if(input == 3){affine();}
			if(input == 4){vigenere();}
			repeat();
		}while(localend!="done"); // end do-while loop
	}
	/** 
	* This method displays a menu and allows the user to select a cipher to use
	*/
	public static void welcome()
	{
		String end=null;
		do {
			// Display the menu
			System.out.println("You are now entering the Cryptanalysis Program, there is no turning back.");
			System.out.println("~Choose your preferred cipher~");
			System.out.println("Shift Cipher(1)");
			System.out.println("Substitution Cipher(2)");
			System.out.println("Affine Cipher(3)");
			System.out.println("Vigenere Cipher(4)");
			System.out.print("Enter the corresponding number: ");
			Scanner cin= new Scanner(System.in);
			input = cin.nextInt();
			if(input==1){end="done";}else
			if(input==2){end="done";}else
			if(input==3){end="done";}else
			if(input==4){end="done";}else{end="no";}
			if(end=="no"){System.out.println("Invalid input.");}
		}while(end!="done"); // end do-while loop
	}
	/**
	* This method allows the user to either use the program again or quit
	*/
	public static void repeat()
	{
		do {
			Scanner cin=new Scanner(System.in);
			System.out.print("Continue?(Y/N) ");
			String repeat = cin.next();
			char repeatChar=repeat.charAt(0);
			if(repeatChar=='Y'){localend="done";}else
			if(repeatChar=='y'){localend="done";}else
			if(repeatChar=='N'){localend="done";end="done";}else
			if(repeatChar=='n'){localend="done";end="done";}else{end="no";}
			if(end=="no") {
				System.out.println("Invalid input.");
			} // end if
		}while(localend!="done"); // end do-while loop
	}
	/** 
	* This method reads in a text file of the user's choosing and stores the text into a char array
	* When the file is read in, all characters are converted to UpperCase letters
	*/
	public static void readIn()
	throws IOException, FileNotFoundException
	{
		System.out.print("Select your message. ");
		System.out.print("Enter the name of your file. ");
		System.out.print("(The format should be /Users/name/~): ");
		Scanner cin= new Scanner(System.in);
		String fileIn = cin.nextLine();
		Scanner read=new Scanner(new FileReader(fileIn));
		toText = read.nextLine();
		while(read.hasNextLine()) {
			toText += read.nextLine();
		}; // end while loop
		text = toText.toCharArray();
		for(int i=0; i<toText.length(); i++) {
			text[i] = Character.toUpperCase(text[i]);
		} // end for loop
		System.out.print("This is the message you selected: ");
		System.out.println(text);
	}
	/** 
	 * This method stores the text into a file of the user's choosing
	 * When the file is written to, all characters are converted to LowerCase letters
	 */
	public static void store()
	throws IOException, FileNotFoundException
	{
		System.out.print("Store your message. ");
		System.out.print("Enter the name of your file. ");
		System.out.print("(The format should be /Users/name/~): ");
		Scanner cin= new Scanner(System.in);
		String fileOut = cin.nextLine();
		FileWriter output=new FileWriter(fileOut);
		BufferedWriter bufferedWriter =new BufferedWriter(output);
		for(int i=0;i<text.length;i++) {
			text[i] = Character.toLowerCase(text[i]);
		} // end for loop
		bufferedWriter.write(text);
		bufferedWriter.close();
	}
	/** 
	 * This method allows the user to choose between either Encryption or Decryption
	 */
	public static void choose()
	{
		String end=null;
		do {
			System.out.print("Encryption or Decryption?(E/D): ");
			Scanner cin= new Scanner(System.in);
			String myInput = cin.next();
			EorD = myInput.charAt(0);
			if(EorD=='E'){end="done";}else
			if(EorD=='e'){end="done";}else
			if(EorD=='D'){end="done";}else
			if(EorD=='d'){end="done";}else{end="no";}
			if(end=="no") {
				System.out.println("Invalid input.");
			} // end if
		}while(end!="done"); // end do-while loop
	}
	/** 
	 * This method allows the user to input a key for the Shift cipher
	 */
	public static void keyShift()
	{
		System.out.print("Enter the key: ");
		Scanner cin=new Scanner(System.in);
		keyInt = cin.nextInt();
	}
	/** 
	 * This method allows the user to input a key for the Substitution cipher
	 * Duplicates within the key are removed via a Linked Hash Set
	 */
	public static void keySub()
	{
		System.out.print("Enter the key: ");
		Scanner cin=new Scanner(System.in);
		keySub = cin.nextLine();
		charKeySub = keySub.toCharArray();
		LinkedHashSet<Character> SubKey=new LinkedHashSet<Character>();
		for(int i=0;i<charKeySub.length;i++){SubKey.add(charKeySub[i]);}
		Object[] yo=SubKey.toArray();
		String tempSubKey[]  = new String[yo.length];
		for(int i=0;i<yo.length;i++) {
			tempSubKey[i]=yo[i].toString();
		} // end for loop
		int counterChars1 = 0;
		charKeySub=new char[yo.length];
		for(int i = 0; i < tempSubKey.length; i++) { 
			int counterLetters1 = 0;  
		        for (int j = 0; j < tempSubKey[i].length(); j++){ 
		        	charKeySub[counterChars1] = tempSubKey[i].charAt(counterLetters1);counterLetters1++;counterChars1++;
		        } // end for loop
		}
		removeDuplicate();
	}
	/** 
	 * This method converts a char array holding the key and a full listing of the
	 * alphabet to a Linked Hash Set which retains the order and removes duplicates
	 * Then the LHS is converted back into a char array
	 */
	public static void removeDuplicate()
	{
		alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		char [] alphabet2=new char[charKeySub.length+alphabet.length];
		LinkedHashSet<Character> alpha=new LinkedHashSet<Character>();
		for(int i=0;i<charKeySub.length;i++) {
			alphabet2[i] = charKeySub[i];
		} // end for loop
		for(int i=0;i<alphabet.length;i++) {
			for(int j=0;j<charKeySub.length;j++) {
				alphabet2[charKeySub.length+i]=alphabet[i];
			} // end for loop
		} // end for loop
		for(int i=0;i<charKeySub.length+alphabet.length;i++) {
			alpha.add(alphabet2[i]);
		} // end for loop
		Object[] objects=alpha.toArray();
		String tempAlpha[]  = new String[objects.length];
		for(int i=0;i<tempAlpha.length;i++) {
			tempAlpha[i]=objects[i].toString();
		} // end for loop
		int counterChars = 0;
		Sub=new char[objects.length];
		for (int i = 0; i < tempAlpha.length; i++) { 
			int counterLetters = 0;  
		        for (int j = 0; j < tempAlpha[i].length(); j++) {
		        	Sub[counterChars] = tempAlpha[i].charAt(counterLetters);counterLetters++;counterChars++;
		        } // end for loop
		} // end for loop
	}
	/**
	 * This method uses a two dimensional array to store and manipulate the alphabet and the keyword
	 * The array reads in the new alphabet from left to right and then top to bottom
	 * Then the data is transfered to a one dimensional array and read from top to bottom and then left to right
	 */
	public static void transposition()
	{
		int columns = charKeySub.length;
		int rows = Sub.length/charKeySub.length;
		int staticRows = rows;
		int extra = 26 -(rows*columns);
		char [][] subcipher = new char[columns+rows][columns+rows];
		int n=0;
		for(int i=0;i<rows;i++) {
			for(int j=0;j<columns;j++) {
				subcipher[i][j]=Sub[n];n++;
			} // end for loop
			if(extra>0 && i==rows-1) {
				i++;
				for(int k=0;k<extra;k++) {
					subcipher[i][k]=Sub[n];n++;
				} // end for loop
			} // end if
		} // end for loop
		n=0;
		for(int i=0;i<columns;i++) {
			if(i+1<=extra) {
				rows++;
			} // end if
			for(int j=0;j<rows;j++) {
				Sub[n]=subcipher[j][i];n++;
			} // end for loop
			rows=staticRows;
		} // end for loop
		for(int i=0;i<alphabet.length;i++) {
			Sub[i] = Character.toUpperCase(Sub[i]);
		} // end for loop
		for(int i=0;i<alphabet.length;i++) {
			alphabet[i] = Character.toUpperCase(alphabet[i]);
		} // end for loop
	}
	/** 
	 * This method allows the user to input a key for the Affine cipher
	 */
	public static void keyAffine()
	{
		System.out.println("Enter the key:");
		Scanner cin=new Scanner(System.in);
		testA();
		System.out.print("B: ");
		keyB = cin.nextInt();
		inverse();
	}
	/** 
	 * This method verifies that the key for A is relatively prime to 26
	 */
	public static void testA()
	{
		Scanner cin=new Scanner(System.in);
		String end=null;
		System.out.print("A: ");
		do {
			keyA = cin.nextInt();
			if(keyA==1){break;}else
			if(keyA==3){break;}else
			if(keyA==5){break;}else
			if(keyA==7){break;}else
			if(keyA==9){break;}else
			if(keyA==11){break;}else
			if(keyA==15){break;}else
			if(keyA==17){break;}else
			if(keyA==19){break;}else
			if(keyA==21){break;}else
			if(keyA==23){break;}else
			if(keyA==25){break;}else{end="no";}
			if(end=="no"){System.out.println("...no");}
		}while(end!="done"); // end do-while loop
	}
	/** 
	 * This method calculates the inverse of the given key value for A
	 */
	public static void inverse()
	{
		int  start = 0;
		for(int i=0;i<26;i++) {
			start = ((keyA)*i)%26;
			if(start == 1) {
				inverseA = i;
			} // end if
		} // end for loop
	}
	/** 
	 * This method allows the user to input a key for the Vigenere cipher
	 */
	public static void keyVigenere()
	{
		System.out.print("Enter the key: ");
		Scanner cin=new Scanner(System.in);
		String vKey = cin.nextLine().toLowerCase();
		while(vKey.length() < toText.length()){vKey+=vKey;}
		keyV = vKey.toCharArray();
		for(int i=0;i<toText.length();i++) {
			keyV[i] = Character.toUpperCase(keyV[i]);
		} // end for loop
	}
	/** 
	 * This method points to either an Encryption or Decryption 
	 * algorithm for the Shift cipher
	 */
	public static void shift()
	throws IOException, FileNotFoundException
	{
		System.out.println("You chose the Shift cipher.");
		choose();
		if(EorD=='E' || EorD== 'e'){shiftEncrypt();}
		if(EorD=='D' || EorD== 'd'){shiftDecrypt();}
	}
	/** 
	 * This method points to either an Encryption or Decryption 
	 * algorithm for the Substitution cipher
	 */
	public static void sub()
	throws IOException, FileNotFoundException
	{
		System.out.println("You chose the Substitution cipher.");
		choose();
		if(EorD=='E' || EorD== 'e'){subEncrypt();}
		if(EorD=='D' || EorD== 'd'){subDecrypt();}
	}
	/**
	 * This method points to either an Encryption or Decryption 
	 * algorithm for the Affine cipher
	 */
	public static void affine()
	throws IOException, FileNotFoundException
	{
		System.out.println("You chose the Affine cipher.");
		choose();
		if(EorD=='E' || EorD== 'e'){affineEncrypt();}
		if(EorD=='D' || EorD== 'd'){affineDecrypt();}
	}
	/**
	 * This method points to either an Encryption or Decryption 
	 * algorithm for the Vigenere cipher
	 */
	public static void vigenere()
	throws IOException, FileNotFoundException
	{
		System.out.println("You chose the Vigenere Cipher.");
		choose();
		if(EorD=='E' || EorD== 'e'){vigenereEncrypt();}
		if(EorD=='D' || EorD== 'd'){vigenereDecrypt();}
	}
	/**
	 * This method encrypts the message using the Shift cipher
	 */
	public static void shiftEncrypt()
	throws IOException, FileNotFoundException
	{
		readIn();
		keyShift();
		for(int i=0;i<toText.length();i++) {
			if(text[i] == ' '){}
			else if(Character.isLetter(text[i])==false){}
			else {text[i] += keyInt;if(text[i]>'Z'){text[i]-=26;}}
		} // end for loop
		System.out.print("This is the encrypted message: ");
		System.out.println(text);
		store();
	}
	/**
	 * This method decrypts the message using the Shift cipher 
	 */
	public static void shiftDecrypt()
	throws IOException, FileNotFoundException
	{
		readIn();
		keyShift();
		for(int i=0;i<toText.length();i++) {
			if(text[i] == ' '){}
			else if(Character.isLetter(text[i])==false){}
			else {text[i] -= keyInt;if(text[i]<'A'){text[i]+=26;}}
		} // end for loop
		System.out.print("This is the decrypted message: ");
		System.out.println(text);
		store();
	}
	/**
	 * This method encrypts the message using the Substitution cipher 
	 */
	public static void subEncrypt()
	throws IOException, FileNotFoundException
	{
		readIn();
		keySub();
		transposition();
		char [] localText=text;
		for(int j=0;j<text.length;j++) {
			for(int i=0;i<alphabet.length;i++) {
				if(text[j]==alphabet[i]){ localText[j]=Sub[i];break; }
			} // end for loop
		} // end for loop
		text = localText;
		System.out.print("This is the decrypted message: ");
		System.out.println(text);
		store();
	}
	/**
	 * This method decrypts the message using the Substitution cipher
	 */
	public static void subDecrypt()
	throws IOException, FileNotFoundException
	{
		readIn();
		keySub();
		transposition();
		char [] localText =text;
		for(int j=0;j<text.length;j++) {
			for(int i=0;i<alphabet.length;i++) {
				if(text[j]==Sub[i]){ localText[j]=alphabet[i];break; }
			} // end for loop
		}
		text = localText;
		System.out.print("This is the decrypted message: ");
		System.out.println(text);
		store();
	}
	/**
	 * This method encrypts the message using the Affine cipher 
	 */
	public static void affineEncrypt()
	throws IOException, FileNotFoundException
	{
		readIn();
		keyAffine();
		for(int i=0;i<toText.length();i++) {
			if(text[i] == ' '){}
			else if(Character.isLetter(text[i])==false){}
			else {text[i]-=65;text[i] = (char) (((keyA*text[i]+keyB)%26)+65);}
		} // end for loop
		System.out.print("This is the encrypted message: ");
		System.out.println(text);
		store();
	}
	/**
	 * This method decrypts the message using the Affine cipher 
	 */
	public static void affineDecrypt()
	throws IOException, FileNotFoundException
	{
		readIn();
		keyAffine();
		for(int i=0;i<toText.length();i++) {
			if(text[i] == ' '){}
			else if(Character.isLetter(text[i])==false){}
			else {text[i]-=65;if(text[i]==0){text[i]+=26;}
			text[i] = (char)((inverseA*(text[i]-keyB))%26+65);if(text[i]<65){text[i]+=26;}}
		} // end for loop
		System.out.print("This is the decrypted message: ");
		System.out.println(text);
		store();
	}
	/**
	 * This method encrypts the message using the Vigenere cipher 
	 */
	public static void vigenereEncrypt()
	throws IOException, FileNotFoundException
	{
		readIn();
		keyVigenere();
		for (int i = 0,j=0;i < toText.length(); i++,j++) {
			if(text[i] == ' '){j--;}
			else if(Character.isLetter(text[i]) == false){j--;}
			else {text[i] = (char)(((text[i] + keyV[j])%26)+65);}
		} // end for loop
		System.out.print("This is the encrypted message: ");
		System.out.println(text);
		store();
	}
	/**
	 * This method decrypts the message using the Vigenere cipher 
	 */
	public static void vigenereDecrypt()
	throws IOException, FileNotFoundException
	{
		readIn();
		keyVigenere();
	        for (int i = 0,j=0;i < toText.length(); i++,j++) {
	        	if(text[i]==' '){j--;}
			else if(Character.isLetter(text[i])==false){j--;}
			else {text[i] = (char)(((text[i] - keyV[j]+26)%26)+65);}
		} // end for loop
	  	System.out.print("This is the decrypted message: ");
	        System.out.println(text);
		store();
	}
}
