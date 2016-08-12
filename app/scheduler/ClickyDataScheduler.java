package scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import models.NewsletterDate;
import play.libs.Akka;
import play.libs.Time.CronExpression;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;

public class ClickyDataScheduler {
	static Cancellable c = null;
	static Cancellable c1 = null;
	
	public static void clickySchedulling() {
		System.out.println("sheduler");
		ActorSystem newsLetter = Akka.system();
		if(c !=null && c1 != null) {
			c.cancel();
			c1.cancel();
		}
		
	    c1 = newsLetter.scheduler().schedule(
			Duration.create(0, TimeUnit.MILLISECONDS),
			Duration.create(1, TimeUnit.DAYS), new Runnable() {
				public void run() {
				    	   System.out.println("Get Clicky Data");
				        controllers.ClickyAnalyticsController.getClickyVisitorList();
				}
	}, newsLetter.dispatcher());
	}
}
