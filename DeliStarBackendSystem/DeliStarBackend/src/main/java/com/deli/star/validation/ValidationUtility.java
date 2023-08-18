package com.deli.star.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtility {
	public static String validateEmail(String email) {
		email = email.trim();
		if(email.length() > 70) {
			return null;
		}
		
		// Email Pattern validation.
		Pattern emailPattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher emailMatcher = emailPattern.matcher(email);

		boolean isValid = emailMatcher.find();
		System.out.println("Email ? is: ".replace("?", email) + isValid);

		if(isValid) {
			return email;
		}

		return null;
	}

	public static String validateGender(String gender) {
		gender = gender.trim();
		gender = gender.toLowerCase();

		// Gender Pattern validation.
		Pattern genderPattern = Pattern.compile("female|male");
		Matcher genderMatcher = genderPattern.matcher(gender);

		boolean isValid = genderMatcher.find();
		System.out.println("Gender ? is:".replace("?", gender) +  isValid);

		if(isValid) {
			gender = (char)((int)gender.charAt(0) - 32) + gender.substring(1);
			System.out.println("edited Gender: " + gender);

			return gender;
		}

		return null;
	}

	public static String validatePhoneNo(String phoneNo) {
		// Phone No Pattern validation.

		phoneNo = phoneNo.trim();
		String processedPhoneNo="";
		for(int i =0; i< phoneNo.length(); i++) {
			if((int)phoneNo.charAt(i) >= 48 && (int)phoneNo.charAt(i) <= 57 || phoneNo.charAt(i) == '+') {
				processedPhoneNo += phoneNo.charAt(i);
			}
		}
		if(processedPhoneNo.length() < 7 || processedPhoneNo.length() > 15) {
			return null;
		}
		Pattern phoneNoPattern = Pattern.compile("^[+][0-9]+$");

		Matcher phoneNoMatcher = phoneNoPattern.matcher(processedPhoneNo);
		boolean isValid = phoneNoMatcher.find();
		System.out.println("Phone No ? is:".replace("?", phoneNo) +  isValid);

		if(isValid) {
			return processedPhoneNo;
		}

		return null;
	}

	public static String validateName(String name) {
		// Name Pattern validation.
		name = name.trim();
		Pattern namePattern = Pattern.compile("^[a-z A-Z]+$");


		Matcher nameMatcher  = namePattern.matcher(name);
		boolean isValid = nameMatcher.find();

		System.out.println("name ? is:".replace("?", name) +  isValid);
		System.out.println("value of find() ::: "+ isValid);

		if(isValid && name.length() <= 25) {
			return name;
		}

		return null;
	}
	
	public static boolean validatePassword(String password) {
		// Password Length Validation.

		if(password.length() < 8 && password.length() > 20) {
			return false;
		}

		return true;
	}
}
