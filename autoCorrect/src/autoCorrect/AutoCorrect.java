package autoCorrect;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;

import javax.swing.JFileChooser;

/**
 * This class implements the idea of the auto-correct. It depends on the
 * words.txt file as a reference for legal words allowed. So that the program
 * works the words.txt file is supposed to be in the same folder of the 
 * program. It requires the user to choose the path of the file to check.
 * This happens through a GUI window.
 * 
 *some of the code was made based on the UOPeople suggestion in Lap 9.
 *
 ******************************************************
 *    Title: Lab 9: Sets in the Java Collection Framework
 *    Author: University of The People
 *    Date: 12/13/2021
 *    Availability: https://my.uopeople.edu/mod/page/view.php?id=268268&inpopup=1
 ******************************************************
 *
 */
public class AutoCorrect { // AutoCorrect begin

	public static void main(String[] args) { // main() begin

		HashSet<String> words = new HashSet<>(); // Set of allowed words.

		try {

			Scanner filein = new Scanner(new File("words.txt"));

			while(filein.hasNext()) {
				String tk = filein.next();
				words.add(tk.toLowerCase()); // makes sure it's lowercased	
			}

			Scanner chosenFile = new Scanner(getInputFileNameFromUser());

			chosenFile.useDelimiter("[^a-zA-Z]+"); // skipping non letter chars

			while(chosenFile.hasNext()) {

				String tk2 = chosenFile.next();

				if(!words.contains(tk2.toLowerCase())) { // token isn't allowed

					// printing the word with the suggestions.
					System.out.println(tk2 + ": "+ corrections(tk2, words));
				}
			}

		} catch (FileNotFoundException e) { // file doesn't exist
			e.printStackTrace();
		}

	} // main() end


	/**
	 * The method asks the user for the file to check using GUI.
	 * 
	 * The implementation was made by the UOPeople
	 *******************************************************
	 *    Title: Lab 9: Sets in the Java Collection Framework
	 *    Author: University of The People
	 *    Date: 12/13/2021
	 *    Availability: https://my.uopeople.edu/mod/page/view.php?id=268268&inpopup=1
	 ******************************************************
	 *
	 * @return File chosen from the user.
	 */
	static File getInputFileNameFromUser() { // getInputFileNameFromUser() begin

		JFileChooser fileDialog = new JFileChooser();
		fileDialog.setDialogTitle("Select File for Input");
		int option = fileDialog.showOpenDialog(null);

		if(option != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		else {
			return fileDialog.getSelectedFile();
		}

	} // getInputFileNameFromUser() end


	/**
	 * This method takes a bad word and a reference base to return possible
	 * suggestions for the possible right words instead.
	 * 
	 * @param badWord to be compared with reference's word
	 * @param dictionary the reference as a base for allowed words
	 * @return TreeSet with the suggestions of words.
	 */
	static TreeSet corrections(String badWord, HashSet dictionary) { // corrections() begin
		TreeSet<String> missSpelled = new TreeSet<>(); // the set with the suggestions

		// checking if removing a char would match any allowed word
		for(int i = 0; i < badWord.length(); i++) {
			String suggestion = badWord.substring(0,i) + badWord.substring(i+1);
			if(dictionary.contains(suggestion)) {
				missSpelled.add(suggestion);
			}	
		}

		// checking if changing any char at any position would match any allowed word
		for(int i = 0; i < badWord.length(); i++) {
			for(char ch = 'a'; ch <= 'z'; ch++) {

				String suggestion = badWord.substring(0,i) + ch + badWord.substring(i+1);

				if(dictionary.contains(suggestion)) {
					missSpelled.add(suggestion);
				}
			}	
		}


		// checking if inserting any char at any position would match any allowed word
		for(int i = 0; i < badWord.length(); i++) {
			for(char ch = 'a'; ch <='z'; ch++) {

				String suggestion = badWord.substring(0,i) + ch + badWord.substring(i);

				if(dictionary.contains(suggestion)) {
					missSpelled.add(suggestion);
				}
			}
		}


		// checking if swapping two neighboring chars at any position would match any allowed word
		for(int i = 0; i < badWord.length()-1; i++) {

			String suggestion = badWord.substring(0,i) + badWord.charAt(i+1) + 
					badWord.charAt(i) + badWord.substring(i+2);

			if(dictionary.contains(suggestion)) {
				missSpelled.add(suggestion);
			}	
		}


		// checking if both split substrings at any position match any of the allowed words
		// the return is the 1stSubstring + " " + 2ndSubstring
		for(int i = 0; i < badWord.length(); i++) {

			String suggestion1 = badWord.substring(0,i);
			String suggestion2 = badWord.substring(i);

			if(dictionary.contains(suggestion1) && dictionary.contains(suggestion2)) {
				missSpelled.add(suggestion1 + " " + suggestion2);
			}	
		}


		// no suggestion was found
		if(missSpelled.isEmpty()) {
			missSpelled.add("(no suggestions)");
		}
		return missSpelled;
	} // corrections() end 

} // AutoCorrect end
