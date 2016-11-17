package viewmodel;

import java.util.Date;

import models.MailchimpList;

public class MailchimpPageVM {

	public Long id;
	public String schedularTime;
	public Long locations_id;
	public Date curr_date;
	public String mailchimpUserName;
	public String mailchimpPassword;
	public String apikey;
	public MailchimpList list;
	public boolean synchronizeContact;
}
