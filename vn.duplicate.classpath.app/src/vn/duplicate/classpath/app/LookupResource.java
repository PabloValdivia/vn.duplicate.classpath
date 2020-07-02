package vn.duplicate.classpath.app;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.osgi.internal.loader.EquinoxClassLoader;
import org.eclipse.osgi.internal.loader.classpath.ClasspathEntry;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(
	    property= {
	            "osgi.command.scope:String=LookupResource",
	            "osgi.command.function:String=lookup"},
	        service=LookupResource.class
	    )
public class LookupResource {
	ComponentContext context;
	
	@Activate
    void activate(ComponentContext context) {
        this.context = context;
    }
	
	public void lookup() {
		Bundle[] bundleLs = context.getUsingBundle().getBundleContext().getBundles();
		for (Bundle bundle : bundleLs) {
			if ("vn.duplicate.classpath.lib".equals(bundle.getSymbolicName())) {
				BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
				ClassLoader libCl = bundleWiring.getClassLoader();
				if (libCl instanceof EquinoxClassLoader) {
					EquinoxClassLoader libOsgiCl = (EquinoxClassLoader)libCl;
					ClasspathEntry [] libCpEntrys = libOsgiCl.getClasspathManager().getHostClasspathEntries();
					for (ClasspathEntry libCpEntry : libCpEntrys) {
						System.out.printf(libCpEntry.getBundleFile().getBaseFile().getAbsolutePath());
						System.out.printf("\n");
					}
				}
				
			}
		}
		
		Bundle appBundle = context.getUsingBundle();
		BundleWiring bundleWiring = appBundle.adapt(BundleWiring.class);
		ClassLoader appCl = bundleWiring.getClassLoader();
		try {
			Enumeration<URL> resourceUrls = appCl.getResources("metainfo/zk/config.xml");
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
