package cuni.software;

import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class Parser {
	
	Set<String> articleTerms = new HashSet<>();
	
	/**
	 * This method retrieves terms from the articles found in the provided link
	 * @param link is a URL link to the atricle.
	 * @return set of terms. 
	 */
	public Set<String> parseLink(String link){
		try {
			Document doc = Jsoup.connect(link).get();
			Elements elements = doc.getElementsByTag("p");
			for(Element e: elements){ 
				if (e.attributes().size() == 0){ //actual content doesn't have any atts in <p> tags
					retrieveItems(e);
				}
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
		return articleTerms;
	}
	
	private String formatWord(String word){
		word.trim();
		word = word.toLowerCase();
		
		int position = -1;
		for (int i = word.length() - 1; i >= 0; i--){
			if (Character.isLetterOrDigit((int)word.charAt(i))){ //resolves all Unicode letters thanks to (int) 
				position = i;
				break;
			}
		}
		if (position < word.length() - 1){
			word = word.substring(0, position + 1);
		}
		
		position = -1;
		
		for(int i = 0; i < word.length(); i++){
			if (Character.isLetterOrDigit((int)word.charAt(i))){ 
				position = i;
				break;
			}
		}
		if (position > 0){
			word = word.substring(position);
		}
		
		return word;
	}
	
	private void retrieveItems(Element e){
		String words[] = e.text().split(" ");
		for(String word : words){		
			word = formatWord(word);
			if (word.length() > 2){
				articleTerms.add(word);
			}		
		}	
	}
}
