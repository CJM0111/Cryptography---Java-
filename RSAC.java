import java.io.*;
import java.util.*;
import java.math.*;
import java.security.SecureRandom;

/** 
 * RSA Program -
 * Manages:
 * RSA Encryption and Decryption
 * Key Generation
 * Computing a Private Key from a Public Key using Fermat's Method
 * @author Chris McDonald
*/

public class rsaC
{
// Global variables
static int size;
static int lengthN;
static char EDchar;
static char block;
static char skip;
static char skip2;
static int hold;
static String messageToE;
static String stringOut;
static PrintWriter pout;
static BigInteger messageToD;
static BigInteger bigIntOut;
static BigInteger p, q;
static BigInteger n;
static BigInteger Phi;
static BigInteger e, d;
static BigInteger r1;
static BigInteger r2;
static SecureRandom random = new SecureRandom();
/** 
 * This method reads in a text file to be encrypted or decrypted
 * The user then decides to encrypt or decrypt and
 * the method calls the appropriate function
 * If the file contains blocked ciphertext upon choosing decryption, the method will 
 * read in the keys, decrypt the message and then store the 
 * decrypted text to the desired file for each block
 */
public static void readIn()
throws IOException, FileNotFoundException
{
	// Read in the file
	System.out.print("Enter the name of your file. ");
	System.out.print("(The format should be /Users/name/~): ");
	Scanner jin= new Scanner(System.in);
	String fileName= jin.nextLine();
	Scanner fileIn=new Scanner(new FileReader(fileName));
	
	// Decide whether to encrypt or decrypt and then execute
	System.out.println("Encrypt or Decrypt?(E/D) ");
	String EDstring = jin.next();
	EDchar = EDstring.charAt(0);
	if(EDchar == 'E' || EDchar == 'e') {
		messageToE = fileIn.nextLine();
		while(fileIn.hasNextLine()){messageToE += fileIn.nextLine();};
		// removes all non-characters
		messageToE = messageToE.replaceAll("\\s","");
		messageToE = messageToE.replaceAll("\\W","");
		System.out.println("Plaintext: " + messageToE);
	} // end if
	else {
		// Execute the decryption of the message
		if(EDchar == 'D' || EDchar == 'd') {
			messageToE = fileIn.nextLine();
			
			if(fileIn.hasNextLine()) {
				System.out.println("Ciphertext: " + messageToE);
				messageToD = new BigInteger("0");
				// Converts into a BigInteger
				messageToD = messageToD.add(new BigInteger(messageToE));
				skip2 = 'Y';
				hold++;
				// Avoids reading in the keys again if this method is called more than once
				if(hold == 1) { 
					readInKeys();
				} // end if
				
				block = 'B';
				
				// Calls the method to execute the encryption or decryption of the message
				encryptordecrypt();
				// If the ciphertext is formatted into blocks, then
				// read in one block at a time, decrypt the block of ciphertext and
				// store the decypted block and repeat until all of the blocks are decrypted
				while(fileIn.hasNextLine()) {
					messageToE = fileIn.nextLine();
					System.out.println("Ciphertext: " + messageToE);
					messageToD = new BigInteger("0");
					messageToD = messageToD.add(new BigInteger(messageToE));
					encryptordecrypt();
				} // end while loop
			} // end if
			else
			{
				// Displays the ciphertext and stores the value into the variable to be decrypted
				System.out.println("Ciphertext: " + messageToE);
				System.out.println(messageToE);
				messageToD = new BigInteger("0");
				messageToD = messageToD.add(new BigInteger(messageToE));
			} // end else
		}
		else { 
			// Avoid input options other than encrypt(E) or decrypt(D)
			System.out.println("INVALID INPUT.");
		} // end else
	} // end else
	
}
/** 
 * This method reads in a text file with the public and/or private keys
 * and then calls the block method if the length of the message is
 * greater than n
 */
public static void readInKeys()
throws IOException, FileNotFoundException
{
	String end = "";
	do {
    		end = "yes"; 
    		// Reads in the keys
		System.out.print("Load your key here~ ");
		System.out.print("Enter the name of your file. ");
		System.out.print("(The format should be /Users/name/~): ");
		Scanner jin= new Scanner(System.in);
		String fileN= jin.nextLine();
		Scanner fileIn=new Scanner(new FileReader(fileN));
		
		// The user chooses which type of key to load
		System.out.println("Is the key public(0) or private(1)?");
		int key = jin.nextInt();
		String tempN, tempE, tempD, tempP, tempQ = " ";
		if(key == 0) {
			tempN = fileIn.nextLine();
			System.out.println("N:" + tempN);
			tempE = fileIn.nextLine();
			System.out.println("E:" + tempE);
			// Converts the variable to a BigInteger
			n = (new BigInteger(tempN));
			e = (new BigInteger(tempE));
			lengthN = tempN.length();
			// Computes the private key from the public key by implementing Fermat's Factorization Method
			System.out.println("Do you want to generate the private key from the public key? Yes(0)/No(1)");
			int y = jin.nextInt();
			if(y == 0) {
				long time1 = new Date().getTime();
		    		System.out.println("N: " + n);
		    		rsaC x = new rsaC();
		    		x.fermat(n);
		    		long time2 = new Date().getTime();
				long runtime = (time2-time1)/1000;
				System.out.println("Run time: " + runtime);
				System.out.println("How many digits are p/q?");
				size = jin.nextInt();
				int z = (int) (Math.log(10)/ Math.log(2));
				int bits = size * z;
				p = r1; q = r2;
				n = p.multiply(q);
				Phi = p.subtract(BigInteger.valueOf(1));
				Phi = Phi.multiply( q.subtract( BigInteger.valueOf(1)));
				d = e.modInverse(Phi);
				System.out.println("D: " + d);
				System.out.println("N: " + n);
				end ="yes";
			} // end if
		} // end if
		if(key == 1) {
			tempN = fileIn.nextLine();
			System.out.println("n:" + tempN);
			tempD = fileIn.nextLine();
			System.out.println("d:" + tempD);
			tempP = fileIn.nextLine();
			System.out.println("p:" + tempP);
			tempQ = fileIn.nextLine();
			System.out.println("q:" + tempQ);
			n = (new BigInteger(tempN));
			d = (new BigInteger(tempD));
			p = (new BigInteger(tempP));
			q = (new BigInteger(tempQ));
			lengthN = tempN.length();
		} // end if
		System.out.println("Load the other key? Yes(0)/No(1)");
		int again = jin.nextInt();
		if(again == 0){ end = "no"; }
	} while(end=="no"); // end do-while loop
	// Seperates the message into blocks of text if the correct conditions are fulfilled
	block();
}
/** 
 * This method writes the encrypted or decrypted text to a file
 */
public static void store()
throws IOException, FileNotFoundException
{
	if(skip != 'Y') {
		// Stores the message to a file
		System.out.print("Store your message. ");
		System.out.print("Enter the name of your file. ");
		System.out.print("(The format should be /Users/name/~): ");
		Scanner jin= new Scanner(System.in);
		String fileOut = jin.nextLine();
		pout=new PrintWriter(fileOut);
	} // end if
	if(EDchar == 'E' || EDchar == 'e') {
		if(block == 'B') {
			String bigInt = bigIntOut.toString();
			pout.println(bigInt);
			skip = 'Y';
		} // end if
		else {
			String bigInt = bigIntOut.toString();
			pout.println(bigInt);
		} // end else
	}
	if(EDchar == 'D' || EDchar == 'd') {
		if(block == 'B') {
			pout.println(stringOut);
			skip = 'Y';
		} // end if
		else { 
			pout.print(stringOut);
		} // end else
	} // end if
}
/** 
 * This method writes the public and/or private keys to a text file
 */
public static void storeKeys()
throws IOException, FileNotFoundException
{
	String end = "";
	do {
    		end = "yes"; 
    		// Store the key(s)
		System.out.print("Store your key here~ ");
		System.out.print("Enter the name of your file. ");
		System.out.print("(The format should be /Users/name/~): ");
		Scanner jin= new Scanner(System.in);
		String fileOut = jin.nextLine();
		PrintWriter output=new PrintWriter(fileOut);
		System.out.println("Is the key public(0) or private(1)?");
		int key = jin.nextInt();
		String tempN, tempE, tempD, tempP, tempQ = " ";
		if(key == 0) {
			System.out.println("The keys: "); 
			System.out.println("N: " + n);
			System.out.println("E: " + e);
			tempN = n.toString();
			tempE = e.toString();
			output.println(tempN);
			output.println(tempE);
			output.close();
		} // end if
		if(key == 1) {
			System.out.println("The keys: ");
			System.out.println("N: " + n); 
			System.out.println("D: " + d);
			System.out.println("P: " + p);
			System.out.println("Q: " + q);
			tempN = n.toString();
			tempD = d.toString();
			tempP = p.toString();
			tempQ = q.toString();
			output.println(tempN);
			output.println(tempD);
			output.println(tempP);
			output.println(tempQ);
			output.close();
		} // end if
		System.out.println("Store the other key?(Yes(0)/No(1)");
		int again = jin.nextInt();
		if(again == 0) { 
			end = "no";
		} // end if
	} while(end=="no"); // end do-while loop
}
/** 
 * This method calculates: p, q, n, Phi, e, d and then
 * calls the function to store the keys in a text file
 * After calculating n, the block method is called to determine
 * whether or not to separate the text into blocks
 */
public static void generateKeys()
throws IOException, FileNotFoundException
{
	Scanner jin = new Scanner(System.in);
	System.out.println("Enter your desired size for the prime numbers: ");
	size = jin.nextInt();
	// Computes the bit size from the desired digit size
	int x = (int) (Math.log(10)/ Math.log(2));
	int bits = size * x;
	p = BigInteger.probablePrime(bits, random);
	q = BigInteger.probablePrime(bits, random);
	n = p.multiply(q);
	System.out.println("N: " + n);
	String tempN = n.toString();
	lengthN = tempN.length();
	// Computes Phi
	Phi = p.subtract(BigInteger.valueOf(1));
	Phi = Phi.multiply( q.subtract( BigInteger.valueOf(1)));
	// Computes e
	do { 
		e = new BigInteger(2*bits, new Random()); }
	while ((e.compareTo(Phi) != 1) || (e.gcd(Phi).compareTo(BigInteger.valueOf(1)) != 0)); // end do-while loop
	// Computes d
	d = e.modInverse(Phi);
	storeKeys();
	block();
}
/** 
 * Separates the plain or cipher text into blocks if
 * the message length > N
 */
public static void block()
throws IOException, FileNotFoundException
{
	// If the message length > the length of N, then seperate into blocks
	if(lengthN < messageToE.length())
	{
		block = 'B';
		String tempN = n.toString();
		lengthN = tempN.length();
		int sizeBlock = (lengthN-1)/2;
		int numofB = messageToE.length()/sizeBlock;
		if(messageToE.length()%sizeBlock != 0){ numofB++; }
		String currentBlock = "";
		String tempMessage = messageToE;
		for(int i=0;i<numofB; i++) {
			if((i+1) == numofB) { 
				currentBlock=tempMessage.substring((i*sizeBlock)); 
			} // end if
			else {
				int upperLimit = (i+1)*sizeBlock;
				currentBlock = tempMessage.substring((i*sizeBlock), upperLimit);
			} // end else
			messageToE = currentBlock;
			encryptordecrypt();
			store();
		} // end for-loop
	} // end if
}
/**
 * Method to compute the square root of a BigInteger
 * and then produce the next integer
 * Used by the method implementing Fermat's Factorization Method - fermat(N)
 */
public BigInteger calcSQR(BigInteger N)
{
    if(N == BigInteger.ZERO || N == BigInteger.ONE) {
    	return N; 
    } // end if
    BigInteger two = BigInteger.valueOf(2L);
    BigInteger x;
    // Starting with x = N/2 avoids magnitude issues with x squared
    for(x = N.divide(two); x.compareTo(N.divide(x)) > 0; x = ((N.divide(x)).add(x)).divide(two)) {
    	if(N.compareTo(x.multiply(x)) == 0) {
    		return x;
    	} // end if
    	else { 
    		return x.add(BigInteger.ONE); 
    	} // end else
    } // end for-loop
}
/** 
 * Fermat's Factorization Method
 */
public void fermat(BigInteger N)
{
    BigInteger a = calcSQR(N);
    BigInteger b2 = (a.multiply(a).subtract(N));
    while(Square(b2) == false) {
    	a = a.add(BigInteger.valueOf(1));
        b2 = (a.multiply(a).subtract(N));
    } // end while
    r1 = a.subtract(calcSQR(b2));
    r2 = N.divide(r1);
    System.out.println("Roots = ("+ r1 +") , ("+ r2 +")");
}
/** 
 * Method to check if N is a perfect square or not 
 * Used by the method implementing Fermat's Factorization Method - fermat(N)
 */
public boolean Square(BigInteger N)
{
    BigInteger sqRoot = calcSQR(N);
    if(sqRoot.multiply(sqRoot).equals(N)) {
    	return true;
    } // end if
    else {
    	return false;
    } // end else
}
/** 
 * This method acts as the menu for the program
 * The user may start fresh and begin by encrypting and go from there
 * or he/she may load known data and encrypt/decrypt
 */
public static void menu()
throws IOException, FileNotFoundException
{
	System.out.println("Create(0) or Load Keys(1)");
	Scanner jin= new Scanner(System.in);
	int choice = jin.nextInt();
	if(choice == 0) {
		generateKeys();
		if(block != 'B')
		{ encryptordecrypt(); }
	} // end if
	if(choice == 1) {
		readInKeys();
		if(block != 'B') { 
			encryptordecrypt(); 
		} // end if
	} // end if
}
/** 
 * This method performs the encryption or decryption algorithm on the given text
 */
public static void encryptordecrypt()
throws IOException, FileNotFoundException
{
	BigInteger plaintextBig, ciphertextBig;
	if(EDchar == 'E' || EDchar == 'e') {
		plaintextBig = new BigInteger(messageToE, 36);
		ciphertextBig = plaintextBig.modPow(e,n);
		System.out.println("Ciphertext: " + 
		ciphertextBig.toString());
		bigIntOut = ciphertextBig;
	} // end if
	if(EDchar == 'D' || EDchar == 'd') {
		plaintextBig = messageToD.modPow(d,n);
		System.out.println("Plaintext After Decryption: " + plaintextBig.toString(36));
		stringOut = plaintextBig.toString(36);
		if(block == 'B') { 
			store();
		} // end if
	} // end if
}
/** 
 * Main Method
 */
public static void main(String[] args) 
throws IOException, FileNotFoundException
{
	readIn();
	if(skip2 != 'Y') { 
		menu();
	}
	if(block != 'B') { 
		store();
	}
	pout.close();
}
} // end Class rsaC
