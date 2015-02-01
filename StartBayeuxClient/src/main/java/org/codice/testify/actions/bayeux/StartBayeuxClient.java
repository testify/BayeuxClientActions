/*
 * Copyright 2015 Codice Foundation
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.codice.testify.actions.bayeux;

import org.codice.testify.actions.Action;
import org.codice.testify.actions.bayeux.support.BayeuxClientFactory;
import org.codice.testify.objects.AllObjects;
import org.codice.testify.objects.TestifyLogger;
import org.codice.testify.objects.Request;
import org.codice.testify.objects.TestProperties;
import org.cometd.client.BayeuxClient;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The StartBayeuxClient class is a Testify Action service for creating a client using the Bayeux protocol
 * @see org.codice.testify.actions.Action
 */

public class StartBayeuxClient implements BundleActivator, Action {

    @Override
    public void executeAction(String s) {

        TestifyLogger.debug("Running StartBayeuxClient Action", this.getClass().getSimpleName());

        // Grab the test properties
        final TestProperties testProperties = (TestProperties) AllObjects.getObject("testProperties");

        final Request request = (Request) AllObjects.getObject("request");

        final BayeuxClientFactory bayeuxClientFactory = new BayeuxClientFactory();

        if (s != null) {
            // Split action info by "=="
            TestifyLogger.debug(s, this.getClass().getSimpleName());
            String[] actionElements = s.split("==");

            // If there are not two action elements, then produce an error
            if (actionElements.length == 2) {

                // Split the two action elements into separate strings
                String clientName = actionElements[0].trim();
                String url = actionElements[1].trim();

                // Check that the client name and url are greater than one character
                if (clientName.length() > 0 && url.length() > 0) {

                    // Create the Bayeux client
                    BayeuxClient client = null;
                    try {
                        client = bayeuxClientFactory.spawnClient(url);
                    } catch (Exception e) {
                        TestifyLogger.error("An Exception occurred while spawning the Bayeux Client: " + e, this.getClass().getSimpleName());
                    }

                    // Store client in AllObjects
                    TestifyLogger.debug("Persisting Bayeux Client with name: " + clientName + " using endpoint: " + url, this.getClass().getSimpleName());
                    AllObjects.setObject(clientName, client);

                    // Store the clients session id as a property
                    String clientSessionProperty = clientName + ".session.id";
                    TestifyLogger.debug("Storing sessionId for client: " + clientName + " as property: " + clientSessionProperty, this.getClass().getSimpleName());
                    testProperties.addProperty(clientSessionProperty, client.getId());

                    // Create response object for Bayeux Client
                    final StringBuilder jsonResponse = new StringBuilder();
                    AllObjects.setObject(clientName + ".response", jsonResponse);
                }
                else { TestifyLogger.error("The two elements contained in action info are zero characters in length", this.getClass().getSimpleName()); }
            }
            else { TestifyLogger.error("Provided Action info: " + s + " is invalid, requires two elements but found: " + actionElements.length, this.getClass().getSimpleName()); }
        }
        else { TestifyLogger.error("Action info must be provided for this action", this.getClass().getSimpleName()); }

    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {

        // Register the StartBayeuxClient service
        bundleContext.registerService(Action.class.getName(), new StartBayeuxClient(), null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }
}