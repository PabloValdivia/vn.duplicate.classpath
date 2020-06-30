package vn.duplicate.classpath.app;

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
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl != null) {
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
		}
	}
}
