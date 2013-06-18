/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.service.security;

import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.exceptions.CSException;

/** 
 * The LoginService authenticates users into the calab system.
 * 
 * @author      doswellj
 * @param applicationName sets the application name for use by CSM
 * @param am  Authentication Manager for CSM.
 */

public class LoginService 
{

	String applicationName = null;
	AuthenticationManager am = null;
	
// TODO Make a singleton 

	
	/**
	 * LoginService Constructor
	 * @param strname name of the application
	 */

	public LoginService(String strname) throws Exception 
	{
		this.applicationName = strname;
		am = SecurityServiceProvider.getAuthenticationManager(this.applicationName);
        //TODO Add Role implementation
	 }
	
	/**
	 * The login method uses CSM to authenticated the user with LoginId and Password credentials 
	 * @param	strusername  LoginId of the user
	 * @param 	strpassword  Encrypted password of the user
	 * @return	boolean identicating whether the user successfully authenticated
	 */
	public boolean login(String strUsername, String strPassword ) throws CSException 
	{
		return am.login( strUsername,strPassword);
	}	
}
