# e4-preferences
This project contains a plug-in with an OSGi declarative service that can be used to contribute preference pages to a JFace `PreferenceDialog` via OSGi services.

This plug-in can be used for migrating an Eclipse 3.x based application to using the Eclipse 4.x platform. It is intended to remove the extension point
_org.eclipse.ui.preferencePages_ and dependencies to _org.eclipse.ui_, which is necessary to get rid of the compatibility layer.

To consume the service via target platform, the following update sites are available:
- 0.2.0 (works with Java 7 and Eclipse Luna)\
  https://github.com/fipro78/e4-preferences/raw/master/releases/0.2.0
- 0.3.0 (works with Java 8 and Eclipse 2019-06 because of OSGi R7 Logging)\
  https://github.com/fipro78/e4-preferences/raw/master/releases/0.3.0

To use the service, existing preference pages need to be modified:

1. Remove the class hierarchy (`implements IWorkbenchPreferencePage`)
2. Remove `init()`
3. Implement a constructor to set the title and the description if necessary

4. Remove the extension point _org.eclipse.ui.preferencePages_

To contribute the preference page to this service, the following steps need to be performed:

5. Add _org.fipro.e4.service.preferences_ to the _Dependencies_ section of the *MANIFEST.MF* file of the plug-in that contributes the preference page
6. Enable DS Annotations support via Window - Preferences - Plug-in Development - DS Annotations
7. Create a `PreferenceNodeContribution` for the `PreferencePage` (e.g. `MyPreferencePage`)

```java
@Component(service=PreferenceNodeContribution.class)
public class MyPreferenceContribution extends PreferenceNodeContribution {

    public MyPreferenceContribution() {
        super("myId", "myLabel", null, 
            MyPreferencePage.class, null, null);
    }
}
```
 
To open a JFace `PreferenceDialog` that looks similar to the known Eclipse workbench preference dialog, you need to create a handler that looks similar to the following snippet:

```java
public class PreferencesHandler {
	
    @Execute
    public void execute(Shell shell, @PrefMgr PreferenceManager manager) {
        PreferenceDialog dialog = new PreferenceDialog(shell, manager) {
        
        @Override
        protected TreeViewer createTreeViewer(Composite parent) {
            TreeViewer viewer = super.createTreeViewer(parent);
				
            viewer.setComparator(new ViewerComparator() {
					
                @Override
                public int category(Object element) {
                    // this ensures that the General preferences page is always on top
                    // while the other pages are ordered alphabetical
                    if (element instanceof ContributedPreferenceNode
                            && ("general".equals(((ContributedPreferenceNode) element).getId()))) {
                        return -1;
                    }
                    return 0;
                }
            });
				
            return viewer;
			}
        };
		
        dialog.open();
    }
}
```
