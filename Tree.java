package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file. The root of the 
	 * tree is stored in the root field.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		
		//First check if HTML file is empty
		if(sc == null){
			return;
		}
		
		root = buildTag(); 
	}
	
	private TagNode buildTag() {
		
		String currentLine = null; // Create current line String 
		
		
			// Error checks
		if(sc.hasNextLine()){
			currentLine = sc.nextLine();
		}
		if(sc.hasNextLine() == false){
			return null;
		}
		
		//now check for tags
		if(currentLine.contains("<") && currentLine.contains("/")){
			return null;
		}
		
		// since it has not returned by now we create a new tagNode
		TagNode newBuild = new TagNode(currentLine, null, null);
		
		// check to make sure there is not "/" and do recursion
		if(currentLine.contains("<") && !currentLine.contains("/")){
			
			newBuild.firstChild=buildTag();
			newBuild.sibling=buildTag();
			
		}
		
		return newBuild;
	}

	
	
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		
		// initial error check
		if(oldTag == null || newTag == null || root == null){
			return;
		}
		
		
		replaceTagPriv(oldTag, newTag, root);
		
	}
	
	private void replaceTagPriv(String tagOld, String tagNew, TagNode tags){
		//again initial error checks and initial scenario
		if(tags == null){
			return;
		}
		//now we see if tags are the same then do recursion
		if(tags.tag.compareTo(tagOld) == 0){
			tags.tag = tagNew;
		}
		
		replaceTagPriv(tagOld, tagNew, tags.firstChild);
		replaceTagPriv(tagOld, tagNew, tags.sibling);
	}
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		
		// error checks
		if(row <= 0){
			return;
		}
		
		boldRowPriv(row, root);
		
	}
	
	private void boldRowPriv(int row, TagNode root2){
		//again, error checks
		if(root2 == null){
			return;
		}
		
		//implement recursion
		if (root2.tag.equals("table")){
			TagNode rfc = root2.firstChild;
			
			for (int i=1; i<row; i++){
				rfc = rfc.sibling;
			}
			
			TagNode s = rfc.firstChild;
			TagNode b = new TagNode("b", s.firstChild, null);
			s.firstChild = b;
			
			while (s.sibling != null){
				s = s.sibling;
				b = new TagNode("b", s.firstChild, null);
				s.firstChild = b;
			}
		}
		else { 
			boldRowPriv(row, root2.firstChild);
			boldRowPriv(row, root2.sibling);
		}
	}
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		
		removeTagPriv(tag, root);
		
		if (tag.equals("ol") || tag.equals("ul")){
			changeToP(root); // For ol or ul
		}
		
	}
	
	// Method for ol or ul to change li to p
	private static void changeToP(TagNode root){
		if (root == null || root.firstChild == null){
			return;
		}
		if (root.firstChild.tag.equals("li") && !(root.tag.equals("ol") || root.tag.equals("ul"))){

			root.firstChild.tag = "p";
		}
		else {
			changeToP(root.firstChild);
			changeToP(root.sibling);
		}
	}
	
	//classic plethora of if/then statements 
	private static void removeTagPriv(String tag, TagNode root){
		if (root == null || (root.firstChild == null && root.sibling == null)){
			return;
		}
		if ((root.firstChild != null) && (root.firstChild.firstChild != null) && root.firstChild.tag.equals(tag)){
			if (tag.equals("ol") || tag.equals("ul")){
				if (root.firstChild.firstChild.sibling != null){

					TagNode temp = root.firstChild.firstChild.sibling;
					if (temp.tag.equals("li")){
						temp.tag = "p";
					}
					while (temp.sibling != null) {
						temp = temp.sibling;
						if (temp.tag.equals("li")){
							temp.tag = "p";
						}
					}
					temp.sibling = root.firstChild.sibling;
					TagNode temp2 = root.firstChild.firstChild;
					if (temp2.tag == "li"){
						temp2.tag = "p";
					}
					root.firstChild = temp2;
				}
				else {
					TagNode temp3 = root.firstChild.firstChild;
					if (temp3.tag.equals("li")){
						temp3.tag = "p";		
					}
					temp3.sibling = root.firstChild.sibling;
					root.firstChild = temp3;
				}	
				
				removeTagPriv(tag, root);
			}
			
			if (tag.equals("p") || tag.equals("em") || tag.equals("b")){
				if (root.firstChild.firstChild.sibling != null) {
					TagNode temp = root.firstChild.firstChild.sibling;
					while (temp.sibling != null) {
						temp = temp.sibling;
					}
					temp.sibling = root.firstChild.sibling;
					TagNode temp2 = root.firstChild.firstChild;
					root.firstChild = temp2;
				}
				else {
					TagNode temp3 = root.firstChild.firstChild;
					temp3.sibling = root.firstChild.sibling;
					root.firstChild = temp3;
				}
				
				removeTagPriv(tag, root);
			}
		}
		if ((root.sibling != null) && root.sibling.tag.equals(tag)) { 
			if (tag.equals("ol") || tag.equals("ul")){
				if (root.sibling.firstChild.sibling != null){
					TagNode temp = root.sibling.firstChild.sibling;
					if (temp.tag.equals("li")){
						temp.tag = "p";
					}
					while (temp.sibling != null) {
						temp = temp.sibling;
						if (temp.tag.equals("li")){
							temp.tag = "p";
						}
					}
					temp.sibling = root.sibling.sibling;
					if (root.sibling.firstChild.tag.equals("li")) {
						root.sibling.firstChild.tag = "p";
					}
					TagNode temp2 = root.sibling.firstChild;
					if (temp2.tag == "li"){
						temp2.tag = "p";
					}
					root.sibling = temp2;
				}
				else {
					TagNode temp3 = root.sibling.firstChild;
					temp3.sibling = root.firstChild.sibling;
					root.sibling = temp3;
				}
				
				removeTagPriv(tag, root);
			}
			
			if (tag.equals("p") || tag.equals("em") || tag.equals("b")){
				if (root.sibling.firstChild.sibling != null) {
					TagNode temp = root.sibling.firstChild.sibling;
					while (temp.sibling != null) {
						temp = temp.sibling;
					}
					temp.sibling = root.sibling.sibling;
					TagNode temp2 = root.sibling.firstChild;
					root.sibling = temp2;
				}
				else {
					TagNode temp3 = root.sibling.firstChild;
					temp3.sibling = root.sibling.sibling;
					root.sibling = temp3;
				}
				removeTagPriv(tag, root);
			}
		}
		else {
			removeTagPriv(tag, root.firstChild);
			removeTagPriv(tag, root.sibling);
		}
		return;
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		
		if (tag.equals("em") || tag.equals("b")){
			addTagPriv(word.toLowerCase(),tag,root);
		}
		return;
	}
	
	private static void addTagPriv(String word, String tag, TagNode root){
		
		if (root == null || (root.firstChild == null && root.sibling == null)){
			return;
		}
		
		else if (root.firstChild != null && root.firstChild.firstChild == null && root.firstChild.tag.toLowerCase().contains(word)){
			if (root.firstChild.tag.equalsIgnoreCase(word)) { 
				TagNode tagGiven = new TagNode(tag, root.firstChild, root.firstChild.sibling);
				root.firstChild = tagGiven;
			}
			else {
				
				int wordIndex = root.firstChild.tag.indexOf(word);

				if (wordIndex < 0){
					return;
				}

				else {
					int len = word.length();
					while (root.firstChild.tag.charAt(wordIndex + len) == '!' || root.firstChild.tag.charAt(wordIndex + len) == '.' 
							|| root.firstChild.tag.charAt(wordIndex + len) == ',' || root.firstChild.tag.charAt(wordIndex + len) == '?' || 
							root.firstChild.tag.charAt(wordIndex + len) == ';' || root.firstChild.tag.charAt(wordIndex + len) == ':'
							){
						len++;
					}					
					if (wordIndex == 0){
						TagNode l = new TagNode(root.firstChild.tag.substring(0, len), root.firstChild.firstChild, null);
						TagNode r = new TagNode(root.firstChild.tag.substring(len), null, root.firstChild.sibling);
						TagNode tagGiven = new TagNode(tag, l, r);
						root.firstChild = tagGiven;
						
					}
					else if (wordIndex > 0 && wordIndex+len < root.firstChild.tag.length() - 1){ 
						TagNode ft = new TagNode(root.firstChild.tag.substring(0, wordIndex), null, null);
						TagNode st = new TagNode(root.firstChild.tag.substring(wordIndex, wordIndex+len), null, null); 
						TagNode fnt = new TagNode(root.firstChild.tag.substring(wordIndex+len), null, root.firstChild.sibling);
						TagNode tagGiven = new TagNode(tag, st, fnt);
						ft.sibling = tagGiven;
						root.firstChild = tagGiven;
					}
					else {
						if (root.firstChild.tag.length() == wordIndex+len){
							TagNode f = new TagNode(root.firstChild.tag.substring(0, wordIndex), null, null);
							TagNode s = new TagNode(root.firstChild.tag.substring(wordIndex), null, null); 
							TagNode tagGiven = new TagNode(tag, s, root.firstChild.sibling);
							f.sibling = tagGiven;
							root.firstChild = tagGiven;
						}
					}
					
				}
			}
		}
		
		else if (root.sibling != null && root.sibling.firstChild == null && root.sibling.tag.toLowerCase().equalsIgnoreCase(word)){
			
			if (root.sibling.tag.equalsIgnoreCase(word)) { 
				TagNode tagGiven = new TagNode(tag, root.sibling, root.sibling.sibling);
				root.sibling = tagGiven;
			}
			else {
				int wordIndex = root.sibling.tag.indexOf(word);

				if (wordIndex < 0){
					return;
				}

				else {
					int len = word.length();
					while (root.sibling.tag.charAt(wordIndex + len) == '!' || root.sibling.tag.charAt(wordIndex + len) == '.' 
							|| root.sibling.tag.charAt(wordIndex + len) == ',' || root.sibling.tag.charAt(wordIndex + len) == '?' || 
							root.sibling.tag.charAt(wordIndex + len) == ';' || root.sibling.tag.charAt(wordIndex + len) == ':'
							){
						len++;
					}					
					if (wordIndex == 0){
						TagNode l = new TagNode(root.sibling.tag.substring(0, len), root.sibling.firstChild, null);
						TagNode r = new TagNode(root.sibling.tag.substring(len), null, root.sibling.sibling);
						TagNode tagGiven = new TagNode(tag, l, r);
						root.sibling = tagGiven;
						
					}
					else if (wordIndex > 0 && wordIndex+len < root.sibling.tag.length() - 1){ 
						TagNode ft = new TagNode(root.sibling.tag.substring(0, wordIndex), null, null);
						TagNode st = new TagNode(root.sibling.tag.substring(wordIndex, wordIndex+len), null, null); 
						TagNode fnt = new TagNode(root.sibling.tag.substring(wordIndex+len), null, root.sibling.sibling);
						TagNode tagGiven = new TagNode(tag, st, fnt);
						ft.sibling = tagGiven;
						root.sibling = tagGiven;
					}
					else {
						if (root.sibling.tag.length() == wordIndex+len){
							TagNode f = new TagNode(root.sibling.tag.substring(0, wordIndex), null, null);
							TagNode s = new TagNode(root.sibling.tag.substring(wordIndex), null, null); 
							TagNode tagGiven = new TagNode(tag, s, root.sibling.sibling);
							f.sibling = tagGiven;
							root.sibling = tagGiven;
						}
					}

				}
			}
		}
		
		else{
			addTagPriv(word, tag, root.firstChild);
			addTagPriv(word, tag, root.sibling);
		}
		return;
	}
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
}
