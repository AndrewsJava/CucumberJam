package harlequinmettle.interfaces;

public interface IPrefiller {

	String[] PREFILLS = {

	"I see \"TEXT\"",// ////
			"I don't see \"TEXT\"",// ////
			"I set the date to \"(\\d\\d-\\d\\d-\\d\\d\\d\\d)\" on DatePicker with index \"TEXT\"",// ////
			"I set the time to \"(\\d\\d:\\d\\d)\" on TimePicker with index \"TEXT\"",// ////
			"I enter text \"TEXT\" into field with id \"ID\"",// ////
			"I clear input field with id \"ID\"",// ////
			"I go back",// ////
			"I press the enter button",// ////
			"I swipe left",// ////
			"I swipe right",// ////
			"I scroll down",// ////
			"I scroll up",// ////
			"I drag from (\\d+):(\\d+) to (\\d+):(\\d+) moving with (\\d+) steps",// ////
			"I press the \"TEXT\" button",// ////
			"I long press view with id \"ID\"",// ////
			"I touch the \"TEXT\" text",// ////
			"I click on screen (\\d+)% from the left and (\\d+)% from the top",// ////
			"I wait up to (\\d+) seconds to see \"TEXT\"",// ////
			"I wait for the view with id \"ID\" to appear",// ////
			"I wait for (\\d+) seconds"

	};

}
