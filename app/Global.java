import play.GlobalSettings;
import play.api.mvc.Result;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;

public class Global extends GlobalSettings {
	public static final int CHAR_LEN = 200;
	public static final String APP_ENV_LOCAL = "local";
	public static final String APP_ENV_VAR = "CURRENT_APPNAME";
	
	// For CORS
	private class ActionWrapper extends Action.Simple {
		public ActionWrapper(Action<?> action) {
			this.delegate = action;
		}

		@Override
		public Promise<SimpleResult> call(Http.Context ctx) throws java.lang.Throwable {
			Promise<SimpleResult> result = this.delegate.call(ctx);
			Http.Response response = ctx.response();
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD"); 
			response.setHeader("Access-Control-Allow-Headers","Cache-Control, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
			return result;
		}
	}
	/*
	 * Adds the required CORS header "Access-Control-Allow-Origin" to successfull requests
	 */
	@Override
	public Action<?> onRequest(Http.Request request, java.lang.reflect.Method actionMethod) {
		return new ActionWrapper(super.onRequest(request, actionMethod));
	}
	/*public <T extends EssentialFilter> Class<T>[] filters() {
        return new Class[]{GzipFilter.class};
    }

		
	@Override
	public void onStart(Application app) {
		//NewsLetter.newsletterSchedulling();
		//TestDriveScheduler.newsletterSchedulling();
		System.err.println("Testing..");
		//MeetingAndTestDriveScheduler.meetingSchedulling();
		//ClickyDataScheduler.clickySchedulling();
	}
	
	
	@Override
	public void onStop(Application app) {
		Logger.info("Application shutdown...");
	}*/
}