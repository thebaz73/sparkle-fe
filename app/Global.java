import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.F;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import util.Configuration;

import static play.mvc.Results.*;

/**
 * Global
 * Created by thebaz on 23/04/15.
 */
public class Global extends GlobalSettings {

    private final Configuration configuration;

    public Global() {
        configuration = Configuration.getInstance();
    }

    @Override
    public F.Promise<Result> onBadRequest(RequestHeader request, String error) {
        return F.Promise.<Result>pure(badRequest("Don't try to hack the URI!"));
    }

    @Override
    public F.Promise<Result> onHandlerNotFound(RequestHeader request) {
        return F.Promise.<Result>pure(notFound(
                views.html.notFoundPage.render(configuration.getSiteName(), request.uri())
        ));
    }

    @Override
    public F.Promise<Result> onError(RequestHeader request, Throwable t) {
        return F.Promise.<Result>pure(internalServerError(
                views.html.errorPage.render(configuration.getSiteName(), t)
        ));
    }

    @Override
    public void onStart(Application application) {
        Logger.info("Sparkle FrontEnd starting...");
        configuration.loadData();
        super.onStart(application);
    }
}
