package harlequinmettle.interfaces;
public interface ICucumberGrammar {
	// NONE
	// Scenario: Calabash swipeing
	String[] LEFT = { "Then I swipe left" };
	String[] RIGHT = { "Then I swipe right" };
	String[] DOWN = { "Then I scroll down" };
	String[] UP = { "Then I scroll up" };
	String[] DRAGLEFT = { "Then I drag from 10:50 to 90:50 moving with 50 steps" };
	String[] DRAGRIGHT = { "Then I drag from 90:50 to 10:50 moving with 50 steps" };
	// Then I select "text" from the menu
	
	// Scenario: Calabash controlls
	String[] BACK = { "Then I go back" };
	String[] MENU = { "Then I press the menu key" };
	String[] ENTERKEY = { "Then I press the enter key" };

	// Scenario: Rotation
	//String[] LANDSCAPE = { "Then I rotate the device to landscape" };
	//String[] PORTRAIT = { "Then I rotate the device to portrait" };
	// 1 AT END
	// Scenario: I can see text on intro
	String[] SEEONLY = { "Then I see \"","\"" };// +"TEXT"

	String[] SEEATLEAST = { "Then I should see text containing \"","\"" };// +"TEXT"

	// Scenario: Calabash negative assertions
	String[] DONTSEE = { "Then I don't see \"","\"" };// +"TEXT"

	// Scenario: Calabash press text

	String[] LONGPRESSTEXT = { "Then I long press \"","\"" };// +"TEXT"
	// Then I press button number 1

	String[] PRESSID = { "Then I long press view with id \"","\"" };// +"ID"
	String[] PRESSTEXT = { "Then I press \"","\"" };// +"TEXT"//general
	// String[] TOUCHTEXT = { "Then I press the text " };// +"TEXT"
	// I press list item number 2
	// Then I long press list item number 3

	// 2 INBETWEEN
	String[] TOUCHSCREEN = { "Then I click on screen ", "% from the left and ",
			"% from the top" };
	// Given I press the "text" button

	// Scenario: Calabash waiting
	// Then I wait for dialog to close
	// Then I wait to see "text"
	// Then I wait up to 10 seconds for "text" to appear
	// 2 ALTERNATING
	String[] ENTERTEXT = { "Then I enter text \"","\" into field with id \"","\"" };
	String[] WAITFORTEXT = { "Then I wait up to ", " seconds to see \"","\"" };
	// Then I wait for the "text" button to appear
	// Then I wait for the "text" screen to appear
	// 1 IN MIDDLE
	String[] WAITFORID = { "Then I wait for the view with id \"","\" to appear" };
	// Then I wait up to 10 seconds for the "text" screen to appear
	// Then I wait upto 10 seconds for the "text" screen to appear
	// Then I wait for a second
	// Then I wait for 1 second
	// Then I wait
	String[] WAIT = { "Then I wait for ", " seconds" };

	// Scenario: Enter text
	// Given I set the "text" date to "11-11-1111"
	// Then I enter "text" into input field number 0
	// Then I enter "text" as "text"
	// String[] ENTER = {"Then I enter "text" into "text"};//INTO
	// CONTENTDESCRIPTION
	// Then I clear input field number 10
	// Then I clear "text"
	// Then I clear input field with id "field_id"
	String[] FROMSPINNER = { "Then I select \"","\" from \"","\"" };// TEXT FROM SPINNER
															// CONTENTDESCRIPTION

	// Then I compare the current screen with the reference image
	// "features/ref1.png" manually

	// 4 INBETWEEN
	String[] DRAG = { "Then I drag from ", ":", " to ", ":",
			" moving with 50 steps" };
	String[][] SINGLE_STATEMENT = {

	};
	String[][] COMMON_STEPS = { SEEONLY, SEEATLEAST, DONTSEE, LONGPRESSTEXT,
			PRESSID, PRESSTEXT, TOUCHSCREEN, WAITFORTEXT, WAITFORID, WAIT,
			LEFT, RIGHT, DOWN, UP, DRAGLEFT, DRAGRIGHT, DRAG, BACK, MENU,
			ENTERKEY, ENTERTEXT, FROMSPINNER 

	};
}
