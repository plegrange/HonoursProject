package Defragmentation;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by FuBaR on 7/12/2016.
 */
public final class IdentifierGenerator {
    private SecureRandom random = new SecureRandom();

    public String nextID(){
        return new BigInteger(100, random).toString(16);
    }
}
