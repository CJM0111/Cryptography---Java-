import java.util.*;
import java.math.*;
/**
 * Brute-force approach to the Discrete Log Problem
 * @author Chris McDonald
 */ 
public class log
{
	public static void main(String [] args)
	{
		BigInteger x = BigInteger.valueOf(1);
		BigInteger y = BigInteger.valueOf(2);
		BigInteger z = BigInteger.ZERO;
		BigInteger i = BigInteger.ZERO;
		BigInteger temp = BigInteger.valueOf(2);
		do {
			// Raise 2 to an exponent with a Big Integer value
			for(;i.compareTo(x) <= 0;i = i.add(BigInteger.ONE)) {
				if(i.equals(BigInteger.valueOf(1))){}
				else{y= y.multiply(temp);}
			} // end for loop
			i = BigInteger.ONE;
			z = y.mod(BigInteger.valueOf(61));
			if(z.equals(BigInteger.valueOf(3))){}
			else{x = x.add(BigInteger.valueOf(1)); y = BigInteger.valueOf(2);}
		}while(!z.equals(BigInteger.valueOf(3))); // end do-while loop
		System.out.println("For 2^x = 3 mod 61, where y is 2^x and z is y mod 61:");
		System.out.println("x: " + x);
		System.out.println("y: " + y);
		System.out.println("z: " + z);
	}
}
