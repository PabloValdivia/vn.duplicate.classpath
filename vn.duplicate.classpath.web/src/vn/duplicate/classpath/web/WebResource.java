package vn.duplicate.classpath.web;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(
	    property= {
	            "osgi.command.scope:String=WebResource",
	            "osgi.command.function:String=lookup"},
	    		service=WebResource.class
	    )
public class WebResource {
	ComponentContext context;
	
	
	@Activate
    void activate(ComponentContext context) {
        this.context = context;
    }
	
	final List<Server> jettyServers = new CopyOnWriteArrayList<>();

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC)
	void addJettyServers(Server serverInstance) {
		jettyServers.add(serverInstance);
	}

	void removeJettyServers(Server serverInstance) {
		jettyServers.remove(serverInstance);
	}
	
	public void lookup () {
		Server defaultInstance = jettyServers.get(0);
		WebAppContext webHandle = defaultInstance.getChildHandlerByClass(WebAppContext.class);
		ClassLoader webClLoader = webHandle.getClassLoader();
		try {
			Enumeration<URL> resourceUrls = webClLoader.getResources("metainfo/zk/config.xml");
			while (resourceUrls.hasMoreElements()) {
				URL resourUrl = resourceUrls.nextElement();
				System.out.println(resourUrl.getPath());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
