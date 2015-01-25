import java.util.*;
import java.math.*;
/**
 * Short program to factor large prime integers using Fermat's Factorization Method'
 * @author Chris McDonald
 */
public class fer
{
	/**
	 * Method to calculate the square root of a Big Integer
	 * and then produce the next integer
	 */
	public static BigInteger calcSQR(BigInteger N)
	{
	    if(N == BigInteger.ZERO || N == BigInteger.ONE) { return N; }
	    BigInteger two = BigInteger.valueOf(2L);
	    BigInteger x;
	    // starting with x = N / 2 avoids magnitude issues with x squared
	    for(x = N.divide(two);
	        x.compareTo(N.divide(x)) > 0;
	        x = ((N.divide(x)).add(x)).divide(two));
	    if(N.compareTo(x.multiply(x)) == 0) {return x;} 
	    else{ return x.add(BigInteger.ONE); }
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
        	} // end while loop
        	BigInteger r1 = a.subtract(calcSQR(b2));
        	BigInteger r2 = N.divide(r1);
        	display(r1, r2);
    	}
    	/** 
     	* Method to display the roots of N
     	*/
    	public void display(BigInteger r1, BigInteger r2)
    	{
        	System.out.println("Roots = ("+ r1 +") , ("+ r2 +")");
    	}
    	/** 
     	* Method to check if N is a perfect square or not 
     	*/
    	public boolean Square(BigInteger N)
    	{
        	BigInteger sqRoot = calcSQR(N);
        	if(sqRoot.multiply(sqRoot).equals(N)) {
        		return true;
        	} // end if
        	else {return false;}
    	}
    	/** 
     	* Main Method
     	*/
    	public static void main(String[] args) 
    	{
    		long time1 = new Date().getTime();
    		// Input the prime integer to factor below in place of 30951
        	BigInteger N = new BigInteger("30951");
        	System.out.println("N: " + N);
        	fer f = new fer();
        	f.fermat(N);
        	long time2 = new Date().getTime();
    		long runtime = (time2-time1)/1000;
    		// Displays the run time of the algorithm
    		System.out.println("Run time: " + runtime);
    	}
}
