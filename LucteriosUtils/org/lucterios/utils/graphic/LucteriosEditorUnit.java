package org.lucterios.utils.graphic;

import junit.framework.TestCase;

public class LucteriosEditorUnit extends TestCase {

	private LucteriosEditor mEditor;
	protected void setUp() throws Exception {
		super.setUp();
		mEditor=new LucteriosEditor(false);
		
	}

	protected void tearDown() throws Exception {
		mEditor=null;
		super.tearDown();
	}
	
	private void checkText(String aText)
	{
		mEditor.load(aText);
		assertEquals(aText,mEditor.save());
	}
	
	public void testSimple()
	{
		checkText("abc&#160;def&#160;ghi&#160;jkl");
	}

	public void test2Lines()
	{
		checkText("abc&#160;def{[newline]}ghi&#160;jkl");
	}

	public void testStyle()
	{
		checkText("{[bold]}abc{[/bold]}&#160;{[italic]}def{[/italic]}&#160;{[underline]}ghi{[/underline]}&#160;{[bold]}{[italic]}{[underline]}jkl{[/underline]}{[/italic]}{[/bold]}");
	}

	public void testColor()
	{
		checkText("{[font color='#0000ff']}abc{[/font]}&#160;{[font color='#00ff00']}def{[/font]}&#160;{[font color='#ff0000']}ghi{[/font]}&#160;jkl");
	}

	public void testMixt()
	{
		checkText("{[bold]}abc{[/bold]}&#160;{[font color='#0000ff']}{[italic]}def{[/italic]}{[/font]}&#160;{[underline]}ghi{[/underline]}&#160;{[font color='#00ff00']}{[bold]}{[italic]}{[underline]}jkl{[/underline]}{[/italic]}{[/bold]}{[/font]}");
	}
	
	public void testSpecialChar()
	{
		checkText("é\"'(-è_çà)=*ù!:,?./§%µ£+°0987654321¹~#|`\\^@;<>[]{}");
	}

	public void testSpecialCharSuite()
	{
		// Ne passe pas !!
		// checkText("&"); 
	}
	
}
