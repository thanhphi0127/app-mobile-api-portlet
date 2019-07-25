/**
 * Author: PhiNT
 * Create date: 01/07/2019
 */
package vn.vnpt.mobile.api;

public class AuthUtil {
	
	public static boolean hasAccess(String username, String password){
		return ( username.equals("kgg") && password.equals("admin@123") );
	}
}
