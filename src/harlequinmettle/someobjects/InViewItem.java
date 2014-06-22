package harlequinmettle.someobjects;

public class InViewItem {
	public static int defaultWait = 25;
	public int waitTime = defaultWait;
	String id = "";
	String text = "";
	String contentDescription;
	String className = "X";
	boolean enabled;

	public InViewItem() {

	}

	public InViewItem(InViewItem calabashItem) {
		this.id = calabashItem.id;
		this.text = calabashItem.text;
		this.className = calabashItem.className;
	}

	public static final String assertText = "I see the text \"TEXT_OR_ID\"";
	public static final String assertTextCode = "performAction('assert_text',text, true)";
	public static final String touchText = "I touch the \"TEXT_OR_ID\" text";
	public static final String touchTextCode = "performAction('click_on_text',text)";

	public static final String waitForId = "I wait for the view with id \"TEXT_OR_ID\" to appear";
	public static final String waitForIdCode = "performAction('wait_for_view_by_id', text)";
	public static final String pressId = "I long press view with id \"TEXT_OR_ID\" ";
	public static final String pressIdCode = "performAction('click_on_view_by_id',view_id)";

	public static final String antiAssertText = "I should not see \"TEXT_OR_ID\" ";
	public static final String antiAssertTextCode = "performAction('assert_text', text, false)";
	public static final String longPressText = "I long press \"TEXT_OR_ID\"";
	public static final String longPressTextCode = "performAction('press_long_on_text', text_to_press)";

	public static final String waitForText = "I wait up to 30 seconds for \"TEXT_OR_ID\" to appear ";
	public static final String waitForTextCode = "performAction('wait_for_text', text, timeout)";

	public static final String clearId = "I clear input field with id \"TEXT_OR_ID\" ";
	public static final String enterId = "I enter text \"TEXT_OR_ID\" into field with id \"TEXT_OR_ID\"";
	public static final String clearIdCode = "performAction('clear_id_field', view_id)";
	public static final String enterIdCode = "performAction('enter_text_into_id_field', text, view_id) ";
	public static final String[] TITLES = {//
	assertText,//
			assertTextCode,//
			touchText,//
			touchTextCode,//
			waitForId,//
			waitForIdCode,//
			pressId,//
			pressIdCode,//
			antiAssertText,//
			antiAssertTextCode,//
			longPressText,//
			longPressTextCode,//
			waitForText,//
			waitForTextCode,//
			//clearId,//
			enterId,//
			//clearIdCode,//
			enterIdCode,//

	};

	public String genericGetText(String chooser) {
		if(chooser.equals(assertText)) {
			return getTextAssertion();
		} else if(chooser.equals(assertTextCode)) {
			return getTextAssertionCode();
		} else if(chooser.equals(touchText)) {
			return getTextTouch();
		} else if(chooser.equals(touchTextCode)) {
			return getTextTouchCode();
		} else if(chooser.equals(waitForId)) {
			return getIdAssertion();
		} else if(chooser.equals(waitForIdCode)) {
			return getIdAssertionCode();
		} else if(chooser.equals(pressId)) {
			return getIdPress();
		} else if(chooser.equals(pressIdCode)) {
			return getIdPressCode();
		} else if(chooser.equals(antiAssertText)) {
			return getAntiTextAssertion();
		} else if(chooser.equals(antiAssertTextCode)) {
			return getAntiTextAssertionCode();
		} else if(chooser.equals(longPressText)) {
			return getLongPressText();
		} else if(chooser.equals(longPressTextCode)) {
			return getLongPressTextCode();
		} else if(chooser.equals(waitForText)) {
			return getWaitForText();
		} else if(chooser.equals(waitForTextCode)) {
			return getWaitForTextCode();
		} else if(chooser.equals(enterId)) {
			return getEnterText();
		} else if(chooser.equals(enterIdCode)) {
			return getEnterTextCode();

		}
		return "#";
	}

	// /////Then /^I see the text "([^\"]*)"$/
	// performAction('assert_text',text, true)
	// /////Then /^I touch the "([^\"]*)" text$/
	// performAction('click_on_text',text)

	// /////Then /^I wait for the view with id "([^\"]*)" to appear$/ do |text|
	// performAction('wait_for_view_by_id', text)
	// /////Then /^I long press view with id "([^\"]*)"$/
	// performAction('click_on_view_by_id',view_id)

	// ///// Then /^I should not see "([^\"]*)"$/
	// performAction('assert_text', text, false)
	// ///// Then /^I long press "([^\"]*)"$/ do |text_to_press|
	// performAction('press_long_on_text', text_to_press)

	// ///// Then /^I wait up to (\d+) seconds for "([^\"]*)" to appear$/
	// performAction('wait_for_text', text, timeout)

	// ///// Then /^I clear input field with id "([^\"]*)"$/ do |view_id|
	// Then /^I enter text "([^\"]*)" into field with id "([^\"]*)"$/ do |text,
	// performAction('clear_id_field', view_id)
	// performAction('enter_text_into_id_field', text, view_id)
	public String getEnterTextCode() {
		if(id.trim().length() > 0)
			return "performAction('clear_id_field', \"" + id + "\")\n\t performAction('enter_text_into_id_field', \"TEST\", \"" + id + "\")";
		else
			return "#getEnterTextCode";
	}

	// Then /^I wait up to (\d+) seconds for "([^\"]*)" to appear$/
	// performAction('wait_for_text', text, timeout)
	public String getWaitForTextCode() {
		if(text.trim().length() > 0)
			return "performAction('wait_for_text', \"" + text + "\", 30)";
		else
			return "#getWaitForTextCode";
	}

	// performAction('press_long_on_text', text_to_press)
	public String getLongPressTextCode() {
		if(text.trim().length() > 0)
			return " performAction('press_long_on_text', \"" + text + "\")";
		else
			return "#getLongPressTextCode";
	}

	// performAction('assert_text', text, false)
	public String getAntiTextAssertionCode() {
		if(text.trim().length() > 0)
			return "performAction('assert_text', \"" + text + "\", false)";
		else
			return "#getAntiTextAssertionCode";
	}

	// /Then /^I wait for the view with id "([^\"]*)" to appear$/ do |text|
	// performAction('wait_for_view_by_id', text)
	public String getIdAssertionCode() {
		if(id.trim().length() > 0)
			return "performAction('wait_for_view_by_id', \"" + id + "\")";
		else
			return "#getIdAssertionCode";
	}

	// Then /^I see the text "([^\"]*)"$/
	// performAction('assert_text',text, true)
	public String getTextAssertionCode() {
		if(text.trim().length() > 0) {
			return "performAction('assert_text', \"" + text + "\", true)";
		} else
			return "#getTextAssertionCode";
	}

	// Then /^I long press view with id "([^\"]*)"$/
	// performAction('click_on_view_by_id',view_id)
	public String getIdPressCode() {
		if(id.trim().length() > 0)
			return "performAction('click_on_view_by_id', \"" + id + "\")";
		else
			return "#getIdPressCode";
	}

	// Then /^I touch the "([^\"]*)" text$/
	// performAction('click_on_text',text)
	public String getTextTouchCode() {
		if(text.trim().length() > 0) {
			return " performAction('click_on_text', \"" + text + "\")";
		} else
			return "#getTextTouchCode";
	}

	// ////////////////////////////////////////////////*******************************************
	// ///////////////////////////////////////////////*******************************************
	// Then /^I clear input field with id "([^\"]*)"$/ do |view_id|
	// Then /^I enter text "([^\"]*)" into field with id "([^\"]*)"$/ do |text,
	// view_id|
	public String getEnterText() {
		if(id.trim().length() > 0)
			return "And I clear input field with id \"" + id + "\"\n\tAnd I enter text \"TEXT\" into field with id \"" + id + "\"";
		else
			return "#getEnterText";
	}

	// Then /^I wait up to (\d+) seconds for "([^\"]*)" to appear$/
	public String getWaitForText() {
		if(text.trim().length() > 0)
			return "And I wait up to 22 seconds for \"" + text + "\" to appear";
		else
			return "#getWaitForText";
	}

	// Then /^I long press "([^\"]*)"$/ do |text_to_press|
	public String getLongPressText() {
		if(text.trim().length() > 0)
			return "And I long press \"" + text + "\"";
		else
			return "#getLongPressText";
	}

	// Then /^I should not see "([^\"]*)"$/
	public String getAntiTextAssertion() {
		if(text.trim().length() > 0)
			return "And I should not see \"" + text + "\"";
		else
			return "#getAntiTextAssertion";
	}

	// /Then /^I wait for the view with id "([^\"]*)" to appear$/ do |text|
	public String getIdAssertion() {
		if(id.trim().length() > 0)
			return "And I wait for the view with id \"" + id + "\" to appear";
		else
			return "#getIdAssertion";
	}

	// Then /^I see the text "([^\"]*)"$/
	public String getTextAssertion() {
		if(text.trim().length() > 0) {
			return "And I see the text \"" + text + "\"";
		} else
			return "#getTextAssertion";
	}

	// Then /^I long press view with id "([^\"]*)"$/
	public String getIdPress() {
		if(id.trim().length() > 0)
			return "And I long press view with id \"" + id + "\"";
		else
			return "#getIdPress";
	}

	// Then /^I touch the "([^\"]*)" text$/
	public String getTextTouch() {
		if(text.trim().length() > 0) {
			return "And I touch the \"" + text + "\" text";
		} else
			return "#getTextTouch";
	}

	public String retrieveId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String retrieveText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String retrieveContentDescription() {
		return contentDescription;
	}

	public void setContentDescription(String contentDescription) {
		this.contentDescription = contentDescription;
	}

	public String getFullClassName() {
		return className;
	}

	public String getClassName() {
		// if(className!=null){
		String[] pathway = className.split("\\.");
		return pathway[pathway.length - 1];
		// }else return "unknown class";
	}

	public void setAndroidObjectClassName(String className) {
		this.className = className;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
