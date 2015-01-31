package org.codice.testify.actions.bayeux;

import org.codice.testify.actions.Action;
import org.codice.testify.objects.AllObjects;
import org.codice.testify.objects.TestifyLogger;
import org.codice.testify.objects.Request;
import org.codice.testify.objects.TestProperties;
import org.cometd.client.BayeuxClient;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The StopBayeuxClient class is a Testify Action service for stopping a bayeux client
 * @see org.codice.testify.actions.Action
 */
public class StopBayeuxClient implements BundleActivator, Action {

    @Override
    public void executeAction(String s) {

        TestifyLogger.debug("Running StopBayeuxClient Action", this.getClass().getSimpleName());

        // Grab the test properties
        final TestProperties testProperties = (TestProperties) AllObjects.getObject("testProperties");

        final Request request = (Request) AllObjects.getObject("request");

        if (s != null) {
            // Split action info by "=="
            TestifyLogger.debug(s, this.getClass().getSimpleName());
            String[] actionElements = s.split("==");

            // If there are not two action elements, then produce an error
            if (actionElements.length == 1) {

                String clientName = actionElements[0];

                // Check that the client name and channel are greater than one character
                if (clientName.length() > 0) {

                    // Retrieve Client
                    TestifyLogger.debug("Retrieving client: " + clientName, this.getClass().getSimpleName());
                    BayeuxClient bayeuxClient = (BayeuxClient)AllObjects.getObject(clientName);

                    // Disconnect Client
                    TestifyLogger.debug("Stopping client: " + clientName, this.getClass().getSimpleName());
                    bayeuxClient.disconnect();

                    // Remove Client
                    TestifyLogger.debug("Destroying Client" + clientName, this.getClass().getSimpleName());
                    // Not possible to remove an object from allobjects at this time
                }
                else { TestifyLogger.error("The two elements contained in action info are zero characters in length", this.getClass().getSimpleName()); }
            }
            else { TestifyLogger.error("Provided Action info: " + s + " is invalid, requires two elements but found: " + actionElements.length, this.getClass().getSimpleName()); }
        }
        else { TestifyLogger.error("Action info must be provided for this action", this.getClass().getSimpleName()); }
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {

        // Register the StopBayeuxClient service
        bundleContext.registerService(Action.class.getName(), new StopBayeuxClient(), null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }
}